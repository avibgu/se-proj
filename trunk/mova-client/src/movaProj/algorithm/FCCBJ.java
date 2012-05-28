package movaProj.algorithm;

import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

@Deprecated
public class FCCBJ implements CSPAlgorithm{

	protected static final int UNINITIALIZED	= -1;
	protected static final int INITIALIZED		= 0;
	protected static final int UNKNOWN			= 1;
	protected static final int SOLUTION			= 2;
	protected static final int IMPOSSIBLE		= 3;

	protected boolean							mConsistent;
	protected int								mStatus;
	protected int								mN;

	protected Vector<Variable>					mVariables;
	protected Vector<OldDomain>					mCurrentDomains;
	protected Vector<Value>						mAssignment;
	
	protected Vector<SortedSet<Variable>>		mConfSets;
	
	protected Vector<Stack<Vector<Value>>>		mReductions;
	protected Vector<Stack<Variable>>			mPastFc;
	protected Vector<Stack<Variable>>			mFutureFc;
	
	public FCCBJ() {
		setStatus(UNINITIALIZED);
	}
	
	public FCCBJ(Vector<Variable> pVariables) {
		init(pVariables);
	}

	protected void init(Vector<Variable> pVariables) {
		
		setStatus(INITIALIZED);
		
		mN = pVariables.size();
		
		mVariables = pVariables;
		
		mCurrentDomains = new Vector<OldDomain>();
		
		for (int i = 0; i < getN(); i++)
//			mCurrentDomains.add(mVariables.get(i).getDomain()); TODO

		mAssignment = new Vector<Value>();
		
		for (int i = 0; i < getN(); i++)
			mAssignment.add(null);
		
		mConfSets = new Vector<SortedSet<Variable>>();
		
		for (int i = 0; i < getN(); i++)
			mConfSets.add(new TreeSet<Variable>());

		mReductions = new Vector<Stack<Vector<Value>>>(getN());
		mPastFc = new Vector<Stack<Variable>>(getN());
		mFutureFc = new Vector<Stack<Variable>>(getN());
		
		for (int i = 0; i < getN(); i++){
			
			mReductions.add(new Stack<Vector<Value>>());
			
			mPastFc.add(new Stack<Variable>());
			mFutureFc.add(new Stack<Variable>());
		}
	}
	
	public void solve(Vector<Variable> pVariables) throws Exception{
		
		init(pVariables);
		solve();
	}
	
	@Override
	public void solve() throws Exception{
		
		if (UNINITIALIZED == getStatus())
			throw new Exception("Please initialize the algorithm with a Problem");

		mConsistent = true;
		
		setStatus(UNKNOWN);
		
		int i = 0;
		
		while (UNKNOWN == getStatus()){
			
			if (mConsistent) i = label(i);
			else i = unlabel(i);
			
			if (i >= getN())				
				setStatus(SOLUTION);

			else if (-1 == i) setStatus(IMPOSSIBLE);
		}
	}

	public int label(int i) {
		
		mConsistent = false;
		boolean tExtansion = true;
		
		while  (!mCurrentDomains.get(i).isEmpty() && !mConsistent){

			//Always going for the first
			mAssignment.set(i, mCurrentDomains.get(i).firstElement());
			
			mConsistent = true;	
			tExtansion = true;
			
			int j;
			
			for(j = i + 1; j < getN() && mConsistent; j++){
				mConsistent = checkForward(i, j);
			}
			
			if(!mConsistent){
				undoAssignment(i, j);
			}
			else tExtansion = true;
			
			if(!tExtansion){
				
				undoAssignment(i, j);
				
				//an attempt to try an another variable in the domain;
				mConsistent = false;
			}
		}

		return (mConsistent) ? i + 1 : i;
	}
	
	public int unlabel(int i) {

		int h = getHFromI(i);
		
		if (h < 0) return h;
		
		mConfSets.get(h).addAll(mConfSets.get(i));
		mConfSets.get(h).addAll(mPastFc.get(i));
		mConfSets.get(h).remove(new Integer(h));
		
		for(int j = i; j >= h + 1; j--){
			
			mConfSets.get(j).clear();
			undoReductions(j);
			updateCurrentDomain(j);
		}
				
		undoReductions(h);
		mCurrentDomains.get(h).remove(mAssignment.get(h));
		mConsistent = !mCurrentDomains.get(h).isEmpty();
		
		return h;
	}
	
	protected int getHFromI(int i) {
		
		int tMax = -1;
			
		if (!mPastFc.get(i).isEmpty()){
		
			for(Variable variable : mPastFc.get(i)){
				
				int tX = mVariables.indexOf(variable);
				
				if (tX > tMax) tMax = tX;
			}
		}

		if(!mConfSets.get(i).isEmpty() && tMax < mVariables.indexOf(mConfSets.get(i).last()))
			tMax = mVariables.indexOf(mConfSets.get(i).last());

		return tMax;
	}
	
	private void undoAssignment(int i, int j) {
		
		mCurrentDomains.get(i).remove(0);//Hard coded 0 
		undoReductions(i);
		mConfSets.get(i).addAll(mPastFc.get(j-1));
	}
	
	public boolean checkForward(int i, int j){

		Vector<Value> tReduction = new Vector<Value>();

		int size = mCurrentDomains.get(j).size();

		for (int k = 0; k < size; k++){

			mAssignment.set(j, mCurrentDomains.get(j).get(k));

			if(mAssignment.get(i).areConflicting(mAssignment.get(j)))
				tReduction.add(mAssignment.get(j));
		}

		if (!tReduction.isEmpty()){

			mCurrentDomains.get(j).removeAll(tReduction);
			mReductions.get(j).push(tReduction);
			mFutureFc.get(i).push(mVariables.get(j));
			mPastFc.get(j).push(mVariables.get(i));
		}

		return !mCurrentDomains.get(j).isEmpty();	
	}

	public void undoReductions(int i){

		Vector<Value> tReduction = null;

		for (Variable variable: mFutureFc.get(i)){

			int j = mVariables.indexOf(variable);
			
			tReduction = mReductions.get(j).pop();
			mCurrentDomains.get(j).addAll(tReduction);
			mPastFc.get(j).pop();
		}

		mFutureFc.get(i).clear();
	}

	public void updateCurrentDomain(int i){

		restoreCurrentDomain(i);

		for (Vector<Value> tReduction : mReductions.get(i)) {
			mCurrentDomains.get(i).removeAll(tReduction);
		}
	}
	
	public void restoreCurrentDomain(int i) {
//		mCurrentDomains.set(i, mVariables.get(i).getDomain());	TODO
	}

	private int getN() {
		return mN;
	}
	
	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int pStatus) {
		mStatus = pStatus;
	}

	@Override
	public boolean isSolved() {
		return getStatus() == SOLUTION;
	}

	@Override
	public Vector<Value> getAssignment() {
		return mAssignment;
	}
}

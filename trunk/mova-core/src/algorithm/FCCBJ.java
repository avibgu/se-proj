package algorithm;

import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

public class FCCBJ implements CSPAlgorithm{

	protected static final int UNINITIALIZED	= -1;
	protected static final int INITIALIZED		= 0;
	protected static final int UNKNOWN			= 1;
	protected static final int SOLUTION			= 2;
	protected static final int IMPOSSIBLE		= 3;
	
	protected Problem							mProblem;
	protected boolean							mConsistent;
	protected int								mStatus;
	protected Vector<Vector<Integer>>			mCurrentDomain;
	
	protected Vector<SortedSet<Integer>>		mConfSets;
	
	protected Vector<Stack<Vector<Integer>>>	mReductions;
	protected Vector<Stack<Integer>>			mPastFc;
	protected Vector<Stack<Integer>>			mFutureFc;
	
	public FCCBJ() {
		mStatus = UNINITIALIZED;
	}
	
	public FCCBJ(Problem problem) {
		init(problem);
	}
	
	@SuppressWarnings("unchecked")
	protected void init(Problem problem) {
		
		mProblem = problem;
		
		mStatus = INITIALIZED;
		
		mCurrentDomain = new Vector<Vector<Integer>>();
		
		for (int i = 0; i < mProblem.getN(); i++)
			mCurrentDomain.add(
					(Vector<Integer>)mProblem.getDomain().get(i).clone());

		mConfSets = new Vector<SortedSet<Integer>>();
		
		for (int i = 0; i < mProblem.getN(); i++)
			mConfSets.add(new TreeSet<Integer>());
		
		mReductions = new Vector<Stack<Vector<Integer>>>(mProblem.getN());
		mPastFc = new Vector<Stack<Integer>>(mProblem.getN());
		mFutureFc = new Vector<Stack<Integer>>(mProblem.getN());
		
		for (int i = 0; i < mProblem.getN(); i++){
			
			mReductions.add(new Stack<Vector<Integer>>());
			
			mPastFc.add(new Stack<Integer>());
			mFutureFc.add(new Stack<Integer>());
		}
	}
	
	public void solve(Problem problem) throws Exception{
		
		init(problem);
		solve();
	}
	
	@Override
	public void solve() throws Exception{
		
		if (UNINITIALIZED == mStatus)
			throw new Exception("Please initialize the algorithm with a Problem");
		
		mProblem.initDataStructures();
		
		mConsistent = true;
		
		mStatus = UNKNOWN;
		
		int i = 0;
		
		while (UNKNOWN == mStatus){
			
			if (mConsistent) i = label(i);
			else i = unlabel(i);
			
			if (i >= mProblem.getN()){
				
				mStatus = SOLUTION;
				mProblem.setSolved(true);
			}
			else if (-1 == i) mStatus = IMPOSSIBLE;
		}
	}

	public int label(int i) {
		
		mConsistent = false;
		boolean tExtansion = true;
		
		while  (!mCurrentDomain.get(i).isEmpty() && !mConsistent){

			//Always going for the first
			mProblem.setVi(i, mCurrentDomain.get(i).firstElement());
			
			mConsistent = true;	
			tExtansion = true;
			
			int j;
			
			for(j = i + 1; j < mProblem.getN() && mConsistent; j++){
				mConsistent = checkForward(i, j);
			}
			
			if(!mConsistent){
				undoAssignment(i, j);
			}
			else tExtansion = labelExtansion(i);
			
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
		mCurrentDomain.get(h).remove(mProblem.getV().get(h));
		mConsistent = !mCurrentDomain.get(h).isEmpty();
		
		return h;
	}
	
	protected int getHFromI(int i) {
		
		int tMax = -1;
			
		if (!mPastFc.get(i).isEmpty()){
		
			for(Integer tX :mPastFc.get(i)){
				
				if (tX > tMax) tMax = tX;
			}
		}

		if(!mConfSets.get(i).isEmpty() && tMax < mConfSets.get(i).last())
			tMax = mConfSets.get(i).last();

		return tMax;
	}
	
	@SuppressWarnings("unchecked")
	public void restoreCurrentDomain(int i) {
		mCurrentDomain.set(i, (Vector<Integer>)mProblem.getDomain().get(i).clone());
	}
	
	private void undoAssignment(int i, int j) {
		
		mCurrentDomain.get(i).remove(0);//Hard coded 0 
		undoReductions(i);
		mConfSets.get(i).addAll(mPastFc.get(j-1));
	}
	
	protected boolean labelExtansion(int i) {
		return true;
	}
	
	public boolean checkForward(int i, int j){

		Vector<Integer> tReduction = new Vector<Integer>();

		int size = mCurrentDomain.get(j).size();

		for (int k = 0; k < size; k++){

			mProblem.getV().set(j, mCurrentDomain.get(j).get(k));

			if(!mProblem.check(i, mProblem.getV().get(i), j, mProblem.getV().get(j)))
				tReduction.add(mProblem.getV().get(j));
		}

		if (!tReduction.isEmpty()){

			mCurrentDomain.get(j).removeAll(tReduction);
			mReductions.get(j).push(tReduction);
			mFutureFc.get(i).push(j);
			mPastFc.get(j).push(i);
		}

		return !mCurrentDomain.get(j).isEmpty();	
	}

	public void undoReductions(int i){

		Vector<Integer> tReduction = null;

		for (Integer j: mFutureFc.get(i)){

			tReduction = mReductions.get(j).pop();
			mCurrentDomain.get(j).addAll(tReduction);
			mPastFc.get(j).pop();
		}

		mFutureFc.get(i).clear();
	}

	public void updateCurrentDomain(int i){

		restoreCurrentDomain(i);

		for (Vector<Integer> tReduction : mReductions.get(i)) {
			mCurrentDomain.get(i).removeAll(tReduction);
		}
	}
}

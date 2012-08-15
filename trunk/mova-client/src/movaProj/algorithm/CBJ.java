package movaProj.algorithm;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

/**
 * This Class represents the Scheduling Algorithm.
 * It gets Variables (which represent Activities) and try to find
 * them a legal Assignment of Values (which represent a schedule).
 */
public class CBJ implements CSPAlgorithm {

	protected static final int UNINITIALIZED = -1;
	protected static final int INITIALIZED = 0;
	protected static final int UNKNOWN = 1;
	protected static final int SOLUTION = 2;
	protected static final int IMPOSSIBLE = 3;

	protected boolean mConsistent;
	protected int mStatus;
	protected int mN;

	protected Vector<Variable> mVariables;
	protected Vector<Domain> mCurrentDomains;
	protected Vector<Value> mAssignment;

	protected Vector<SortedSet<Variable>> mConfSets;

	public CBJ() {
		setStatus(UNINITIALIZED);
	}

	public CBJ(Vector<Variable> pVariables) {
		init(pVariables);
	}

	protected void init(Vector<Variable> pVariables) {

		setStatus(INITIALIZED);

		mN = pVariables.size();

		mVariables = pVariables;

		mCurrentDomains = new Vector<Domain>();

		for (int i = 0; i < getN(); i++)
			mCurrentDomains.add(mVariables.get(i).getDomain());

		mAssignment = new Vector<Value>();

		for (int i = 0; i < getN(); i++)
			mAssignment.add(null);

		mConfSets = new Vector<SortedSet<Variable>>();

		for (int i = 0; i < getN(); i++)
			mConfSets.add(new TreeSet<Variable>());
	}

	/**
	 * @inheritDoc
	 */
	public void solve(Vector<Variable> pVariables) throws Exception {
		init(pVariables);
		solve();
	}

	@Override
	/**
	 * @inheritDoc
	 */
	public void solve() throws Exception {

		if (UNINITIALIZED == getStatus())
			throw new Exception(
					"Please initialize the algorithm with a Problem");

		mConsistent = true;

		setStatus(UNKNOWN);

		int i = 0;

		while (UNKNOWN == getStatus()) {

			if (mConsistent)
				i = label(i);
			else
				i = unlabel(i);

			if (i >= getN())
				setStatus(SOLUTION);

			else if (-1 == i)
				setStatus(IMPOSSIBLE);
		}
	}

	/**
	 * this method tries to assign Value to Variable i  
	 * 
	 * @param i the Variable that we tries to aggign a Value to
	 * @return <i>i + 1</i> if the assignment has succeeded, or <i>i<i> otherwise
	 */
	public int label(int i) {

		mConsistent = false;

		while (!mCurrentDomains.get(i).isEmpty() && !mConsistent) {

			mConsistent = true;

			try {
				mAssignment.set(i, mCurrentDomains.get(i).nextValue());
			} catch (Exception e) {
				return -1;
			}

			int h;

			for (h = 0; h < i && mConsistent; h++)
				mConsistent = !mAssignment.get(i).areConflicting(
						mAssignment.get(h));

			if (!mConsistent)
				mConfSets.get(i).add(mVariables.get(h - 1));
		}

		return (mConsistent) ? i + 1 : i;
	}

	/**
	 * this method remove the assigned Value from Variable i  
	 * 
	 * @param i the Variable that we remove its Value
	 * @return the index of the Variable that we want to assign him new Value
	 */
	public int unlabel(int i) {

		int h = getHFromI(i);

		if (h < 0)
			return h;

		mConfSets.get(i).remove(mConfSets.get(i).last()); // removes h
		mConfSets.get(h).addAll(mConfSets.get(i));

		for (int j = h + 1; j <= i; j++) {

			mConfSets.get(j).clear();
			mVariables.get(i).getDomain().resetIndexes();
		}

		mConsistent = !mCurrentDomains.get(h).isEmpty();

		return h;
	}

	protected int getHFromI(int i) {

		if (mConfSets.get(i).isEmpty())
			return -1;

		return mVariables.indexOf(mConfSets.get(i).last());
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
	/**
	 * @inheritDoc
	 */
	public boolean isSolved() {
		return getStatus() == SOLUTION;
	}

	@Override
	/**
	 * @inheritDoc
	 */
	public Vector<Value> getAssignment() {
		return mAssignment;
	}
}

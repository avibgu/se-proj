package algorithm;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class CBJ {

	protected SortedSet<Constraint> mFullDomain = null;
	protected SortedSet<Constraint> mCurrentDomain = null;

	protected SortedSet<Integer> mConfSet = null;
	protected Vector<Integer> mAssignedVariables = null;
	protected Vector<Integer> mAllVariables = null;

	protected int mID = -1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CBJ(SortedSet<Integer> pFullDomain, int pID) {

		// TODO: init mAllVariables..

		mID = pID;

		mCurrentDomain = new TreeSet(pFullDomain);

		mConfSet = new TreeSet<Integer>();

		mAssignedVariables = new Vector<Integer>();

		if (isFirstAgent()) {

			Assignment cpa = new Assignment();

			cpa.assign(mID, getFirstElementInCurrentDomain());

			mAssignedVariables.add(mID);

			int nextAgent = getNextAgent();

			sendLabel(cpa, mAssignedVariables, nextAgent);
		}
	}

	private void sendLabel(Assignment pCPA, Vector<Integer> pAssignedVariables,
			int pNextAgent) {
		// TODO Auto-generated method stub

	}

	private boolean isFirstAgent() {
		return mAllVariables.get(0) == mID;
	}

	private int getNextAgent() {

		for (int i = 0; i < mAllVariables.size(); i++)
			if (!mAssignedVariables.contains(i) && mID < i)
				return i;

		return -1;
	}

	protected Constraint getFirstElementInCurrentDomain() {

		return mCurrentDomain.first();
	}

	private void removeFirstElementFromCurrentDomain() {

		mCurrentDomain.remove(mCurrentDomain.first());
	}

	// TODO: @WhenReceived("LABEL")
	public void handleLABEL(Assignment pCPA, Vector<Integer> pAssignedVariables) {

		mAssignedVariables = pAssignedVariables;

		int h = 0;

		boolean consistent = false;

		while (!mCurrentDomain.isEmpty() && !consistent) {

			consistent = true;

			pCPA.assign(mID, getFirstElementInCurrentDomain());

			for (h = 0; h < mAssignedVariables.size() && consistent; h++)
				consistent = amIConsistentWithThisVar(pCPA, h);

			if (!consistent) {

				mConfSet.add(mAssignedVariables.get(h - 1));
				removeFirstElementFromCurrentDomain();
			}
		}

		desicion(consistent, pCPA, getNextAgent());
	}

	private boolean amIConsistentWithThisVar(Assignment pCPA, int pVar) {

		return !pCPA.getAssignment(mID).isOverlap(pCPA.getAssignment(pVar));
	}

	protected void desicion(boolean consistent, Assignment pCPA, int toWho) {

		if ((pCPA.getNumberOfAssignedVariables() == mAssignedVariables.size())
				&& consistent)
			finish(pCPA);

		else if ((mAssignedVariables.size() == 0 && mCurrentDomain.isEmpty()))
			finishWithNoSolution();

		else if (consistent) {

			Vector<Integer> tAV = new Vector<Integer>(mAssignedVariables);

			if (!tAV.contains(mID) && toWho != mID)
				tAV.add(mID);

			sendLabel(pCPA, tAV, toWho);
		}

		else if (!consistent) {

			if (mConfSet.isEmpty()) {
				finishWithNoSolution();
				return;
			}

			int h = getH();

			mConfSet.remove(new Integer(h));

			pCPA.unassign(mID);

			sendUnlabel(pCPA, mConfSet, toWho);
		}
	}

	private void sendUnlabel(Assignment pCPA, SortedSet<Integer> mConfSet2,
			int toWho) {
		// TODO Auto-generated method stub

	}

	private void finishWithNoSolution() {
		// TODO Auto-generated method stub

	}

	private void finish(Assignment cpa) {
		// TODO Auto-generated method stub
		// maybe to notify all agent that a solution has found..
	}

	private int getH() {

		int last = mAssignedVariables.lastElement();

		for (Integer var : mAssignedVariables)
			if (mConfSet.contains(var))
				last = var;

		return last;
	}

	// TODO: @WhenReceived("UNLABEL")
	public void handleUNLABEL(Assignment cpa, SortedSet<Integer> confSetOfI) {

		mConfSet.addAll(confSetOfI);

		mCurrentDomain.remove(cpa.getAssignment(mID));

		// TODO
		clearAndRestore(cpa);

		desicion(!mCurrentDomain.isEmpty(), cpa, mID);
	}

	protected void clearAndRestore(Assignment cpa) {

		Set<Integer> dontSend = new HashSet<Integer>();

		for (Integer var : mAssignedVariables) {

			if (mID == var)
				break;

			dontSend.add(var);
		}

		for (int i = 0; i < mAllVariables.size(); i++) {

			if (!dontSend.contains(i) && mID != i) {

				cpa.unassign(i);
				sendClearAndRestore(i);
			}
		}
	}

	private void sendClearAndRestore(int i) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	// TODO: @WhenReceived("CLEAR_AND_RESTORE")
	public void handleCLEARANDRESTORE() {

		mConfSet.clear();
		mCurrentDomain = new TreeSet(mFullDomain);
	}
}

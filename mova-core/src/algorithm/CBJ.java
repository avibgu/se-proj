package algorithm;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class CBJ {

	protected SortedSet<Integer> mFullDomain = null;
	protected SortedSet<Integer> mCurrentDomain = null;
	protected SortedSet<Integer> mConfSet = null;
	protected Vector<Integer> mAssignedVariables = null;
	protected Vector<Integer> mAllVariables = null;
	protected int mID = -1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CBJ(SortedSet<Integer> pFullDomain, int pID) {

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

			print(mID + " assigned the value "
					+ getFirstElementInCurrentDomain()
					+ " and sent LABEL to next agent");
		}
	}

	private void sendLabel(Assignment pCPA, Vector<Integer> pAssignedVariables,
			int pNextAgent) {
		// TODO Auto-generated method stub

	}

	private boolean isFirstAgent() {
		// TODO Auto-generated method stub
		return false;
	}

	private int getNextAgent() {

		for (int i = 0; i < mAllVariables.size(); i++)
			if (!mAssignedVariables.contains(i) && i < mID)
				return i;

		return -1;
	}

	protected int getFirstElementInCurrentDomain() {

		return mCurrentDomain.first();
	}

	private void removeFirstElementFromCurrentDomain() {

		mCurrentDomain.remove(mCurrentDomain.first());
	}

	@WhenReceived("LABEL")
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
		// TODO  
//		getProblem().isConsistent(getId(),
//				pCPA.getAssignment(getId()), mAssignedVariables.get(h),
//				pCPA.getAssignment(mAssignedVariables.get(h)));

		return false;
	}

	protected void desicion(boolean consistent, Assignment cpa, int toWho) {

		if ((cpa.getNumberOfAssignedVariables() == getNumberOfVariables())
				&& consistent)
			finish(cpa);

		else if ((mAssignedVariables.size() == 0 && mCurrentDomain.isEmpty()))
			finishWithNoSolution();

		else if (consistent) {

			Vector<Integer> tAV = new Vector<Integer>(mAssignedVariables);

			if (!tAV.contains(getId()) && toWho != getId())
				tAV.add(getId());

			send("LABEL", cpa, tAV).to(toWho);
		}

		else if (!consistent) {

			if (mConfSet.isEmpty()) {
				finishWithNoSolution();
				return;
			}

			int h = getH();

			mConfSet.remove(new Integer(h));

			cpa.unassign(getId());

			send("UNLABEL", cpa, mConfSet).to(h);
		}
	}

	private int getH() {

		int last = mAssignedVariables.lastElement();

		for (Integer var : mAssignedVariables)
			if (mConfSet.contains(var))
				last = var;

		return last;
	}

	@WhenReceived("UNLABEL")
	public void handleUNLABEL(Assignment cpa, SortedSet<Integer> confSetOfI) {

		print(getId() + " got UNLABEL from " + getCurrentMessage().getSender()
				+ " with cpa: " + cpa + " and confSet: " + confSetOfI);

		mConfSet.addAll(confSetOfI);

		mCurrentDomain.remove(cpa.getAssignment(getId()));

		// TODO
		clearAndRestore(cpa);

		desicion(!mCurrentDomain.isEmpty(), cpa, getId());
	}

	protected void clearAndRestore(Assignment cpa) {

		Set<Integer> dontSend = new HashSet<Integer>();

		for (Integer var : mAssignedVariables) {

			if (getId() == var)
				break;

			dontSend.add(var);
		}

		for (int i = 0; i < getNumberOfVariables(); i++) {

			if (!dontSend.contains(i) && getId() != i) {

				cpa.unassign(i);
				send("CLEAR_AND_RESTORE").to(i);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@WhenReceived("CLEAR_AND_RESTORE")
	public void handleCLEARANDRESTORE() {

		print(getId() + " got CLEAR_AND_RESTORE from "
				+ getCurrentMessage().getSender());

		mConfSet.clear();
		mCurrentDomain = new TreeSet(getDomain());
	}

	private void print(String string) {
		System.err.println(string);
		System.err.flush();
	}

}

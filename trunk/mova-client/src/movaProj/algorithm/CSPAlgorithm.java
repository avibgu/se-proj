package movaProj.algorithm;

import java.util.Vector;

public interface CSPAlgorithm {

	/**
	 * This method start the Algorithm.
	 * 
	 * @throws Exception
	 *             in case the algorithm hasn't initialized correctly
	 */
	public void solve() throws Exception;

	/**
	 * This method start the Algorithm.
	 * 
	 * @param pVariables
	 *            Set of Variables which represent the Activities that we want
	 *            to schedule.
	 * @throws Exception
	 *             in case the algorithm hasn't initialized correctly
	 */
	public void solve(Vector<Variable> pVariables) throws Exception;

	/**
	 * @return <i>true</i> in case the algorithm found a solution.
	 */
	public boolean isSolved();

	/**
	 * @return the vector of Values which represents a schedule (instances of
	 *         Activities). the schedule is legal only if <i>isSolved() ==
	 *         true</i>.
	 */
	public Vector<Value> getAssignment();
}

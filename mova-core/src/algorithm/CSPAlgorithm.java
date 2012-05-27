package algorithm;

import java.util.Vector;

public interface CSPAlgorithm {

	public void solve() throws Exception;
	public void solve(Vector<Variable> pVariables) throws Exception;
	public boolean isSolved();
	public Vector<Value> getAssignment();
}

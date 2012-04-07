package algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class Problem {

	protected	int											mN;
	protected	int											mD;
	protected	double										mP1;
	protected	double										mP2;
	protected	Vector<Integer>								mV;
	protected	Vector<Vector<Integer>>						mDomain;
	protected	Vector<Vector<Map<VariablesPair, Boolean>>>	mConstraints;
	protected	boolean										mSolved;
	protected	int											mCCs;
	protected	int											mAssignments;
	protected	Random										mRandom;
	
	/**
	 * n – the number of variables.
	 * d – the domain size.
	 * p1 – the probability for a constraint between 2 variables.
	 * p2 – the probability for a conflict between 2 constrained values.
	 */	
	public Problem(int n, int d, double p1, double p2) {
		this(n, d, p1, p2, new Random(17));
	}
	
	public Problem(int n, int d, double p1, double p2, Random random) {
		
		setN(n);
		setD(d);
		setP1(p1);
		setP2(p2);
		
		setRandom(random);
		
		initDataStructures();
		initConstraints();
	}

	public void initDataStructures() {
		
		Vector<Integer> tmpV = new Vector<Integer>(getN());
		
		for (int i = 0; i < getN(); i++)
			tmpV.add(new Integer(-1));

		setV(tmpV);
		
		setDomain(new Vector<Vector<Integer>>(getN()));
		
		for (int i = 0; i < getN(); i++){
			
			Vector<Integer> tmpVec = new Vector<Integer>(getD());
			
			for (int j = 0; j < getD(); j++)
				tmpVec.add(new Integer(j));
			
			getDomain().add(tmpVec);
		}
		
		setSolved(false);
		setCCs(0);
		setAssignments(0);
	}
	
	/**
	 * p1 – the probability for a constraint between 2 variables.
	 * p2 – the probability for a conflict between 2 constrained values.
	 */
	protected void initConstraints() {
		
		Vector<Vector<Map<VariablesPair, Boolean>>> tConstraints =
			new Vector<Vector<Map<VariablesPair, Boolean>>>(getN());

		Vector<Map<VariablesPair, Boolean>> tmpVec = null;
		Map<VariablesPair, Boolean> tmpMap = null;
		
		boolean dontHaveConstarint = false;
		
		for (int i = 0; i < getN(); i++) {

			tmpVec = new Vector<Map<VariablesPair, Boolean>>(getN());
			
			for (int j = 0; j < getN(); j++){
				
				if (j < i){
					
					tmpVec.add(null);
					continue;
				}
				
				tmpMap = new HashMap<VariablesPair, Boolean>(getD()*getD());
				
				dontHaveConstarint = (i == j) || getRandom().nextDouble() > getP1();
				
				for (int di = 0; di < getD(); di++){
					
					for (int dj = 0; dj < getD(); dj++){
						
						if (dontHaveConstarint || getRandom().nextDouble() > getP2())
							tmpMap.put(new VariablesPair(di, dj), true);
						
						else
							tmpMap.put(new VariablesPair(di, dj), false);
					}
				}
				
				tmpVec.add(tmpMap);
			}
			
			tConstraints.add(tmpVec);
		}

		setConstraints(tConstraints);
	}
	
	/**
	 * returns true iff there is no conflict between <var1,val1>
	 * to <var2,val2> in this CSP problem instance
	 * 
	 * @param var1
	 * @param val1
	 * @param var2
	 * @param val2
	 * @return
	 */
	public boolean check(int var1, int val1, int var2, int val2){
				
		incCCs();
		
		if (var1 <= var2)
			return getConstraints().get(var1).get(var2).get(new VariablesPair(val1, val2));
		
		else
			return getConstraints().get(var2).get(var1).get(new VariablesPair(val2, val1));
	}
	
	@Override
	public String toString() {
		return "N=" + getN() + ", D=" + getD() + ", P1=" + getP1() + ", P2=" + getP2();
	}
	
	public String printSolution() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Assignment = ");
		
		for (int i = 0; i < getN(); i++)
			sb.append("<" + i + "," + getV().get(i) + ">,");
		
		sb.deleteCharAt(sb.lastIndexOf(","));
		
		sb.append(" , CCs=" + getCCs() + ", Assignments=" + getAssignments());
		
		return sb.toString();
	}

	protected void setN(int n) {
		this.mN = n;
	}

	public int getN() {
		return mN;
	}

	public void setD(int d) {
		this.mD = d;
	}

	public int getD() {
		return mD;
	}

	public void setP1(double p1) {
		this.mP1 = p1;
	}

	public double getP1() {
		return mP1;
	}

	public void setP2(double p2) {
		this.mP2 = p2;
	}

	public double getP2() {
		return mP2;
	}

	protected void setV(Vector<Integer> v) {
		this.mV = v;
	}

	public Vector<Integer> getV() {
		return mV;
	}
	
	public void setVi(int i, Integer element) {
		
		incAssignments();
		
		getV().set(i, element);
	}

	protected void setDomain(Vector<Vector<Integer>> currentDomain) {
		this.mDomain = currentDomain;
	}

	public Vector<Vector<Integer>> getDomain() {
		return mDomain;
	}

	public void setConstraints(Vector<Vector<Map<VariablesPair, Boolean>>> constraints) {
		this.mConstraints = constraints;
	}

	public Vector<Vector<Map<VariablesPair, Boolean>>> getConstraints() {
		return mConstraints;
	}

	public void setSolved(boolean solved) {
		this.mSolved = solved;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setCCs(int cCs) {
		mCCs = cCs;
	}

	public int getCCs() {
		return mCCs;
	}
	
	public void incCCs(){
		mCCs++;
	}

	public void setAssignments(int assignments) {
		this.mAssignments = assignments;
	}

	public int getAssignments() {
		return mAssignments;
	}
	
	public void incAssignments(){
		mAssignments++;
	}

	public void setRandom(Random random) {
		this.mRandom = random;
	}

	public Random getRandom() {
		return mRandom;
	}
}

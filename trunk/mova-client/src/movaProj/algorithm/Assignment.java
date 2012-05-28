package movaProj.algorithm;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class Assignment {

	protected Map<Integer,Constraint> map = null;
	
	public Assignment() {
		map = new HashMap<Integer, Constraint>();
	}
	
	
	public void assign(int var, Constraint val) {
		map.put(var, val);
	}

	public Constraint getAssignment(int var) {
		return map.get(var);
	}

	public int getNumberOfAssignedVariables() {
		return map.size();
	}

	public void unassign(int var) {
		map.remove(var);
	}
}

package algorithm;

import java.util.HashMap;
import java.util.Map;

public class Assignment {

	protected Map<Integer,Constreint> map = null;
	
	public Assignment() {
		map = new HashMap<Integer, Constreint>();
	}
	
	
	public void assign(int var, Constreint val) {
		map.put(var, val);
	}

	public Constreint getAssignment(int var) {
		return map.get(var);
	}

	public int getNumberOfAssignedVariables() {
		return map.size();
	}

	public void unassign(int var) {
		map.remove(var);
	}
}

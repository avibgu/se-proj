package priority;

public enum Priority {
	URGENT(1),
	HIGH(2),
	MEDIUM(3),
	LOW(4);
	
	private final int severity;
	
	Priority(int severity) {
		this.severity = severity;
	}
	
	public int severity(){
		return this.severity;
	}
	
}

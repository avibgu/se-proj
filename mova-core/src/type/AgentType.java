package type;

public class AgentType {
	
	public transient static final String COORDINATOR = "COORDINATOR";
	public transient static final String SECURITY_OFFICER = "SECURITY_OFFICER";
	public transient static final String SECRETARY = "SECRETARY";
	public transient static final String NETWORK_MANAGER = "NETWORK_MANAGER";
	public transient static final String LOGISTIC_MANAGER = "LOGISTIC_MANAGER";
	public transient static final String SOUND_MANAGER = "SOUND_MANAGER";
	public transient static final String RFID = "RFID";
	
	protected String mType;
	
	public AgentType(String pType) {
		mType = pType;
	}
	
	public String getType() {
		return mType;
	}

	public void setType(String pType) {
		mType = pType;
	}

	@Override
	public boolean equals(Object pOther) {
		
		if (!(pOther instanceof AgentType))
			return false;
		
		return ((AgentType)pOther).getType().equals(mType);
	}
	
	@Override
	public int hashCode() {
		return mType.hashCode();
	}
	
	public String toString(){
		return mType;
	}
	
	@Deprecated
	public static String[] values() {
		return new String[]{COORDINATOR,SECURITY_OFFICER,SECRETARY,NETWORK_MANAGER,LOGISTIC_MANAGER,SOUND_MANAGER,RFID};
	}
}

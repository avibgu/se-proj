package type;

public class AgentType {
	
	public static final String COORDINATOR = "COORDINATOR";
	public static final String SECURITY_OFFICER = "SECURITY_OFFICER";
	public static final String SECRETARY = "SECRETARY";
	public static final String NETWORK_MANAGER = "NETWORK_MANAGER";
	public static final String LOGISTIC_MANAGER = "LOGISTIC_MANAGER";
	public static final String SOUND_MANAGER = "SOUND_MANAGER";
	public static final String RFID = "RFID";
	
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
	
	public String toString(){
		return mType;
	}
	
	@Deprecated
	public static String[] values() {
		return new String[]{COORDINATOR,SECURITY_OFFICER,SECRETARY,NETWORK_MANAGER,LOGISTIC_MANAGER,SOUND_MANAGER,RFID};
	}
}

package type;

public class ItemType {
	
	public transient static final String PROJECTOR = "PROJECTOR";
	public transient static final String LAPTOP = "LAPTOP";
	public transient static final String MOUSE = "MOUSE";
	public transient static final String CABLE = "CABLE";
	public transient static final String CHAIR = "CHAIR";
	public transient static final String SPEAKER = "SPEAKER";
	public transient static final String STAND = "STAND";
	public transient static final String BOARD = "BOARD";
	public transient static final String LAZER_CURSOR = "LAZER_CURSOR";
	public transient static final String BOARD_PEN = "BOARD_PEN";
	public transient static final String ERASER = "ERASER";
	
	protected String mType;
	
	public ItemType(String pType) {
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
		
		if (!(pOther instanceof ItemType))
			return false;
		
		return ((ItemType)pOther).getType().equals(mType);
	}
	
	public String toString(){
		return mType;
	}
	
	@Deprecated
	public static String[] values() {
		return new String[]{PROJECTOR,LAPTOP,MOUSE,CABLE,CHAIR,SPEAKER,STAND,BOARD,LAZER_CURSOR,BOARD_PEN,ERASER};
	}
}

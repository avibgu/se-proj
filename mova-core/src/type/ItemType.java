package type;

public class ItemType {
	
	public static final String PROJECTOR = "PROJECTOR";
	public static final String LAPTOP = "LAPTOP";
	public static final String MOUSE = "MOUSE";
	public static final String CABLE = "CABLE";
	public static final String CHAIR = "CHAIR";
	public static final String SPEAKER = "SPEAKER";
	public static final String STAND = "STAND";
	public static final String BOARD = "BOARD";
	public static final String LAZER_CURSOR = "LAZER_CURSOR";
	public static final String BOARD_PEN = "BOARD_PEN";
	public static final String ERASER = "ERASER";
	
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
	
	@Deprecated
	public static String[] values() {
		return new String[]{PROJECTOR,LAPTOP,MOUSE,CABLE,CHAIR,SPEAKER,STAND,BOARD,LAZER_CURSOR,BOARD_PEN,ERASER};
	}
}

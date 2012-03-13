package algorithm;

import java.util.Date;

public class TimeConstreint implements Constreint{

	protected Date mStartTime = null;
	protected Date mEndTime = null;
	
	public TimeConstreint(Date pStartTime, Date pEndTime) {

		mStartTime = pStartTime;
		mEndTime = pEndTime;
	}
	
	@Override
	public boolean isOverlap(Constreint other){

		if (!(other instanceof TimeConstreint))
			return false;
		
		//TODO
		return false;
	}
}

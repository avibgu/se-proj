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
		
		Date otherStart = ((TimeConstreint)other).mStartTime;
		Date otherEnd = ((TimeConstreint)other).mEndTime;
		
		if (otherStart.compareTo(mStartTime) >= 0 && otherStart.compareTo(mEndTime) <= 0)
			return true;
		
		if (otherEnd.compareTo(mStartTime) >= 0 && otherEnd.compareTo(mEndTime) <= 0)
			return true;
		
		return false;
	}
}

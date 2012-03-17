package priority;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PrioritiesCompareToTest {

	protected Priority				mHigh;
	protected Priority				mMedium;
	protected Priority				mLow;
	
	@Before
	public void setUp() throws Exception {

		mHigh = Priority.HIGH;
		mMedium = Priority.MEDIUM;
		mLow = Priority.LOW;
	}

	@Test
	public void test() {

		assertTrue(mHigh.compareTo(mMedium) < 0);
		assertTrue(mHigh.compareTo(mLow) < 0);
		
		assertTrue(mMedium.compareTo(mHigh) > 0);
		assertTrue(mMedium.compareTo(mLow) < 0);
		
		assertTrue(mLow.compareTo(mHigh) > 0);
		assertTrue(mLow.compareTo(mMedium) > 0);
	}

}

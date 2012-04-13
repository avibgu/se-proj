package resource;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import client.MovaClient;

public class ActivityResourceTest {
	
	private MovaClient _mc;
	
	@BeforeClass
    public void setUp() throws Exception{
		_mc = new MovaClient();
    }
	
	@Test
	public void testSendActivity() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeActivityStatus() {
		fail("Not yet implemented");
	}

	@AfterClass
    public static void tearDown() throws IOException{

    }
}

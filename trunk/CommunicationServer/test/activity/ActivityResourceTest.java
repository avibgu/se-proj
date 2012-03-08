package activity;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;

import javax.ws.rs.Path;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import type.AgentType;
import actor.Activity;
import actor.Agent;

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

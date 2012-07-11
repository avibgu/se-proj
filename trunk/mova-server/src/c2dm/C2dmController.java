package c2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import type.MessageType;
import db.DBHandler;
/**
 * The C2dmController class is a singleton class used to
 * receive messages from the server resources and send them
 * to the mobile Mova clients via the C2DM servers as push messages
 */
public class C2dmController {

	public static final String SENDER_ID = "movaC2DM@gmail.com";
	public static final String SENDER_PW = "movaC2DM";
	private static final C2dmController mInstance = new C2dmController();

	public int mCounter = 0;
	private DBHandler mDb = DBHandler.getInstance();

	public C2dmController() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Returns the only instance of the class
	 * @return the only instance of the class
	 */
	public static C2dmController getInstance() {
		return mInstance;
	}
	/**
	 * Sends a given message to all agent ids given
	 * @param pCollapseKey
	 * @param pMessage the message to send
	 * @param pAgentsIds the agent ids to send to
	 * @param pMessageType the type of the messsage
	 */
	public void sendMessageUsingRegistrationId(String pCollapseKey, String pMessage,
			String registrationId, MessageType pMessageType){
		boolean success = false;
		while (!success){
			try{
				
				String authToken = getAuthToken();
				URL url = new URL("https://android.apis.google.com/c2dm/send");
				HttpsURLConnection
						.setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
				HttpsURLConnection request = (HttpsURLConnection) url
						.openConnection();
		
				request.setDoOutput(true);
				request.setDoInput(true);
		
				StringBuilder buf = new StringBuilder();
				buf.append("registration_id").append("=")
						.append((URLEncoder.encode(registrationId, "UTF-8")));
				buf.append("&collapse_key").append("=")
						.append((URLEncoder.encode(pCollapseKey, "UTF-8")));
				buf.append("&data.message").append("=")
						.append((URLEncoder.encode(pMessage, "UTF-8")));
				buf.append("&data.messageType").append("=")
				.append((URLEncoder.encode(pMessageType.toString(), "UTF-8")));
		
				request.setRequestMethod("POST");
				request.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				request.setRequestProperty("Content-Length", buf.toString()
						.getBytes().length + "");
				request.setRequestProperty("Authorization", "GoogleLogin auth="
						+ authToken);
		
				OutputStreamWriter post = new OutputStreamWriter(
						request.getOutputStream());
				post.write(buf.toString());
				post.flush();
		
				BufferedReader in = new BufferedReader(new InputStreamReader(
						request.getInputStream()));
				buf = new StringBuilder();
		
				String inputLine;
		
				while ((inputLine = in.readLine()) != null) {
					buf.append(inputLine);
				}
		
				post.close();
				in.close();
		
				int code = request.getResponseCode();
		
				if (code == 200) {
					success = true;
		
				} else if (code == 503) {
					
		
				} else if (code == 401) {
					getNewAuthKey();
					sendMessageUsingRegistrationId(pCollapseKey, pMessage, registrationId, pMessageType);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessageToDevice(String pCollapseKey, String pMessage,
			Vector<String> pAgentsIds, MessageType pMessageType) {

		String authToken = getAuthToken();
		URL url;
		
		try {
			
			if (pAgentsIds == null){ // Get all agents ids.
				pAgentsIds = mDb.getAgentIds();
			}
			
			for (String agentId : pAgentsIds) {

				// Find the registration id

				String regId = mDb.getAgentRegistrationId(agentId);

				url = new URL("https://android.apis.google.com/c2dm/send");
				HttpsURLConnection
						.setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
				HttpsURLConnection request = (HttpsURLConnection) url
						.openConnection();

				request.setDoOutput(true);
				request.setDoInput(true);

				StringBuilder buf = new StringBuilder();
				buf.append("registration_id").append("=")
						.append((URLEncoder.encode(regId, "UTF-8")));
				buf.append("&collapse_key").append("=")
						.append((URLEncoder.encode(pCollapseKey, "UTF-8")));
				buf.append("&data.message").append("=")
						.append((URLEncoder.encode(pMessage, "UTF-8")));
				buf.append("&data.messageType").append("=")
				.append((URLEncoder.encode(pMessageType.toString(), "UTF-8")));

				request.setRequestMethod("POST");
				request.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				request.setRequestProperty("Content-Length", buf.toString()
						.getBytes().length + "");
				request.setRequestProperty("Authorization", "GoogleLogin auth="
						+ authToken);

				OutputStreamWriter post = new OutputStreamWriter(
						request.getOutputStream());
				post.write(buf.toString());
				post.flush();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						request.getInputStream()));
				buf = new StringBuilder();

				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					buf.append(inputLine);
				}

				post.close();
				in.close();

				int code = request.getResponseCode();

				if (code == 200) {
					System.out.println("SUCCESS");
				} else if (code == 503) {
					System.out.println("FAILED2");
				} else if (code == 401) {
					System.out.println("FAILED3");
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getAuthToken() {
		
		String authToken = mDb.getAuthKey();
		//String authToken = null;
		
		if (authToken == null) {
			return getNewAuthKey();
		}else{
			return authToken;
		}
	}
	
	private String getNewAuthKey(){
		System.out.println("asking C2DM server for auth token...");
		String authToken = null;
		StringBuilder buf = new StringBuilder();

		HttpsURLConnection request = null;
		OutputStreamWriter post = null;
		try {
			URL url = new URL("https://www.google.com/accounts/ClientLogin");
			request = (HttpsURLConnection) url.openConnection();
			request.setDoOutput(true);
			request.setDoInput(true);

			buf.append("accountType").append("=")
					.append((URLEncoder.encode("GOOGLE", "UTF-8")));
			buf.append("&Email").append("=")
					.append((URLEncoder.encode(SENDER_ID, "UTF-8")));
			buf.append("&Passwd").append("=")
					.append((URLEncoder.encode(SENDER_PW, "UTF-8")));
			buf.append("&service").append("=")
					.append((URLEncoder.encode("ac2dm", "UTF-8")));
			buf.append("&source").append("=")
					.append((URLEncoder.encode("myco-pushapp-1.0", "UTF-8")));

			request.setRequestMethod("POST");
			request.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			request.setRequestProperty("Content-Length", buf.toString()
					.getBytes().length + "");

			post = new OutputStreamWriter(request.getOutputStream());
			post.write(buf.toString());
			post.flush();

			int code = request.getResponseCode();
			if (code == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						request.getInputStream()));
				buf = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					if (inputLine.startsWith("Auth=")) {
						authToken = inputLine.substring(5);
					}
					buf.append(inputLine);
				}
				post.close();
				in.close();
				

			} else if (code == 403) {
				
			}

			if (authToken != null) {
				mDb.deleteAuthKey();
				mDb.insertAuthKey(authToken);
			}
		return authToken;

		} catch (Exception e) {
			// _log.error("unable to make https post request to c2dm server",
			// e);
			// TODO: do something about it
			e.printStackTrace();
			return null;
		}
	
	}
	private static class CustomizedHostnameVerifier implements HostnameVerifier {
		public boolean verify(String pHostname, SSLSession pSession) {
			return true;
		}
	}

}

package c2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/c2dm")
public class C2dmController {

	public static final String SENDER_ID = "movaC2DM@gmail.com";
	public static final String SENDER_PW = "movaC2DM";
	public List<String> registration = new ArrayList<String>() ;
	public int counter = 0;
	
	@GET
	@Path("/saveRegistrationId/{id}")
	public String saveRegistrationId(@PathParam("id") String id){
		System.out.println("HERE");
		registration.add(id);
		sendMessageToDevice("a", id, "Hi Shiran");
		return "MMMMMMM";
	}
	
	public static void sendMessageToDevice(String collapseKey, String registrationId, String message){
		String authToken = getAutoToken();
		
		// _log.info("sending message...");
	        
        //HttpsURLConnection.setDefaultHostnameVerifier(new FakeHostnameVerifier()); 
        URL url;
		try {
			url = new URL("https://android.apis.google.com/c2dm/send");
			HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier()); 
			HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
			
	        request.setDoOutput(true);
	        request.setDoInput(true);
	
	        StringBuilder buf = new StringBuilder();
	        buf.append("registration_id").append("=").append((URLEncoder.encode(registrationId, "UTF-8")));
	        buf.append("&collapse_key").append("=").append((URLEncoder.encode(collapseKey, "UTF-8")));
	        buf.append("&data.message").append("=").append((URLEncoder.encode(message, "UTF-8")));
	        
	        request.setRequestMethod("POST");
	        request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        request.setRequestProperty("Content-Length", buf.toString().getBytes().length+"");
	        request.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
	        
	        OutputStreamWriter post = new OutputStreamWriter(request.getOutputStream());
	        post.write(buf.toString());
	        post.flush();
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        buf = new StringBuilder();
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            buf.append(inputLine);
	        }
	        post.close();
	        in.close();
	
	      //  _log.info("response from C2DM server:\n" + buf.toString());
	        
	        int code = request.getResponseCode();
	      //  _log.info("response code: " + request.getResponseCode());
	      //  _log.info("response message: " + request.getResponseMessage());
	        if (code == 200) {
	            //TODO: check for an error and if so, handle
	            
	        } else if (code == 503) {
	            //TODO: check for Retry-After header; use exponential backoff and try again
	            
	        } else if (code == 401) {
	            //TODO: get a new auth token
	        }
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getAutoToken() {
		String authToken=null;
		/*  String authToken = _dao.getAuthToken();
	        if (authToken != null) {
	            _log.info("retrieved auth token from db: " + authToken);
	            return authToken;
	        }*/
	        
	       System.out.println("asking C2DM server for auth token...");
	        
	        StringBuilder buf = new StringBuilder();
	        
	        HttpsURLConnection request = null;
	        OutputStreamWriter post = null;
	        try {
	            URL url = new URL("https://www.google.com/accounts/ClientLogin");
	            request = (HttpsURLConnection) url.openConnection();
	            request.setDoOutput(true);
	            request.setDoInput(true);

	            buf.append("accountType").append("=").append((URLEncoder.encode("GOOGLE", "UTF-8")));
	            buf.append("&Email").append("=").append((URLEncoder.encode(SENDER_ID, "UTF-8")));
	            buf.append("&Passwd").append("=").append((URLEncoder.encode(SENDER_PW, "UTF-8")));
	            buf.append("&service").append("=").append((URLEncoder.encode("ac2dm", "UTF-8")));
	            buf.append("&source").append("=").append((URLEncoder.encode("myco-pushapp-1.0", "UTF-8")));
	            
	            request.setRequestMethod("POST");
	            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	            request.setRequestProperty("Content-Length", buf.toString().getBytes().length+"");
	            
	            post = new OutputStreamWriter(request.getOutputStream());
	            post.write(buf.toString());
	            post.flush();
	            
	            int code = request.getResponseCode();
	           // _log.info("response code: " + request.getResponseCode());
	           // _log.info("response message: " + request.getResponseMessage());
	            if (code == 200) {
	                BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
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
	               // _log.info("response from C2DM server:\n" + buf.toString());
	                
	            } else if (code == 403) {
	                //TODO: handle error conditions
	            }
	            
	            if (authToken != null) {
	                //_log.info("storing auth token: " + authToken);
	                //_dao.saveAuthToken(authToken);
	            }
	            
	            return authToken;
	            
	        } catch (Exception e) {
	            //_log.error("unable to make https post request to c2dm server", e);
	            //TODO: do something about it
	        	e.printStackTrace();
	            return null;
	        }
	}
	
	private static class CustomizedHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	
}

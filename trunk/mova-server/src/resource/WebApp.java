package resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/webapp")
public class WebApp {

	@GET
	@Path("/main")
	public String mainGet(String jsonObject) {
		return page(WebApp.class.getResourceAsStream("index.html"));
	}

	private String page(InputStream pInputStream) {

		StringBuilder stringBuilder = new StringBuilder();
		
		BufferedReader br = null;

		try {

			br = new BufferedReader(new InputStreamReader(pInputStream));

			while (br.ready())
				stringBuilder.append(br.readLine());

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
}

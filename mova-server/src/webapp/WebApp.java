package webapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/webapp")
public class WebApp {

	@GET
	@Path("/main")
	public String mainGet(String jsonObject) {
		return page("../../WebContent/html/index.html");
	}

	private String page(String pFileName) {

		StringBuilder stringBuilder = new StringBuilder();

		File file = new File(pFileName);

		FileInputStream fis = null;
		BufferedReader br = null;

		try {

			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));

			while (br.ready())
				stringBuilder.append(br.readLine());

			fis.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}
}

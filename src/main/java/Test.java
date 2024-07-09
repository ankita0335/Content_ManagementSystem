import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/testpage")
public class Test extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Part file  = req.getPart("image");
		String imgFileName = file.getSubmittedFileName();
		
		String path = getServletContext().getRealPath("")+"imgs";
		
		File folder = new File(path);
		file.write(path+File.separator+imgFileName);
		System.out.println(path);
	}
}

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

@WebServlet("/adminpage")
@MultipartConfig
public class AdminPage extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//logout,newpost,viewpost
		PrintWriter out = resp.getWriter();
		String btn = req.getParameter("btn");
		
		if(btn.equalsIgnoreCase("logout")) {
			HttpSession session = req.getSession();
			session.invalidate();
			resp.setContentType("text/html");
			out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>You Logged Out Successfully</h3>");
			RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
			rd.include(req, resp);
		}
		
		
		if(btn.equalsIgnoreCase("newpost")) {
			req.setAttribute("event", "newpost");
			resp.setContentType("text/html");
			RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
			rd.include(req, resp);
		}
		
		
		if(btn.equalsIgnoreCase("viewpost")) {
			req.setAttribute("event", "viewpost");
			resp.setContentType("text/html");
			RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
			rd.include(req, resp);
		}
		
		if(btn.equalsIgnoreCase("delete") || btn.equalsIgnoreCase("edit")) {
			String c_id = req.getParameter("search").trim();
			if(c_id.length()>0) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","Aarav","admin");
					PreparedStatement ps2 = con.prepareStatement("SELECT * FROM content WHERE c_id='"+c_id+"'");
					ResultSet rs2 = ps2.executeQuery();
					if(rs2.next()) {
						PreparedStatement ps1 = con.prepareStatement("SELECT * FROM content WHERE c_id='"+c_id+"'");
						ResultSet rs = ps1.executeQuery();
						while(rs.next()) {
							req.setAttribute("cid", rs.getString("c_id"));
							req.setAttribute("ctitle", rs.getString("c_title"));
							req.setAttribute("currdate", rs.getString("curr_date"));
							req.setAttribute("cimage", rs.getString("c_image"));
							req.setAttribute("cauther", rs.getString("c_auther"));
							req.setAttribute("carticle", rs.getString("c_article"));
						}
						System.out.println("while end");
						if(btn.equalsIgnoreCase("edit")) {
							req.setAttribute("event","searched");
							req.setAttribute("msg", "Searched Results");
							resp.setContentType("text/html");
							out.print("<h2 style='color:red;position:absolute;top:100px;left:40%;z-index:1;'>Updation/Edit not Working!.</h2>");
							RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
							rd.include(req, resp);
						}
						if(btn.equalsIgnoreCase("delete")) {
							PreparedStatement ps = con.prepareStatement("DELETE FROM content WHERE c_id='"+c_id+"'");
							int i1=ps.executeUpdate();
							if(i1>0){
								req.setAttribute("event","searched");
								req.setAttribute("msg", "Deleted Data");
								resp.setContentType("text/html");
								out.print("<h2 style='color:red;position:absolute;top:100px;left:40%;z-index:1;'>You Deleted.</h2>");
								RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
								rd.include(req, resp);
							}
						}
					}
					else {
						req.setAttribute("event","search");
						resp.setContentType("text/html");
						req.setAttribute("event", "viewpost");
						out.print("<h3 style='color:red;position:absolute;top:100px;left:40%;z-index:1;'>Item Not Found.</h3>");
						RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
						rd.include(req, resp);
					}
				
				}catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				resp.setContentType("text/html");
				req.setAttribute("event", "viewpost");
				out.print("<h3 style='color:red;position:absolute;top:100px;left:40%;z-index:1;'>Enter Project Id</h3>");
				RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
				rd.include(req, resp);
			}
		}
		
//Posting The Articles	
		if(btn.equalsIgnoreCase("publish")) {
			String ptitle = req.getParameter("ptitle").trim();
			String pid = req.getParameter("pid").trim().toLowerCase();
			String pauther = req.getParameter("pauther").trim();
			String particle = req.getParameter("particle").trim();
			
			Part file  = req.getPart("pimage");
	
			if(ptitle.length()==0||pid.length()==0||pauther.length()==0||file==null || particle.length()==0) {
				resp.setContentType("text/html");
				req.setAttribute("event","newpost");
				out.print("<h3 style='color:red;position:absolute;top:110px;left:40%;z-index:1;'>Entry Failled!</b> '*' denote mandatory field.</h3>");
				RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
				rd.include(req, resp);
			}
			else {
				//Saving The Image
				String imgFileName = file.getSubmittedFileName();
				String path = getServletContext().getRealPath("")+"imgs";
				File folder = new File(path);
				file.write(path+File.separator+imgFileName);
				
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","Aarav","admin");
					
					//Checking Id Duplicate
					PreparedStatement ps2 = con.prepareStatement("SELECT * FROM content WHERE c_id='"+pid+"'");
					ResultSet rs2 = ps2.executeQuery();
					while(rs2.next()) {
						if(pid.equals(rs2.getString("c_id"))) {
							resp.setContentType("text/html");
							req.setAttribute("event", "newpost");
							out.print("<h3 style='color:red;position:absolute;top:110px;left:40%;z-index:1;'>Post Id must be unique</h3>");
							RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
							rd.include(req, resp);
						}
					}
					
					String sql1 = "insert into content values('"+pid+"','"+ptitle+"',now(),'"+imgFileName+"','"+pauther+"','"+particle+"')";
					PreparedStatement ps1 = con.prepareStatement(sql1);
					int i = ps1.executeUpdate();
					System.out.println("after execute");
					if(i>0) {
						resp.setContentType("text/html");
						req.setAttribute("event", "viewpost");
						out.print("<h3 style='color:red;position:absolute;top:110px;left:40%;z-index:1;'>Article Published Successfully</h3>");
						RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
						rd.include(req, resp);
					}
					else {
						resp.setContentType("text/html");
						req.setAttribute("event", "viewpost");
						out.print("<h3 style='color:red;position:absolute;top:110px;left:40%;z-index:1;'>Something Wrong Happened</h3>");
						RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
						rd.include(req, resp);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
					
			}
		}
	}
}

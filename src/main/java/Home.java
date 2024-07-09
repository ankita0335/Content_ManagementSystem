import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@WebServlet("/homepage")
public class Home extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//login_dashboard,register_dashboard,login_btn,register_btn
		PrintWriter out = resp.getWriter();
		String btn = req.getParameter("btn");
		
		if(btn.equalsIgnoreCase("login_dashboard")) {
			req.setAttribute("event", "login_dashboard");
			resp.setContentType("text/html");
			RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
			rd.include(req, resp);
		}
		
		
		if(btn.equalsIgnoreCase("register_dashboard")) {
			req.setAttribute("event", "register_dashboard");
			resp.setContentType("text/html");
			RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
			rd.include(req, resp);
		}
		
		
		if(btn.equalsIgnoreCase("login_btn")) {
			String uname = req.getParameter("username").trim().toLowerCase();
			String pass = req.getParameter("password").trim();
				if(uname.length()==0 || pass.length()==0){
		    		resp.setContentType("text/html");
					out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>Enter Username and password</h3>");
					RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
					rd.include(req, resp);
		    	}
				else {
				
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","Aarav","admin");
						PreparedStatement ps = con.prepareStatement("select email,pass,full_name,user_type from register");
						ResultSet rs = ps.executeQuery();
						boolean flag=false;
						while(rs.next()) {
							if(uname.equals(rs.getString("email")) && pass.equals(rs.getString("pass"))) {
								flag = true;
								HttpSession session = req.getSession();
								session.setAttribute("email", rs.getString("email"));
								session.setAttribute("name", rs.getString("full_name"));
								if("admin".equalsIgnoreCase(rs.getString("user_type"))) {
									RequestDispatcher rd = req.getRequestDispatcher("adminPage.jsp");
									rd.forward(req, resp);
								}
								else {
									resp.setContentType("text/html");
									out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>logged in successfully <br>Welcome "+rs.getString("full_name")+"</h3>");
									RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
									rd.forward(req, resp);
								}	
							}
						}
						if(!flag) {
							resp.setContentType("text/html");
							out.print("<center><h2 style='Color:red;'>Invalid UserName or Password</h2></center>");
							RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
							rd.include(req, resp);
						}
					
				}
				catch (Exception e) {
					resp.setContentType("text/html");
					out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>Some Technical Issue Occured</h3>");
					RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
					rd.include(req, resp);
					e.printStackTrace();
				}
			}
		}
		
//Registration Code Starts Here		
		if(btn.equalsIgnoreCase("register_btn")) {
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","Aarav","admin");
				String fullname = req.getParameter("fullname").trim();
				String email = req.getParameter("emailid").trim().toLowerCase();
				String phone = req.getParameter("phone").trim();
				String user_type = req.getParameter("user_type").trim();
				String pass = req.getParameter("pass").trim();
				String cpass = req.getParameter("cpass").trim();
				String dob = req.getParameter("dob");
				
				String sql = "SELECT email from register";
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if(fullname.length()==0||email.length()==0||pass.length()==0||cpass.length()==0) {
					resp.setContentType("text/html");
					out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>Registration Failled!</b> '*' denote mandatory field.</h3>");
					RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
					rd.include(req, resp);
				}
				else {
					while(rs.next()) {
						if(email.equals(rs.getString("email"))) {
							resp.setContentType("text/html");
							out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>This email id is already registered</h3>");
							RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
							rd.include(req, resp);
						}
					}
					if(pass.equals(cpass)==false) {
						resp.setContentType("text/html");
						out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>Passwords and Confirm Password must be same.</h3>");
						RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
						rd.include(req, resp);
					}
					else {
						String sql1 = "insert into register values('"+fullname+"','"+email+"','"+dob+"','"+phone+"','"+user_type+"','"+pass+"')";
						PreparedStatement ps1 = con.prepareStatement(sql1);
						int i = ps1.executeUpdate();
						if(i>0) {
							resp.setContentType("text/html");
							out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>Registered Successfully, Go to Login Page</h3>");
							RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
							rd.include(req, resp);
						}
					}
				}
					
			}catch (Exception e) {
				resp.setContentType("text/html");
				out.print("<h3 style='color:red;text-align:center;margin-top:100px;'>Some Technical Issue Occured</h3>");
				RequestDispatcher rd = req.getRequestDispatcher("home.jsp");
				rd.include(req, resp);
				e.printStackTrace();
			}
		}
	}
}

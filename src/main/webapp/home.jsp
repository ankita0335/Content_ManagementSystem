<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.sql.*" %>
    <%@page import="java.sql.Blob" %>
    <%@page import="java.util.*" %>
    <%@page import="java.io.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title></title>
	<link rel="stylesheet" type="text/css" href="home.css">
</head>
<body>
	<form id="container" action="homepage" method="post">
		<div id="content_parent">
			<header id="header">
			<% String st=null,st1; 
			OutputStream outputStream;
			%>
			<% st = (String)session.getAttribute("name");
				if(st==null){
					st1="Register Yourself to Get Updated";
				}
				else{
					st1="Welcome Mr. "+st;
				}
			%>
				<span><h2><%=st1%></h2></span>
				<span>
					<button name="btn" value="login_dashboard" class="login1" >Login to Dashboard</button>
					<button name="btn" value="register_dashboard" class="register1">Register</button>
				</span>
			</header>
			
<%if((request.getAttribute("event")!="login_dashboard") && (request.getAttribute("event")!="register_dashboard")){%>			
			
			
			<div id="allTables">
				
				<%
					
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","Aarav","admin");
						PreparedStatement ps = con.prepareStatement("select *from content");
						ResultSet rs = ps.executeQuery();
						while(rs.next()){
							%>
				<table>
					<tr>
						<th> <label>Content Title : <%=rs.getString("c_title") %></label> </th>
					</tr>
					<tr>
						<td><label>Published On: <%=rs.getString("curr_date") %></label></td>
					</tr>
					<tr>
						<td><label>Published By: <%=rs.getString("c_auther") %></label></td>
					</tr>
					<tr>
						<td><div id="imag"><img src="imgs/<%= rs.getString("c_image")%>" alt="Image"></div></td>
					</tr>
					<tr>
						<td><label> 
							<p><%=rs.getString("c_article") %>
							</p>

						 </label></td>
					</tr>
				</table>
				<hr>
					<%
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
						%>
				
				 <!-- Second Table Start Here. -->
				 
			</div>
			<% }%>
			<!-- Login Page is Here -->
			<%if(request.getAttribute("event")=="login_dashboard"){%>
			<div id="signin">
				<h2>Login Here</h2>
				<label>Email Id : </label><input type="text" name="username"><br>
				<label>Password : </label><input type="password" name="password"><br>
				<button name="btn" value="login_btn" class="login_btn">Login</button>
			</div>
			<%} %>

			<!-- Registration Page is Here -->
			<%if(request.getAttribute("event")=="register_dashboard"){
			%>
			<div id="registeration">
				<h2>Register Here</h2>
				<label>Full Name* : </label><input type="text" name="fullname"><br>
				<label>Email Id* : </label><input type="text" name="emailid"><br>
				<label>DOB: </label><input type="date" name="dob"><br>
				<label>Phone No. : </label><input type="text" name="phone"><br>
				<label>UserType*</label><br>
						<select name="user_type" id="pstatus">
					        <option value="newuser">New-User</option> 
					        <option value="admin">Admin</option> 
					    </select><br>
				<label>Password : </label><input type="password" name="pass"><br>
				<label>Confirm Password : </label><input type="password" name="cpass"><br>
				<button name="btn" value="register_btn" class="signup_btn">Register</button>
			</div>
			<%} %>
		</div>
	</form>
</body>
</html>
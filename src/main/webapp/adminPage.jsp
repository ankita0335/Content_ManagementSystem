<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.sql.*" %>
    <%@page import="java.util.*" %>
    <%@page import="java.io.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Admin Pannel</title>
	<link rel="stylesheet" type="text/css" href="adminPage.css">
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
	<form id="container" action="adminpage" method="post" enctype="multipart/form-data">
		<div>
			<div id="header">
				<div>
					<h1>Welcome <%=session.getAttribute("name") %></h1>
				</div>
				<div id="sidebar">
					<button name="btn" value="logout">Logout</button>
					<button name="btn" value="newpost"><i class="fa-regular fa-pen-to-square"></i> New Post</button>
					<button name="btn" value="viewpost"><i class="fa-solid fa-eye"></i> View Posts</button>
				</div>
			</div>
			<div id="content">
				<h2>Welcome to admin Pannel</h2>
				<h3>This is your Dashboard, Where you can manage your posts</h3><br>

				<!-- View Table -->
<%if(request.getAttribute("event")=="viewpost"){%>
				<div id="viewpost">
					<h2><b><u>View Post</u></b></h2>
					<div class="editButton">
						<input type="text" name="search" placeholder="Enter Post Number"><button class="edit" name="btn" value="edit">Search/Edit</button><button class="delete" name="btn" value="delete">Delete</button>
					</div>
					<table class="view_Table">
						<thead>
							<tr>
								<th>post_Id</th>
								<th>post_Date</th>
								<th>Post_Auther</th>
								<th>Post_Title</th>
								<th>post_Image</th>
								<th>post_Content</th>
							</tr>
						</thead>
						<tbody>

<%
					
try {
	Class.forName("com.mysql.cj.jdbc.Driver");
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms","Aarav","admin");
	PreparedStatement ps = con.prepareStatement("select *from content");
	ResultSet rs = ps.executeQuery();
	while(rs.next()){
%>

							<tr>
								<td><%=rs.getString("c_id") %></td>
								<td><%=rs.getString("curr_date") %></td>
								<td><%=rs.getString("c_auther") %></td>
								<td><%=rs.getString("c_title") %></td>
								<td><img src="imgs/<%= rs.getString("c_image")%>" alt="Image" style="height:80px;width:80px;"></td>
								<td><%=rs.getString("c_article") %></td>
							</tr>

<%} 
}
catch (Exception e) {
	e.printStackTrace();
}
	%>


						</tbody>
					</table>
				</div>
<%} %>

<%if(request.getAttribute("event")=="searched"){%>
				<div id="viewpost">
					<h2><b><u><%=request.getAttribute("msg") %></u></b></h2>
					<div class="editButton">
						<input type="text" name="search" placeholder="Enter Post Number"><button class="edit" name="btn" value="edit">Search/Edit</button><button class="delete" name="btn" value="delete">Delete</button>
					</div>
					<table class="view_Table">
						<thead>
							<tr>
								<th>post_ID</th>
								<th>post_Date</th>
								<th>Post_Auther</th>
								<th>Post_Title</th>
								<th>post_Image</th>
								<th>post_Content</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><%=request.getAttribute("cid") %></td>
								<td><%=request.getAttribute("currdate") %></td>
								<td><%=request.getAttribute("cauther") %></td>
								<td><%=request.getAttribute("ctitle") %></td>
								<td><img src="imgs/<%= request.getAttribute("cimage")%>" alt="Image" style="height:80px;width:80px;"></td>
								<td><%=request.getAttribute("carticle") %></td>
							</tr>
						</tbody>
					</table>
				</div>
<%} %>


<%if(request.getAttribute("event")=="newpost"){%>
				<div class="newPosts">
					<h2>Write Your Post Here <i class="fa-solid fa-hand-point-down"></i></h2>
					<span> <label> Post Title* : </label> <input type="text" name="ptitle"></span>
					<span> <label>Give Unique id* : </label> <input type="text"name="pid"></span>
					<span> <label>Post Auther* : </label> <input type="text"name="pauther"></span>
					<span> <label>Post Image* : </label> <input type="file"name="pimage"></span>
					<span> <label>Post Content (at least 150 character)* : <br></label> <textarea name="particle"></textarea> </span>
					<span> <button class="publish" name="btn" value="publish">Publish</button> </span>
				</div>
<%} %>
			</div>
		</div>
	</form>
</body>
</html>
<!-- postNo,postDate,PostAuther,PostTitle,postImage,postContent -->
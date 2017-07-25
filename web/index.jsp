<%--
    Document   : index1
    Created on : Mar 26, 2011, 8:14:22 PM
    Author     : arpan
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.io.*,java.util.*,java.net.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="queryBean" class="intranetFileSearch.Query" scope="request" />
<%!HttpSession session;
    String userID=null;
%>
<%
            session = request.getSession(true);

            if (session != null) {
                userID = (String) session.getAttribute("userID");
            }
%>
<%
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server

            response.setHeader("Cache-Control", "no-store");//Directs caches not to store the page under any circumstance

            response.setHeader("Pragma", "no-cache");//HTTP 1.0 backward compatibility

            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--


-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Plain &amp; Clean  by Free CSS Templates</title>
<link href="style.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="wrapper">
	<div id="header" class="container">
		<div id="logo">
			<h1><a href="#">FileKitty</a></h1>
			<p>Search files on FTP servers</p>
		</div>
		<div id="menu">
			<ul>
				<li class="current_page_item"><a href="#">Home</a></li>
				<li><a href="servers.do">File Servers</a></li>
                                <%--
                                <li><a href="http://mail.loogle.com">LMail</a></li>
                                --%>
                    <%
                    if (userID == null) {
                    %>
                    <li><a href="signin.do">Sign in</a></li>
                    <li><a href="register.do">Register</a></li>
                    <%            } else {
                    %>
                    <li><a href="signout.do">Logout</a></li>
                    <%            }
                    %>
				<li><a href="#">About</a></li>
				<li><a href="#">Contact</a></li>
			</ul>
		</div>
	</div>
	<!-- end #header -->
	<div id="page" class="container">
		<div id="content">
			<div class="post">
				<h2 class="title"><a href="#">Welcome</a></h2>

             <form action="" method="post" name="queryForm" id="queryForm">
                <input type="text" name="query" size="70" title="Enter your query here" style="font-size:large"/>
                <br>
                <h3>
                    <input type="submit" style="font-size:large" value="File Search" title="Searches the local FTP and SMB servers" onclick="document.getElementById('queryForm').setAttribute('action','fileSearch.do');"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="submit" style="font-size:large" value="Web Search" title="Searches the local web servers" onclick="document.getElementById('queryForm').setAttribute('action','webSearch.do');"/>
                </h3>
                <h5>
                    <a href="fileSearch.do" style="font-size:small;color:#888"><i>Advanced File Search</i></a>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="webSearch.do" style="font-size:small;color:#888"><i>Advanced Web Search</i></a>
                </h5>
            </form>
                            


				<p class="meta"><span class="date">October 24, 2011</span><span class="posted">Posted by <a href="#">Someone</a></span></p>
				<div style="clear: both;">&nbsp;</div>
				<div class="entry">
					<p>This is <strong>Plain & Clean </strong>, a free, fully standards-compliant CSS template designed by FreeCssTemplates<a href="http://www.nodethirtythree.com/"></a> for <a href="http://www.freecsstemplates.org/">Free CSS Templates</a>.  This free template is released under a <a href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0</a> license, so you’re pretty much free to do whatever you want with it (even use it commercially) provided you keep the links in the footer intact. Aside from that, have fun with it :)</p>
					<p>Sed lacus. Donec lectus. Nullam pretium nibh ut turpis. Nam bibendum. In nulla tortor, elementum ipsum. Proin imperdiet est. Phasellus dapibus semper urna. Pellentesque ornare, orci in felis. Donec ut ante. In id eros. Suspendisse lacus turpis, cursus egestas at sem.</p>
					<p class="links"><a href="#" class="more">Read More</a><a href="#" title="b0x" class="comments">Comments</a></p>
				</div>
			</div>
			<div class="post">
				<h2 class="title"><a href="#">Lorem ipsum sed aliquam</a></h2>
				<p class="meta"><span class="date">October 20, 2011</span><span class="posted">Posted by <a href="#">Someone</a></span></p>
				<div style="clear: both;">&nbsp;</div>
				<div class="entry">
					<p>Sed lacus. Donec lectus. Nullam pretium nibh ut turpis. Nam bibendum. In nulla tortor, elementum vel, tempor at, varius non, purus. Mauris vitae nisl nec metus placerat consectetuer. Donec ipsum. Proin imperdiet est. Phasellus <a href="#">dapibus semper urna</a>. Pellentesque ornare, orci in consectetuer hendrerit, urna elit eleifend nunc, ut consectetuer nisl felis ac diam. Etiam non felis. Donec ut ante. In id eros. Suspendisse lacus turpis, cursus egestas at sem.  Mauris quam enim, molestie in, rhoncus ut, lobortis a, est.</p>
					<p class="links"><a href="#" class="more">Read More</a><a href="#" title="b0x" class="comments">Comments</a></p>
				</div>
			</div>
			<div class="post">
				<h2 class="title"><a href="#">Consecteteur hendrerit </a></h2>
				<p class="meta"><span class="date">October 10, 2011</span><span class="posted">Posted by <a href="#">Someone</a></span></p>
				<div style="clear: both;">&nbsp;</div>
				<div class="entry">
					<p>Sed lacus. Donec lectus. Nullam pretium nibh ut turpis. Nam bibendum. In nulla tortor, elementum vel, tempor at, varius non, purus. Mauris vitae nisl nec metus placerat consectetuer. Donec ipsum. Proin imperdiet est. Phasellus <a href="#">dapibus semper urna</a>. Pellentesque ornare, orci in consectetuer hendrerit, urna elit eleifend nunc, ut consectetuer nisl felis ac diam. Etiam non felis. Donec ut ante. In id eros. Suspendisse lacus turpis, cursus egestas at sem.  Mauris quam enim, molestie in, rhoncus ut, lobortis a, est.</p>
					<p class="links"><a href="#" class="more">Read More</a><a href="#" title="b0x" class="comments">Comments</a></p>
				</div>
			</div>
			<div style="clear: both;">&nbsp;</div>
		</div>
		<!-- end #content -->
		<div id="sidebar">
			<ul>
				<li>
					<div id="search" >
						<form method="get" action="#">
							<div></div>
						</form>
					</div>
					<div style="clear: both;">&nbsp;</div>
				</li>
				<li>
					<h2>Aliquam tempus</h2>
					<p>Mauris vitae nisl nec metus placerat perdiet est. Phasellus dapibus semper consectetuer hendrerit.</p>
				</li>
				<li>
					<h2>Categories</h2>
					<ul>
						<li><a href="#">Aliquam libero</a></li>
						<li><a href="#">Consectetuer adipiscing elit</a></li>
						<li><a href="#">Metus aliquam pellentesque</a></li>
						<li><a href="#">Suspendisse iaculis mauris</a></li>
						<li><a href="#">Urnanet non molestie semper</a></li>
						<li><a href="#">Proin gravida orci porttitor</a></li>
					</ul>
				</li>
				<li>
					<h2>Blogroll</h2>
					<ul>
						<li><a href="#">Aliquam libero</a></li>
						<li><a href="#">Consectetuer adipiscing elit</a></li>
						<li><a href="#">Metus aliquam pellentesque</a></li>
						<li><a href="#">Suspendisse iaculis mauris</a></li>
						<li><a href="#">Urnanet non molestie semper</a></li>
						<li><a href="#">Proin gravida orci porttitor</a></li>
					</ul>
				</li>
				<li>
					<h2>Archives</h2>
					<ul>
						<li><a href="#">Aliquam libero</a></li>
						<li><a href="#">Consectetuer adipiscing elit</a></li>
						<li><a href="#">Metus aliquam pellentesque</a></li>
						<li><a href="#">Suspendisse iaculis mauris</a></li>
						<li><a href="#">Urnanet non molestie semper</a></li>
						<li><a href="#">Proin gravida orci porttitor</a></li>
					</ul>
				</li>
			</ul>
		</div>
		<!-- end #sidebar -->
		<div style="clear: both;">&nbsp;</div>
	</div>
	<!-- end #page -->
</div>
<div id="footer-content" class="container">
	<div id="footer-bg">
		<div id="column1">
			<h2>Welcome to my website</h2>
			<p>In posuere eleifend odio quisque semper a</p>
		</div>
		<div id="column2">
			<h2>Etiam rhoncus volutpat</h2>
			<p>Sed lacus. Donec lectus. Nullam pretium n</p>
		</div>
		<div id="column3">
			<h2>Recommended Links</h2>
			<ul>
				<li><a href="#">Consectetuer adipiscing elit</a></li>
				<li><a href="#">Metus aliquam pellentesque</a></li>
			</ul>
		</div>
	</div>
</div>
<div id="footer">
	<p>Copyright (c) 2012 filekitty.com. All rights reserved. Developed by arpan gupta.</p>
</div>
<!-- end #footer -->
</body>
</html>


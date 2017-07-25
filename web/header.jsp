<%-- 
    Document   : header
    Created on : Mar 4, 2011, 1:08:00 AM
    Author     : Bloodhound
--%>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%!HttpSession session;
    String userID;
%>
<%
            session = request.getSession(true);

            if (session != null) {
                userID = (String) session.getAttribute("userID");
            }
%>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="images/style.css" type="text/css" media="all"  />
        <title>Loogle</title>
    </head>

    <body>
        <script type="text/javascript" language="javascript">
            var browserType;
            var  flag= 0;
            if (document.layers) {browserType = "nn4"}
            if (document.all) {browserType = "ie"}
            if (window.navigator.userAgent.toLowerCase().match("gecko")) {
                browserType= "gecko"
            }

            function hide2(divName) {
                if (browserType == "gecko" )
                    document.poppedLayer = eval('document.getElementById(divName)');
                else if (browserType == "ie")
                    document.poppedLayer = eval('document.getElementById(divName)');
                else
                    document.poppedLayer = eval('document.layers[divName]');
                document.poppedLayer.style.display = "none";
            }

            function show2(divName) {
                if (browserType == "gecko" )
                    document.poppedLayer = eval('document.getElementById(divName)');
                else if (browserType == "ie")
                    document.poppedLayer = eval('document.getElementById(divName)');
                else
                    document.poppedLayer = eval('document.layers[divName]');
                document.poppedLayer.style.display = "inline";
            }
            function toggle(divName){
                if(flag == 0) show2(divName);
                else hide2(divName);
                flag = 1-flag;
            }
            function sizeQueryTypeChange(selectBox)
            {
                if(selectBox.options[selectBox.selectedIndex].value == "any")
                {
                    document.searchForm.sizeValue.disabled = true;
                    document.searchForm.sizeUnit.disabled = true;
                }
                else
                {
                    document.searchForm.sizeValue.disabled = false;
                    document.searchForm.sizeUnit.disabled = false;
                }
            }
            function serversQueryChange(selectBox)
            {
                if(selectBox.options[selectBox.selectedIndex].value == "ip")
                    document.searchForm.serversValue.disabled = false;
                else
                    document.searchForm.serversValue.disabled = true;
            }
            function fileTypeChange(selectBox)
            {
                if(selectBox.options[selectBox.selectedIndex].value == "extension")
                    document.searchForm.fileExtension.disabled = false;
                else
                    document.searchForm.fileExtension.disabled = true;
            }


        </script>
        <div id="header">
            <%
                if(userID!=null)
                {
            %>
            <div align="right">
                <font size="2">
                    <b>Welcome <a href="editProfile.do" style="color:white" title="Edit your profile"><%=userID%></a></b> &nbsp;&nbsp;
                </font>
            </div>
            <%
                }
            %>
            <h1 id="logo"><a href="index.jsp" style="color:white;">Intranet File Search</a></h1>
            <div id="slogan"><a href="index.jsp" style="color:white;">SRMCEM Lucknow</a></div>
        </div>
        <div id="nav">
            <div id="nbar">
                <ul>
                    <li id="selected"><a href="fileSearch.do">Home</a></li>
                    <li><a href="webSearch.do">Web Search</a></li>
                    <li><a href="servers.do">Servers</a></li>
                    <li><a href="about.do">About</a></li>
                    <%
            if (userID == null) {
                    %>
                    <%--
                    <li><a onclick="toggle('login')" style="cursor:pointer">Login</a></li>
                    --%>
                    <li><a href="signin.do">Login</a></li>
                    <li><a href="register.do">Register</a></li>
                    <%  } else {
                    %>
                    <li><a href="signout.do">Logout</a></li>
                    <%            }
                    %>
                    
                </ul>
            </div>
        </div>
    </body>
</html>

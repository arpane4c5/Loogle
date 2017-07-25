<%-- 
    Document   : createuser
    Created on : Apr 29, 2011, 6:21:36 AM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%!HttpSession session;
    String adminID;
%>
<%
            session = request.getSession(true);
            if (session != null)
            {
                adminID = (String) session.getAttribute("adminID");
            }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loogle</title>
    </head>
    <body>
        <div id="content">
        <h1>
            Create User on Server
        </h1>
        <br>
        <html:form action="adduser.do" method="post">
                <table width="100%" cellspacing="15px">
                    <tr>
                        <td align="right" width="25%">
                            Username :
                        </td>
                        <td axis="left" width="25%">
                            <html:text property="username"/>
                        </td>
                        <td align="left" width="50%">
                            <html:errors property="username"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" width="25%">
                            Password :
                        </td>
                        <td align="left" width="25%">
                            <html:password property="password"/>
                        </td>
                        <td align="left" width="50%">
                            <html:errors property="password"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" width="25%">
                           Retype Password :
                        </td>
                        <td align="left" width="25%">
                            <html:password property="cpassword"/>
                        </td>
                        <td align="left" width="50%">
                            <html:errors property="cpassword"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <html:reset value="Reset"/>
                        </td>
                        <td align="left">
                            <html:submit value="Submit"/>
                        </td>
                    </tr>
                </table>
            </html:form>
        </div>
    </body>
</html>

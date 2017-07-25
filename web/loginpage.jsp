<%-- 
    Document   : loginpage
    Created on : Mar 24, 2011, 10:31:54 PM
    Author     : arpan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server

            response.setHeader("Cache-Control", "no-store");//Directs caches not to store the page under any circumstance

            response.setHeader("Pragma", "no-cache");//HTTP 1.0 backward compatibility

            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="images/style.css" type="text/css" media="all"  />
        <title>Loogle</title>

    </head>
    <body>
        <div id="content">
            <h1>Login</h1>
            <br>
            <html:form action="Login.do" method="post" >
                <table width="100%" cellspacing="15px">
                    <tr>
                        <td>
                            <%--
                            <html:errors property="invalidLogin" />
                            
                            <html:messages id=""
                           --%>
                        </td>
                    </tr>
                <tr>
                    <td align="right" width="25%">
                        Username :
                    </td>
                    <td align="left" width="25%">
                        <html:text property="username" />
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
                        <html:password property="password" />
                    </td>
                    <td align="left" width="50%">
                        <html:errors property="password"/>
                    </td>
                </tr>
        <%
            java.util.Date today = new java.util.Date();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = s.format(today);
        %>
        <html:hidden property="timestamp" value='<%=timestamp%>' />

                <tr>
                    <td align="right" width="25%">
                        <html:reset value="Reset" />
                    </td>
                    <td align="left" width="25%">
                        <html:submit value="Submit" />
                    </td>
                    <td width="50%"></td>
                </tr>
                <tr>
                    <td align="center" colspan="2" width="25%">
                        <a href="forgotPwdPage.do">
                            <font style="font-size:small">Forgot Password</font>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" width="25%">
                        <a href="register.do">New User Sign Up</a>
                    </td>
                    <td width="50%"></td>
                </tr>
            </table>
    </html:form>
        </div>
    </body>
</html>

<%--
    Document   : adminEditProfile
    Created on : Apr 14, 2011, 3:57:35 PM
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
            <h1>Edit Profile ::</h1>
            <br>
            <html:form action="adminChangePwd.do" method="post" >
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
                        Current Password :
                    </td>
                    <td align="left" width="25%">
                        <html:password property="currPassword" />
                    </td>
                    <td align="left" width="50%">
                        <html:errors property="currPassword"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="25%">
                        New Password :
                    </td>
                    <td align="left" width="25%">
                        <html:password property="newPassword" />
                    </td>
                    <td align="left" width="50%">
                        <html:errors property="newPassword" />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="25%">
                        Retype Password :
                    </td>
                    <td align="left" width="25%">
                        <html:password property="cNewPassword" />
                    </td>
                    <td align="left" width="50%">
                        <html:errors property="cNewPassword"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" width="25%">
                        <html:reset value="Reset" />
                    </td>
                    <td align="left" width="25%">
                        <html:submit value="Submit" />
                    </td>
                    <td width="50%"></td>
                </tr>
            </table>
    </html:form>
        </div>
    </body>
</html>

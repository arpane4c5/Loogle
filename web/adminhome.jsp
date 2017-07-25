<%-- 
    Document   : adminhome
    Created on : Mar 19, 2011, 3:14:43 AM
    Author     : Arpan
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
<%
    if(adminID==null)
    {
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loogle</title>
    </head>
    <body>
        <div id="content">
        <h1>
            Administrator Login
        </h1>
        <br>
        <html:form action="adminlogin.do" method="post">
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
        <%
            java.util.Date today = new java.util.Date();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = s.format(today);
        %>
        <html:hidden property="timestamp" value='<%=timestamp%>' />
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
<%
    }
            else
            {
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loogle</title>
    </head>
    <body>
        <div id="content">
            <h1>Admin Home</h1>
            <h3>Perform the following functionalities by clicking the corresponding buttons:</h3>
            <h3>Automatic Crawling :</h3>
            <form action="crawl.do" method="post">
                <h3>Default Crawling after every 24 hours (If server is not turned off):</h3>
                <table width="60%" align="center" border="0">
                <tr>
                    <td align="left">
                        For Scheduled crawling process ::
                    </td>
                    <td align="left">
                            <input type="submit" value="Click here"/>
                    </td>
                </tr>
            </table>
            </form>
            <br>
            <h3>Manual Crawling :</h3>
            <form action="FTPManualCrawl.do" method="post">
            <table width="60%" align="center" border="0">
                <tr>
                    <td align="left">
                        Crawl FTP servers ::
                    </td>
                    <td align="left">
                            <input type="submit" value="Click here"/>
                    </td>
                </tr>
            </table>
            </form>
            <form action="SMBManualCrawl.do" method="post">
                <table border="0" width="60%" align="center">
                <tr>
                    <td align="left">
                        Crawl SMB servers ::
                    </td>
                    <td align="left">
                            <input type="submit" value="Click here"/>
                    </td>
                </tr>
            </table>
            </form>
            <form action="webManualCrawl.do" method="post">
                <table border="0" width="60%" align="center">
                <tr>
                    <td align="left">
                        Crawl Web servers ::
                    </td>
                    <td align="left">
                            <input type="submit" value="Click here"/>
                    </td>
                </tr>
            </table>
            </form>            
        </div>
        <%--
        <%
        try
        {
            intranetFileSearch.FTPTriggerRunner trigger=new intranetFileSearch.FTPTriggerRunner();
            trigger.task();
        }
        catch(Exception e)
        {
            System.out.println("Excetion in admin home :: "+e.getMessage());
        }
        %>
        --%>
    </body>
</html>
<%
    }
%>
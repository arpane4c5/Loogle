<%-- 
    Document   : signinfail
    Created on : Apr 9, 2011, 11:40:36 AM
    Author     : arpan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loogle</title>
    </head>
    <body>
        <div id="content">
            <table width="100%" border="0">
                <tr>
                    <td>
                        <h1>Invalid attempt to login. </h1>
                        <h2><a href="javascript:history.back(-1);">Back to Login.</a></h2>
                        <html:messages id="LoginError"/>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>

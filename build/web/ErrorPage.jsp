<%-- 
    Document   : ErrorPage
    Created on : Mar 4, 2011, 2:47:03 PM
    Author     : Bloodhound
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
                        <h1>Error occurred !</h1>
                        <html:messages id="LoginError"/>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>

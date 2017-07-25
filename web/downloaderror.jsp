<%-- 
    Document   : downloaderror
    Created on : Apr 11, 2011, 10:32:18 PM
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
                        <h1>The file cannot be downloaded due to one of the following reasons:</h1>
                        <ul>
                            <li><h4>The file has been removed from the source path.</h4></li>
                            <li><h4>You may be trying to download a directory.</h4></li>
                            <li><h4>Access to the file is denied.</h4></li>
                            <li><h4>Network Connection problem.</h4></li>
                        </ul>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>

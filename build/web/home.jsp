<%-- 
    Document   : layout
    Created on : Mar 4, 2011, 1:30:19 AM
    Author     : Bloodhound
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-tiles.tld" prefix="t" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="images/style.css" type="text/css" media="all"  />
        <title>Loogle</title>
    </head>
    <body>
        <table align="center" width="100%" border="0">
            <tr>
                <td colspan="2" valign="middle">
                    <t:insert attribute="header"/>
                </td>
            </tr>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td valign="top">
                                <t:insert attribute="body"/>
                            </td>
                            <td valign="top">
                                <t:insert attribute="rightmenu"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" valign="middle">
                    <t:insert attribute="footer"/>
                </td>
            </tr>
        </table>
    </body>
</html>

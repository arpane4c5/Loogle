<%-- 
    Document   : About
    Created on : 13 Dec, 2010, 6:32:47 PM
    Author     : Arpan Gupta
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" language="java" import="java.util.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server

            response.setHeader("Cache-Control", "no-store");//Directs caches not to store the page under any circumstance

            response.setHeader("Pragma", "no-cache");//HTTP 1.0 backward compatibility

            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="images/style.css" type="text/css" media="all"  />
        <title>Loogle</title>
    </head>
    <body>
                        <br/><br/>
                            <div id = "TextMessage" > 
                                Intranet File Search has been made on the lines of DC++.<br/><br/>
                                <h3>Men Behind</h3>
                                <ul>
                                    <li>Ankit Shukla</li>
                                    <li>Arpan Gupta</li>

                                </ul>
                            <br/>
                                <h3>Internal Details</h3>
                                <ul>
                                    <li>Intranet LAN is scanned for ftp servers and SMB shares on a daily basis.</li>
                                    <li>The files found on the ftp servers and the smb shares are indexed using their path and IP address.</li>
                                    <li>The search query entered by the users is compared to the path and filenames.</li>
                                    <li>The web search indexes web pages of the websites hosted on the intranet servers.</li>
                                    <li>The contents of the web pages are not indexed with the File Search indexes but are indexed separately.</li>
                                    <li>The users can also register to receive updates from the servers, provided they give their intranet email id.(eg. ankit@loogle.com)</li>
                                    <!--
                                    <li>The servers determined/found out by daily scan are pinged every 15 mins to determine if they are currently active.</li>
                                    <li>Servers found inactive for a period of more than 7 days are removed from the list of ftp servers.</li>
                                    <li>Filelist for servers are refreshed 4 times a day.</li>
                                    -->
                                </ul>
                            <br/><br/>
                            <i>Please mail comments, requests or suggestions to <b>arpangupta@hotmail.com</b> or <b>mailankitshukla@gmail.com</b></i>
                
                        <br />
                            </div>
            <!-- End Wrap2 -->
        <!-- End Wrap -->
    </body>
</html>

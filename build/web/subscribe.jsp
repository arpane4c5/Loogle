<%-- 
    Document   : subscribe
    Created on : Mar 27, 2011, 1:44:19 AM
    Author     : arpan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
 <jsp:useBean id="queryBean" class="intranetFileSearch.Query" scope="request" />
<%
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server

            response.setHeader("Cache-Control", "no-store");//Directs caches not to store the page under any circumstance

            response.setHeader("Pragma", "no-cache");//HTTP 1.0 backward compatibility

            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="images/style.css" type="text/css" media="all"  />
        <title>Loogle</title>
    </head>
    <body>
                    <div id="content" >
                    <%
                        Vector <String []> results = queryBean.getServerList();
                        if(results==null)
                        {
                    %>
                        <h2>Error</h2>
                        <h4> Internal Server Error </h4>
                        <p> Could not retrive the list of servers. Please retry after some time.</p>
                        <br />
                    <%
                        }
                        else
                        {
                     %>
                           <h1>Subscribe to feeds from servers</h1>
                           <%
                                if(results.size()==0)
                                {
                                    out.println("<h5> No servers found.</h5><br />)");
                                }
                                else
                                {
                           %>
                           <form action="UpdateSubscribe" class="special">
                            <table align="center" width="100%" cellpadding="8px">
                                <tr bgcolor="#DDDDDD">
                                    <th><center><b>SR.No</b></center></th>
                                    <th><center><b>IP</b></center></th>
                                    <th><center><b>Share</b></center></th>
                                    <th><center><b>Status</b></center></th>
                                    <th><center><b>Server Type</b></center></th>
                                    <th><center><b>Update Now</b></center></th>
                                    <th><center><b>Subscribe</b></center></th>
                                </tr>
                                <%
                                    boolean colour=true;
                                    for(int i=0; i<results.size(); i++)
                                    {
                                        if(colour)
                                        {
                                            out.println("<tr bgcolor=\"#F0F0F0\">");
                                            colour=false;
                                        }
                                        else
                                        {
                                            colour=true;
                                            out.println("<tr bgcolor=\"#F9F9F9\">");
                                        }
                                        String []row=results.elementAt(i);
                                        int k=i+1;
                                        out.println("<td width=\"10%\"><center>"+k+" </center></td>");
                                        out.println("<td width=\"30%\"><center>"+row[0]+"</center></td>");
                                        out.println("<td width=\"15%\"><center>"+row[1]+"</center></td>");
                                        out.println("<td width=\"15%\"><center>"+row[2]+"</center></td>");
                                        out.println("<td width=\"10%\"><center>"+row[3]+"</center></td>");
                                        String key=null;
                                        if(row[3].compareTo("FTP")==0)
                                            key=row[0]+":"+"0";
                                        if(row[3].compareTo("SMB")==0)
                                            key=row[0]+":"+"1";
                                        out.println("<td width=\"10%\"><center><input type=\"checkbox\" name=\""+key+":0\" value=\"1\"></center></td>");
                                        out.println("<td width=\"10%\"><center><input type=\"checkbox\" name=\""+key+":1\" value=\"1\"></center></td>");
                                        out.println("</tr>");
                                    }
                                %>
                             </table>
                             <table bgcolor="#FFFFFF" border="0" align="center" width="100%">
                                 <tr>
                                     <td width=50%>
                                         <center>
                                             <input type="submit" value="Submit"/>
                                        </center>
                                     </td>
                                     <td width=50%>
                                         <center>
                                            <input type="reset" value="reset"/>
                                         </center>
                                     </td>
                                 </tr>
                                 <tr><td></td> </tr>
                             </table>
                         </form>
                         <br />
                    <%
                            }
                        }
                    %>
                    </div>
    </body>
</html>

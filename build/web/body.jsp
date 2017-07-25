<%-- 
    Document   : body
    Created on : Mar 4, 2011, 1:08:23 AM
    Author     : Bloodhound
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.*,java.util.*,java.net.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<jsp:useBean id="queryBean" class="intranetFileSearch.Query" scope="request" />
<%
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server

            response.setHeader("Cache-Control", "no-store");//Directs caches not to store the page under any circumstance

            response.setHeader("Pragma", "no-cache");//HTTP 1.0 backward compatibility

            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale
%>
<%!
    String searchForm;
    List<intranetFileSearch.SearchResult> results;
    Map parameterMap;
%>
<%
            results=null;
            parameterMap=request.getParameterMap();
            searchForm=intranetFileSearch.Search.genForm(parameterMap);
            parameterMap = request.getParameterMap();
            String query = request.getParameter("query");
            if (query != null && query.compareTo("") != 0)
                results = intranetFileSearch.Search.processQuery(query, parameterMap);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Loogle</title>
    </head>
    <body>
                            <div id="content">
                        <h1>Search</h1>
                        <%
                         out.println(searchForm);
                         //System.out.println(searchForm);
                         if(query!=null && query.compareTo("")!=0)
                         {
                             if(results==null)
                                 out.println("<h3>Internal Server Error</h3>. An error occured while trying to process your query. Please try after some time.<br />");
                             else
                             {
                                if(results.size()==0)
                                    out.println("No results found.<br />");
                                else
                                {
                        %>

                        <table width=100% cellpadding=8px>
                            <tr bgcolor="#DDDDDD">
                                <th><center><b>SR.No</b></center></th>
                                <th><center><b>FileName</b></center></th>
                                <th><center><b>Type</b></center></th>
                                <th><center><b>Size</b></center></th>
                                <th><center><b>Ip</b></center></th>
                                <th><center><b>Server Type</b></center></th>
                                <th><center><b>Download</b></center></th>
                            </tr>
                            <%
                                boolean colour=true;
                                for(int i=0; i < results.size(); i++) {
                                    if(colour){
                                        out.println("<tr bgcolor=\"#F0F0F0\">");
                                        colour=false;
                                        }
                                    else {
                                        colour=true;
                                        out.println("<tr bgcolor=\"#F9F9F9\">");
                                        }
                                    int j=i+1;
                                    intranetFileSearch.SearchResult sr=results.get(i);

                                    out.println("<td width=\"5%\"><center>"+j+"</center></td>");
                                    out.println("<td width=\"50%\"><center><A title=\""+sr.getFilePath()+
                                            "\">"+sr.getFileName()+"</a></center></td>");
                                    out.println("<td width=\"7%\"><center>"+sr.getFileType()+"</center></td>");
                                    String size=queryBean.getFileSize(sr.getFileSize());
                                    out.println("<td width=\"7%\"><center>"+size+"</center></td>");
                                    out.println("<td width=\"18%\"><center>"+sr.getServerIp()+"</center></td>");
                                    out.println("<td width=\"8%\"><center>"+sr.getTypeOfServer()+"</center></td>");
                                    if(sr.getTypeOfServer().equals("SMB"))
                                    {
                                        out.println("<td width=\"5%\"><center><form action=\"SMBDownload.do\" target=\"_blank\" method=\"post\">" +
                                            "<input type=\"hidden\" name=\"path\" value=\""+sr.getFilePath()+"\"/>" +
                                            "<input type=\"submit\" value=\"Download\"/></form></center></td>");
                                    }
                                    else
                                    {
                                        out.println("<td width=\"5%\"><center><form action=\"FTPDownload.do\" target=\"_blank\" method=\"post\">" +
                                            "<input type=\"hidden\" name=\"path\" value=\""+sr.getFilePath()+"\"/>" +
                                            "<input type=\"submit\" value=\"Download\"/></form></center></td>");
                                    }
                                    out.println("</tr>");
                                }

                                %>
                                </table>
                                <%
                                }
                            }
                         }
                         %>
                            </div>
    </body>
</html>

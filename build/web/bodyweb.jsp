<%-- 
    Document   : bodyweb
    Created on : Mar 19, 2011, 1:06:27 PM
    Author     : Arpan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.*,java.util.*,java.net.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import = "javax.servlet.*, javax.servlet.http.*,org.apache.lucene.analysis.*,
         org.apache.lucene.analysis.standard.StandardAnalyzer, org.apache.lucene.document.*,
         org.apache.lucene.index.*, org.apache.lucene.search.*, org.apache.lucene.queryParser.*,
         org.apache.lucene.demo.*, org.apache.lucene.demo.html.Entities" %>
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
    String query="";

    public String escapeHTML(String s)
    {
        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("\"", "&quot;");
        s = s.replaceAll("'", "&apos;");
        return s;
    }
%>
<%

            results=null;
            parameterMap=request.getParameterMap();
            searchForm=intranetFileSearch.Search.genWebForm(parameterMap);
            //parameterMap = request.getParameterMap();
            query = request.getParameter("query");

            //if (query != null && query.compareTo("") != 0)
            //    results = intranetFileSearch.Search.processQuery(query, parameterMap);
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
        boolean error = false;                  //used to control flow for error messages
        //String indexName = "D:/E/NetBeansProjects/JSearchEngine/index";       //local copy of the configuration variable
        String indexName="C:/Loogle/index";
        IndexSearcher searcher = null;          //the searcher used to open/search the index
        Query queryWeb = null;                     //the Query created by the QueryParser
        Hits hits = null;                       //the search results
        int startindex = 0;                     //the first index displayed on this page
        int maxpage    = 10;                    //the maximum items displayed on this page
        String queryString = null;              //the query entered in the previous page
        String startVal    = null;              //string version of startindex
        String maxresults  = null;              //string version of maxpage
        int thispage = 0;                       //used for the for/next either maxpage or
                                                //hits.length() - startindex - whichever is
                                                //less

        try
        {
          searcher = new IndexSearcher(indexName);      //create an indexSearcher for our page
                                                        //NOTE: this operation is slow for large
                                                        //indices (much slower than the search itself)
                                                        //so you might want to keep an IndexSearcher
                                                        //open

        } catch (Exception e)
        {                         //any error that happens is probably due
                                                        //to a permission problem or non-existant
                                                        //or otherwise corrupt index
%>
                <p>ERROR opening the Index - contact system admin!</p>
                
<%                error = true;                                  //don't do anything up to the footer
        }
%>
<%
                                        //did we open the index?
                queryString = request.getParameter("query");           //get the search criteria
                startVal    = request.getParameter("startat");         //get the start index
                maxresults  = request.getParameter("maxresults");      //get max results per page

                try
                {
                        maxpage    = Integer.parseInt(maxresults);    //parse the max results first
                        startindex = Integer.parseInt(startVal);      //then the start index
                } catch (Exception e) { } //we don't care if something happens we'll just start at 0
                                          //or end at 50


/*
                if (queryString == null)
                        throw new ServletException("no query "+       //if you don't have a query then
                                                   "specified");      //you probably played on the
                                                                      //query string so you get the
                                                                      //treatment
*/
                if (queryString!=null)
                {
                Analyzer analyzer = new StandardAnalyzer();           //construct our usual analyzer
                try
                {
                        QueryParser qp = new QueryParser("contents", analyzer);
                        queryWeb = qp.parse(queryString); //parse the
                } catch (ParseException e) {                          //query and construct the Query
                                                                      //object
                                                                      //if it's just "operator error"
                                                                      //send them a nice error HTML

                    if(query.compareTo("")!=0)
                    {
%>
                        <p>Error while parsing query.</p>
<%
                    }
                        error = true;                                 //don't bother with the rest of
                                                                      //the page
                }
        }
                else
                    error=true;
%>
<%
        if (error == false && searcher != null) {                     // if we've had no errors
                                                                      // searcher != null was to handle
                                                                      // a weird compilation bug
                thispage = maxpage;                                   // default last element to maxpage
                hits = searcher.search(queryWeb);                        // run the query
                if (hits.length() == 0) {                             // if we got no results tell the user
%>
                <p> Unable to find the query string. </p>
<%
                error = true;                                        // don't bother with the rest of the
                                                                     // page
                }
        }

        if (error == false && searcher != null) {
%>
           <table width=100% cellpadding=8px>
               <tr bgcolor="#DDDDDD">
                   <th><center><b>Document</b></center></th>
                   <th><center><b>Summary</b></center></th>
                </tr>
<%
                if ((startindex + maxpage) > hits.length()) {
                        thispage = hits.length() - startindex;      // set the max index to maxpage or last
                }                                                   // actual search result whichever is less

                boolean colour=true;
                for (int i = startindex; i < (thispage + startindex); i++) {  // for each element

                        if(colour){
                               out.println("<tr bgcolor=\"#F0F0F0\">");
                               colour=false;
                        }
                        else {
                               colour=true;
                               out.println("<tr bgcolor=\"#F9F9F9\">");
                        }
                        Document doc = hits.doc(i);                    //get the next document
                        String doctitle = doc.get("title");            //get its title
                        String url = doc.get("path");                  //get its path field
                        if (url != null && url.startsWith("../webapps/")) { // strip off ../webapps prefix if present
                                url = url.substring(10);
                        }
                        if ((doctitle == null) || doctitle.equals("")) //use the path if it has no title
                                doctitle = url;
                                                                       //then output!

                        out.println("<td width=\"25%\"><center><a href=\""+url+"\">"+doctitle+"</a><br><a href=\""+url+"\"><font size=1 color='grey'>"+url+"</font></a></center></td>");
                        out.println("<td width=\"75%\"><center>"+doc.get("summary")+"<center></td>");
                        out.println("</tr>");
                }
%>
<%                if ( (startindex + maxpage) < hits.length()) {   //if there are more results...display
                                                                   //the more link

                        String moreurl="webSearch.do?query=" +
                                       URLEncoder.encode(queryString) +  //construct the "more" link
                                       "&amp;maxresults=" + maxpage +
                                       "&amp;startat=" + (startindex + maxpage);
%>
                <tr>
                        <td></td><td><a href="<%=moreurl%>">More Results>></a></td>
                </tr>
<%
                }
%>
                </table>

<%       }                                           
         if (searcher != null)
                searcher.close();
%>




        </div>
    </body>
</html>

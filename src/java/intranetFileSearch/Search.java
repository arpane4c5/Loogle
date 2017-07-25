package intranetFileSearch;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Forms a sql command from the query string and returns search results.
 * @author Arpan Gupta
 */
public class Search {

    private Search() {
    }
    private static final String INITIAL_QUERY_STRING =
            "SELECT * from files where Size > 0";
    private static final Long BYTES_KB = new Long(1024);
    private static final Long BYTES_MB = BYTES_KB * 1024;
    private static final Long BYTES_GB = BYTES_MB * 1024;

    /**
     * Processes the search query and returns the results. Returns null in case
     * of any internal error.
     * @param query Query given by the user.
     * @param constraints Additional constraints on the results
     * @return A list of SearchResults.
     */
    public static List<SearchResult> processQuery(final String query,
            final Map<String, String[]> constraints) {

        String[] words = query.split(" ");
        String queryString = INITIAL_QUERY_STRING;
        boolean queryGiven = false;
        List<SearchResult> results = new ArrayList();

        // TODO  : Enable '+', '"' and dictionary support with
        // advanced query parsing.
        for (String word : words) {
            if (!word.equals("")) {
                queryGiven = true;
                queryString += (" AND Path like '%" + word + "%'");
            }
        }

        if (!queryGiven) {
            // no query word is entered
            return results;
        }

        /* Check for server type constraints
           0 for FTP servers
         * 1 for SMB servers
         */
        if (constraints.containsKey("serverType")) {
            String[] serverType = constraints.get("serverType");
            if (serverType[0].equals("FTP")) {
                queryString += " AND serverType = 0";
            } else if (serverType[0].equals("SMB")) {
                queryString += " AND serverType = 1";
            }
        }

        /* Size Query checking */
        if (constraints.containsKey("sizeUnit")) {

            String[] sizeQueryType = constraints.get("sizeQueryType");
            String[] sizeUnit = constraints.get("sizeUnit");
            String[] sizeValueArg = constraints.get("sizeValue");
            Long sizeValue = Long.parseLong(sizeValueArg[0]);

            if (sizeUnit[0].equals("mb")) {
                sizeValue *= BYTES_MB;
            } else if (sizeUnit[0].equals("gb")) {
                sizeValue *= BYTES_GB;
            } else if (sizeUnit[0].equals("kb")) {
                sizeValue *= BYTES_KB;
            }

            if (sizeQueryType[0].equals("atleast")) {
                queryString += (" AND Size > " + sizeValue.toString());
            } else if (sizeQueryType[0].equals("atmost")) {
                queryString += (" AND Size < " + sizeValue.toString());
            } else if (sizeQueryType[0].equals("exact")) {
                queryString += (" AND Size = " + sizeValue.toString());
            }
        }

        /* Check for sorting constraint */
        if (constraints.containsKey("sortedBy")) {
            String[] sortingCriteria = constraints.get("sortedBy");
            queryString += " ORDER BY";
            if (sortingCriteria[0].equals("name")) {
                queryString += " Name";
            } else if (sortingCriteria[0].equals("ip")) {
                queryString += " Ip";
            } else if (sortingCriteria[0].equals("size")) {
                queryString += " Size";
            } else if (sortingCriteria[0].equals("type")) {
                queryString += " Extension";
            }
            queryString += (" " + constraints.get("sortOrder")[0]);
        }

        if (constraints.containsKey("maxResults")) {
            queryString += (" LIMIT " + constraints.get("maxResults")[0]);
        }

        QueryProcessor qp = new QueryProcessor();
        try {
            qp.init();
            qp.connect();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        try {
            ResultSet resultSet = qp.getRecords(queryString);
            while (resultSet.next()) {
                String ip = resultSet.getString(1);
                String path = resultSet.getString(2);
                String fileName = resultSet.getString(3);
                String extention = resultSet.getString(4);
                Long size = resultSet.getLong(5);
                int serverType = resultSet.getInt(7);
                SearchResult searchResult = new SearchResult(ip, path,
                        fileName, extention, size, serverType);
                results.add(searchResult);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            qp.close();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return results;
    }

    public static String genWebForm(final Map<String, String[]> paramMap)
    {
        String form = "<form name=\"searchForm\" action=\"webSearch.do\" method=\"post\">\n";
        String query = "";
        if (paramMap.containsKey("query")) {
            String[] temp = paramMap.get("query");
            query += temp[temp.length - 1];
            System.err.println("Search.java : Found search query :" + query);
        }
        else
        {
            System.err.println("Search.java : Found no search query string");
        }
        form = form + "<p>Query&nbsp;&nbsp;<input type=\"text\" name=\"query\" value=\""
                + query + "\"/>&nbsp;&nbsp;\n<input type=\"submit\" value=\"Search\"/>\n</p>\n";

        form = form + "<p>Max results per page&nbsp;&nbsp;<select name=\"maxresults\">\n<option value=\"50\" selected>50</option>" +
                "\n<option value=\"25\">25</option>\n<option value=\"10\">10</option>\n<option value=\"5\">5</option>\n</select>\n";
        form+= "</form>";
        return form;
    }

    public static String genForm(final Map<String, String[]> paramMap) {
        String form = "<form name=\"searchForm\" action=\"fileSearch.do\" method=\"post\">\n";
        String query = "";
        if (paramMap.containsKey("query")) {
            String[] temp = paramMap.get("query");
            query += temp[temp.length - 1];
            System.err.println("Search.java : Found search query :" + query);
        }
        else {
            System.err.println("Search.java : Found no search query string");
        }
        form = form + "<p>Filename&nbsp;&nbsp;<input type=\"text\" name=\"query\" value=\""
                + query + "\"/>&nbsp;&nbsp;\n<input type=\"submit\" value=\"Search\"/>\n</p>\n<h2>Advanced Options</h2>\n" +
                "<p>\nServer Type&nbsp;\n<select name=\"serverType\">\n";
        String serverType = "ALL";
        if (paramMap.containsKey("serverType")) {
            serverType = paramMap.get("serverType")[0];
        }
        if (serverType.compareTo("ALL") == 0) {
            form += "<option value=\"ALL\" selected>&nbsp;ALL&nbsp;</option>\n";
        } else {
            form += "<option value=\"ALL\" >&nbsp;ALL&nbsp;</option>\n";
        }
        if (serverType.compareTo("FTP") == 0) {
            form += "<option value=\"FTP\" selected>&nbsp;FTP&nbsp;</option>\n";
        } else {
            form += "<option value=\"FTP\" >&nbsp;FTP&nbsp;</option>\n";
        }
        if (serverType.compareTo("SMB") == 0) {
            form += "<option value=\"SMB\" selected>&nbsp;SMB&nbsp;</option>\n";
        } else {
            form += "<option value=\"SMB\" >&nbsp;SMB&nbsp;</option>\n";
        }
        form += "</select>\n</p>\n<p>\nSort by&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n<select name=\"sortedBy\">";

        String sortedBy = "size";
        if (paramMap.containsKey("sortedBy")) {
            sortedBy = paramMap.get("sortedBy")[0];
        }

        if (sortedBy.compareTo("name") == 0) {
            form += "<option value=\"name\" selected>&nbsp;Name&nbsp;</option>\n";
        } else {
            form += "<option value=\"name\" >&nbsp;Name&nbsp;</option>\n";
        }
        if (sortedBy.compareTo("type") == 0) {
            form += "<option value=\"type\" selected>&nbsp;Type&nbsp;</option>\n";
        } else {
            form += "<option value=\"type\" >&nbsp;Type&nbsp;</option>\n";
        }
        if (sortedBy.compareTo("size") == 0) {
            form += "<option value=\"size\" selected>&nbsp;Size&nbsp;</option>\n";
        } else {
            form += "<option value=\"size\" >&nbsp;Size&nbsp;</option>\n";
        }
        if (sortedBy.compareTo("ip") == 0) {
            form += "<option value=\"ip\" selected>&nbsp;IP&nbsp;</option>\n";
        } else {
            form += "<option value=\"ip\" >&nbsp;IP&nbsp;</option>\n";
        }
        form += "</select>\n<select name=\"sortOrder\">\n";

        String sortOrder = "DESC";
        if (paramMap.containsKey("sortOrder")) {
            sortOrder = paramMap.get("sortOrder")[0];
        }

        if (sortOrder.compareTo("ASC") == 0) {
            form += "<option value=\"ASC\" selected>&nbsp;Ascending&nbsp;</option>\n<option value=\"DESC\" >&nbsp;Descending&nbsp;</option>\n";
        } else {
            form += "<option value=\"ASC\" >&nbsp;Ascending&nbsp;</option>\n<option value=\"DESC\" selected>&nbsp;Descending&nbsp;</option>\n";
        }
        form += "</select>\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\nAdded within&nbsp;&nbsp;\n<select name=\"modifiedUnit\" >\n";

        String modifiedUnit = "anytime";
        if (paramMap.containsKey("modifiedUnit")) {
            modifiedUnit = paramMap.get("modifiedUnit")[0];
        }

        if (modifiedUnit.compareTo("anytime") == 0) {
            form += "<option value=\"anytime\" selected>&nbsp;Anytime&nbsp;</option>\n";
        } else {
            form += "<option value=\"anytime\" >&nbsp;Anytime&nbsp;</option>\n";
        }
        if (modifiedUnit.compareTo("1") == 0) {
            form += "<option value=\"1\" selected>&nbsp;1 Day&nbsp;</option>\n";
        } else {
            form += "<option value=\"1\" >&nbsp;1 Day&nbsp;</option>\n";
        }
        if (modifiedUnit.compareTo("2") == 0) {
            form += "<option value=\"2\" selected>&nbsp;2 Days&nbsp;</option>\n";
        } else {
            form += "<option value=\"2\" >&nbsp;2 Days&nbsp;</option>\n";
        }
        if (modifiedUnit.compareTo("3") == 0) {
            form += "<option value=\"3\" selected>&nbsp;3 Days&nbsp;</option>\n";
        } else {
            form += "<option value=\"3\" >&nbsp;3 Days&nbsp;</option>\n";
        }
        if (modifiedUnit.compareTo("4") == 0) {
            form += "<option value=\"4\" selected>&nbsp;4 Days&nbsp;</option>\n";
        } else {
            form += "<option value=\"4\" >&nbsp;4 Days&nbsp;</option>\n";
        }
        if (modifiedUnit.compareTo("5") == 0) {
            form += "<option value=\"5\" selected>&nbsp;5 Days&nbsp;</option>\n";
        } else {
            form += "<option value=\"5\" >&nbsp;5 Days&nbsp;</option>\n";
        }
        form += "</select>\n</p>\n<p>\nSize&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n<select name=\"sizeQueryType\" onChange=\"sizeQueryTypeChange(this)\">\n";

        String sizeQueryType = "any";
        if (paramMap.containsKey("sizeQueryType")) {
            sizeQueryType = paramMap.get("sizeQueryType")[0];
        }

        if (sizeQueryType.compareTo("any") == 0) {
            form += "<option value=\"any\" selected>&nbsp;Any&nbsp;</option>\n";
        } else {
            form += "<option value=\"any\" >&nbsp;Any&nbsp;</option>\n";
        }
        if (sizeQueryType.compareTo("atleast") == 0) {
            form += "<option value=\"atleast\" selected>&nbsp;Atleast&nbsp;</option>\n";
        } else {
            form += "<option value=\"atleast\" >&nbsp;Atleast&nbsp;</option>\n";
        }
        if (sizeQueryType.compareTo("atmost") == 0) {
            form += "<option value=\"atmost\" selected>&nbsp;Atmost&nbsp;</option>\n";
        } else {
            form += "<option value=\"atmost\" >&nbsp;Atmost&nbsp;</option>\n";
        }
        if (sizeQueryType.compareTo("exactsize") == 0) {
            form += "<option value=\"exactsize\" selected>&nbsp;Exact Size&nbsp;</option>\n";
        } else {
            form += "<option value=\"exactsize\" >&nbsp;Exact Size&nbsp;</option>\n";
        }
        form += "</select>\n";

        String sizeValue = "0";
        if (paramMap.containsKey("sizeValue")) {
            sizeValue = paramMap.get("sizeValue")[0];
        }

        if (sizeQueryType.compareTo("any") == 0) {
            form += "<input type=\"text\" name=\"sizeValue\" size=\"5\" maxlength=\"15\" disabled value=\"" + sizeValue + "\">\n<select name=\"sizeUnit\" disabled />\n";
        } else {
            form += "<input type=\"text\" name=\"sizeValue\" size=\"5\" maxlength=\"15\" value=\"" + sizeValue + "\">\n<select name=\"sizeUnit\" />\n";
        }

        String sizeUnit = "mb";
        if (paramMap.containsKey("sizeUnit")) {
            sizeUnit = paramMap.get("sizeUnit")[0];
        }

        if (sizeUnit.compareTo("b") == 0) {
            form += "<option value=\"b\" selected>&nbsp;B&nbsp;</option>\n";
        } else {
            form += "<option value=\"b\" >&nbsp;B&nbsp;</option>\n";
        }
        if (sizeUnit.compareTo("kb") == 0) {
            form += "<option value=\"kb\" selected>&nbsp;KB&nbsp;</option>\n";
        } else {
            form += "<option value=\"kb\" >&nbsp;KB&nbsp;</option>\n";
        }
        if (sizeUnit.compareTo("mb") == 0) {
            form += "<option value=\"mb\" selected>&nbsp;MB&nbsp;</option>\n";
        } else {
            form += "<option value=\"mb\" >&nbsp;MB&nbsp;</option>\n";
        }
        if (sizeUnit.compareTo("gb") == 0) {
            form += "<option value=\"gb\" selected>&nbsp;GB&nbsp;</option>\n";
        } else {
            form += "<option value=\"gb\" >&nbsp;GB&nbsp;</option>\n";
        }
        if (sizeUnit.compareTo("tb") == 0) {
            form += "<option value=\"tb\" selected>&nbsp;TB&nbsp;</option>\n";
        } else {
            form += "<option value=\"tb\" >&nbsp;TB&nbsp;</option>\n";
        }
        form += "</select>\n&nbsp;&nbsp;&nbsp;Servers&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n<select name=\"serversQuery\" onChange=\"serversQueryChange(this)\">\n";

        String serversQuery = "all";
        if (paramMap.containsKey("serversQuery")) {
            serversQuery = paramMap.get("serversQuery")[0];
        }

        if (serversQuery.compareTo("all") == 0) {
            form += "<option value=\"all\" selected>&nbsp;All&nbsp;</option>\n";
        } else {
            form += "<option value=\"all\" >&nbsp;All&nbsp;</option>\n";
        }
        if (serversQuery.compareTo("active") == 0) {
            form += "<option value=\"active\" selected>&nbsp;Active&nbsp;</option>\n";
        } else {
            form += "<option value=\"active\" >&nbsp;Active&nbsp;</option>\n";
        }
        if (serversQuery.compareTo("ip") == 0) {
            form += "<option value=\"ip\" selected>&nbsp;IP&nbsp;</option>\n";
        } else {
            form += "<option value=\"ip\" >&nbsp;IP&nbsp;</option>\n";
        }
        form += "</select>\n";

        String serversValue = "";
        if (paramMap.containsKey("serversValue")) {
            serversValue = paramMap.get("serversValue")[0];
        }

        if (serversQuery.compareTo("ip") == 0) {
            form += "<input type=\"text\" name=\"serversValue\" size=\"15\" maxlength=\"15\" value=\"" + serversValue + "\" />\n";
        } else {
            form += "<input type=\"text\" name=\"serversValue\" size=\"15\" maxlength=\"15\" disabled value=\"" + serversValue + "\" />\n";
        }
        form += "</p>\n<p>File Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n<select name=\"fileType\" onChange=\"fileTypeChange(this)\">\n";

        String fileType = "any";
        if (paramMap.containsKey("fileType")) {
            fileType = paramMap.get("fileType")[0];
        }

        if (fileType.compareTo("any") == 0) {
            form += "<option value=\"any\" selected>&nbsp;Any&nbsp;</option>\n";
        } else {
            form += "<option value=\"any\" >&nbsp;Any&nbsp;</option>\n";
        }
        if (fileType.compareTo("audio") == 0) {
            form += "<option value=\"audio\" selected>&nbsp;Audio&nbsp;</option>\n";
        } else {
            form += "<option value=\"audio\" >&nbsp;Audio&nbsp;</option>\n";
        }
        if (fileType.compareTo("compressed") == 0) {
            form += "<option value=\"compressed\" selected>&nbsp;Compressed&nbsp;</option>\n";
        } else {
            form += "<option value=\"compressed\" >&nbsp;Compressed&nbsp;</option>\n";
        }
        if (fileType.compareTo("document") == 0) {
            form += "<option value=\"document\" selected>&nbsp;Document&nbsp;</option>\n";
        } else {
            form += "<option value=\"document\" >&nbsp;Document&nbsp;</option>\n";
        }
        if (fileType.compareTo("executable") == 0) {
            form += "<option value=\"executable\" selected>&nbsp;Executable&nbsp;</option>\n";
        } else {
            form += "<option value=\"executable\" >&nbsp;Executable&nbsp;</option>\n";
        }
        if (fileType.compareTo("picture") == 0) {
            form += "<option value=\"picture\" selected>&nbsp;Picture&nbsp;</option>\n";
        } else {
            form += "<option value=\"picture\" >&nbsp;Picture&nbsp;</option>\n";
        }
        if (fileType.compareTo("video") == 0) {
            form += "<option value=\"video\" selected>&nbsp;Video&nbsp;</option>\n";
        } else {
            form += "<option value=\"video\" >&nbsp;Video&nbsp;</option>\n";
        }
        if (fileType.compareTo("directory") == 0) {
            form += "<option value=\"directory\" selected>&nbsp;Directory&nbsp;</option>\n";
        } else {
            form += "<option value=\"directory\" >&nbsp;Directory&nbsp;</option>\n";
        }
        if (fileType.compareTo("extension") == 0) {
            form += "<option value=\"extension\" selected>&nbsp;Extension&nbsp;</option>\n";
        } else {
            form += "<option value=\"extension\" >&nbsp;Extension&nbsp;</option>\n";
        }
        form += "</select>\n";

        String fileExtension = "";
        if (paramMap.containsKey("fileExtension")) {
            fileExtension = paramMap.get("fileExtension")[0];
        }

        if (fileType.compareTo("extension") == 0) {
            form += "<input type=\"text\" name=\"fileExtension\" size=\"5\" maxlength=\"10\" value=\"" + fileExtension + "\"/>\n";
        } else {
            form += "<input type=\"text\" name=\"fileExtension\" size=\"5\" maxlength=\"10\" disabled value=\"" + fileExtension + "\"/>\n";
        }
        form += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Max Results&nbsp;&nbsp;&nbsp;\n<select name=\"maxResults\">\n";
        String maxResults = "50";
        if(paramMap.containsKey("maxResults")) {
            maxResults = paramMap.get("maxResults")[0];
        }
        if (maxResults.compareTo("25") == 0) {
            form += "<option value=\"25\" selected>&nbsp;25&nbsp;</option>\n";
        } else {
            form += "<option value=\"25\" >&nbsp;25&nbsp;</option>\n";
        }
        if (maxResults.compareTo("50") == 0) {
            form += "<option value=\"50\" selected>&nbsp;50&nbsp;</option>\n";
        } else {
            form += "<option value=\"50\" >&nbsp;50&nbsp;</option>\n";
        }
        if (maxResults.compareTo("100") == 0) {
            form += "<option value=\"100\" selected>&nbsp;100&nbsp;</option>\n";
        } else {
            form += "<option value=\"100\" >&nbsp;100&nbsp;</option>\n";
        }
        if (maxResults.compareTo("200") == 0) {
            form += "<option value=\"200\" selected>&nbsp;200&nbsp;</option>\n";
        } else {
            form += "<option value=\"200\" >&nbsp;200&nbsp;</option>\n";
        }
        if (maxResults.compareTo("500") == 0) {
            form += "<option value=\"500\" selected>&nbsp;500&nbsp;</option>\n";
        } else {
            form += "<option value=\"500\" >&nbsp;500&nbsp;</option>\n";
        }
        if (maxResults.compareTo("1000") == 0) {
            form += "<option value=\"1000\" selected>&nbsp;1000&nbsp;</option>\n";
        } else {
            form += "<option value=\"1000\" >&nbsp;1000&nbsp;</option>\n";
        }
        form += "</select>\n</p>\n</form>\n";
        return form;
    }

    public static void main(String[] args) {
        Map<String, String[]> m = new HashMap<String, String[]>();
        String query = "Drupal Videos";
        List<SearchResult> results = processQuery(query, m);
        System.out.println(results.size());
        System.out.println(Search.genForm(m));
        for (SearchResult result : results) {
            System.out.println(result.getFilePath());
        }
    }
}

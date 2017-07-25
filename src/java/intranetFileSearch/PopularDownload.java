package intranetFileSearch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Returns the most popular downloads in the recent time.
 */
public class PopularDownload {

    private PopularDownload() { }

    /**
     * Returns the most popular downloads in last 5 days as a list of string.
     * It will return null in case of any internal error/exception.
     * @param numberOfFiles The number of top results required.
     * @return Most popular downloads as a list of strings.
     */
    public static List<String> popularDownloads(final int numberOfFiles) {
        List<String> fileNames = new ArrayList<String>();
        QueryProcessor qp = new QueryProcessor();

        try {
            qp.init();
            qp.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String query = "select Name,count(*) from Downloads Group By Name Order by count(*) DESC LIMIT ";
        query += Integer.toString(numberOfFiles);
        try {
            ResultSet resultSet = qp.getRecords(query);
            while (resultSet.next()) {
                String fileName = resultSet.getString(1);
                fileNames.add(fileName);
            }
            qp.close();
        } catch (SQLException e) {
            return null;
        }

        return fileNames;
    }

    /**
     * Deletes the downloads which occured earlier than a threshold time.
     * This function is to be run after a specified time interval to clean the
     * downloads table.
     * @throws SQLException, FileNotFoundException, IOException,
     * ClassNotFoundException, InstantiationException, IllegalAccessException.
     */
    public static void cleanUp() throws SQLException, IOException,
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        Date date = new Date();
        Timestamp timeThreshold = new Timestamp(date.getTime());
        timeThreshold.setTime(timeThreshold.getTime() - ScanDaily.UPDATE_THRESHOLD);
        QueryProcessor qp = new QueryProcessor();
        qp.init();
        qp.connect();
        String query = "DELETE FROM Downloads where DownloadTime < '" +
                timeThreshold.toString() + "'";
        qp.updateData(query);
        qp.close();
    }

    public static void main(String args[]) {
        List<String> results = popularDownloads(5);
        for (String result : results) {
            System.out.println(result);
        }
        try {
            cleanUp();
        } catch(Exception e) {
            e.printStackTrace();
        }
        results = popularDownloads(5);
        System.out.println(results.size());
    }
}

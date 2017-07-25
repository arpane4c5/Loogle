/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package intranetFileSearch;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import webIndexActions.WebCrawlAction;

/**
 *
 * @author Arpan
 */
public class FTPRunnerJob implements Job
{
    public FTPRunnerJob()
    {

    }

    public void execute(JobExecutionContext context)
    {
        try
        {
            System.out.println("In FTPRunnerJob :: Executing the Job at "+new Date()+" by "+context.getTrigger().getName());

            try
            {
                ScanDaily sd=new ScanDaily();       //For FTP
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
            }

            try
            {
                SmbCrawler sc=new SmbCrawler(10);   //For CIFS
                sc.crawl();
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
            }

            try
            {
                WebCrawlAction wca=new WebCrawlAction();    //For web
                wca.crawl();
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
            }

            //For Deleting the Blacklisted words.
            
        }
        catch(Exception e)
        {
            System.out.println("Exception in FTPRunnerJob :: "+e.getMessage());
        }
    }
    /*
    public static void main(String... args)
    {
        try
        {
            ScanDaily s=new ScanDaily();
        }
        catch(Exception e)
        {
            System.out.println("Exception in Job :: "+e);
        }
    }
     */
}

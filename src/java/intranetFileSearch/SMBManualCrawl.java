/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package intranetFileSearch;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author arpan
 */
public class SMBManualCrawl implements Job
{
    public SMBManualCrawl()
    {

    }

    public void execute(JobExecutionContext context)
    {
            System.out.println("In SMBManualCrawl :: Executing the Job at "+new Date()+" by "+context.getTrigger().getName());

            try
            {
                SmbCrawler sc=new SmbCrawler(10);   //For CIFS
                sc.crawl();
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
            }
            //For Deleting the Blacklisted words.
    }

}

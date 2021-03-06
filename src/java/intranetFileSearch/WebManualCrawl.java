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
 * @author arpan
 */
public class WebManualCrawl implements Job
{
    public WebManualCrawl()
    {

    }

    public void execute(JobExecutionContext context)
    {
            System.out.println("In FTPRunnerJob :: Executing the Job at "+new Date()+" by "+context.getTrigger().getName());

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

}

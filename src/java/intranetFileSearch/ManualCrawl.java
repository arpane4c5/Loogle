/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package intranetFileSearch;

import webIndexActions.WebCrawlAction;

/**
 *
 * @author arpan
 */
public class ManualCrawl
{

    public boolean startFTPCrawl()
    {
            System.out.println("In ManualCrawl ::");
            try
            {
                ScanDaily sd=new ScanDaily();       //For FTP
                return true;
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
                return false;
            }
    }

    public boolean startSMBCrawl()
    {
            try
            {
                SmbCrawler sc=new SmbCrawler(10);   //For CIFS
                sc.crawl();
                return true;
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
                return false;
            }
    }

    public boolean startWebCrawl()
    {
            try
            {
                WebCrawlAction wca=new WebCrawlAction();    //For web
                wca.crawl();
                return true;
            }
            catch(Exception e)
            {
                System.err.println("Exception :: "+e.getMessage());
                return false;
            }
            //For Deleting the Blacklisted words.
     }

}

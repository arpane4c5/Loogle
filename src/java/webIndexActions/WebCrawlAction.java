/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//File for creating the web index.

package webIndexActions;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import java.net.URL;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.lucene.index.IndexReader;

/**
 *
 * @author arpan
 */
public class WebCrawlAction {

    private static IndexWriter writer;		  // new index being built
    private static ArrayList indexed;
    private static String beginDomain="technotatva";

    String index="C:/Loogle/index";
    String linksFilePath="c:/Loogle/WebConfigLinks.txt";
    boolean create = true;

    public void crawl()
    {
        String urls[];
        try
        {
            urls=getLinksFromFile(linksFilePath);
        }
        catch(Exception e)
        {
            System.err.println("Exception : "+e.getMessage());
            return;
        }
        int i=0;

        if(IndexReader.indexExists(new File(index)))
            create=false;
        else
            create=true;

        try
        {
            writer = new IndexWriter(index, new StandardAnalyzer(),create, new IndexWriter.MaxFieldLength(1000000));
            indexed = new ArrayList();

            while(urls[i]!=null||(!urls[i].equals("")))
            {
                try
                {
                    indexDocs(urls[i]);
                }
                catch(Exception e)
                {
                    System.err.println("Exception in creating index :: "+e.getMessage());
                }
                i++;
            }
        }
        catch(Exception e)
        {
            System.err.println("Exception occured.");
        }
        finally
        {
            //System.out.println("Optimizing...");
            try
            {
                writer.optimize();
                writer.close();
            }
            catch(Exception exp)
            {
                System.err.println("Corrupt Index Exception.");
                return;
            }
        }
    }

    private String[] getLinksFromFile(String filePath)throws FileNotFoundException,IOException
    {
        //  Scan the configuration file for list of ranges.
        String[] ret=new String[500];

        try
        {
            //FileOutputStream fout=new FileOutputStream("ABC.txt");
            FileInputStream fstream=new FileInputStream(filePath);
            DataInputStream in=new DataInputStream(fstream);
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i=0;
            while((strLine=br.readLine())!=null)
            {
                ret[i]=strLine;
                i++;
            }
            in.close();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            return ret;
        }
    }
    private static void indexDocs(String url) throws Exception
    {
        //index page
        Document doc = HTMLDocument.Document(url);
        System.out.println("adding " + doc.get("path"));
        try {
            indexed.add(doc.get("path"));
            writer.addDocument(doc);		  // add docs unconditionally
            //TODO: only add html docs
            //and create other doc types


            //get all links on the page then index them
            LinkParser lp = new LinkParser(url);
            URL[] links = lp.ExtractLinks();

            for (URL l : links) {
                //make sure the url hasnt already been indexed
                //make sure the url contains the home domain
                //ignore urls with a querystrings by excluding "?"
                if ((!indexed.contains(l.toURI().toString())) && (l.toURI().toString().contains(beginDomain)) && (!l.toURI().toString().contains("?"))) {
                    //don't index zip files
                    if (!l.toURI().toString().endsWith(".zip"))
                    {
                    System.out.print(l.toURI().toString());
                    indexDocs(l.toURI().toString());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static String Domain(String url)
    {
        int firstDot = url.indexOf(".");
        int lastDot =  url.lastIndexOf(".");
        return url.substring(firstDot+1,lastDot);
    }

    public static void main(String ...args)
    {
        WebCrawlAction wca=new WebCrawlAction();
        wca.crawl();
    }
}

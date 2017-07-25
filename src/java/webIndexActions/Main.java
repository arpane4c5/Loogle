
package webIndexActions;

import java.io.File;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.net.URL;
import java.util.ArrayList;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Main
{
    
    private static IndexWriter writer;		  // new index being built
    private static ArrayList indexed;
    private static String beginDomain="technotatva";

    public static void main(String[] args) throws Exception{
        
        //String index = "D:/E/NetBeansProjects/JSearchEngine/index";
        String index="C:/Loogle/index";
        boolean create = true;
        //String link = "http://192.200.12.2:8889/forms/frmservlet";
        String link= "http://192.200.12.2:8889/forms/frmservlet";
        //beginDomain = Domain(link);
        System.out.println(beginDomain);

        if(IndexReader.indexExists(new File(index)))
        {
            create=false;
        }
        else
            create=true;
        System.out.println("create = "+create);
        //IndexReader reader = IndexReader.open(FSDirectory.getDirectory(new File(index)), true);
        writer = new IndexWriter(index, new StandardAnalyzer(),create, 
                               new IndexWriter.MaxFieldLength(1000000));
        indexed = new ArrayList();
        
        indexDocs(link);
        
        System.out.println("Optimizing...");
        writer.optimize();
        writer.close();
           
    }

    private static void indexDocs(String url) throws Exception {

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


}

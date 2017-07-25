//package intranetWebSearch;
//
///**
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.index.TermEnum;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;
//
//import java.io.File;
//import java.util.Date;
//import java.util.Arrays;
//
///** Indexer for HTML files. */
//public class IndexHTML {
//  private IndexHTML() {}
//
//  private static boolean deleting = false;	  // true during deletion pass
//  private static IndexReader reader;		  // existing index
//  private static IndexWriter writer;		  // new index being built
//  private static TermEnum uidIter;		  // document id iterator
//
//  /** Indexer for HTML files.*/
//  public static void main(String[] args) {
//    try {
//        String argv[]=new String[4];
//        argv[0]="-create";
//        argv[1]="-index";
//        argv[2]="C:/Loogle/index";
//        argv[3]="http:\\\\facebook.com\\index.html";
//
//
//      File index = new File("index");
//      boolean create = false;
//      File root = null;
//      System.out.println(">>>>>>>0>>>>>>>");
//      String usage = "IndexHTML [-create] [-index <index>] <root_directory>";
//
//      if (argv.length == 0) {
//        System.err.println("Usage: " + usage);
//        return;
//      }
//      System.out.println(">>>>>>1>>>>>>");
//      for (int i = 0; i < argv.length; i++) {
//          System.out.println(i+"  "+argv.length);
//        if (argv[i].equals("-index"))
//        {		  // parse -index option
//            //System.out.println("index>>>>>"+i);
//          index = new File(argv[++i]);
//
//        }
//        else if (argv[i].equals("-create"))
//        {	  // parse -create option
//            //System.out.println(">>>>"+i);
//          create = true;
//        }
//        else if (i != argv.length-1)
//        {
//          System.err.println("This is Usage: " + usage);
//          return;
//        }
//        else
//          root = new File(argv[i]);
//      }
//      System.out.println(">>>>>>2>>>>>>"+root.getPath());
//
//      if(root == null) {
//        System.err.println("Specify directory to index");
//        System.err.println("Usage: " + usage);
//        return;
//      }
//
//      //create=false;
//
//      Date start = new Date();
//
//      if (!create) {				  // delete stale docs
//        deleting = true;
//        indexDocs(root, index, create);
//      }
//      writer = new IndexWriter(FSDirectory.open(index), new StandardAnalyzer(Version.LUCENE_CURRENT), create,
//                               new IndexWriter.MaxFieldLength(1000000));
//      indexDocs(root, index, create);		  // add new docs
//
//      System.out.println("Optimizing index...");
//      writer.optimize();
//      writer.close();
//
//      Date end = new Date();
//
//      System.out.print(end.getTime() - start.getTime());
//      System.out.println(" total milliseconds");
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  /* Walk directory hierarchy in uid order, while keeping uid iterator from
//  /* existing index in sync.  Mismatches indicate one of: (a) old documents to
//  /* be deleted; (b) unchanged documents, to be left alone; or (c) new
//  /* documents, to be indexed.
//   */
//
//  private static void indexDocs(File file, File index, boolean create)
//       throws Exception {
//      System.out.println(">>>>>>>>>x");
//    if (!create) {				  // incrementally update
//
//      reader = IndexReader.open(FSDirectory.open(index), false);		  // open existing index
//      uidIter = reader.terms(new Term("uid", "")); // init uid iterator
//
//      indexDocs(file);
//
//      if (deleting) {				  // delete rest of stale docs
//        while (uidIter.term() != null && uidIter.term().field() == "uid") {
//          System.out.println("deleting " +
//              HTMLDocument.uid2url(uidIter.term().text()));
//          reader.deleteDocuments(uidIter.term());
//          uidIter.next();
//        }
//        deleting = false;
//      }
//
//      uidIter.close();				  // close uid iterator
//      reader.close();				  // close existing index
//
//    } else					  // don't have exisiting
//    {
//     System.out.println(">>>>>>3>>>>>>");
//        indexDocs(file);
//    }
//  }
//
//  private static void indexDocs(File file) throws Exception {
//    if (file.isDirectory()) {
//      System.out.println(">>>1>> "+file+"\t"+file.isDirectory());                                              // if a directory
//      String[] files = file.list();		  // list its files
//      Arrays.sort(files);			  // sort the files
//      for (int i = 0; i < files.length; i++)	  // recursively index them
//        indexDocs(new File(file, files[i]));
//
//    } else if (file.getPath().endsWith(".html") || // index .html files
//      file.getPath().endsWith(".htm") || // index .htm files
//      file.getPath().endsWith(".txt") ||
//      file.getPath().endsWith(".jsp")) { // index .txt files
//
//        System.out.println(">>>2>>>>"+file+"\t"+file.isFile());
//      if (uidIter != null) {
//        String uid = HTMLDocument.uid(file);	  // construct uid for doc
//
//        while (uidIter.term() != null && uidIter.term().field() == "uid" &&
//            uidIter.term().text().compareTo(uid) < 0) {
//          if (deleting) {			  // delete stale docs
//            System.out.println("deleting " +
//                HTMLDocument.uid2url(uidIter.term().text()));
//            reader.deleteDocuments(uidIter.term());
//          }
//          uidIter.next();
//        }
//        if (uidIter.term() != null && uidIter.term().field() == "uid" &&
//            uidIter.term().text().compareTo(uid) == 0) {
//          uidIter.next();			  // keep matching docs
//        } else if (!deleting) {			  // add new docs
//          Document doc = HTMLDocument.Document(file);
//          System.out.println("adding " + doc.get("path"));
//          writer.addDocument(doc);
//        }
//      } else {					  // creating a new index
//        Document doc = HTMLDocument.Document(file);
//        System.out.println("adding " + doc.get("path"));
//        writer.addDocument(doc);		  // add docs unconditionally
//      }
//    }
//  }
//}

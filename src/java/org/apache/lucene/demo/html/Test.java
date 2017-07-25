package org.apache.lucene.demo.html;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.*;

class Test {
  public static void main(String[] argv) throws IOException, InterruptedException {
      String arg[]=new String[2];
      arg[0]="-dir";
      arg[1]="D:/Academics/wiki";

    if ("-dir".equals(arg[0])) {
      String[] files = new File(arg[1]).list();
      for(int j=0;j<files.length;j++)
      {
          System.out.println(files[j]);
      }

      java.util.Arrays.sort(files);
      for (int i = 0; i < files.length; i++) {
	System.err.println(files[i]);
	File file = new File(arg[1], files[i]);
	parse(file);
      }
    } else
      parse(new File(arg[0]));
  }

  public static void parse(File file) throws IOException, InterruptedException {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      HTMLParser parser = new HTMLParser(fis);
      System.out.println("Title: " + Entities.encode(parser.getTitle()));
      System.out.println("Summary: " + Entities.encode(parser.getSummary()));
      System.out.println("Content:");
      LineNumberReader reader = new LineNumberReader(parser.getReader());
      for (String l = reader.readLine(); l != null; l = reader.readLine())
        System.out.println(l);
    } finally {
      if (fis != null) fis.close();
    }
  }
}

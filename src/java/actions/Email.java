/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import java.io.*;
import java.net.*;

public class Email {

   public static void main (String args[]) throws IOException {
      Socket sock;
      BufferedReader bir;
      PrintWriter pw;
      String command;

      sock = new Socket("192.200.12.8", 25); // SMTP is on port 25
      bir  = new BufferedReader(new InputStreamReader( sock.getInputStream() ));

      pw   = new PrintWriter(sock.getOutputStream(),true);  // autoFlush

      System.out.println(bir.readLine());

      command="mail from: arpan.e4c5@gmail.com";
      pw.println(command);
      System.out.println("\n<"+command+">:"+bir.readLine());

      command="rcpt to:   arpangupta@hotmail.com";
      pw.println(command);
      System.out.println("\n<"+command+">:"+bir.readLine());

      command="data";
      pw.println(command);
      System.out.println("\n<"+command+">:"+bir.readLine());

      command="Subject: Testing mail";
      pw.println(command);

      command= "This is the content.";
      pw.println(command);

      command=".";
      pw.println(command);
      System.out.println("\n"+bir.readLine());

      sock.close();

   }
}

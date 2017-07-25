/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package formbeans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author arpan
 */
/*
* Checks for invalid characters
* in email addresses
*/
public class EmailValidation
{
   public static boolean checkEmail(String email)
   {
       try
       {
      //String input = "@sun.com";
      //Checks for email addresses starting with
      //inappropriate symbols like dots or @ signs.
      Pattern p = Pattern.compile("^\\.|^\\@");
      Matcher m = p.matcher(email);
      if (m.find())
      {
          System.out.println(">>>>>>>>>54>>>>>");
         System.err.println("Email addresses don't start" +
                            " with dots or @ signs.");
        return false;
      }
      System.out.println(">>>>>>>>>52>>>>>");
      //Checks for email addresses that start with
      //www. and prints a message if it does.
      p = Pattern.compile("^www\\.");
      m = p.matcher(email);
      if (m.find())
      {
        System.out.println("Email addresses don't start" +
                " with \"www.\", only web pages do.");
        return false;
      }
      //p=Pattern.compile("/^[\\w-\\.]+\\@[\\w\\.-]+\\.[a-z]{2,4}$/");
      p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
      m = p.matcher(email);
      StringBuffer sb = new StringBuffer();
      boolean result = m.find();

      return (!result);
      }
       catch(Exception e)
       {
           System.err.println("Exception in Pattern Matching :: "+e.getMessage());
           return true;
       }
      //boolean deletedIllegalChars = false;

      /*
      while(result) {
         deletedIllegalChars = true;
         m.appendReplacement(sb, "");
         result = m.find();
      }

      // Add the last segment of input to the new String
      m.appendTail(sb);

      input = sb.toString();

      if (deletedIllegalChars) {
         System.out.println("It contained incorrect characters" +
                           " , such as spaces or commas.");
      }
       */
   }
}

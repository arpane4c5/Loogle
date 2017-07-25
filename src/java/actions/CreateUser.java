/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//Path of the password file to be changed in realtime.

package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 *
 * @author root
 */
public class CreateUser {

    String pathPwdFile="/root/Desktop/m/pass";
    
    public boolean create(String username,String password)
    {
        String str=null;
        boolean fileCreated=false;

        try
	{
                fileCreated=this.createPwdFile(password);         //creating temporary password file.

                if(!fileCreated)
                {
                    System.out.println("The Password file was not created !!!");
                    return false;
                }
                else
                    System.out.println("Password file created at the specified location.");

                ProcessBuilder pb=new ProcessBuilder("bash","-c","useradd "+username);
                pb.redirectErrorStream(true);

                Process shell=pb.start();
                int shellExitStatus=shell.waitFor();
                this.displayShellOutput(shell);
                System.out.println(shellExitStatus);

                if(shellExitStatus==0)
                {
                    pb.command("bash","-c","passwd "+username+" < "+pathPwdFile);
                    shell=pb.start();
                
                    shellExitStatus=shell.waitFor();
                    this.displayShellOutput(shell);
                    System.out.println(shellExitStatus);
                    if(shellExitStatus==0)
                        return true;
                }
                return false;
                //Process p=Runtime.getRuntime().exec("useradd ");
                //p.waitFor();
	}
	catch(Exception e)
	{
		System.out.println(e.getMessage());
                return false;
	}
    }

    public boolean createPwdFile(String pwd)
    {
        String text=pwd+"\n"+pwd;
        try
        {
            File f=new File(pathPwdFile);
            FileOutputStream fout=new FileOutputStream(f);
            int i=0;
            for(int j=0;j<text.length();j++)
            {
                i=(int)text.charAt(j);
                fout.write(i);
            }
            fout.close();
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void displayShellOutput(Process p)
    {
         BufferedReader shellIn=new BufferedReader(new InputStreamReader(p.getInputStream()));
         int c;
         try
         {
            while((c=shellIn.read())!=-1)
            {
                System.out.write(c);
            }

            shellIn.close();
         }
         catch(Exception exp)
         {
             System.out.println(exp.getMessage());
         }
    }
}

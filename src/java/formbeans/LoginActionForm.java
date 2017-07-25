/*
 * LoginActionForm.java
 *
 * Created on September 6, 2009, 11:09 PM
 */

package formbeans;

import java.util.Date;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author arpan
 * @version
 */

public class LoginActionForm extends org.apache.struts.validator.ValidatorForm
{
    
    private String username;
    
    private String password;

    private String timestamp;

    private String invalidLogin;
    
    /**
     * @return
     */
    
    public LoginActionForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    
   
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getUsername() == null || getUsername().length() < 1) {
            errors.add("username", new ActionMessage("error.username.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        else if(!usernameValidate(getUsername()))
        {
            errors.add("username",new ActionMessage("error.username.basicValidation"));
        }

        if(getPassword()==null || getPassword().length()<1)
        {
            errors.add("password",new ActionMessage("error.password.required"));
        }
        return errors;
    }

    public boolean usernameValidate(String field)
    {
        boolean res=true;
        char chArray[]=field.toCharArray();
        if(!((chArray[0]>='A'&&chArray[0]<='Z')||(chArray[0]>='a'&&chArray[0]<='z')))
        {
            res=false;
        }
        for(int i=1;i<chArray.length;i++)
        {
            if(!((chArray[i]>='A'&&chArray[i]<='Z')||(chArray[i]>='a'&&chArray[i]<='z')||(chArray[i]>='0'&&chArray[i]<='9')
                    ||chArray[i]=='_'))
            {
                res=false;
            }
        }
        return res;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package formbeans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author root
 */
public class CreateUserActionForm extends org.apache.struts.action.ActionForm {
    
    private String username;
    private String password;
    private String cpassword;

    /**
     * @return
     */

    /**
     *
     */
    public CreateUserActionForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (getUsername() == null || getUsername().length() < 1) {
            errors.add("username", new ActionMessage("error.username.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        else if(!usernameValidate(getUsername()))
        {
            errors.add("username",new ActionMessage("error.username.basicValidationUseradd"));
        }

        if (getPassword() == null || getPassword().length() < 1) {
            errors.add("password", new ActionMessage("error.password.required"));
        }

        if (getCpassword() == null || getCpassword().length() < 1) {
            errors.add("cpassword", new ActionMessage("error.cpassword.required"));
        }
        else if(!getPassword().equals(getCpassword()))
        {
            errors.add("cpassword", new ActionMessage("error.cpassword.NoMatch"));
        }


        /*
        if (!fieldValidate(getUsername())){
            errors.add("username",new ActionMessage("error.username.required1"));
        }
        */
        return errors;
    }

    //username validation
    public boolean usernameValidate(String field)
    {
        boolean res=true;
        char chArray[]=field.toCharArray();

        for(int i=0;i<chArray.length;i++)
        {
            if(!((chArray[i]>='A'&&chArray[i]<='Z')||(chArray[i]>='a'&&chArray[i]<='z')||(chArray[i]>='0'&&chArray[i]<='9')
                    ||chArray[i]=='_'))
            {
                res=false;
            }
        }
        return res;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the pwd
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the cpwd
     */
    public String getCpassword() {
        return cpassword;
    }

    /**
     * @param cpwd the cpwd to set
     */
    public void setCpassword(String cpwd) {
        this.cpassword = cpwd;
    }

    /**
     * @return the email
     */
}

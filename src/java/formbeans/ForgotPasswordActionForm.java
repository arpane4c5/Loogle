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
 * @author arpan
 */
public class ForgotPasswordActionForm extends org.apache.struts.action.ActionForm {
    
    private String email;
    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String string) {
        email = string;
    }

    public ForgotPasswordActionForm() {
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
        if (getEmail() == null || getEmail().length() < 1) {
            errors.add("email", new ActionMessage("error.email.required"));
            // TODO: add 'error.name.required' key to your resources
        }
        else if(!usernameValidate(getEmail()))
        {
            errors.add("email",new ActionMessage("error.username.basicValidation"));
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
}

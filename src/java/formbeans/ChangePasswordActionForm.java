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
public class ChangePasswordActionForm extends org.apache.struts.action.ActionForm {
    
    private String currPassword;

    private String newPassword;

    private String cNewPassword;
    /**
     *
     */
    public ChangePasswordActionForm() {
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
        if(getCurrPassword()==null || getCurrPassword().length()<1)
        {
            errors.add("currPassword",new ActionMessage("error.password.required"));
        }

        if(getNewPassword()==null || getNewPassword().length()<1)
        {
            errors.add("newPassword",new ActionMessage("error.password.required"));
        }
        else if(getNewPassword().length()<5)
        {
            errors.add("newPassword",new ActionMessage("error.password.minlength"));
        }

        if(getcNewPassword()==null || getcNewPassword().length()<1)
        {
            errors.add("cNewPassword",new ActionMessage("error.password.required"));
        }
        else if(!getNewPassword().equals(getcNewPassword()))
        {
            errors.add("cNewPassword", new ActionMessage("error.cpassword.NoMatch"));
        }
        return errors;
    }

    /**
     * @return the currPassword
     */
    public String getCurrPassword() {
        return currPassword;
    }

    /**
     * @param currPassword the currPassword to set
     */
    public void setCurrPassword(String currPassword) {
        this.currPassword = currPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the cNewPassword
     */
    public String getcNewPassword() {
        return cNewPassword;
    }

    /**
     * @param cNewPassword the cNewPassword to set
     */
    public void setcNewPassword(String cNewPassword) {
        this.cNewPassword = cNewPassword;
    }
}

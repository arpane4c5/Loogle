<%-- 
    Document   : registrationform
    Created on : Mar 10, 2011, 8:37:05 PM
    Author     : Bloodhound
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" language="java" import="java.util.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
            response.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server

            response.setHeader("Cache-Control", "no-store");//Directs caches not to store the page under any circumstance

            response.setHeader("Pragma", "no-cache");//HTTP 1.0 backward compatibility

            response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="images/style.css" type="text/css" media="all"  />
        <title>Loogle</title>
    </head>
    <body>
         <div id="content">

         <h1>Registration</h1>
         <h4>You can register using your intranet email account to receive regular updates.</h4>
         Note : All the fields are required.
         <html:form action="registeruser.do" method="post" ><!--onsubmit="return validate(this)">-->
             <table width="100%" border="0" cellspacing="15px">
                 <tr>
                     <td align="right" width="25%">
                        Username : &nbsp;
                     </td>
                     <td width="25%">
                         <html:text property="username" maxlength="45"/>
                     </td>
                    <td width="50%">
                        <font color="RED">
                            <html:errors property="username"/>
                        </font>
                    </td>
                 </tr>
                 <tr>
                     <td align="right" width="25%">
                        Email* : &nbsp;
                      </td>
                      <td width="25%">
                          <html:text property="email" maxlength="45"/>
                      </td>
                      <td align="left">
                          <i>@loogle.com</i>
                      </td>
                 </tr>
                 <tr>
                     <td width="25%" colspan="2">&nbsp;</td>
                     <td align="left" width="50%">
                          <html:errors property="email" />
                      </td>
                 </tr>
                 <tr>
                    <td align="right" width="25%">
                        Password : &nbsp;
                    </td>
                    <td width="25%">
                        <html:password property="password" maxlength="50"/>
                    </td>
                    <td>
                            <html:errors property="password" />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="25%">
                        Confirm Password : &nbsp;
                    </td>
                    <td width="25%">
                        <html:password property="cpassword" maxlength="50"/>
                    </td>
                    <td>
                        <html:errors property="cpassword" />
                    </td>
                </tr>
                <tr>
                    <td align="right" width="25%">
                        <html:reset value="Reset" />
                    </td>
                    <td align="left" width="25%">
                        <html:submit value="Next>" />
                    </td>
                    <td>

                    </td>
                </tr>
                <tr>
                    <td align="left" colspan="3">
                        <p>* Please provide your servers username <i>eg: user</i>. Your email id will be <i>user@loogle.com</i><br/>
                            &nbsp;&nbsp;You can see your emails after signing in with <i>user</i> and your server login password at 
                            <a href="http://mail.loogle.com">LMail.com</a><br/>
                           * Make sure you have a user login account on the loogle server with username <i>user</i><br/>
                           * If you do not have a user account on the server then contact the administrator.
                        </p>
                    </td>
                </tr>
             </table>
        </html:form>
         </div>
        <script type="text/javascript">
        function validate(thisForm)
        {
            with(thisForm)
            {
            var mail=thisForm.email.value;
            if(mail==null||mail=="")
            {
                alert("Hi Value="+mail);
                return true;
            }
            else
                return checkEmail(mail);
            }
        }
        function checkEmail(email)
        {
            with(email)
            {
                alert("Value email  :: \""+email.value+"\"");
            if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email.value))
            {
                return (true);
            }
            else
            {
                alert("Invalid E-mail Address! Please re-enter.");
                return (false);
            }
            }
        }
    </script>
    </body>
</html>

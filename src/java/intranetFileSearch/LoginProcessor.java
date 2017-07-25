package intranetFileSearch;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Arpan Gupta
 */
public class LoginProcessor extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException, IOException.
     */
    protected void processRequest(final HttpServletRequest request,
            final HttpServletResponse response)
            throws ServletException, IOException {

        // Forces caches to obtain a new copy of the page from the origin server
        response.setHeader("Cache-Control", "no-cache");
        // Directs caches not to store the page under any circumstance
        response.setHeader("Cache-Control", "no-store");
        // HTTP 1.0 backward compatibility
        response.setHeader("Pragma", "no-cache");
        // Causes the proxy cache to see the page as "stale
        response.setDateHeader("Expires", 0);

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String userID = (String) session.getAttribute("userID");
        RequestDispatcher rd;
        if (userID == null)
        {
            System.out.println(userID);
            rd = getServletContext().getRequestDispatcher("/ErrorPage.jsp?id=1");
            PrintWriter out = response.getWriter();
            QueryProcessor qp = new QueryProcessor();
            String username = (String) request.getParameter("username");
            String password = (String) request.getParameter("password");
            String timestamp = (String) request.getParameter("timestamp");
            try {
                if (username == null || password == null || username.compareTo("") == 0
                        || password.compareTo("") == 0)
                {
                    System.err.println("LoginProcessor :Invalid form parameters\n....Forwarding"
                            + "to Error Page - Error Code 2");
                    rd = getServletContext().getRequestDispatcher("/ErrorPage.jsp?id=2");
                } 
                else
                {
                    qp.init();
                    try
                    {
                        qp.connect();
                        String query = "select LoginTime from users where Username=\""
                                + username + "\" and Password=\"" + password + "\";";
                        try
                        {
                            ResultSet rs = qp.getRecords(query);
                            if (rs.next() == false)
                            {
                                System.err.println("....Forwarding to Error Page - Error Code 2");
                                rd = getServletContext().getRequestDispatcher("/ErrorPage.jsp?id=3");
                            }
                            else
                            {
                                Timestamp t = rs.getTimestamp(1);
                                String tout = t.toString();
                                System.err.println("LoginProcessor :Internal Date " + tout);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date din = sdf.parse(timestamp);
                                java.util.Date dout = sdf.parse(tout);
                                if (din.after(dout) == true)
                                {
                                    query = "update users set LoginTime=\"" + timestamp
                                            + "\" where Username=\"" + username + "\"and"
                                            + " Password=\"" + password + "\";";
                                    int rows = qp.updateData(query);
                                    System.err.println("LoginProcessor :Number of rows updated :"
                                            + rows + "\n....Forwarding to Home Page");
                                    session.setAttribute("userID", username);
                                    rd = getServletContext().getRequestDispatcher("fileSearch.do");
                                }
                                else
                                {
                                    rd = getServletContext().getRequestDispatcher("fileSearch.do");
                                    System.err.println("LoginProcessor :Invalid attempt to login\n"
                                            + "....Forwarding to Home Page");
                                }
                            }
                        } 
                        catch (SQLException sqle)
                        {
                            rd = getServletContext().getRequestDispatcher("/ErrorPage.jsp?id=1");
                            System.err.println("LoginProcessor : Could not execute query ("
                                    + query + ") on the database\n"
                                    + sqle + "\n....Forwarding to Error Page - Error Code 1");
                        }
                        finally
                        {
                            qp.close();
                        }
                    } 
                    catch (Exception f)
                    {
                        System.err.println("LoginProcessor : Could not connect to database\n" + f
                                + "....Forwarding to Error Page - Error Code 1");
                    }
                }
            } 
            catch (Exception e)
            {
                System.err.println("LoginProcessor : Could not or read from "
                        + "DataBaseConnectionParameters.properties file\n"
                        + e + "....Forwarding to Error Page - Error Code 1");
            }
            finally
            {
                rd.forward(request, response);
                out.close();
            }
        } 
        else
        {
            System.err.println("LoginProcessor :Found session for user " + userID);
            rd = getServletContext().getRequestDispatcher("/index.jsp");
            rd.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}

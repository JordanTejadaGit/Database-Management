/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataaccess.UsersDB;
import domain.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.AccountService;

public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountService as = new AccountService();
        HttpSession session = request.getSession();
        String code = request.getParameter("uuidPassword");
        
        session.removeAttribute("messageAdmin");
        session.removeAttribute("edit");
        session.removeAttribute("messageInventory");
        session.removeAttribute("editCategoryBoolean");
        session.removeAttribute("itemListAll");
        session.removeAttribute("searchedItemList");
        session.removeAttribute("searchedItemListUser");

        
        if (code != null ) {
            try {
                Users found = as.uuidPassword(code);
                if (found != null) {
                    session.setAttribute("resetPassword", true);
                    session.setAttribute("resetUserPassword", found);
                }
            } catch (Exception ex) {
                Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            session.removeAttribute("resetPassword");
        }
        
        
        getServletContext().getRequestDispatcher("/WEB-INF/reset.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Users user = new Users();
        AccountService as = new AccountService();

        String action = request.getParameter("action");
        String emailReset = "jordantejada.school+" + request.getParameter("resetEmail");
        String newPassword = request.getParameter("resetPassword");

        if (action != null) {
            if (action.equals("reset")) {
                UsersDB userDB = new UsersDB();
                try {
                    List<Users> userList = userDB.getEmail(emailReset);
                    String url = request.getRequestURL().toString();
                    String path = getServletContext().getRealPath("/WEB-INF");
                    try {
                        as.passwordEmail(userList.get(0).getUsername(), path, url);
                        session.setAttribute("message", "Check your email for reset link!");
                    } catch (Exception ex) {
                        Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                        session.setAttribute("message", "Error");
                    }

                } catch (Exception ex) {
                    Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("message", "Error");
                }

            }
            if (action.equals("newPasswordSubmit")) {
                user = (Users) session.getAttribute("resetUserPassword");
                try {
                    as.resetPassword(user.getUsername(), newPassword);
                    session.setAttribute("message", "Reset Password Successful");
                    session.removeAttribute("resetPassword");
                    session.removeAttribute("resetUserPassword");
                } catch (Exception ex) {
                    Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("message", "Error");
                }
            }
        }
        response.sendRedirect("login");
    }
}

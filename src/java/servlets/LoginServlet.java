/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import domain.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import services.AccountService;

/**
 *
 * @author 694952
 */
public class LoginServlet extends HttpServlet {

    boolean signUp = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountService as = new AccountService();
        HttpSession session = request.getSession();

        Users user = new Users();
        user = (Users) session.getAttribute("loginUser");

        String transfer = (String) session.getAttribute("transfer");

        session.removeAttribute("messageAdmin");
        session.removeAttribute("edit");
        session.removeAttribute("messageInventory");
        session.removeAttribute("editCategoryBoolean");
        session.removeAttribute("itemListAll");
        session.removeAttribute("searchedItemList");
        session.removeAttribute("searchedItemListUser");
        
        String code = request.getParameter("uuid");

        try {
            signUp = (boolean) session.getAttribute("signUp");
        } catch (Exception ex) {
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            signUp = false;
        }

        if (signUp == true) {
            request.setAttribute("signUp", signUp);
        }

        if (code != null) {
            try {
                List<Users> userList = as.getAll();
                for (int i = 0; i < userList.size(); i++) {
                    if (code.equals(userList.get(i).getEmailConfirmationUUID())) {
                        try {
                            String path = getServletContext().getRealPath("/WEB-INF");
                            String url = request.getRequestURL().toString();
                            as.welcomeEmail(userList.get(i).getUsername(), url, path);
                            session.setAttribute("message", "Confirmation Successful");
                        } catch (Exception ex) {
                            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                            session.setAttribute("message", "Confirmation Error");
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("message", "Confirmation Error");
            }
        }

        if (transfer != null) {
            if (transfer.equals("logout")) {
                session.removeAttribute("loginUsername");
                session.removeAttribute("loginUser");
                session.removeAttribute("transfer");
                session.setAttribute("message", "You have succesfully logged out!");
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            } else if (transfer.equals("inventory")) {
                session.removeAttribute("transfer");
                response.sendRedirect("inventory");
            } else if (transfer.equals("admin")) {
                session.removeAttribute("transfer");
                response.sendRedirect("admin");
            }
        } else if (user != null && user.getIsAdmin() == true) {
            response.sendRedirect("admin");
        } else if (user != null) {
            response.sendRedirect("inventory");
        } else {
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String loginUsername = (String) request.getParameter("loginUser");
        String loginPass = (String) request.getParameter("loginPass");

        AccountService as = new AccountService();

        Users user = new Users();

        String registerUsername = request.getParameter("registerUsername");
        String registerPassword = request.getParameter("registerPassword");
        String registerEmail = request.getParameter("registerEmail");
        String registerFirst = request.getParameter("registerFirst");
        String registerLast = request.getParameter("registerLast");

        String action = request.getParameter("action");
        boolean duplicate = false;
        if (action.equals("login")) {

            if (loginUsername == null || loginUsername == "" || loginPass == null || loginPass == "") {
                session.setAttribute("message", "Invalid login");
                response.sendRedirect("login");
            } else {
                try {
                    user = as.login(loginUsername, loginPass);
                    if (user == null) {
                        session.setAttribute("message", "Invalid login");
                        response.sendRedirect("login");
                    } else if (user.getActive() == false) {
                        session.setAttribute("message", "User Currently InActive. Please confirm your email or get an Admin to activate your account.");
                        response.sendRedirect("login");
                    } else if (user.getIsAdmin() == true) {
                        session.setAttribute("loginUser", user);
                        session.setAttribute("loginUsername", user.getUsername());
                        response.sendRedirect("admin");
                    } else {
                        session.setAttribute("loginUser", user);
                        session.setAttribute("loginUsername", user.getUsername());
                        response.sendRedirect("inventory");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("message", "Invalid login");
                    response.sendRedirect("login");
                }

            }
        } else if (action.equals("signUp")) {
            signUp = true;
            session.setAttribute("signUp", signUp);
            session.removeAttribute("message");
            response.sendRedirect("login");
        } else if (action.equals("register")) {
            if (registerUsername == null || registerUsername.isEmpty() || registerPassword == null || registerPassword.isEmpty()
                    || registerEmail == null || registerEmail.isEmpty() || registerFirst == null || registerFirst.isEmpty()
                    || registerLast == null || registerLast.isEmpty()) {
                session.setAttribute("message", "Please Fill In Form");
                response.sendRedirect("login");
            } else {
                try {
                    List<Users> userList = as.getAll();
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUsername().equals(registerUsername)) {
                            session.setAttribute("message", "Username Already Taken");
                            response.sendRedirect("login");
                            duplicate = true;
                        }
                    }
                    
                    if (duplicate == false) {
                        try {

                            as.insert(registerUsername, registerPassword, ("jordantejada.school+" + registerEmail), registerFirst, registerLast, false, false);
                            session.setAttribute("registeringUser", as.get(registerUsername));
                            try {
                                String url = request.getRequestURL().toString();
                                Users userConfirmation = (Users) session.getAttribute("registeringUser");
                                String path = getServletContext().getRealPath("/WEB-INF");
                                as.confirmEmail(userConfirmation.getUsername(), userConfirmation.getEmail(), url, path);
                                session.removeAttribute("signUp");
                                session.setAttribute("message", "Registration Succesful, please confirm your email");
                                response.sendRedirect("login");
                            } catch (Exception ex) {
                                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.removeAttribute("signUp");
                                session.setAttribute("message", "Error");
                                response.sendRedirect("login");
                            }

                        } catch (Exception ex) {
                            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                            session.removeAttribute("signUp");
                            session.setAttribute("message", "Error");
                            response.sendRedirect("login");
                        }
                    }
                } catch (Exception ex) {
                                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.removeAttribute("signUp");
                                session.setAttribute("message", "Error");
                                response.sendRedirect("login");
                            }

            }
        }

    }

}

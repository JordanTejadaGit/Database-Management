/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import domain.Categories;
import domain.Items;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.AccountService;
import domain.Users;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import services.InventoryService;

/**
 *
 * @author 694952
 */
public class AdminServlet extends HttpServlet {

    boolean edit = false;
    boolean editCategory = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = new Users();

        AccountService as = new AccountService();
        InventoryService is = new InventoryService();

        session.removeAttribute("messageInventory");
        session.removeAttribute("searchedItemListUser");
        session.removeAttribute("signUp");

        String action = request.getParameter("action");
        try {
            edit = (boolean) session.getAttribute("edit");
        } catch (Exception ex) {
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            edit = false;
        }
        user = (Users) session.getAttribute("editUser");
        request.setAttribute("editUser", user);

        if (edit == true) {
            request.setAttribute("edit", edit);
        }

        try {
            editCategory = (boolean) session.getAttribute("editCategoryBoolean");
        } catch (Exception ex) {
            Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
            editCategory = false;
        }
        if (editCategory == true) {
            request.setAttribute("editCategoryBoolean", editCategory);
        }

        if (action != null) {
            if (action.equals("logout")) {
                session.setAttribute("transfer", action);
                response.sendRedirect("login");
            } else if (action.equals("inventory")) {
                session.setAttribute("transfer", action);
                response.sendRedirect("inventory");
            } else if (action.equals("admin")) {
                response.sendRedirect("admin");
            }

        } else {
            try {
                List<Categories> categories = is.getAllCat();
                List<Users> users = as.getAll();
                request.setAttribute("searchedItemList", (List<Items>) session.getAttribute("searchedItemList"));
                request.setAttribute("itemListAll", (List<Items>) session.getAttribute("itemListAll"));
                request.setAttribute("categories", categories);
                request.setAttribute("users", users);
                getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("message", "error");
                getServletContext().getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Users user = new Users();
        AccountService as = new AccountService();
        InventoryService is = new InventoryService();

        boolean duplicate = false;

        String action = request.getParameter("action");
        String deleteUsername = request.getParameter("deleteUsername");
        String editUserpost = request.getParameter("editUserpost");

        String addUsername = request.getParameter("addUsername");
        String addPassword = request.getParameter("addPassword");
        String addEmail = request.getParameter("addEmail");
        String addFirst = request.getParameter("addFirst");
        String addLast = request.getParameter("addLast");

        String addCategoryName = request.getParameter("addCategoryName");

        String editUsername = request.getParameter("editUsername");
        String editPassword = request.getParameter("editPassword");
        String editEmail = request.getParameter("editEmail");
        String editFirst = request.getParameter("editFirst");
        String editLast = request.getParameter("editLast");
        String active = request.getParameter("active");
        String admin = request.getParameter("admin");

        String editCategoryName = request.getParameter("editCategoryName");
        
        String itemSearch = request.getParameter("itemSearch");

        if (action != null) {
            if (action.equals("add")) {
                if (addUsername == null || addUsername.isEmpty() || addPassword == null || addPassword.isEmpty()
                        || addEmail == null || addEmail.isEmpty() || addFirst == null || addFirst.isEmpty()
                        || addLast == null || addLast.isEmpty()) {
                    session.setAttribute("messageAdmin", "Please Fill In Form");
                    session.removeAttribute("itemListAll");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("edit");
                    session.removeAttribute("editCategoryBoolean");
                    response.sendRedirect("admin");
                } else {
                    try {
                        List<Users> userList = as.getAll();
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getUsername().equals(addUsername)) {
                                session.setAttribute("messageAdmin", "Username Already Taken");
                                response.sendRedirect("admin");
                                session.removeAttribute("searchedItemList");
                                session.removeAttribute("itemListAll");
                                duplicate = true;
                            }
                        }
                        if (duplicate == false) {
                            try {
                                as.insert(addUsername, addPassword, ("jordantejada.school+" + addEmail), addFirst, addLast, true, false);
                                session.removeAttribute("edit");
                                session.removeAttribute("editCategoryBoolean");
                                session.removeAttribute("itemListAll");
                                session.removeAttribute("searchedItemList");
                                session.setAttribute("messageAdmin", "User Added");
                                response.sendRedirect("admin");

                            } catch (Exception ex) {
                                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.removeAttribute("edit");
                                session.removeAttribute("editCategoryBoolean");
                                session.removeAttribute("itemListAll");
                                session.setAttribute("messageAdmin", "Error");
                                session.removeAttribute("searchedItemList");
                                response.sendRedirect("admin");
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                        session.removeAttribute("edit");
                        session.removeAttribute("editCategoryBoolean");
                        session.removeAttribute("searchedItemList");
                        session.setAttribute("messageAdmin", "Error");
                        session.removeAttribute("itemListAll");
                        response.sendRedirect("admin");
                    }
                }
            } else if (action.equals("Delete")) {
                user = (Users) session.getAttribute("loginUser");
                if (deleteUsername.equals(user.getUsername())) {
                    session.setAttribute("messageAdmin", "Can't Delete Your Own User");
                    session.removeAttribute("editCategoryBoolean");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("itemListAll");
                    response.sendRedirect("admin");
                } else {
                    try {
                        as.delete(deleteUsername, user.getUsername());
                        session.setAttribute("messageAdmin", "User Deleted");
                        session.removeAttribute("editCategoryBoolean");
                        session.removeAttribute("searchedItemList");
                        session.removeAttribute("itemListAll");
                        session.removeAttribute("edit");
                        response.sendRedirect("admin");
                    } catch (Exception ex) {
                        Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                        session.setAttribute("messageAdmin", "Error");
                        session.removeAttribute("editCategoryBoolean");
                        session.removeAttribute("searchedItemList");
                        session.removeAttribute("itemListAll");
                        session.removeAttribute("edit");
                        response.sendRedirect("admin");
                    }
                }
            } else if (action.equals("Edit")) {
                edit = true;
                session.setAttribute("edit", edit);

                try {
                    user = as.get(editUserpost);
                    session.removeAttribute("messageAdmin");
                    session.removeAttribute("editCategoryBoolean");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("itemListAll");
                    response.sendRedirect("admin");
                    session.setAttribute("editUser", user);
                } catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.removeAttribute("editCategoryBoolean");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("itemListAll");
                    session.setAttribute("messageAdmin", "Error");
                    response.sendRedirect("admin");
                }
            } else if (action.equals("save")) {
                if (editUsername == null || editUsername.isEmpty()
                        || editPassword == null || editPassword.isEmpty()
                        || editEmail == null || editEmail.isEmpty() || editFirst == null || editFirst.isEmpty()
                        || editLast == null || editLast.isEmpty()) {
                    session.setAttribute("messageAdmin", "Please Fill In Form");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("itemListAll");
                    response.sendRedirect("admin");
                } else {
                    try {
                        List<Users> userList = as.getAll();
                        Users edittedUser = (Users) session.getAttribute("loginUser");
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getUsername().equals(editUsername) && !editUsername.equals(edittedUser.getUsername())) {
                                session.setAttribute("messageAdmin", "Username Already Taken");
                                session.removeAttribute("itemListAll");
                                session.removeAttribute("searchedItemList");
                                response.sendRedirect("admin");
                                duplicate = true;
                            }
                        }
                        if (duplicate == false) {
                            boolean activeEdit = false;
                            boolean adminEdit = false;
                            user = (Users) session.getAttribute("editUser");
                            if (active.equals("active")) {
                                activeEdit = true;
                            }
                            if (admin.equals("admin")) {
                                adminEdit = true;
                            }
                            try {
                                as.update(editUsername, editPassword, ("jordantejada.school+" + editEmail), editFirst, editLast, activeEdit, adminEdit, user.getUsername());
                                session.setAttribute("messageAdmin", "User Edited");
                                session.removeAttribute("editCategoryBoolean");
                                session.removeAttribute("searchedItemList");
                                session.removeAttribute("itemListAll");
                                session.removeAttribute("edit");
                                response.sendRedirect("admin");
                            } catch (Exception ex) {
                                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.setAttribute("messageAdmin", "Error");
                                session.removeAttribute("editCategoryBoolean");
                                session.removeAttribute("searchedItemList");
                                session.removeAttribute("itemListAll");
                                session.removeAttribute("edit");
                                response.sendRedirect("admin");
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                        session.setAttribute("messageAdmin", "Error");
                        session.removeAttribute("searchedItemList");
                        session.removeAttribute("editCategoryBoolean");
                        session.removeAttribute("edit");
                        session.removeAttribute("itemListAll");
                        response.sendRedirect("admin");
                    }
                }
            } else if (action.equals("addCategory")) {
                if (addCategoryName == null || addCategoryName.isEmpty()) {
                    session.setAttribute("messageAdmin", "Please Fill In Form");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("itemListAll");
                    response.sendRedirect("admin");
                } else {
                    try {
                        List<Categories> categoriesList = is.getAllCat();
                        is.insertCategory(addCategoryName, categoriesList.size() + 1);
                        session.setAttribute("messageAdmin", "Category Successfully Added");
                        session.removeAttribute("editCategoryBoolean");
                        session.removeAttribute("searchedItemList");
                        session.removeAttribute("edit");
                        session.removeAttribute("itemListAll");
                        response.sendRedirect("admin");
                    } catch (Exception ex) {
                        Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                        session.setAttribute("messageAdmin", "Error");
                        session.removeAttribute("editCategoryBoolean");
                        session.removeAttribute("edit");
                        session.removeAttribute("searchedItemList");
                        session.removeAttribute("itemListAll");
                        response.sendRedirect("admin");
                    }

                }
            } else if (action.equals("Edit Category")) {
                editCategory = true;
                session.setAttribute("editCategoryBoolean", editCategory);
                String editCategoryID = request.getParameter("editCategoryID");
                session.setAttribute("editCategoryID", editCategoryID);

                try {
                    Categories category = new Categories();
                    int editCategoryIDINT = Integer.parseInt(editCategoryID);
                    category = is.getCategory(editCategoryIDINT);
                    session.setAttribute("editCategory", category);
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("edit");
                    session.removeAttribute("itemListAll");
                    session.removeAttribute("messageAdmin");
                    response.sendRedirect("admin");
                } catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("messageAdmin", "Error");
                    session.removeAttribute("itemListAll");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("edit");
                    response.sendRedirect("admin");
                }
            } else if (action.equals("saveCategory")) {
                try {
                    String editCategoryID = (String) session.getAttribute("editCategoryID");
                    is.updateCategory(editCategoryName, Integer.parseInt(editCategoryID));
                    session.setAttribute("messageAdmin", "Category Succefully Edited");
                    session.removeAttribute("editCategoryBoolean");
                    session.removeAttribute("edit");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("itemListAll");
                    response.sendRedirect("admin");

                } catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("messageAdmin", "Error");
                    session.removeAttribute("itemListAll");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("edit");
                    response.sendRedirect("admin");
                }
            } else if (action.equals("searchAll")) {
                try {
                    List<Items> itemList = is.getAllItems();
                    session.setAttribute("itemListAll", itemList);
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("edit");
                    session.removeAttribute("messageAdmin");
                    response.sendRedirect("admin");
                    
                } catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("messageAdmin", "Error");
                    session.removeAttribute("searchedItemList");
                    session.removeAttribute("edit");
                    response.sendRedirect("admin");
                }
            } else if (action.equals("itemSearchSubmit"))  {
                if (itemSearch == null || itemSearch.isEmpty()) {
                    session.setAttribute("messageAdmin", "Please Fill In Search");
                    session.removeAttribute("itemListAll");
                    session.removeAttribute("edit");
                    session.removeAttribute("searchedItemList");
                    response.sendRedirect("admin");
                }
                else {
                    try {
                        List<Items> itemList = is.getSearchItem(itemSearch);
                        session.setAttribute("searchedItemList", itemList);
                        session.removeAttribute("itemListAll");
                        session.removeAttribute("edit");
                        session.removeAttribute("messageAdmin");
                        response.sendRedirect("admin");
                    }
                    catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("messageAdmin", "Error");
                    session.removeAttribute("itemListAll");
                    session.removeAttribute("edit");
                    session.removeAttribute("searchedItemList");
                    response.sendRedirect("admin");
                }
                }
                
            }

        }
    }
}

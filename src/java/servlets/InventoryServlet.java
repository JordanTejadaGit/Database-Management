/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import domain.Categories;
import domain.Items;
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
import services.InventoryService;

/**
 *
 * @author 694952
 */
public class InventoryServlet extends HttpServlet {

    boolean ownEdit = false;
    boolean editItem = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        Users user = new Users();
        user = (Users) session.getAttribute("loginUser");

        InventoryService is = new InventoryService();

        session.removeAttribute("messageAdmin");
        session.removeAttribute("edit");
        session.removeAttribute("editCategoryBoolean");
        session.removeAttribute("itemListAll");
        session.removeAttribute("searchedItemList");
        session.removeAttribute("signUp");

        ownEdit = false;
        editItem = false;
        session.removeAttribute("ownEdit");
        session.removeAttribute("editItem");

        if (action != null) {
            if (action.equals("logout")) {
                session.setAttribute("transfer", action);
                response.sendRedirect("login");
            } else if (action.equals("inventory")) {
                response.sendRedirect("inventory");
            } else if (action.equals("admin")) {
                if (user.getIsAdmin() == true) {
                    session.setAttribute("transfer", action);
                    response.sendRedirect("admin");
                } else {
                    request.setAttribute("messageInventory", "Invalid Access");
                    getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
                }
            } else if (action.equals("ownEdit")) {
                ownEdit = true;
                session.setAttribute("ownEdit", ownEdit);
                getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
            }

        } else {
            try {
                List<Items> items = is.getAll(user.getUsername());
                List<Categories> categories = is.getAllCat();
                request.setAttribute("searchedItemListUser", (List<Items>) session.getAttribute("searchedItemListUser"));
                session.setAttribute("categories", categories);
                session.setAttribute("list", items);
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        InventoryService is = new InventoryService();
        Users user = new Users();
        AccountService as = new AccountService();

        user = (Users) session.getAttribute("loginUser");
            boolean duplicate = false;

        List<Items> itemList = user.getItemsList();

        String action = request.getParameter("action");

        String addCategory = request.getParameter("addCategory");
        String addItemName = request.getParameter("addItemName");
        String addPrice = request.getParameter("addPrice");

        String editCategory = request.getParameter("editCategory");
        String editItemName = request.getParameter("editItemName");
        String editPrice = request.getParameter("editPrice");

        String deleteItem = request.getParameter("deleteItem");

        String editUsername = request.getParameter("editUsername");
        String editPassword = request.getParameter("editPassword");
        String editEmail = request.getParameter("editEmail");
        String editFirst = request.getParameter("editFirst");
        String editLast = request.getParameter("editLast");
        String active = request.getParameter("active");
        
        String itemSearch = request.getParameter("itemSearch");

        if (action != null) {
            if (action.equals("add")) {
                if (addPrice != null) {
                    try {
                        double price = Double.parseDouble(addPrice);

                        if (addItemName == null || addItemName.isEmpty() || addPrice == null || price < 0) {
                            session.setAttribute("messageInventory", "Invalid Entries");
                            session.removeAttribute("searchedItemListUser");
                            response.sendRedirect("inventory");
                        } else {
                            try {
                                is.insert(itemList.size() + 1, addItemName, price, user.getUsername(), Integer.parseInt(addCategory));
                                session.setAttribute("messageInventory", "Item Added");
                                session.removeAttribute("searchedItemListUser");
                                response.sendRedirect("inventory");
                            } catch (Exception ex) {
                                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.removeAttribute("searchedItemListUser");
                                session.setAttribute("messageInventory", "Error");
                                response.sendRedirect("inventory");
                            }
                        }
                    } catch (NumberFormatException ex) {
                        session.setAttribute("messageInventory", "Invalid Entries");
                        session.removeAttribute("searchedItemListUser");
                        response.sendRedirect("inventory");
                    }

                } else {
                    session.setAttribute("messageInventory", "Invalid Entries");
                    session.removeAttribute("searchedItemListUser");
                    response.sendRedirect("inventory");
                }
            } else if (action.equals("Delete")) {

                try {
                    String errorCheck = is.delete(Integer.parseInt(deleteItem), user.getUsername());
                    if (errorCheck == null) {
                        session.setAttribute("messageInventory", "Wrong User");
                        session.removeAttribute("searchedItemListUser");
                        response.sendRedirect("inventory");
                    } else {
                        session.setAttribute("messageInventory", "Item Deleted");
                        session.removeAttribute("searchedItemListUser");
                        response.sendRedirect("inventory");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.removeAttribute("searchedItemListUser");
                    session.setAttribute("messageInventory", "Error");
                    response.sendRedirect("inventory");
                }
            } else if (action.equals("save")) {
                if (editPassword == null || editPassword.isEmpty()
                        || editEmail == null || editEmail.isEmpty() || editFirst == null || editFirst.isEmpty()
                        || editLast == null || editLast.isEmpty()) {
                    session.setAttribute("messageInventory", "Please Fill In Form");
                    session.removeAttribute("searchedItemListUser");
                    response.sendRedirect("inventory");
                } else {
                    try {
                        List<Users> userList = as.getAll();
                        Users edittedUser = (Users) session.getAttribute("loginUser");
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getUsername().equals(editUsername) && !editUsername.equals(edittedUser.getUsername())) {
                                session.removeAttribute("searchedItemListUser");
                                session.setAttribute("messageInventory", "Username Already Taken");
                                response.sendRedirect("admin");
                                duplicate = true;
                            }
                        }
                        if (duplicate == false) {
                            boolean activeEdit = false;
                            user = (Users) session.getAttribute("loginUser");
                            if (active.equals("active")) {
                                activeEdit = true;
                            }
                            try {
                                as.update(editUsername, editPassword, ("jordantejada.school+" + editEmail), editFirst, editLast, activeEdit, false, user.getUsername());
                                session.setAttribute("messageInventory", "User Edited");
                                session.removeAttribute("searchedItemListUser");
                                session.setAttribute("loginUser", as.get(editUsername));
                                user = (Users) session.getAttribute("loginUser");
                                if (user.getActive() == false) {
                                    session.setAttribute("transfer", "logout");
                                    session.removeAttribute("searchedItemListUser");
                                    response.sendRedirect("login");
                                } else {
                                    session.removeAttribute("searchedItemListUser");
                                    response.sendRedirect("inventory");
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.setAttribute("messageInventory", "Error");
                                session.removeAttribute("searchedItemListUser");
                                response.sendRedirect("inventory");
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
                        session.removeAttribute("searchedItemListUser");
                        
                    }
                }
            } else if (action.equals("Edit Item")) {
                editItem = true;
                session.setAttribute("editItem", editItem);
                session.setAttribute("editItemID", request.getParameter("editItemID"));
                session.removeAttribute("searchedItemListUser");
                session.removeAttribute("messageInventory");
                getServletContext().getRequestDispatcher("/WEB-INF/inventory.jsp").forward(request, response);
            } else if (action.equals("saveItem")) {
                if (editPrice != null) {
                    try {
                        double price = Double.parseDouble(editPrice);
                        String editItemID = (String) session.getAttribute("editItemID");

                        if (editItemName == null || editItemName.isEmpty() || editPrice == null || price < 0) {
                            session.setAttribute("messageInventory", "Invalid Entries");
                            session.removeAttribute("searchedItemListUser");
                            response.sendRedirect("inventory");
                        } else {
                            try {
                                Integer itemID = Integer.parseInt(editItemID);
                                Integer categoryID = Integer.parseInt(editCategory);
                                is.update(itemID, editItemName, price, categoryID);
                                session.setAttribute("messageInventory", "Item Updated");
                                session.removeAttribute("searchedItemListUser");
                                response.sendRedirect("inventory");
                            } catch (Exception ex) {
                                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                                session.setAttribute("messageInventory", "Error");
                                session.removeAttribute("searchedItemListUser");
                                response.sendRedirect("inventory");
                            }
                        }
                    } catch (NumberFormatException ex) {
                        session.setAttribute("messageInventory", "Invalid Entries");
                        session.removeAttribute("searchedItemListUser");
                        response.sendRedirect("inventory");
                    }
                } else {
                    session.setAttribute("messageInventory", "Invalid Entries");
                    session.removeAttribute("searchedItemListUser");
                    response.sendRedirect("inventory");
                }
            } else if (action.equals("itemSearchSubmit"))  {
                if (itemSearch == null || itemSearch.isEmpty()) {
                    session.setAttribute("messageInventory", "Please Fill In Search");
                    session.removeAttribute("searchedItemListUser");
                    response.sendRedirect("inventory");
                }
                else {
                    try {
                        List<Items> itemSearchList = is.getSearchItemUser(itemSearch, user.getUsername());
                        session.setAttribute("searchedItemListUser", itemSearchList);
                        session.removeAttribute("messageInventory");
                        response.sendRedirect("inventory");
                    }
                    catch (Exception ex) {
                    Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("messageInventory", "Error");
                    session.removeAttribute("searchedItemListUser");
                    response.sendRedirect("inventory");
                }
                }
                
            } else if (action.equals("searchAll")) {
                session.removeAttribute("messageInventory");
                session.removeAttribute("searchedItemListUser");
                    response.sendRedirect("inventory");
            }
            

        }
    }
}

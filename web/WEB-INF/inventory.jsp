<%-- 
    Document   : inventory
    Created on : Jul 18, 2021, 6:30:55 PM
    Author     : 694952
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inventory Page</title>
        <style>
            .list th, .list tr, .list td{
                border: 1px solid black;
                padding-left: 5px;
                padding-right: 5px;
            }

            .list table{
                border-collapse: collapse;
                border-spacing: 0;
                width: 100%;
                border: 1px solid #ddd;
            }
            .column {
                float: left;
                width: 50%;
                padding: 5px;
            }
            .row {
                margin-left:-5px;
                margin-right:-5px;
            }
            * {
                box-sizing: border-box;
            }
            .row::after {
                content: "";
                clear: both;
                display: table;
            }
            table {

                border: 1px solid black;
                box-shadow: 2px 2px 12px darkslategrey;
                width: 370px;
                min-width: 370px;
                padding: 20px;
                margin-bottom: 10px;

            }
            body {
                background-color: beige;
            }

            h1 {
                font-family: 'Anton', sans-serif;
                font-size: 400%;
                text-shadow: 5px 5px 10px gray;
            }
        </style>
    </head>
    <body>
        <h1>Home Inventory</h1>
        <h3>Menu</h3>
        <ul>
            <li><a href="<c:url value="/inventory">
                       <c:param name="action" value="inventory" />
                   </c:url>">Inventory</a> </li>
            <li><a href="<c:url value="/inventory">
                       <c:param name="action" value="admin" />
                   </c:url>">Admin</a> </li>
                <c:if test = "${loginUser.getIsAdmin() == false}">
                <li><a href="<c:url value="/inventory">
                           <c:param name="action" value="ownEdit" />
                       </c:url>">Edit Account</a> </li>
                </c:if>
            <li><a href="<c:url value="/inventory">
                       <c:param name="action" value="logout" />
                   </c:url>">Logout</a> </li>
        </ul>
        <h2>Inventory for ${loginUser.firstName} ${loginUser.lastName}</h2>
        <h3>Item Search</h3>
        <form action="inventory" method="post">
            <table>
                <tr>
                    <td>
                        Item Name:
                    </td>
                    <td>
                        <input type="text" name="itemSearch">
                    </td>
                    <td>
                        <input type="submit" value="Submit">
                        <input type="hidden" name="action" value="itemSearchSubmit">
                    </td>
                </tr>
                <tr>
                            </form>

                    <td>
                        <form action="inventory" method="post">
                            <input type="submit" value="Search All Items">
                            <input type="hidden" name="action" value="searchAll">
                        </form>
                    </td>

                </tr>
            </table>
        <br>
        <c:if test="${searchedItemListUser != null}">
            <table class="list">
                <tr>
                    <th>
                        Category
                    </th>
                    <th>
                        Name
                    </th>
                    <th>
                        Price
                    </th>
                </tr>
                <c:forEach items="${searchedItemListUser}" var="items">
                    <tr>
                        <td>
                            ${items.getCategory().getCategoryName()}
                        </td>
                        <td>
                            ${items.getItemName()}
                        </td>
                        <td>
                            $${items.getPrice()}
                        </td>
                        <td>
                            <form action="inventory" method="post">
                                <input type="submit" value="Delete" name="action">
                                <input type="hidden" name="deleteItem" value="${items.itemID}">
                            </form>
                        </td>
                        <td>
                            <form action="inventory" method="post">
                                <input type="submit" value="Edit Item" name="action">
                                <input type="hidden" name="editItemID" value="${items.itemID}">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${searchedItemListUser == null}">

            <table class="list">
                <tr>
                    <th>
                        Category
                    </th>
                    <th>
                        Name
                    </th>
                    <th>
                        Price
                    </th>
                </tr>
                <c:forEach items="${list}" var="items">
                    <tr>
                        <td>
                            ${items.getCategory().getCategoryName()}
                        </td>
                        <td>
                            ${items.getItemName()}
                        </td>
                        <td>
                            $${items.getPrice()}
                        </td>
                        <td>
                            <form action="inventory" method="post">
                                <input type="submit" value="Delete" name="action">
                                <input type="hidden" name="deleteItem" value="${items.itemID}">
                            </form>
                        </td>
                        <td>
                            <form action="inventory" method="post">
                                <input type="submit" value="Edit Item" name="action">
                                <input type="hidden" name="editItemID" value="${items.itemID}">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <form method="post" action="inventory">
            <table>
                <tr>
                    <td>
                        Category:
                    </td>
                    <td>
                        <select name="addCategory">
                            <c:forEach items="${categories}" var="categories">
                                <option value="${categories.getCategoryID()}">${categories.getCategoryName()}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Name:
                    </td>
                    <td>
                        <input type="text" name="addItemName">
                    </td>
                </tr>
                <tr>
                    <td>
                        Price:
                    </td>
                    <td>
                        <input type="text" name="addPrice">
                    </td>
                </tr>
                <tr>
                    <td>
                    <input type="submit" value="Add">
            <input type="hidden" name="action" value="add">
                    </td>
                    
                </tr>

            </table>
        </form>
        <c:set var = "editItem" value="${editItem}" scope="session"/>
        <c:if test = "${editItem == true}">
            <form method="post" action="inventory">
                <h2>Edit Item</h2>
                <table>
                    <tr>
                        <td>
                            Category:
                        </td>
                        <td>
                            <select name="editCategory">
                                <c:forEach items="${categories}" var="categories">
                                    <option value="${categories.getCategoryID()}">${categories.getCategoryName()}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Name:
                        </td>
                        <td>
                            <input type="text" name="editItemName">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Price:
                        </td>
                        <td>
                            <input type="text" name="editPrice">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Save Item">
                            <input type="hidden" name="action" value="saveItem">
                        </td>
                    </tr>

                </table>

            </form>
        </c:if>
        <c:if test = "${ownEdit == true}">
            <form action="inventory" method="post">
                <h2>Edit Account</h2>
                <table>
                    <tr>
                        <td>
                            Username:
                        </td>
                        <td>
                            <input type="text" name="editUsername" placeholder="${loginUser.getUsername()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Password: 
                        </td>
                        <td>
                            <input type="password" name="editPassword">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Email: 
                        </td>
                        <td>
                            <input type="text" name="editEmail" placeholder="${loginUser.getEmail()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            First Name: 
                        </td>
                        <td>
                            <input type="text" name="editFirst" placeholder="${loginUser.getFirstName()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Last Name: 
                        </td>
                        <td>
                            <input type="text" name="editLast" placeholder="${loginUser.getLastName()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Active:
                        </td>
                        <td>
                            <select id="active" name="active">
                                <option value="active">Active</option>
                                <option value="non-active">Non-Active 
                                </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Save">
                            <input type="hidden" name="action" value="save">
                        </td>
                    </tr>
                </table>
            </form>
        </c:if>
        <b>${messageInventory}</b>
    </body>
</html>

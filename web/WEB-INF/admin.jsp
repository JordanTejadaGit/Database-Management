<%-- 
    Document   : admin
    Created on : Jul 18, 2021, 6:30:43 PM
    Author     : 694952
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
            .message {
                color: red;
                font-size: 25px;
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
        <h2>Logged in User: ${loginUser.getFirstName()} ${loginUser.getLastName()}</h2>
        <h3>Menu</h3>
        <ul>
            <li><a href="<c:url value="/admin">
                       <c:param name="action" value="inventory" />
                   </c:url>">Inventory</a> </li>
            <li><a href="<c:url value="/admin">
                       <c:param name="action" value="admin" />
                   </c:url>">Admin</a> </li>
            <li><a href="<c:url value="/admin">
                       <c:param name="action" value="logout" />
                   </c:url>">Logout</a> </li>
        </ul>
        <h2>Item Search</h2>
        <form action="admin" method="post">
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
        </form>

    <tr>
        <td>
            <form action="admin" method="post">
                <input type="submit" value="Search All Items">
                <input type="hidden" name="action" value="searchAll">
            </form>
        </td>
    </tr>
</table>

<c:if test="${itemListAll != null}">
    <table class="list">
        <tr>
            <th>
                Item Name
            </th>
            <th>
                Category
            </th>
            <th>
                Owner Name
            </th>
        </tr>
        <c:forEach items="${itemListAll}" var="item">
            <tr>
                <td>
                    ${item.getItemName()}
                </td>
                <td>
                    ${item.getCategory().getCategoryName()}
                </td>
                <td>
                    ${item.getOwner().getFirstName()} ${item.getOwner().getLastName()}
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${searchedItemList != null}">
    <table class="list">
        <tr>
            <th>
                Item Name
            </th>
            <th>
                Category
            </th>
            <th>
                Owner Name
            </th>
        </tr>
        <c:forEach items="${searchedItemList}" var="item">
            <tr>
                <td>
                    ${item.getItemName()}
                </td>
                <td>
                    ${item.getCategory().getCategoryName()}
                </td>
                <td>
                    ${item.getOwner().getFirstName()} ${item.getOwner().getLastName()}
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<b class="message">${messageAdmin}</b>
<div class="row">
    <div class="column">
        <h2>Manage Users</h2>
        <table class="list">
            <tr>
                <th>
                    Username
                </th>
                <th>
                    First Name
                </th>
                <th>
                    Last Name
                </th>
                <th>
                    Delete
                </th>
                <th>
                    Edit
                </th>
            </tr>
            <c:forEach items="${users}" var="users">
                <tr>
                    <td>
                        ${users.getUsername()}
                    </td>
                    <td>
                        ${users.getFirstName()}
                    </td>
                    <td>
                        ${users.getLastName()}
                    </td>
                <form action="admin" method="post">
                    <td>
                        <input type="submit" value="Delete" name="action">
                        <input type="hidden" name="deleteUsername" value="${users.getUsername()}">
                    </td>
                </form>
                <form action="admin" method="post">
                    <td>
                        <input type="submit" value="Edit" name="action">
                        <input type="hidden" name="editUserpost" value="${users.getUsername()}">
                    </td>
                </form>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="column">
        <h2>Manage Categories</h2>
        <table class="list">
            <tr>
                <th>
                    Category ID
                </th>
                <th>
                    Category Name
                </th>
                <th>
                    Edit
                </th>
            </tr>
            <c:forEach items="${categories}" var="categories">
                <tr>
                    <td>
                        ${categories.getCategoryID()}
                    </td>
                    <td>
                        ${categories.getCategoryName()}
                    </td>
                <form action="admin" method="post">
                    <td>
                        <input type="submit" value="Edit Category" name="action">
                        <input type="hidden" name="editCategoryID" value="${categories.getCategoryID()}">
                    </td>
                </form>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<div class="row">
    <c:if test = "${edit == false || edit == null}">
        <div class="column">
            <h2>Add User</h2>
            <form action="admin" method="post">
                <table>
                    <tr>
                        <td>
                            Username:
                        </td>
                        <td>
                            <input type="text" name="addUsername">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Password: 
                        </td>
                        <td>
                            <input type="password" name="addPassword">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Email: 
                        </td>
                        <td>
                            <input type="text" name="addEmail">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            First Name: 
                        </td>
                        <td>
                            <input type="text" name="addFirst">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Last Name: 
                        </td>
                        <td>
                            <input type="text" name="addLast">
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
        </div>
    </c:if>
    <c:if test = "${edit == true}">
        <div class="column">
            <h2>Edit User ${editUser.getUsername()}</h2>
            <form action="admin" method="post">
                <table>
                    <tr>
                        <td>
                            Username:
                        </td>
                        <td>
                            <input type="text" name="editUsername" value="${editUser.getUsername()}">
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
                            <input type="text" name="editEmail" placeholder="${editUser.getEmail()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            First Name: 
                        </td>
                        <td>
                            <input type="text" name="editFirst" placeholder="${editUser.getFirstName()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Last Name: 
                        </td>
                        <td>
                            <input type="text" name="editLast" placeholder="${editUser.getLastName()}">
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
                            Admin:
                        </td>
                        <td>
                            <select id="admin" name="admin">
                                <option value="non-admin">Non-Admin</option>
                                <option value="admin">Admin
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
        </div>
    </c:if>
    <c:if test="${editCategoryBoolean == false || editCategoryBoolean == null}">
        <div class="column">
            <h2>Add Category</h2>
            <form action="admin" method="post">
                <table>
                    <tr>
                        <td>
                            Category Name:
                        </td>
                    </tr>

                    <td>
                        <input type="text" name="addCategoryName">
                    </td>
                    <tr>
                        <td>
                            <input type="submit" value="Save">
                            <input type="hidden" name="action" value="save">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </c:if>

    <c:if test="${editCategoryBoolean == true}">
        <div class="column">
            <h2>
                Edit Category ${editCategory.getCategoryName()}
            </h2>
            <form action="admin" method="post">
                <table>
                    <tr>
                        <td>
                            Category Name:
                        </td>
                        <td>
                            <input type="text" name="editCategoryName" placeholder="${editCategory.getCategoryName()}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Save">
                            <input type="hidden" name="action" value="saveCategory">
                        </td>
                    </tr>

                </table>

            </form>
        </div>
    </c:if>
</div>
</body>
</html>

<%-- 
    Document   : reset
    Created on : Aug 15, 2021, 9:05:39 PM
    Author     : 694952
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
        <style>
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
        <c:if test="${resetPassword == null || resetPassword == false}">
            <h1>Reset Password</h1>
            <h2>Please enter email address to reset your password.</h2>
            <form action="reset" method="post">
                <table>
                    <tr>
                        <td>
                            Email Address:
                        </td>
                        <td>
                            <input type="text" name="resetEmail">
                        </td>
                    <tr>
                        <td>
                            <input type="submit" value="Submit">
                <input type="hidden" name="action" value="reset">
                        </td>
                        
                    </tr>
                    </tr>
                </table>
            </form>
        </c:if>
            <c:if test="${resetPassword == true}">
                <h1>Enter New Password</h1>
            <form action="reset" method="post">
                <table>
                    <tr>
                        <td>
                            <input type="password" name="resetPassword">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Submit">
                            <input type="hidden" name="action" value="newPasswordSubmit">
                        </td>
                    </tr>
                </table>
            </form>
            </c:if>
    </body>
</html>

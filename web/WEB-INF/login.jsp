<%-- 
    Document   : login
    Created on : Jul 18, 2021, 6:31:08 PM
    Author     : 694952
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
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
        <h1>HOME nVentory</h1>
        <c:set var = "signUp" value="${signUp}" scope="session"/>
        <c:if test = "${signUp == false || signUp == null}">
            <h2>Login</h2>
            <form action="login" method="post">
                <table>
                    <tr>
                        <td>
                            Username:
                        </td>
                        <td>
                            <input type="text" name="loginUser">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Password:
                        </td>
                        <td>
                            <input type="password" name="loginPass">
                        </td>

                    </tr>
                    <tr>
                        <td>
                        <input type="submit" value="Log in">
                <input type="hidden" name="action" value="login">
                        </td>
                        
                    </tr>
            </form>
            <form action="login" method="post">
                <tr>
                <td>
                    <input type="submit" value="Sign-Up">
                    <input type="hidden" name="action" value="signUp">
                </td>
                </tr>
            </form>
            </table>
            <a href="<c:url value="/reset">
                       <c:param name="action" value="reset" />
                   </c:url>">Reset Password</a>
        </c:if>
        <c:if test = "${signUp == true}">
            <h2>Registration</h2>
            <form action="login" method="post">
                <table>
                    <tr>
                        <td>
                            Username:
                        </td>
                        <td>
                            <input type="text" name="registerUsername">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Password: 
                        </td>
                        <td>
                            <input type="password" name="registerPassword">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Email: 
                        </td>
                        <td>
                            <input type="text" name="registerEmail">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            First Name: 
                        </td>
                        <td>
                            <input type="text" name="registerFirst">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Last Name: 
                        </td>
                        <td>
                            <input type="text" name="registerLast">
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <input type="submit" value="Register">
                <input type="hidden" name="action" value="register">
                        </td>
                    </tr>
                </table>
            </form>
        </c:if>
        <b>${message}</b>
    </body>
</html>

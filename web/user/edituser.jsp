<%--
  Created by IntelliJ IDEA.
  User: BKComputer
  Date: 6/4/2020
  Time: 12:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit user</title>
</head>
<body>
<h1>Edit user</h1>
<h2>
    <a href="./users">Back to list user</a>
</h2>
<form method="post">
    <fieldset>
        <legend>User information</legend>
        <table>
            <tr>
                <td>User ID</td>
<%--                value="${requestScope["product"].getDescription()}"--%>
                <td><input type="text"  value="${requestScope["userExiting"].getId()}"></td>
            </tr>
            <tr>
                <td>User name</td>
                <td><input type="text" name="newName" value="${requestScope["userExiting"].getName()}"></td>
            </tr>
            <tr>
                <td>User Email</td>
                <td><input type="text" name="newEmail" value="${requestScope["userExiting"].getEmail()}"></td>
            </tr>
            <tr>
                <td>User name</td>
                <td><input type="text" name="newCountry" value="${requestScope["userExiting"].getCountry()}"></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Update"></td>
            </tr>
        </table>
    </fieldset>
</form>
</body>
</html>

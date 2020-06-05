<%--
  Created by IntelliJ IDEA.
  User: BKComputer
  Date: 6/4/2020
  Time: 11:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create new User</title>
</head>
<body>
<h1>User Management Appication</h1>
<h2>
    <a href="./users">Back to list user</a>
</h2>
<h3 style="color: gold">${requestScope["message"]}</h3>
<form method="post">
    <table>
        <tr>
            <td>User Name</td>
            <td><input type="text" name="name" size="45"></td>
        </tr>
        <tr>
            <td>User Email</td>
            <td><input type="text" name="email" size="45"></td>
        </tr>
        <tr>
            <td>User Country</td>
            <td><input type="text" name="country" size="45"></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" name="country" size="45" value="Create new User"></td>
        </tr>
    </table>
</form>
</body>
</html>

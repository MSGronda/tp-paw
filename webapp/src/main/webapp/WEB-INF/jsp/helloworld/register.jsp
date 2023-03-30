<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="/css/main.css"/>
</head>
<body>
<h2>Register</h2>
<c:url var="registerUrl" value="/register"/>
<form action="${registerUrl}" method="post">
    <label for="email">Email: </label>
    <input type="text" name="email" id="email"/>
    <br/>
    <label for="nombre">Name: </label>
    <input type="text" name="nombre" id="nombre"/>
    <br/>
    <label for="password">Password: </label>
    <input type="password" name="password" id="password">
    <br/>
    <button type="submit">Let's go!</button>
</form>
</body>
</html>

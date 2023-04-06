<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
</head>
<body>
<h2>Register</h2>
<c:url var="registerUrl" value="/register"/>
<form action="${registerUrl}" method="post">
    <label for="email">Email: </label>
    <input type="text" name="email" id="email"/>
    <br/>
    <label for="username">Username: </label>
    <input type="text" name="username" id="username"/>
    <br/>
    <label for="password">Password: </label>
    <input type="password" name="password" id="password">
    <br/>
    <button type="submit">Let's go!</button>
</form>
</body>
</html>

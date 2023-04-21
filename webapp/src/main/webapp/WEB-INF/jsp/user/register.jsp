<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Register</title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        main {
            background-color: #efefef;
            flex: 1 0 auto;
            display: flex;
            flex-direction: column;
            padding: 8px;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main>
    <h2>Register</h2>
    <c:url var="registerUrl" value="/register"/>
    <form:form modelAttribute="UserForm" action="${registerUrl}" method="post" >
        <label:label for="email">Email: </label:label>
        <input type="text" name="email" id="email"/>
        <br/>
        <label for="username">Username: </label>
        <input type="text" name="username" id="username"/>
        <br/>
        <label for="password">Password: </label>
        <input type="password" name="password" id="password">
        <br/>
        <button type="submit">Let's go!</button>
    </form:form>
</main>
</body>
</html>

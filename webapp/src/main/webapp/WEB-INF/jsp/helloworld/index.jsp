<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Hello</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/materialize.css"  media="screen,projection"/>
</head>
<body>
<h2>Hello ${user.email}!</h2>
</body>
</html>

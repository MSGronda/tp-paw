<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>${query} - Uni</title>
    <jsp:include page="../components/head_shared.jsp"/>
    <style>
        .search-area {
            min-height: 70%; width: 100%;
            display: flex; flex-direction: column; align-items: center;
            background-color: #f3f3f3; padding-top: 1rem;padding-bottom: 3rem;
        }
    </style>
</head>
<body>
    <jsp:include page="../components/navbar.jsp"/>

    <div class="search-area">

        <c:forEach var="subject" items="${subjects}">
            <sl-card class="card-basic">
                    ${subject.name}
            </sl-card>
        </c:forEach>
    </div>

    <jsp:include page="../components/footer.jsp"/>
    <%-- SCRIPT FOR SHOELACE --%>
    <script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/shoelace-autoloader.js"></script>
</body>
</html>

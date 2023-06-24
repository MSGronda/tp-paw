<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html lang="en">
<head>
  <title>Uni</title>

  <jsp:include page="../components/head_shared.jsp"/>

  <style>
      main {
          display: flex;
          flex-direction: column;
          gap: 0.25rem;
      }

    .card-basic {
        width: 100%;
        transition: transform 0.2s ease-in-out;
    }

    .card-basic h3 {
        font-style: normal;
        font-weight: 500;
        margin: 1rem;
    }

    .card-basic:hover {
        cursor: pointer;
        transform: scale(1.02);
    }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-70">
  <h1><spring:message code="degrees.title" /></h1>
      <c:forEach var="degree" items="${degrees}">
        <a href="<c:url value="/degree/${degree.id}"/>">
          <sl-card class="card-basic">
            <h3>
              <c:out value="${degree.name}"/>
            </h3>
          </sl-card>
        </a>
      </c:forEach>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>

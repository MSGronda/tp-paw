<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
  <title>Error</title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>
      sl-button {
          width: fit-content;
      }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-50">
  <h1><spring:message code="page_not_found"/></h1>
  <p><spring:message code="page_not_found.info"/></p>
  <br>
  <sl-button href="${pageContext.request.contextPath}/" variant="primary"><spring:message code="error.return"/></sl-button>
</main>
<jsp:include page="../components/footer.jsp" />
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>

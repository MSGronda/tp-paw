<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <h1>User Not Found</h1>
  <p>The user you are trying to view does not exist.</p>
  <br>
  <sl-button href="${pageContext.request.contextPath}/" variant="primary">Return to Home</sl-button>
</main>
<jsp:include page="../components/footer.jsp" />
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>

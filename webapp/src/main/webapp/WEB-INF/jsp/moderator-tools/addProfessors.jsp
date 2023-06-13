<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title><spring:message code="subject.create" /></title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>

  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50">
  <sl-card>
    <table>

    </table>
  </sl-card>
  <form:form modelAttribute="SubjectForm" class="col s12" method="post" action="${CreateSubject}">

  </form:form>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>


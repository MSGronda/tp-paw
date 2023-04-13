<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
  <title>Uni</title>

  <jsp:include page="../components/head_shared.jsp"/>

  <style>
      main {
          background-color: #efefef;
          flex: 1 0 auto;
          display: flex;
          flex-direction: column;
      }

      sl-tab-panel::part(base) {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));

          padding: 1.25rem;
          gap: 1rem;
      }

      sl-tab-group::part(nav) {
          background-color: #fff;
      }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main>
  <sl-tab-group>
    <c:forEach var="degree" items="${degrees}">
      <sl-tab slot="nav" panel="degree-${degree.id}">
        <c:out value="${degree.name}"/>
      </sl-tab>
    </c:forEach>

    <c:forEach var="degree" items="${degrees}">
      <sl-tab-panel name="degree-${degree.id}">
        <c:forEach var="subject" items="${subjects[degree]}">
          <c:set var="subject" value="${subject}" scope="request"/>
          <c:set var="subProfs" value="${profs[subject]}" scope="request"/>
          <c:import url="../components/subject_card.jsp"/>
        </c:forEach>
      </sl-tab-panel>
    </c:forEach>
  </sl-tab-group>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>

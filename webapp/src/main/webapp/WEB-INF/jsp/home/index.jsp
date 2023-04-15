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
          padding: 8px;
      }

      sl-tab-panel.semester-panel::part(base) {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));

          padding: 1.25rem;
          gap: 1rem;
      }

      sl-tab-panel.degree-panel::part(base) {
          display: flex;
          flex-direction: column;
          padding: 0;
      }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main>
  <sl-tab-group class="degree-group container-70">
    <c:forEach var="degree" items="${degrees}">
      <sl-tab slot="nav" panel="degree-${degree.id}">
        <c:out value="${degree.name}"/>
      </sl-tab>
    </c:forEach>
    <sl-tab slot="nav" disabled="true">
      Ingeniería Industrial
    </sl-tab>
    <sl-tab slot="nav" disabled="true">
      Ingeniería Química
    </sl-tab>
    <sl-tab slot="nav" disabled="true">
      Ingeniería Mecánica
    </sl-tab>

    <c:forEach var="degree" items="${degrees}">
      <sl-tab-panel class="degree-panel" name="degree-${degree.id}">
        <sl-tab-group class="semester-group">
          <c:forEach var="semester" items="${degree.semesters}">
            <sl-tab slot="nav" panel="semester-${semester}">
              <c:out value="${semester.number}° Semester"/>
            </sl-tab>
          </c:forEach>

          <c:forEach var="semester" items="${degree.semesters}">
            <sl-tab-panel class="semester-panel" name="semester-${semester}">
              <c:forEach var="subjectId" items="${semester.subjectIds}">
                <c:set var="subject" value="${subsById[subjectId]}" scope="request"/>
                <c:set var="subProfs" value="${profsBySubId[subjectId]}" scope="request"/>
                <c:import url="../components/subject_card.jsp"/>
              </c:forEach>
            </sl-tab-panel>
          </c:forEach>
        </sl-tab-group>
      </sl-tab-panel>
    </c:forEach>
  </sl-tab-group>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>

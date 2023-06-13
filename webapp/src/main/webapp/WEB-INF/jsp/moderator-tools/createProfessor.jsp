<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title><spring:message code="professor.info" /></title>
  <jsp:include page="../components/head_shared.jsp"/>

  <style>
    .table {
      display: flex;
      justify-content: center;
      align-items: center;
    }
    td {
      font-size: 1.2rem;
      padding-left: 1rem;
      padding-right: 1rem;
    }
    table {
      width: 100%;
    }
    .table-row {
      display: flex;
      justify-content: space-around;
      align-items: baseline;
    }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50">
  <sl-tab-group>
    <sl-tab slot="nav" panel="general">Crear Profesor</sl-tab>
    <sl-tab slot="nav" panel="custom">Agregar a Materia</sl-tab>
    <sl-tab slot="nav" panel="advanced">Advanced</sl-tab>
    <sl-tab slot="nav" panel="disabled" disabled>Disabled</sl-tab>

    <sl-tab-panel name="general">
      <form:form modelAttribute="professorForm" class="col s12" method="post" action="${CreateProfessor}">
        <div class="table">
          <table>
            <thead>
            <th colspan="2"><h1><spring:message code="professor.info"/></h1></th>
            </thead>
            <tbody>
            <tr class="table-row">
              <td><spring:message code="professor.name"/></td>
              <td>
                <form:errors path="name" cssClass="error" element="p"/>
                <sl-input name="name" path="name" value="${professorForm.name}" help-text="<spring:message code="professor.name.help" />"></sl-input>
              </td>
            </tr>
            <tr>
              <td class="column-center">
                <sl-button type="submit" variant="success"><spring:message code="professor.create"/></sl-button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>

      </form:form>
    </sl-tab-panel>
    <sl-tab-panel name="custom">This is the custom tab panel.</sl-tab-panel>
    <sl-tab-panel name="advanced">This is the advanced tab panel.</sl-tab-panel>
    <sl-tab-panel name="disabled">This is a disabled tab panel.</sl-tab-panel>
  </sl-tab-group>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>


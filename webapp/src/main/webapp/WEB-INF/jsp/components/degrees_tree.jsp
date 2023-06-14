<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style>
    .YearItem {
      display: flex;
      flex-direction: row;
      width: 100%;
    }

    .checkbox-label{
      margin-top: 1.4rem;
    }

    .tree{
      width: 100%;
    }

    .elective-tree{
      margin-left: 1.65rem;
      width: 100%;
    }
</style>

<c:set var="degrees" value="${requestScope.degrees}"/>

<c:forEach var="degree" items="${degrees}">
  <h1 id="degree-${degree.id}" style="display: none" class="degree-title"><c:out value="${degree.name}"/></h1>
</c:forEach>
<h3 style="font-weight: normal"><spring:message code="register.selectSubjects"/></h3>
<c:forEach var="degree" items="${degrees}">
  <sl-tree id="tree-${degree.id}" style="display: none" class="degree-tree">
    <c:forEach var="year" items="${degree.years}">
      <div class="YearItem">
        <div class="checkbox-label">
          <sl-checkbox id="degree-${degree.id}-year-checkbox-${year.number}" class="degree-${degree.id}-year-checkbox"></sl-checkbox>
        </div>
        <sl-tree-item class="tree">
          <h3 ><spring:message code="subject.year" arguments="${year.number}"/></h3>
          <c:forEach var="subject" items="${year.subjects}">
            <sl-tree-item>
              <sl-checkbox id="degree-${degree.id}-year-${year.number}-subject-${subject.id}" class="degree-${degree.id}-year-${year.number}-subject">
                <c:out value="${subject.name}"/>
              </sl-checkbox>
            </sl-tree-item>
          </c:forEach>
        </sl-tree-item>
      </div>
    </c:forEach>
    <div id="elective-tree-${degree.id}" style="display:none;">
      <div class="YearItem">
        <sl-tree-item class="elective-tree">
          <h3><spring:message code="home.electives"/></h3>
          <c:forEach var="elective" items="${degree.electives}">
            <sl-tree-item>
              <sl-checkbox id="elective-${degree.id}-subject-${elective.id}" class="elective-${degree.id}-subject">
                <c:out value="${elective.name}"/>
              </sl-checkbox>
            </sl-tree-item>
          </c:forEach>
        </sl-tree-item>
      </div>
    </div>
  </sl-tree>
</c:forEach>
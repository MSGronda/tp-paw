<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
  <title>${subject.name}</title>
  <jsp:include page="../components/head_shared.jsp"/>
  <style>
      .info {
          padding-bottom: 2rem;
      }
      .review_bt {
          width: 15rem;
          align-self: center;
          justify-self: center;
      }

      .filter {
          display: flex;
          flex-direction: row-reverse;
          margin-right: 20%;
      }

      h1 {
          font-weight: 500;
          font-size: 25px
      }

      .review-column {
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          align-items: center;
          width: 100%;
          padding-bottom: 3rem;

      }

      .card-header {
          width: 45%;
          margin: 15px;
          /*max-width: 2000px;*/
      }

      .card-header [slot='header'] {
          display: flex;
          align-items: center;
          justify-content: space-between;
      }

      .card-header h3 {
          margin: 0;
      }

      .break-text {
          overflow-wrap: break-word;
          margin-bottom: 2%;
      }

      a {
          color: #0369a1;
          background-color: transparent;
          text-decoration: none;
      }

      hr {
          width: 30rem;
      }

      table {
          border-collapse: collapse;
          width: 100%;
          margin-bottom: 1rem;
          box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.05);
          background-color: #fff;
          color: #4b4f56;
          border-radius: 0.25rem;
      }

      thead {
          background-color: #f8f9fa;
      }
      thead th {
          text-align: left;
          font-weight: 600;
          padding: 0.6rem;
          border-bottom: 1px solid #e9ecef;
      }
      tbody th{
          text-align: left;
          font-weight: 600;
          padding: 0.6rem;
          border-bottom: 1px solid #e9ecef;
      }
      tbody tr {
          transition: background-color 0.15s ease-in-out;
      }

      tbody tr:hover {
          background-color: #f8f9fa;
      }
      tbody td {
          border-bottom: 1px solid #e9ecef;
          padding: 0.75rem;
      }
      .main-body{
          width: 100%;
          height: 100%;
      }
  </style>

</head>
<body>

<jsp:include page="../components/navbar.jsp"/>

<main>
  <div class="info container-50">
    <h1>
      <c:out value="${subject.name}"/> - <c:out value="${subject.id}"/>
    </h1>
    <sl-card class="main-body">
      <sl-tab-group>
        <sl-tab slot="nav" panel="general-panel"><spring:message code="subject.general"/></sl-tab>
        <sl-tab slot="nav" panel="times-panel"><spring:message code="subject.times"/></sl-tab>
        <sl-tab slot="nav" panel="professors-panel"><spring:message code="subject.classProf"/></sl-tab>

          <sl-tab-panel name="general-panel">
              <table>
                  <tbody>
                    <tr>
                        <th><spring:message code="subject.department"/></th>
                        <td><c:out value="${subject.department}"/></td>
                    </tr>
                    <tr>
                        <th><spring:message code="subject.credits"/> </th>
                        <td><c:out value="${subject.credits}"/></td>
                    </tr>
                  <tr>
                      <th><spring:message code="subject.prerequisites"/></th>
                      <td>
                          <c:if test="${empty prereqNames}">
                              <spring:message code="subject.prerequisites?"/>
                          </c:if>
                          <c:forEach var="prerec" items="${prereqNames}" varStatus="status">
                              <a href='<c:url value="/subject/${prerec.key}"/>'><c:out value="${prerec.value}"/></a>
                              <c:if test="${not status.last}">
                                  ,
                              </c:if>
                          </c:forEach>
                      </td>
                  </tr>
                    <tr>
                        <th><spring:message code="subject.professors"/></th>
                        <td>
                            <c:forEach var="proffesor" items="${professors}" varStatus="status">
                                <sl-badge variant="primary">
                                    <c:out value="${proffesor.name}"/>
                                </sl-badge>

                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <th><spring:message code="subject.difficulty"/></th>
                        <td>
                            <c:choose>
                                <c:when test="${difficulty == 0}">
                                    <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
                                </c:when>
                                <c:when test="${difficulty == 1}">
                                    <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
                                </c:when>
                                <c:when test="${difficulty == 2}">
                                    <sl-badge size="medium" variant="danger"><spring:message code="form.hard"/></sl-badge>
                                </c:when>
                                <c:otherwise>
                                    <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif"/></sl-badge>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th><spring:message code="subject.time" /></th>
                        <td>
                            <c:choose>
                                <c:when test="${time == 0}">
                                    <sl-badge size="medium" ariant="primary"><spring:message code="form.NotTimeDemanding" /></sl-badge>
                                </c:when>
                                <c:when test="${time == 1}">
                                    <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding" /></sl-badge>
                                </c:when>
                                <c:otherwise>
                                    <sl-badge size="medium" variant="neutral"><spring:message code="form.noDif" /></sl-badge>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                  </tbody>
              </table>

          </sl-tab-panel>

        <sl-tab-panel name="times-panel">
            <table>
                <thead>
                    <tr>
                        <th><spring:message code="subject.classCode"/></th>
                        <th><spring:message code="subject.classDay"/></th>
                        <th><spring:message code="subject.classTimes"/></th>
                        <th><spring:message code="subject.classMode"/></th>
                        <th><spring:message code="subject.classBuilding"/></th>
                        <th><spring:message code="subject.classNumber"/></th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="clase" items="${classes}">
                    <c:forEach var="horario" items="${clase.classTimes}"  varStatus="status">
                        <tr>
                            <td>
                                <c:if test="${status.first}">
                                    <c:out value="${clase.idClass}"/>
                                </c:if>
                            </td>
                            <td><spring:message code="subject.classDay${horario.day}"/></td>

                            <td><c:out value="${horario.startTime.hours}:${horario.startTime.minutes}"/><c:if test="${horario.startTime.minutes  == 0}">0</c:if>
                                - <c:out value="${horario.endTime.hours}:${horario.endTime.minutes}"/><c:if test="${horario.endTime.minutes == 0}">0</c:if>
                            </td>
                            <td><c:out value="${horario.mode}"/></td>
                            <td><c:out value="${horario.building}"/></td>
                            <td><c:out value="${horario.classLoc}"/></td>
                        </tr>
                    </c:forEach>
                </c:forEach>
                </tbody>
        </table>
    </sl-tab-panel>

          <sl-tab-panel name="professors-panel">
              <table>
                  <thead>
                  <tr>
                      <th><spring:message code="subject.classCode"/></th>
                      <th><spring:message code="subject.classProf"/></th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach var="clase" items="${classes}">
                      <tr>
                      <td>
                          <c:out value="${clase.idClass}"/>
                      </td>
                      <td>
                          <c:forEach var="prof" items="${classProfs[clase.idClass]}">
                              <sl-badge variant="primary">
                                  <c:out value="${prof.name}"/>
                              </sl-badge>
                          </c:forEach>
                      </td>

                      </tr>
                  </c:forEach>
                  </tbody>
              </table>
          </sl-tab-panel>
    </sl-tab-group>
    </sl-card>





  </div>
  <sl-button href='<c:url value="/review/${subject.id}"/>' variant="primary" size="large" pill class="review_bt">
    <spring:message code="subject.review"/></sl-button>
  <br/>
  <hr/>
  <div class="filter">
    <sl-tooltip content="Open viewing sorts" placement="bottom">
      <sl-button variant="text" size="large">
        <sl-icon slot="suffix" name="sort-down"></sl-icon>
        <spring:message code="subject.sort"/></sl-button>
    </sl-tooltip>
  </div>

  <div class="review-column">
    <c:if test="${empty reviews}">
      <h3><spring:message code="subject.noreviews"/></h3>
    </c:if>
    <c:forEach var="review" items="${reviews}">
      <sl-card class="card-header">
        <div slot="header">
          <c:out value="${review.userEmail}"/>
        </div>

        <div class="break-text">
          <c:out value="${review.text}"/>
        </div>
        <div>
          <c:choose>
            <c:when test="${review.easy == 0}">
              <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
            </c:when>
            <c:when test="${review.easy == 1}">
              <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
            </c:when>
            <c:otherwise>
              <sl-badge size="medium" variant="danger"><spring:message code="form.hard"/></sl-badge>
            </c:otherwise>
          </c:choose>

          <c:choose>
            <c:when test="${review.timeDemanding == 1}">
              <sl-badge size="medium" variant="warning"><spring:message code="form.timeDemanding"/></sl-badge>
            </c:when>
            <c:otherwise>
              <sl-badge size="medium" ariant="primary"><spring:message code="form.NotTimeDemanding"/></sl-badge>
            </c:otherwise>
          </c:choose>
        </div>
      </sl-card>
    </c:forEach>
  </div>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>

</body>
</html>

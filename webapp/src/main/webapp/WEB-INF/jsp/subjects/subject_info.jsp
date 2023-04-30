<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
  <title>${subject.name}</title>
  <jsp:include page="../components/head_shared.jsp"/>
  <style>
      #more {
          display: none;
      }
      .order-menu{
          display: flex;
          flex-direction: row;
          justify-items:center;
          align-items: center;
      }
      #diffuclty-down{
          font-size: 0.8rem;
      }
      #diffuclty-up{
          font-size: 0.8rem;
      }
      #timedemand-down{
          font-size: 0.8rem;
      }
      #timedemand-up{
          font-size: 0.8rem;
      }

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
          padding-bottom: 3rem;

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
      .main-body{
          width: 100%;
          height: 100%;
      }
      .breadcrumb-area{
          padding-top: 1rem;
      }


      .text-center{
          display: flex;
          justify-content: center;
      }

      <jsp:include page="../components/table_style.jsp"/>

  </style>

</head>
<body>

<jsp:include page="../components/navbar.jsp"/>

<main>
  <div class="info container-50">

      <div class="breadcrumb-area">
          <sl-breadcrumb>
              <sl-breadcrumb-item><a href='<c:url value="/"/>'><spring:message code="subject.home"/></a></sl-breadcrumb-item>
              <c:choose>
                  <c:when test="${year == 0}">
                      <sl-breadcrumb-item><a href='<c:url value="/"/>'><spring:message code="home.electives"/></a></sl-breadcrumb-item>
                  </c:when>
                  <c:otherwise>
                      <sl-breadcrumb-item><a href='<c:url value="/"/>'><spring:message code="subject.year" arguments="${year}"/></a></sl-breadcrumb-item>
                  </c:otherwise>
              </c:choose>
          </sl-breadcrumb>
      </div>
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
                          <c:forEach var="prof" items="${clase.professors}">
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
    <c:if test="${didReview}">
        <sl-button variant="primary" size="large" pill class="review_bt" disabled><spring:message code="subject.review"/></sl-button>
        <br/>
        <div class="text-center">
            <spring:message code="subject.alreadyReviewed"/>
        </div>
    </c:if>
    <c:if test="${!didReview}">
        <sl-button href='<c:url value="/review/${subject.id}"/>' variant="primary" size="large" pill class="review_bt">
            <spring:message code="subject.review"/></sl-button>
    </c:if>
  <br/>
  <hr/>
  <div class="filter">
    <sl-dropdown>
      <sl-button variant="text" slot="trigger" size="large" caret>
        <spring:message code="subject.sort"/>
        <sl-icon slot="prefix" name="sort-down"></sl-icon>
      </sl-button>
      <sl-menu>
        <sl-menu-item id="difficulty-order">
            <div class="order-menu">
                <spring:message code="subject.order.difficulty"/>
                <section id="diffuclty-down">
                    <sl-icon slot="suffix" name="arrow-down" ></sl-icon>
                </section>
                <section id="diffuclty-up">
                    <sl-icon slot="suffix" name="arrow-up" ></sl-icon>
                </section>
            </div>
        </sl-menu-item>
        <sl-menu-item id="timedemand-order">
            <div class="order-menu">
                <spring:message code="subject.order.time"/>
                <section id="timedemand-down">
                    <sl-icon slot="suffix" name="arrow-down" ></sl-icon>
                </section>
                <section id="timedemand-up">
                    <sl-icon slot="suffix" name="arrow-up"></sl-icon>
                </section>
            </div>
        </sl-menu-item>
      </sl-menu>
    </sl-dropdown>
  </div>

  <div class="review-column container-50">
    <c:if test="${empty reviews}">
      <h3><spring:message code="subject.noreviews"/></h3>
    </c:if>
    <c:forEach  var="review" items="${reviews}">

        <c:set var="review" value="${review}" scope="request"/>
        <c:set var="fromProfile" value="${false}" scope="request"/>
        <c:set var="userVotes" value="${userVotes}" scope="request"/>
        <c:import url="../components/review_card.jsp"/>

    </c:forEach>
  </div>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="${pageContext.request.contextPath}/js/subject-view.js" defer></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script>
    const showMore = document.querySelector('.showMore');

    if(showMore !== null){
        showMore.addEventListener('click',() => {
            const dots = document.getElementById("dots");
            const moreText = document.getElementById("more");

            if (dots.style.display === "none") {
                dots.style.display = "inline";
                showMore.innerHTML = "<spring:message code="subject.showMore" /><sl-icon name=\"chevron-down\"></sl-icon>";
                moreText.style.display = "none";
            } else {
                dots.style.display = "none";
                showMore.innerHTML = "<spring:message code="subject.showLess" /><sl-icon name=\"chevron-up\"></sl-icon>";
                moreText.style.display = "inline";
            }
        });
    }

    // perdon

    function updateCounters(formId, changeLikes, changeDislikes){
        const likehtml = $('#like-number-'+formId)
        const like_string = likehtml.text()
        likehtml.text(parseInt(like_string) + changeLikes)

        const dislikehtml = $('#dislike-number-'+formId)
        const dislike_string = dislikehtml.text()
        dislikehtml.text(parseInt(dislike_string) + changeDislikes)
    }

    function submitForm(formId, prevVote ,newVote) {

        $('#' + formId + ' input[name=vote]').val(newVote)

        $.ajax({
            url: '${pageContext.request.contextPath}/voteReview',
            type: 'POST',
            data: $('#'+formId).serialize(),
            success: function(response) {

                // const likeChage = 1, dislikeChange = prevVote === 0 ? 0 : -1
                var likeChange, dislikeChange


                const like = $('#like-icon-'+formId)
                const dislike = $('#dislike-icon-'+formId)
                switch (newVote) {
                    case 0:
                        like.css("color","#4a90e2")
                        dislike.css("color","#4a90e2")

                        if(prevVote === 1)
                            updateCounters(formId,-1,0);
                        else
                            updateCounters(formId,0,-1);
                        break;
                    case 1:
                        like.css("color","#f5a623")
                        dislike.css("color","#4a90e2")
                        if(prevVote === 0)
                            updateCounters(formId,1,0)
                        else
                            updateCounters(formId,1,-1)
                        break;
                    case -1:
                        like.css("color","#4a90e2")
                        dislike.css("color","#f5a623")
                        if(prevVote === 0)
                            updateCounters(formId,0,1)
                        else
                            updateCounters(formId,-1,1)
                        break;
                }

            },
            error: function(xhr, status, error) {

            }
        });
    }
    $(document).ready(function() {
        $('.vote-button').click(function() {
            const formId = $(this).data('form-id');
            const newVote = $(this).data('form-value');

            const prevVote = parseInt($('#' + formId + ' input[name=vote]').val())

            if(newVote !== prevVote)
                submitForm(formId, prevVote ,newVote);
            else
                submitForm(formId, prevVote ,0);
        })
    });

</script>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ar.edu.itba.paw.models.enums.Difficulty" %>
<%@ page import="ar.edu.itba.paw.models.enums.TimeDemanding" %>
<%@ page import="ar.edu.itba.paw.models.enums.SubjectProgress" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
          /*align-self: center;*/
          /*justify-self: center;*/
      }
      .progress-bt{
          width: 7rem;
          padding-left: 1rem;
      }


      .filter {
          display: flex;
          flex-direction: row;
          align-items: center;
          justify-content: space-between;
      }
      .actual-filter{
          margin-left: 27%;
      }
      .filter-dropdown{
          margin-right: 27%;
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
      .review-and-progress{
          display: flex;
          align-items: center;
          justify-content: center;
      }
      .review-and-progress-area{
          display: flex;
          flex-direction: column;
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
              <sl-breadcrumb-item><a href='<c:url value="/degree/${user.degree.id}"/>'><c:out value="${user.degree.name}"/></a></sl-breadcrumb-item>
              <c:choose>
                  <c:when test="${year == 0}">
                      <sl-breadcrumb-item><a href='<c:url value="/degree/${user.degree.id}?tab=electives"/>'><spring:message code="home.electives"/></a></sl-breadcrumb-item>
                  </c:when>
                  <c:otherwise>
                      <sl-breadcrumb-item><a href='<c:url value="/degree/${user.degree.id}?tab=${year}"/>'><spring:message code="subject.year" arguments="${year}"/></a></sl-breadcrumb-item>
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
                          <c:if test="${empty subject.prerequisites}">
                              <spring:message code="subject.prerequisites?"/>
                          </c:if>
                          <c:forEach var="prereq" items="${subject.prerequisites}" varStatus="status">
                              <a href='<c:url value="/subject/${prereq.id}"/>'><c:out value="${prereq.name}"/></a>
                              <c:if test="${not status.last}">
                                  ,
                              </c:if>
                          </c:forEach>
                      </td>
                  </tr>
                    <tr>
                        <th><spring:message code="subject.professors"/></th>
                        <td>
                            <c:forEach var="professor" items="${subject.professors}" varStatus="status">
                                <sl-badge variant="primary">
                                    <c:out value="${professor.name}"/>
                                </sl-badge>

                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <th><spring:message code="subject.difficulty"/></th>
                        <td>
                            <c:choose>
                                <c:when test="${subject.reviewStats.getDifficulty() == Difficulty.EASY}">
                                    <sl-badge size="medium" variant="success"><spring:message code="form.easy"/></sl-badge>
                                </c:when>
                                <c:when test="${subject.reviewStats.getDifficulty() == Difficulty.MEDIUM}">
                                    <sl-badge size="medium" variant="primary"><spring:message code="form.normal"/></sl-badge>
                                </c:when>
                                <c:when test="${subject.reviewStats.getDifficulty() == Difficulty.HARD}">
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
                                <c:when test="${subject.reviewStats.getTimeDemanding() == TimeDemanding.LOW}">
                                    <sl-badge size="medium" variant="success"><spring:message code="form.NotTimeDemanding" /></sl-badge>
                                </c:when>
                                <c:when test="${subject.reviewStats.getTimeDemanding() == TimeDemanding.MEDIUM}">
                                    <sl-badge size="medium" variant="primary"><spring:message code="form.averageTimeDemand" /></sl-badge>
                                </c:when>
                                <c:when test="${subject.reviewStats.getTimeDemanding() == TimeDemanding.HIGH}">
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
                <c:forEach var="subjectClass" items="${subject.classes}">
                    <c:forEach var="classTime" items="${subjectClass.classTimes}"  varStatus="status">
                        <tr>
                            <td>
                                <c:if test="${status.first}">
                                    <c:out value="${subjectClass.classId}"/>
                                </c:if>
                            </td>
                            <td><spring:message code="subject.classDay${classTime.day}"/></td>

                            <td><c:out value="${classTime.startTime.hour}:${classTime.startTime.minute}"/><c:if test="${classTime.startTime.minute  == 0}">0</c:if>
                                - <c:out value="${classTime.endTime.hour}:${classTime.endTime.minute}"/><c:if test="${classTime.endTime.minute == 0}">0</c:if>
                            </td>
                            <td><c:out value="${classTime.mode}"/></td>
                            <td><c:out value="${classTime.building}"/></td>
                            <td><c:out value="${classTime.classLoc}"/></td>
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
                  <c:forEach var="subjectClass" items="${subject.classes}">
                      <tr>
                      <td>
                          <c:out value="${subjectClass.classId}"/>
                      </td>
                      <td>
                          <c:forEach var="prof" items="${subjectClass.professors}">
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
    <div class="review-and-progress-area">
        <div class="review-and-progress">
            <c:if test="${didReview}">
                <sl-button variant="primary" size="large" pill class="review_bt" disabled><spring:message code="subject.review"/></sl-button>
            </c:if>
            <c:if test="${!didReview}">
                <sl-button href='<c:url value="/review/${subject.id}"/>' variant="primary" size="large" pill class="review_bt">
                    <spring:message code="subject.review"/></sl-button>
            </c:if>


            <form id="sub-progress" style="margin: 0" >
                <input type="hidden" name="idSub" id="idSub" value="${subject.id}">
                <input type="hidden" name="progress" id="progress" value="${progress.value}">
                <sec:authorize access="isAuthenticated()">
                    <sl-tooltip content="<spring:message code="subject.progress.tooltip"/>">
                        <c:choose>
                            <c:when test="${progress == SubjectProgress.DONE}">
                                <sl-button class="progress-bt" variant="primary" size="large" pill data-form-id="sub-progress" data-form-value="1">
                                    <spring:message code="subject.progress.done"/>
                                </sl-button>
                            </c:when>
                            <c:otherwise>
                                <sl-button class="progress-bt" variant="primary" size="large" outline pill data-form-id="sub-progress" data-form-value="1">
                                    <spring:message code="subject.progress.pending"/>
                                </sl-button>
                            </c:otherwise>
                        </c:choose>
                    </sl-tooltip>
                </sec:authorize>
            </form>
        </div>
        <br/>

        <c:if test="${didReview}">
            <div class="text-center">
                <spring:message code="subject.alreadyReviewed"/>
            </div>
        </c:if>
    </div>

  <br/>
  <hr/>
    <c:if test="${not empty reviews}">
        <div class="filter">
            <div class="actual-filter">
                <spring:message code="subject.actualFilter"/>
                <c:choose>
                    <c:when test="${order == \"easy\" && dir == \"desc\"}">
                        <spring:message code="subject.order.difficulty"/>
                        <spring:message code="subject.directionDesc"/>
                    </c:when>
                    <c:when test="${order == \"timedemanding\" && dir == \"asc\"}">
                        <spring:message code="subject.order.time"/>
                        <spring:message code="subject.directionAsc"/>
                    </c:when>
                    <c:when test="${order == \"timedemanding\" && dir == \"desc\"}">
                        <spring:message code="subject.order.time"/>
                        <spring:message code="subject.directionDesc"/>
                    </c:when>
                    <c:otherwise>
                        <spring:message code="subject.order.difficulty"/>
                        <spring:message code="subject.directionAsc"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="filter-dropdown">
                <sl-dropdown>
                    <sl-button variant="text" slot="trigger" size="large" caret>
                        <spring:message code="subject.sort"/>
                        <sl-icon slot="prefix" name="sort-down"></sl-icon>
                    </sl-button>
                    <sl-menu>
                        <sl-menu-item id="difficulty-order">
                            <div class="order-menu">
                                <spring:message code="subject.order.difficulty"/>
                                <section id="difficulty-down">
                                    <sl-icon slot="suffix" name="arrow-down" ></sl-icon>
                                </section>
                                <section id="difficulty-up">
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
        </div>
    </c:if>

  <div class="review-column container-50">
    <c:if test="${empty reviews}">
      <h3><spring:message code="subject.noreviews"/></h3>
    </c:if>
    <c:forEach  var="review" items="${reviews}">
        <c:set var="review" value="${review}" scope="request"/>
        <c:set var="fromProfile" value="${false}" scope="request"/>
        <c:set var="userVotes" value="${userVotes}" scope="request"/>
        <c:set var="user" value="${user}" scope="request"/>
        <c:import url="../components/review_card.jsp"/>

    </c:forEach>
      <c:if test="${not empty reviews}">
          <div>
              <sl-radio-group name="pagination-radio" value="${currentPage}">
                  <sl-radio-button id="prevPage" value="-1" <c:if test="${currentPage-1 <= 0}">disabled</c:if>>
                      <sl-icon slot="prefix" name="chevron-left"></sl-icon>
                      <spring:message code="subject.previousPage" />
                  </sl-radio-button>
                  <c:forEach var="pageNum" begin="1" end="${totalPages}">
                      <c:choose>
                          <c:when test="${(pageNum > currentPage -2 and pageNum < currentPage + 2) or (pageNum == 1 or pageNum == totalPages)}">
                              <div class="active-pag-buttons">
                                  <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
                              </div>
                          </c:when>
                          <c:when test="${pageNum < totalPages and pageNum == currentPage + 2}">
                              <div class="active-pag-buttons">
                                  <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
                                  <sl-radio-button value="..." disabled>...</sl-radio-button>
                              </div>
                          </c:when>
                          <c:when test="${pageNum > 1 and pageNum == currentPage - 2}">
                              <div class="active-pag-buttons">
                                  <sl-radio-button value="..." disabled>...</sl-radio-button>
                                  <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
                              </div>
                          </c:when>
                          <c:otherwise>
                              <div class="hidden-pag-buttons">
                                  <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
                              </div>
                          </c:otherwise>
                      </c:choose>

                  </c:forEach>
                  <sl-radio-button id="nextPage" value="0" <c:if test="${currentPage >= totalPages}">disabled</c:if>>
                      <sl-icon slot="suffix" name="chevron-right"></sl-icon>
                      <spring:message code="subject.nextPage" />
                  </sl-radio-button>
              </sl-radio-group>
          </div>
      </c:if>
  </div>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="${pageContext.request.contextPath}/js/subject_progress.js"></script>
<script src="${pageContext.request.contextPath}/js/url-param-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/review_card.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script>
    const showMoreElems = document.querySelectorAll('.showMore');
    showMoreElems.forEach(item => item.addEventListener('click',() => {
        const dots = item.parentElement.querySelector(".dots");
        const moreText = item.parentElement.querySelector(".more");
        if(dots !== null && moreText !== null){
            if (dots.style.display === "none") {
                dots.style.display = "inline";
                item.innerHTML = "<spring:message code="subject.showMore" /><sl-icon name=\"chevron-down\"></sl-icon>";
                moreText.style.display = "none";
            } else {
                dots.style.display = "none";
                item.innerHTML = "<spring:message code="subject.showLess" /><sl-icon name=\"chevron-up\"></sl-icon>";
                moreText.style.display = "inline";
            }
        }
    }))

    $(document).ready(function() {
        $('.vote-button').click(function() {
            const formId = $(this).data('form-id');
            const newVote = $(this).data('form-value');

            const prevVote = parseInt($('#' + formId + ' input[name=vote]').val())

            if(newVote !== prevVote)
                submitReviewVoteForm('${pageContext.request.contextPath}/voteReview',formId, prevVote ,newVote);
            else
                submitReviewVoteForm('${pageContext.request.contextPath}/voteReview',formId, prevVote ,0);
        })

        <sec:authorize access="isAuthenticated()">
            $('.progress-bt').click(function() {
                const formId = $(this).data('form-id');
                const prevProgress = parseInt($('#' + formId + ' input[name=progress]').val())

                if( 1- prevProgress === 0)
                    submitSubjectProgressForm('${pageContext.request.contextPath}/subjectProgress', formId, 1 - prevProgress, "<spring:message code="subject.progress.pending"/>");
                else
                    submitSubjectProgressForm('${pageContext.request.contextPath}/subjectProgress', formId, 1 - prevProgress, "<spring:message code="subject.progress.done"/>");
            })
        </sec:authorize>
    });

</script>
<script>
    let urlParams = new URLSearchParams(window.location.search);
    let dir = urlParams.get('dir');

    const orderBtns = [
        ['difficulty-order','difficulty','difficulty-down','difficulty-up'],
        ['timedemand-order','timedemanding','timedemand-down','timedemand-up'],
    ]
    const asc='asc'
    const desc = 'desc'
    for(let elem in orderBtns) {
        const btnElem = document.getElementById(orderBtns[elem][0])
        if(typeof btnElem !== null && elem !== 'undefined' ){
            btnElem.addEventListener('click',
                function(event) {
                    event.preventDefault();
                    let url = window.location.href;
                    url = addOrUpdateParam(url,"order",orderBtns[elem][1]);
                    url = addOrUpdateParam(url,"pageNum","1");
                    if(dir !== null && dir === asc){
                        url = addOrUpdateParam(url,"dir",desc);
                    } else {
                        url = addOrUpdateParam(url,"dir",asc);
                    }
                    window.location.href = url;
                });
            if(orderBtns[elem][0] === 'difficulty-order' && dir === desc){
                const section1 = document.getElementById(orderBtns[elem][2]);
                const section2 = document.getElementById(orderBtns[elem][3]);
                section1.style.display = "block";
                section2.style.display = "none";
            } else if(orderBtns[elem][0] === 'timedemand-order' && dir === desc) {
                const section1 = document.getElementById(orderBtns[elem][2]);
                const section2 = document.getElementById(orderBtns[elem][3]);
                section1.style.display = "block";
                section2.style.display = "none";
            }else if(orderBtns[elem][0] === 'difficulty-order' && dir === asc){
                const section1 = document.getElementById(orderBtns[elem][2]);
                const section2 = document.getElementById(orderBtns[elem][3]);
                section1.style.display = "none";
                section2.style.display = "block";
            } else {
                const section1 = document.getElementById(orderBtns[elem][2]);
                const section2 = document.getElementById(orderBtns[elem][3]);
                section1.style.display = "none";
                section2.style.display = "block";
            }
        }
    }

    let prevButton = document.getElementById("prevPage");
    prevButton.addEventListener('click',
        function(event) {
            event.preventDefault();
            let url = window.location.href;
            let pageNum = Number(urlParams.get('pageNum')) -1;
            if(pageNum >= 0){
                url = addOrUpdateParam(url,"pageNum",pageNum.toString());
            } else {
                url = addOrUpdateParam(url,"pageNum","0");
            }
            window.location.href = url;
        });
    let nextButton = document.getElementById("nextPage");
    nextButton.addEventListener('click',
        function(event) {
            event.preventDefault();
            let url = window.location.href;
            let pageNum = Number(urlParams.get('pageNum')) +1;
            if(pageNum >= 0){
                url = addOrUpdateParam(url,"pageNum",pageNum.toString());
            } else {
                url = addOrUpdateParam(url,"pageNum","0");
            }
            window.location.href = url;
        });

    let elements = document.getElementsByClassName("pageNumButton");
    for (let i = 0, len = elements.length; i < len; i++) {
         elements[i].addEventListener('click',
             function(event) {
                 event.preventDefault();
                 let url = window.location.href;
                 let pageNum = Number(elements[i].value);
                 url = addOrUpdateParam(url,"pageNum",pageNum.toString());
                 window.location.href = url;
             });
    }
    let activePaginationButtons = document.getElementsByClassName("active-pag-buttons");
    for (let i = 0, len = activePaginationButtons.length; i < len; i++) {
        activePaginationButtons[i].style.display = 'block';
    }
    let hiddenPaginationButtons = document.getElementsByClassName("hidden-pag-buttons");
    for (let i = 0, len = hiddenPaginationButtons.length; i < len; i++) {
        hiddenPaginationButtons[i].style.display = 'none';
    }
</script>
</body>
</html>

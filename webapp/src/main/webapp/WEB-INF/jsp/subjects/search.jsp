<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <title>${query} - Uni</title>
  <jsp:include page="../components/head_shared.jsp"/>
  <style>
      .search-area {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));

          padding: 0 1rem 2rem 1rem;
          gap: 1rem;
      }
      .not-found-area{
        display: flex;
        flex-direction: row;
        justify-content: center;
        width: 100%;
        padding-top: 5rem;
      }

      .filter-area {
          width: 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
          padding-top: 1rem;
      }
      h3{
        font-weight: 500;
        font-size: 25px;
      }

      .filter {
          width: 100%;
      }

      .filter-section {
          display: none;
          justify-content: center;
      }

      .filter-option {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 20%;
          padding-left: 1rem;
          padding-right: 1rem;
      }

      .remove-filter {
          padding-top: .5rem;
      }

      .filter-button {
          --sl-input-border-width: 0px;
      }

      sl-button.filter-button::part(base):hover {
          background-color: #efefef;
      }
      sl-button.filter-button::part(base) {
         background-color: #efefef;
       }
      .vert-divider {
          height: 11.5rem;
      }
  </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>

<main class="container-70">
  <div class="filter-area">
    <div class="filter">

      <sl-button size="small" variant="default" id="toggle-filters">
        <sl-icon slot="prefix" name="filter"></sl-icon>
        <spring:message code="search.filter"/>
      </sl-button>


      <section class="filter-section" id="filter-section">
        <div class="filter-option">
          <h5><spring:message code="search.dpt"/></h5>

          <sl-button-group>
            <sl-button class="filter-button" size="small" variant="default" id="ambiente-credit-filter" pill>
              <spring:message code="search.dpt.ambiente"/>
            </sl-button>
            <section id="remove-ambiente-param-section">
              <sl-button class="filter-button" id="remove-ambiente-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>

          <sl-button-group>
            <sl-button class="filter-button" size="small" variant="default" id="ciencias-credit-filter" pill>
              <spring:message code="search.dpt.ciencias"/>
            </sl-button>
            <section id="remove-ciencias-param-section">
              <sl-button class="filter-button" id="remove-ciencias-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>

          <sl-button-group>
          <sl-button class="filter-button" size="small" variant="default" id="economia-credit-filter" pill>
              <spring:message code="search.dpt.economia"/>
            </sl-button>
            <section id="remove-economia-param-section">
              <sl-button class="filter-button" id="remove-economia-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>

          <sl-button-group>
            <sl-button class="filter-button" size="small" variant="default" id="sistemas-credit-filter" pill>
              <spring:message code="search.dpt.sistemas"/>
            </sl-button>
            <section id="remove-sistemas-param-section">
              <sl-button class="filter-button" id="remove-sistemas-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>

        </div>

        <sl-divider class="vert-divider" style="--color: #cbcbcb;" vertical></sl-divider>

        <div class="filter-option">
          <h5><spring:message code="search.credits"/></h5>

          <sl-button-group>
            <sl-button class="filter-button" size="small" variant="default" id="min-credit-filter" pill>
              <spring:message code="search.credits.min"/>
            </sl-button>
            <section id="remove-min-credits-param-section">
              <sl-button class="filter-button" id="remove-min-credits-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>
          <sl-button-group>
            <sl-button class="filter-button" size="small" variant="default" id="med-credit-filter" pill>
                <spring:message code="search.credits.med"/>
            </sl-button>
            <section id="remove-med-credits-param-section">
              <sl-button class="filter-button" id="remove-med-credits-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>


          <sl-button-group>
            <sl-button class="filter-button" size="small" variant="default" id="max-credit-filter" pill>
              <spring:message code="search.credits.max"/>
            </sl-button>
            <section id="remove-max-credits-param-section">
              <sl-button class="filter-button" id="remove-max-credits-filter" variant="default" size="small" pill>
                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
              </sl-button>
            </section>
          </sl-button-group>
        </div>

        <sl-divider class="vert-divider" style="--color: #cbcbcb;" vertical></sl-divider>

        <div class="filter-option">
          <h5><spring:message code="search.sort"/></h5>
          <sl-button class="filter-button" size="small" variant="default" id="order-by-name">
            <spring:message code="search.sort.alphabetically"/>
          </sl-button>
          <sl-button class="filter-button" size="small" variant="default" id="order-by-credits">
            <spring:message code="search.sort.credits"/>
          </sl-button>
          <sl-button class="filter-button" size="small" variant="default" id="order-by-id">
            <spring:message code="search.sort.id"/>
          </sl-button>
        </div>
      </section>


      <sl-divider style="--color: #cbcbcb;"></sl-divider>
    </div>
  </div>

  <c:choose>
  <c:when test="${subjects.isEmpty()}">
    <div class="not-found-area">
      <h3>
        <spring:message code="search.not-found" arguments="${query}"/>
      </h3>

    </div>
  </c:when>
  <c:otherwise>
    <div class="search-area">
    <c:forEach var="subject" items="${subjects}">
      <c:set var="subject" value="${subject}" scope="request"/>
      <c:set var="reviewCount" value="${reviewStats[subject.id].reviewCount}" scope="request"/>
      <c:set var="difficulty" value="${reviewStats[subject.id].difficulty}" scope="request"/>
      <c:set var="time" value="${reviewStats[subject.id].timeDifficulty}" scope="request"/>
      <c:import url="../components/subject_card.jsp"/>
    </c:forEach>
    </div>
  </c:otherwise>
  </c:choose>




</main>

<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>

<script src="${pageContext.request.contextPath}/js/search-view.js" defer></script>
</body>
</html>

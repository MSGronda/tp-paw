<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <title>${query}
    <c:if test="${query.length() > 0}"> - </c:if>
    Uni</title>
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

      .filter-action {
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

          <c:forEach var="dptName" items="${relevantFilters.get(\"department\")}">
            <sl-button-group>
              <sl-button id="${dptName.hashCode()}" class="filter-button" size="small" variant="default" pill>
                  <c:out value="${dptName}"/>
              </sl-button>
              <section id="remove-section-${dptName.hashCode()}">
                <sl-button id="remove-${dptName.hashCode()}" class="filter-button" variant="default" size="small" pill>
                  <sl-icon class="filter-action" name="x-lg" label="Remove"></sl-icon>
                </sl-button>
              </section>
            </sl-button-group>
          </c:forEach>

        </div>

        <sl-divider class="vert-divider" style="--color: #cbcbcb;" vertical></sl-divider>

        <div class="filter-option">
          <h5><spring:message code="search.credits"/></h5>

          <c:forEach var="creditsName" items="${relevantFilters.get(\"credits\")}">
            <sl-button-group>
              <sl-button id="credits-${creditsName}" class="filter-button" size="small" variant="default" pill>
                <spring:message code="search.credits.number" arguments="${creditsName}"/>
              </sl-button>
              <section id="remove-section-${creditsName}">
                <sl-button id="remove-${creditsName}" class="filter-button" variant="default" size="small" pill>
                  <sl-icon class="filter-action" name="x-lg" label="Remove"></sl-icon>
                </sl-button>
              </section>
            </sl-button-group>
          </c:forEach>
        </div>

        <sl-divider class="vert-divider" style="--color: #cbcbcb;" vertical></sl-divider>

        <div class="filter-option">
          <h5><spring:message code="search.sort"/></h5>
          <sl-button-group>
              <sl-button class="filter-button" size="small" variant="default" id="order-by-name">
                <spring:message code="search.sort.alphabetically"/>
              </sl-button>

              <sl-button class="filter-button" id="direction-subname-up" variant="default" size="small" pill>
                <sl-icon class="filter-action" name="arrow-up" label="Direction"></sl-icon>
              </sl-button>

            <sl-button class="filter-button" id="direction-subname-down" variant="default" size="small" pill>
              <sl-icon class="filter-action" name="arrow-down" label="Direction"></sl-icon>
            </sl-button>

          </sl-button-group>

          <sl-button-group>
                <sl-button class="filter-button" size="small" variant="default" id="order-by-credits">
                  <spring:message code="search.sort.credits"/>
                </sl-button>

                <sl-button class="filter-button" id="direction-credits-up" variant="default" size="small" pill>
                  <sl-icon class="filter-action" name="arrow-up" label="Direction"></sl-icon>
                </sl-button>
            <sl-button class="filter-button" id="direction-credits-down" variant="default" size="small" pill>
              <sl-icon class="filter-action" name="arrow-down" label="Direction"></sl-icon>
            </sl-button>
          </sl-button-group>

          <sl-button-group>
                <sl-button class="filter-button" size="small" variant="default" id="order-by-id">
                  <spring:message code="search.sort.id"/>
                </sl-button>

                <sl-button class="filter-button" id="direction-id-up" variant="default" size="small" pill>
                  <sl-icon class="filter-action" name="arrow-up" label="Direction"></sl-icon>
                </sl-button>
              <sl-button class="filter-button" id="direction-id-down" variant="default" size="small" pill>
                <sl-icon class="filter-action" name="arrow-down" label="Direction"></sl-icon>
              </sl-button>
          </sl-button-group>
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
      <c:set var="progress" value="${subjectProgress.getOrDefault(subject.id, 0)}" scope="request"/>
      <c:import url="../components/subject_card.jsp"/>
    </c:forEach>
    </div>
  </c:otherwise>
  </c:choose>




</main>

<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="${pageContext.request.contextPath}/js/url-param-utils.js"></script>
<script>
  let params = new URLSearchParams(window.location.search);

  const filterSection = document.getElementById('filter-section');
  const keys = params.keys();
  if(!keys.next().done && keys.next().done) // no tiene parametros aparte de q
    filterSection.style.display = "none"
  else
    filterSection.style.display = "flex"


  // Department filters
  let dpt = params.get("department")

  const dptFilterBtns = [
    <c:forEach var="dpt" items="${relevantFilters[\"department\"]}">
    [ "${dpt.hashCode()}",  "${dpt}" , "remove-${dpt.hashCode()}", "remove-section-${dpt.hashCode()}"],
    </c:forEach>
  ]

  for(let elem in dptFilterBtns){
    // Comportamiento de boton de aplicar filtro
    document.getElementById(dptFilterBtns[elem][0]).addEventListener('click',
            function() { window.location.href = addOrUpdateParam(window.location.href,"department",dptFilterBtns[elem][1])});

    // Comportamiento de boton de eliminar filtro
    document.getElementById(dptFilterBtns[elem][2]).addEventListener('click',
            function() { window.location.href = removeURLParam(window.location.href,"department")});

    // Visibiliad de boton de eliminar filtro
    const section = document.getElementById(dptFilterBtns[elem][3])
    console.log(dptFilterBtns[elem][3])
    if(dpt === dptFilterBtns[elem][1])
      section.style.display = "block";
    else
      section.style.display = "none"
  }



  // Credit filters
  let credits = params.get("credits")
  console.log(credits)

  const creditFilterBtns = [
    <c:forEach var="credit" items="${relevantFilters[\"credits\"]}">
    ["credits-${credit}", "${credit}", "remove-${credit}", "remove-section-${credit}"],
    </c:forEach>
  ]
  for(let elem in creditFilterBtns)
  {
    document.getElementById(creditFilterBtns[elem][0]).addEventListener('click',
            function() { window.location.href = addOrUpdateParam(window.location.href,"credits",creditFilterBtns[elem][1]);});
    document.getElementById(creditFilterBtns[elem][2]).addEventListener('click',
            function() { window.location.href = removeURLParam(window.location.href,"credits")});
    const section = document.getElementById(creditFilterBtns[elem][3])
    if(credits === creditFilterBtns[elem][1])
      section.style.display = "block";
    else
      section.style.display = "none"
  }



  // Filter section
  const toggleBtn = document.getElementById('toggle-filters');

  toggleBtn.addEventListener('click', function() {
    if(filterSection.style.display === 'none')
      filterSection.style.display = 'flex';
    else
      filterSection.style.display = 'none';
  });


  const ob = params.get("ob")
  const dir = params.get("dir")

  // Order by btns
  const addOrderByBtns = [
    ['order-by-name', "subname", "direction-subname-up","direction-subname-down"],
    ['order-by-credits', "credits", "direction-credits-up","direction-credits-down"],
    ['order-by-id', "id", "direction-id-up", "direction-id-down"]
  ]
  for(let elem in addOrderByBtns)
  {
    document.getElementById(addOrderByBtns[elem][0]).addEventListener('click',
            function() {
              window.location.href = addOrUpdateParam(addOrUpdateParam(window.location.href,"ob",addOrderByBtns[elem][1]), "dir", "asc");
            });
    const sectionUp = document.getElementById(addOrderByBtns[elem][2])
    const sectionDown = document.getElementById(addOrderByBtns[elem][3])

    sectionUp.addEventListener('click', function() { window.location.href = addOrUpdateParam(window.location.href,"dir", "desc");})
    sectionDown.addEventListener('click', function() { window.location.href = addOrUpdateParam(window.location.href,"dir", "asc");})

    if(ob === addOrderByBtns[elem][1]){
      if(dir === "asc"){
        sectionUp.style.display = 'block';
        sectionDown.style.display = 'none'
      }
      else{
        sectionUp.style.display = 'none';
        sectionDown.style.display = 'block';
      }
    }
    else{
      sectionUp.style.display = 'none';
      sectionDown.style.display = 'none';
    }
  }
</script>
</body>
</html>

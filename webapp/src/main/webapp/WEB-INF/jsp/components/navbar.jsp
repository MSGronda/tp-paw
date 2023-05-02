<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<style>
    .nav-bar {
        background-color: white;
        width: 100%;
        height: 55px;
        display: flex;
        justify-content: center;
        align-items: center
    }

    .nav-bar-area {
        width: 85%;
        display: flex;
        justify-content: space-around;
        align-items: center
    }

    .search-bar {
        width: 100% !important;
    }

    .form-area {
        padding: 0;
        margin: 0;
        width: 40%
    }

    .dropdown::part(listbox){
      color: #0369a1;
    }
</style>

<div class="nav-bar">
  <div class="nav-bar-area">

    <a href="${pageContext.request.contextPath}/">
      <img height="50px" src="${pageContext.request.contextPath}/img/uni.jpg" alt="UNI"/>
    </a>

    <form id="input-form" class="form-area">
      <spring:message code="navbar.search.placeholder" var="SearchPlaceholder"/>
      <sl-input id="input-text" class="search-bar" placeholder="${SearchPlaceholder}">
        <sl-icon name="search" slot="prefix"></sl-icon>
      </sl-input>
    </form>


    <sl-button-group label="Alignment">
      <sl-select class="dropdown" value="1">
        <c:forEach var="degree" items="${degrees}">
          <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
        </c:forEach>
        <sl-divider></sl-divider>
        <sl-option disabled>Ingeniería Industrial</sl-option>
        <sl-option disabled>Ingeniería Mecánica</sl-option>
        <sl-option disabled>Ingeniería Naval</sl-option>
        <sl-option disabled>Ingeniería Electrónica</sl-option>
        <sl-option disabled>Ingeniería Química</sl-option>
        <sl-option disabled>Ingeniería Petróleo</sl-option>
        <sl-option disabled>Bioingeniería</sl-option>
      </sl-select>
      <sl-button variant="text"><spring:message code="navbar.subject"/></sl-button>
      <sl-button variant="text"><spring:message code="navbar.builder"/></sl-button>
    </sl-button-group>
    <sec:authorize access="!isAuthenticated()">
      <sl-button variant="success" href="<c:url value="/login"/>"><spring:message code="navbar.access"/></sl-button>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
      <sl-button variant="primary" href="<c:url value="/profile"/>"><spring:message code="navbar.profile"/></sl-button>
    </sec:authorize>
  </div>

</div>

<script defer>
    const form = document.querySelector('#input-form');
    const input = document.querySelector('#input-text');

    form.addEventListener('submit', function (event){
        event.preventDefault();
        const searchQuery = input.value.trim();
        if (searchQuery.length > 0) {
            window.location.href = `${pageContext.request.contextPath}/search/\${encodeURIComponent(searchQuery)}`;
        }
    });
</script>

<script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.4.0/dist/components/select/select.js"></script>


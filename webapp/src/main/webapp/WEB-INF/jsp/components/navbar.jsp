<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<style>
    .nav-bar {
        background-color: white;
        width: 100%;
        min-height: 3.5rem;
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
    .nav-button::part(base){
      color: #3d3d3d;
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
      <sl-button class="nav-button" variant="text" href="${pageContext.request.contextPath}/degree/${user.degree.id}"><spring:message code="navbar.curriculum"/></sl-button>
      <sl-button class="nav-button" variant="text" href="${pageContext.request.contextPath}/builder"><spring:message code="navbar.builder"/></sl-button>
      <sl-button class="nav-button" variant="text" href="${pageContext.request.contextPath}/search"><spring:message code="navbar.subject"/></sl-button>
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
            window.location.href = `${pageContext.request.contextPath}/search?q=\${encodeURIComponent(searchQuery)}`;
        }
    });
</script>

<script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.4.0/dist/components/select/select.js"></script>


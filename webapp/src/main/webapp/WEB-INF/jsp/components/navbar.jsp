<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style>
    .nav-bar {background-color: white; width: 100%; height: 55px; display: flex; justify-content: center; align-items:center}
    .nav-bar-area {width: 85%;  display: flex; justify-content: space-around; align-items:center}
    .search-bar {width: 100% !important;}
    .form-area  {padding: 0; margin: 0; width: 40%}
</style>


<div class="nav-bar">
    <div class="nav-bar-area">

        <img height="50px" src="${pageContext.request.contextPath}/img/uni.jpg" alt="UNI"/>

        <form id="input-form" class="form-area">
            <spring:message code="navbar.search.placeholder" var="SearchPlaceholder"/>
            <sl-input id="input-text" class="search-bar" placeholder="${SearchPlaceholder}">
                <sl-icon name="search" slot="prefix"></sl-icon>
            </sl-input>
        </form>


        <sl-button-group label="Alignment">
            <sl-button variant="text"><spring:message code="navbar.degree" /></sl-button>
            <sl-button variant="text"><spring:message code="navbar.subject" /></sl-button>
            <sl-button variant="text"><spring:message code="navbar.builder" /></sl-button>
        </sl-button-group>
        <sl-button variant="primary"><spring:message code="navbar.login" /></sl-button>
    </div>

</div>

<script src="${pageContext.request.contextPath}/js/search.js" defer></script>

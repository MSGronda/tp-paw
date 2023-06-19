<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title><spring:message code="home.welcome"/></title>
    <jsp:include page="components/head_shared.jsp"/>
    <style>
        sl-card.landing{
            --border-radius: 3%
        }
        .landing{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100%;
            width: 35%;
            text-align: center;
        }

        .buttons{
            display: flex;
            flex-direction: row;
            justify-content: space-evenly;
            width: 50%;
        }

        .header{
            /*padding-bottom: 0.rem;;*/
        }
        .background{
            background: linear-gradient(to bottom,#4a90e2, #0369a1);
            /*background-color: #0369a1;*/
            height: 100%;
        }
        .fullHeight{
            width: 100%;
            height: 100%;
        }
        .landing-row{
            height: 100%;
            width: 100%;
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: space-evenly;
        }
        .get-started{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        .info{
            color: white;
        }
    </style>
</head>
<body>
<jsp:include page="components/default_navbar.jsp"/>
<div class="background ">
    <main class="container-50 fullHeight">
        <div class="landing-row">
            <sl-card class="landing">
                <h1 class="header">
                    <spring:message code="home.landing"/>
                </h1>
                <div class="get-started">
                    <h3 class="subtitle"><spring:message code="home.get-stared"/></h3>
                    <div class="buttons">
                        <sl-button variant="primary" href="<c:url value="/login"/>"><spring:message code="home.login"/></sl-button>
                        <sl-button variant="primary" outline  href="<c:url value="/register"/>"><spring:message code="home.register"/></sl-button>
                    </div>
                </div>
            </sl-card>
            <div class="info">
                <h3><spring:message code="landing.feat1"/></h3>
                <h3><spring:message code="landing.feat2"/></h3>
                <h3><spring:message code="landing.feat3"/></h3>
            </div>
        </div>


    </main>
</div>

<jsp:include page="components/body_scripts.jsp"/>
</body>
</html>

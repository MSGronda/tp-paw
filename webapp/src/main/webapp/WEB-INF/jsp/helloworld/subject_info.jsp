<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>Title</title>

    <jsp:include page="../components/head_shared.jsp"/>
    <style>
        .general-area {background-color: #efefef}
    </style>
</head>
<body>

<jsp:include page="../components/navbar.jsp"/>

<div>
    <div class="bread_tag">
        <sl-breadcrumb>
            <sl-breadcrumb-item>
                <sl-icon slot="prefix" name="house"></sl-icon>
                <c:out value="${degree.name}" />
            </sl-breadcrumb-item>
            <sl-breadcrumb-item>
                First Semester
            </sl-breadcrumb-item>
        </sl-breadcrumb>
    </div>
    <div class="title"  >
        <h1>
            <c:out value="${subject.name}"/>
        </h1>
    </div>
    <div class="info">
        <sl-card class="card-basic">
            Department: <c:out value="${subject.department}" />
            <br>
            Credits: <c:out value="${subject.credits}" />
            <br>
            Prerequisites:
                <c:forEach var="prerec" items="${subject.prerequisites}">
                    <c:out value="${prerec}" />
                </c:forEach>
            <br>
            Professors:
                <c:forEach var="proffesor" items="${professors}">
                    <c:out value="${proffesor}" />
                </c:forEach>
        </sl-card>
    </div>
    <div>
        <sl-button href="" variant="primary" size="large" pill class="review_bt">Review Subject</sl-button>
    </div>
    <br/>
    <hr/>
    <div class="filter">
        <sl-tooltip content="Open viewing sorts" placement="bottom">
            <sl-button variant="text" size="large"><sl-icon slot="suffix" name="sort-down"></sl-icon> Sort</sl-button>
        </sl-tooltip>
    </div>

    <div class="probando">
        <c:forEach var="review" items="${reviews}">
            <sl-card class="card-header">
                <div slot="header">
                    <c:out value="${review.title}"/>
                </div>

                <div class="break-text">
                    <c:out value="${review.text}"/>
                </div>
            </sl-card>
        </c:forEach>
    </div>
</div>



<%-- SCRIPT FOR SHOELACE --%>
<script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/shoelace-autoloader.js"></script>
</body>
</html>

<style>
    .card-basic {
        max-width: 70%;
    }
    .info {
        margin-left: 10%;
        margin-bottom: 2%;
    }
    .review_bt {
        width: 20%;
        margin-left: 40%;
    }
    .bread_tag {
        margin-top:2%;
        margin-left: 10%;
    }
    .title {
        margin-left: 10%;
        margin-top: -2%;
    }
    .filter {
        display: flex;
        flex-direction: row-reverse;
        margin-right: 20%;
    }
    h1 {
        font-size: 34px;
        font-weight: bold;
    }

    .probando{
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        align-items: center;
        width: 100%;

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
    }
</style>

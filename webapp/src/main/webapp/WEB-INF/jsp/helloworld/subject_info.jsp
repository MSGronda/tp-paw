
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>

    <title>Title</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/materialize.min.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="${pageContext.request.contextPath}/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="${pageContext.request.contextPath}/css/main.css" type="text/css" rel="stylesheet" media="screen,projection"/>

<%-- INCLUDE FOR SHOELACE --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/themes/light.css" />

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
                Home
            </sl-breadcrumb-item>
            <sl-breadcrumb-item>Clothing</sl-breadcrumb-item>
            <sl-breadcrumb-item>Shirts</sl-breadcrumb-item>
        </sl-breadcrumb>
    </div>
    <div class="title"  >
        <h1>Algebra 72.33</h1>
    </div>
    <div class="info">
        <sl-card class="card-basic">
            This is just a basic card. No image, no header, and no footer. Just your content.
        </sl-card>
    </div>
    <div>
        <sl-button href="" variant="primary" size="large" pill class="review_bt">Review Subject</sl-button>
    </div>
    <hr></hr>
    <div class="filter">
        <sl-tooltip content="Open viewing sorts" placement="bottom">
            <sl-button variant="text" size="large"><sl-icon slot="suffix" name="sort-down"></sl-icon> Sort</sl-button>
        </sl-tooltip>
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
</style>

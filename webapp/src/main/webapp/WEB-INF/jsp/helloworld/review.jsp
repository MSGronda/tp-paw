<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subject Review</title>
    <!-- CSS  -->
    <link href="${pageContext.request.contextPath}/css/materialize.min.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="${pageContext.request.contextPath}/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>
<body class="general-area">
<jsp:include page="../components/navbar.jsp"/>
<div class="container">
    <div>
        <h4>Make a review about ...</h4>
    </div>
    <form class="col s12">
        <div class="tags-removable">
            <sl-input type="email" placeholder="Email"></sl-input>
            <br />
            <sl-textarea label="Write your review"></sl-textarea>
            <br />
            <sl-tag size="medium" variant="danger" removable>Hard</sl-tag>
            <sl-tag size="medium" variant="primary" removable>Light</sl-tag>
        </div>
    </form>
</div>
<%-- INCLUDE FOR SHOELACE --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/themes/light.css" />
<script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/shoelace-autoloader.js"></script>

<script>
    const div = document.querySelector('.tags-removable');

    div.addEventListener('sl-remove', event => {
        const tag = event.target;
        tag.style.opacity = '0';
        setTimeout(() => (tag.style.opacity = '1'), 2000);
    });
</script>
<style>
    .tags-removable sl-tag {
        transition: var(--sl-transition-medium) opacity;
    }
    .general-area {
        background-color: #efefef
    }
</style>
</body>
</html>

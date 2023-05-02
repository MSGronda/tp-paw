<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>

<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico">

<%-- INCLUDE FOR SHOELACE --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.4.0/dist/themes/light.css" />

<!-- CSS  -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/main.css" type="text/css" rel="stylesheet" media="screen,projection"/>

<style>
    * {font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol'}
    .pusher {
        padding-bottom: 6rem;
        padding-top: 3rem;
    }
</style>

<script async type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.4.0/dist/shoelace.js"></script>

<script>
    window.addEventListener('load', () => {
        document.body.classList.add('ready');
    })
</script>

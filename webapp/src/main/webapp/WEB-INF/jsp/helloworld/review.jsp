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
<%--            <sl-tag size="medium" variant="danger" removable>Hard</sl-tag>--%>
<%--            <sl-tag size="medium" variant="success" removable>Easy</sl-tag>--%>
<%--            <sl-tag size="medium" variant="warning" removable>Heavy</sl-tag>--%>
<%--            <sl-tag size="medium" variant="primary" removable>Light</sl-tag>--%>
            <form class="custom-validity">
                <sl-radio-group label="Select an option" name="a" value="1">
                    <sl-radio-button value="1">Hard</sl-radio-button>
                    <sl-radio-button value="2">Easy</sl-radio-button>
                    <sl-radio-button value="3">Heavy</sl-radio-button>
                    <sl-radio-button value="3">Light</sl-radio-button>
                </sl-radio-group>
                <br />
                <sl-button type="submit" variant="primary">Submit</sl-button>
            </form>
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
        if(tag.style.opacity === '1'){
            tag.style.opacity = '0.3';
        } else {
            tag.style.opacity = '1';
        }
    });
    const form = document.querySelector('.custom-validity');
    const radioGroup = form.querySelector('sl-radio-group');
    const errorMessage = 'You must choose the last option';

    // Set initial validity as soon as the element is defined
    customElements.whenDefined('sl-radio').then(() => {
        radioGroup.setCustomValidity(errorMessage);
    });

    // Update validity when a selection is made
    form.addEventListener('sl-change', () => {
        const isValid = radioGroup.value === '3';
        radioGroup.setCustomValidity(isValid ? '' : errorMessage);
    });

    // Handle form submit
    form.addEventListener('submit', event => {
        event.preventDefault();
        alert('All fields are valid!');
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

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><c:out value="${subject.name}"/> - <spring:message code="review.many.title"/></title>
    <jsp:include page="../components/head_shared.jsp"/>
    <style>
        .row{
            display: flex;
        }
        .progress-row{
            display: flex;
            flex-direction: row;
            align-items: center;
            justify-content: center;
            width: 100%;
            padding-top: 1rem;
        }
        .progress-bar-wrapper{
            width: 80%;
        }
        li{
            color: transparent !important;
        }
    </style>
    <link href="${pageContext.request.contextPath}/css/progress-step-bar.css" rel="stylesheet" />
</head>
<body>
<main class="container-50">
    <div class="progress-row">
        <div class="progress-bar-wrapper"></div>
    </div>
    <div class="row">
        <h1><spring:message code="review.header" arguments="${subject.name}" argumentSeparator="\0"/></h1>
    </div>
    <c:url var="CreateReview" value="/many-reviews?${requestScope['javax.servlet.forward.query_string']}"/>
    <form:form modelAttribute="ReviewForm" class="col s12" method="post" action="${CreateReview}">
        <div class="tags-removable">
            <form:errors path="text" cssClass="error" element="p"/>
            <spring:message code="reviewForm.text.label" var="TextLabel"/>
            <sl-textarea name="text" label="${TextLabel}" value="${ReviewForm.text}"></sl-textarea>
            <br/>

            <form:errors path="difficulty" cssClass="error" element="p"/>
            <spring:message code="form.label" var="ButtonLabel"/>
            <spring:message code="form.easy.help" var="EasyHelp"/>
            <sl-radio-group label="${ButtonLabel}" value="${ReviewForm.difficulty}" name="difficulty" help-text="${EasyHelp}">
                <sl-radio-button value="0"><spring:message code="form.easy"/></sl-radio-button>
                <sl-radio-button value="1"><spring:message code="form.normal"/></sl-radio-button>
                <sl-radio-button value="2"><spring:message code="form.hard"/></sl-radio-button>
            </sl-radio-group>
            <br />

            <form:errors path="timeDemanding" cssClass="error" element="p"/>
            <spring:message code="form.timeDemanding.help" var="TimeDemandingHelp"/>
            <sl-radio-group name="timeDemanding" value="${ReviewForm.timeDemanding}" help-text="${TimeDemandingHelp}">
                <sl-radio-button value="0"><spring:message code="form.NotTimeDemanding"/></sl-radio-button>
                <sl-radio-button value="1"><spring:message code="form.averageTimeDemand"/></sl-radio-button>
                <sl-radio-button value="2"><spring:message code="form.timeDemanding"/></sl-radio-button>
            </sl-radio-group>
            <br />
            <div>
                <form:errors path="anonymous" cssClass="error" element="p"/>
                <sl-radio-group name="anonymous" value="${ReviewForm.anonymous}">
                    <sl-radio-button value="false"><spring:message code="form.public"/></sl-radio-button>
                    <sl-radio-button value="true"><spring:message code="form.anonymous"/></sl-radio-button>
                </sl-radio-group>
            </div>
            <br />
            <sl-button onclick="skip()"><spring:message code="review.many.skip"/></sl-button>
            <sl-button type="submit" variant="success" onclick="this.disabled = true"><spring:message code="form.submit"/></sl-button>
        </div>
    </form:form>
</main>
<jsp:include page="../components/body_scripts.jsp"/>
<script src="<c:url value="/js/progress-step-bar.js" />"></script>
<script src="<c:url value="/js/url-param-utils.js" />"></script>
<script>
    const items = []
    for(let i=0; i<${total}; i++){
        items.push(""+i);
    }

    ProgressBar.init(
        items
        ,
        ""+${current - 1},
        'progress-bar-wrapper'
    );

    function skip(){
        const params = new URLSearchParams(window.location.search);

        if(parseInt(params.get("current")) === parseInt(params.get("total")) - 1){
            window.location.href = '<c:url value="/"/>'
            return;
        }

        const r = params.get("r");

        const subs =  r.split(" ");
        subs.shift()
        const newSubs = subs.join(" ")

        const newCount =  "" + (parseInt(params.get("current")) + 1)

        window.location.href = addOrUpdateParam(addOrUpdateParam(window.location.href, "r", newSubs), "current", newCount)
    }
</script>
</body>
</html>

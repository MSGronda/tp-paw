<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <jsp:include page="../components/head_shared.jsp"/>
    <title>Finish Semester</title>

<style>
    .confirm-area{
        height: 100%;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .confirm-card{
        width: 60vh;
    }
    .confirm-card::part(base){
        height: 70vh;
    }
    .confirm-card::part(body){
        height: 100%;
    }

    .subject-list{
        max-height: 47vh;
        display: flex;
        flex-direction: column;
        justify-content: start;
        overflow-y: auto;
    }
    .subject-item{
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .submit-area{
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
</style>
</head>
<body>
    <main class="container-small">
        <div class="confirm-area">
            <c:url var="userSemester" value="/builder/finish"/>
            <form:form id="subject-form" modelAttribute="UserSemesterFinishForm" action="${userSemester}" method="post" >
            <sl-card id="check-card" class="confirm-card">
                <div slot="header">
                    <h3 style="margin: 0.4rem 0"><spring:message code="builder.finish.selectPassed"/></h3>
                </div>
                <div class="subject-list">
                    <input name="subjectIds" type="hidden" id="hiddenInput"/>
                    <c:forEach varStatus="status" var="subjectClass" items="${user.userSemester}">
                        <div class="subject-item">
                            <span><c:out value="${subjectClass.subject.name}"/> - <c:out value="${subjectClass.subject.id}"/> </span>
                            <sl-checkbox name="${subjectClass.subject.id}" id="subject-check-${subjectClass.subject.id}"></sl-checkbox>
                        </div>
                        <c:if test="${!status.last}"><sl-divider></sl-divider></c:if>
                    </c:forEach>
                </div>

                <div slot="footer">
                    <div class="submit-area">
                        <sl-button href="${pageContext.request.contextPath}/"><spring:message code="builder.finish.cancel"/></sl-button>
                        <sl-button variant="success" onclick="submit()" type="submit"><spring:message code="builder.finish.submit"/></sl-button>
                    </div>
                </div>
            </sl-card>
            </form:form>
        </div>
    </main>
<jsp:include page="../components/body_scripts.jsp"/>
<script>
    function submit(){
        const subjectList = []
        const checkboxes = document.querySelectorAll('[id^="subject-check-"]')

        for(let checkNum in checkboxes){
            if(checkboxes[checkNum].checked){
                subjectList.push(checkboxes[checkNum].getAttribute('name'));
            }
        }

        document.getElementById('hiddenInput').value = JSON.stringify(subjectList)
        document.getElementById('subject-form').submit()
    }
</script>
</body>
</html>
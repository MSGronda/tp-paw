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
                    <h3 style="margin: 0.4rem 0">Select the subjects you passed</h3>
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
                        <sl-button href="${pageContext.request.contextPath}/">Cancel</sl-button>
                        <sl-button variant="success" onclick="next()">Next</sl-button>
                    </div>
                </div>
            </sl-card>


            <sl-card id="review-card" class="confirm-card" style="display: none;">
                <div slot="header">
                    <h3>Please consider reviews the subjects you passed:</h3>
                </div>
                <div class="subject-list">
                    <c:forEach varStatus="status" var="subjectClass" items="${user.userSemester}">
                        <div style="display: none" id="review-${subjectClass.subject.id}" class="subject-item">
                            <sl-button href="${pageContext.request.contextPath}/review/${subjectClass.subject.id}"
                           target="_blank" variant="text" size="medium">
                                <c:out value="${subjectClass.subject.name}"/> - <c:out value="${subjectClass.subject.id}"/>
                            </sl-button>
                        </div>
                        <c:if test="${!status.last}"><sl-divider></sl-divider></c:if>
                    </c:forEach>
                </div>
                <div slot="footer">
                    <div class="submit-area">
                        <sl-button onclick="prev()">Back</sl-button>
                        <sl-button variant="success" onclick="submit()" type="submit">Submit</sl-button>
                    </div>
                </div>
            </sl-card>
            </form:form>
        </div>
    </main>
<jsp:include page="../components/body_scripts.jsp"/>
<script>
    function prev(){
        document.getElementById("check-card").style.display = 'block'
        document.getElementById("review-card").style.display = 'none'
    }
    function next(){
        const checkboxes = document.querySelectorAll('[id^="subject-check-"]')
        let counter = 0;
        for(let checkNum in checkboxes){
            if(checkboxes[checkNum].checked){
                document.getElementById("review-" + checkboxes[checkNum].getAttribute("name")).style.display = 'block';
                counter++;
            }
        }
        if(counter === 0){
            submit();
        }
        document.getElementById("check-card").style.display = 'none'
        document.getElementById("review-card").style.display = 'block'
    }

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

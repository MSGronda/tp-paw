<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="userform.header" /></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .select-button{
            margin-bottom: 1rem;
        }

        .YearItem {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            width: 100%;
            align-items: center;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
<main class="container-small pusher container-account">


        <c:url var="registerUrl" value="/register"/>
        <form:form modelAttribute="UserForm" action="${registerUrl}" method="post" >
            <div id="step1">
                <h1><spring:message code="userform.header" /></h1>
                <form:errors path="email" cssClass="error" element="p"/>
                <c:if test="${EmailAlreadyUsed == true}">
                    <p class="error"><spring:message code="register.emailAlreadyUsed"/></p>
                </c:if>
                <spring:message code="reviewForm.email.placeholder" var="EmailPlaceHolder"/>
                <sl-input name="email" path="email" placeholder="${EmailPlaceHolder}" value="${UserForm.email}"></sl-input>

                <br/>

                <form:errors path="name" cssClass="error" element="p"/>
                <spring:message code="userform.name" var="NamePlaceholder"/>
                <sl-input name="name" path="name" placeholder="${NamePlaceholder}" value="${UserForm.name}"></sl-input>

                <br/>

                <form:errors path="password" cssClass="error" element="p"/>
                <spring:message code="userform.password" var="PasswordPlaceholder"/>
                <sl-input name="password" type="password" path="password" placeholder="${PasswordPlaceholder}" value="${UserForm.password}" password-toggle></sl-input>

                <br/>

                <form:errors path="passwordConfirmation" cssClass="error" element="p"/>
                <spring:message code="userform.passwordConfirmation" var="PasswordConfirmationPlaceholder"/>
                <sl-input name="passwordConfirmation" type="password" path="passwordConfirmation" placeholder="${PasswordConfirmationPlaceholder}" value="${UserForm.passwordConfirmation}" password-toggle></sl-input>

                <br/>

    <%--            <sl-button type="submit" variant="success" onclick="this.disabled = true"><spring:message code="userform.submit"/></sl-button>--%>
                <sl-button variant="success" onclick="nextStep()"><spring:message code="register.next"/></sl-button>
            </div>
            <div id="step2" style="display: none">
                <h1><spring:message code="register.whatAreYouStudying"/></h1>

                <spring:message code="register.selectOne" var="SelectPlaceHolder"/>
                <sl-select id="select-degree" placeholder="${SelectPlaceHolder}" class="select-button">
                    <c:forEach var="degree" items="${degrees}">
                        <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
                    </c:forEach>
                </sl-select>
                <br/>
                <sl-button variant="success" onclick="previousStep()"><spring:message code="register.previous"/></sl-button>
                <sl-button variant="success" onclick="nextStep()" id="nextButton" disabled><spring:message code="register.next"/></sl-button>
            </div>
            <div id="step3" style="display: none">
                <c:forEach var="degree" items="${degrees}">
                    <h1 id="degree-${degree.id}"><c:out value="${degree.name}"/></h1>
                </c:forEach>
                <h3 style="font-weight: normal"><spring:message code="register.selectSubjects"/></h3>
                <c:forEach var="degreeSet" items="${degreeMapAndYearSubjects}">
                    <sl-tree id="tree-${degreeSet.key}">
                        <c:forEach var="yearSet" items="${degreeSet.value}">
                            <div class="YearItem">
                                <sl-tree-item>
                                    <h3 ><spring:message code="subject.year" arguments="${yearSet.key}"/></h3>
                                    <c:forEach var="subject" items="${yearSet.value}">
                                        <sl-tree-item>
                                            <sl-checkbox id="year-${yearSet.key}-subject-${subject.id}" class="year-${yearSet.key}-subject">
                                                <c:out value="${subject.name}"/>
                                            </sl-checkbox>
                                        </sl-tree-item>
                                    </c:forEach>
                                </sl-tree-item>
                                <sl-checkbox id="year-checkbox-${yearSet.key}" class="year-checkbox"></sl-checkbox>
                            </div>

                        </c:forEach>
                    </sl-tree>
                </c:forEach>
                <br/>
                <sl-button variant="success" onclick="previousStep()"><spring:message code="register.previous"/></sl-button>
                <sl-button type="submit" variant="success" onclick="this.disabled = true"><spring:message code="userform.submit"/></sl-button>
            </div>
        </form:form>
</main>
<jsp:include page="../components/footer.jsp"/>
<script>
    var degreeId;

    function nextStep() {
        var currentStepValue = getCurrentStep();
        var currentStep = document.getElementById("step" + currentStepValue);
        currentStep.style.display = "none";

        var nextStep = document.getElementById("step" + ( currentStepValue + 1));
        nextStep.style.display = "block";
    }

    function previousStep() {
        var currentStepValue = getCurrentStep();
        // Hide the current step
        var currentStep = document.getElementById("step" + currentStepValue);
        currentStep.style.display = "none";

        // Show the previous step
        var previousStep = document.getElementById("step" + (currentStepValue - 1));
        previousStep.style.display = "block";
    }

    function getCurrentStep() {
        // Determine the current step based on the displayed component
        for (var i = 1; i <= 3; i++) {
            var step = document.getElementById("step" + i);
            if (step.style.display !== "none") {
                return i;
            }
        }
    }

    document.getElementById('select-degree').addEventListener('sl-change', updateDegreeSelection);

    function updateDegreeSelection() {
        degreeId = document.getElementById('select-degree').value;
        console.log(degreeId)

        var nextButton = document.getElementById('nextButton');
        nextButton.disabled = false;
    }

    // Get all year checkboxes
    const yearCheckboxes = document.querySelectorAll('.year-checkbox');

    // Attach event listeners to each year checkbox
    yearCheckboxes.forEach((checkbox) => {
        const year = checkbox.id.split('-')[2]; // Extract the year value from the checkbox ID
        const yearSubjectCheckboxes = document.querySelectorAll('.year-' + year + '-subject');
        yearSubjectCheckboxes.forEach((subjectCheckbox) => {
            subjectCheckbox.addEventListener('sl-change', function () {
                const checked = document.getElementById(subjectCheckbox.id).checked;
                if( !checked ){
                    checkbox.checked = false;
                }else{
                    //chequear que estan todas prendidas
                    let allSelected = true;
                    const yearSubjectCheckboxes = document.querySelectorAll('.year-' + year + '-subject');
                    yearSubjectCheckboxes.forEach((subjectCheckbox2) => {
                        if ( !subjectCheckbox2.checked ){
                            allSelected = false;
                        }
                    })
                    checkbox.checked = allSelected;
                }
            });
        });



        checkbox.addEventListener('sl-change', function () {
            const year = this.id.split('-')[2]; // Extract the year value from the checkbox ID
            const checked = document.getElementById('year-checkbox-'+year).checked;

            // Get all checkboxes within the corresponding year
            const yearSubjectCheckboxes = document.querySelectorAll('.year-' + year + '-subject');

            // Set the checked property of each year subject checkbox
            yearSubjectCheckboxes.forEach((subjectCheckbox) => {
                subjectCheckbox.checked = checked;
                // console.log(subjectCheckbox.id);
                subjectCheckbox.addEventListener('sl-change', function () {
                    const checked = document.getElementById(subjectCheckbox.id).checked;
                    if( !checked ){
                        checkbox.checked = false;
                    }
                });
            });
        });
    });


</script>
<script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.4.0/dist/components/tree/tree.js"></script>

</body>
</html>

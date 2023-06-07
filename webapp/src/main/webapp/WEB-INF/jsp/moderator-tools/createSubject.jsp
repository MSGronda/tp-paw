<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><spring:message code="subject.new" /></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
        .add-button {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 2rem;
        }
        .table {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        td {
            font-size: 1.2rem;
            padding-left: 1rem;
            padding-right: 1rem;
        }
        table {
            width: 100%;
        }
        .selection {
            width: 80%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>
    <jsp:include page="../components/navbar.jsp" />

    <sl-dialog label="<spring:message code="subject.prerequisites.add"/>" class="dialog-header-actions">
        <sl-icon-button href="<c:url value="/create-professor"/>" target="_blank" rel="noopener noreferrer" slot="header-actions" name="plus-lg"></sl-icon-button>
        <input id="requirement" list="requirements" class="selection">
        <datalist id="requirements">
            <c:forEach items="${subjects}" var="subject">
                <option value="${subject.id} - ${subject.name}" id="${subject.id}"></option>
            </c:forEach>
        </datalist>
        <sl-button slot="footer" variant="primary" onclick="addRequirement()"><spring:message code="subject.prerequisites.add.short"/></sl-button>
    </sl-dialog>

    <main class="container-50">
    <form:form modelAttribute="subjectForm" class="col s12" method="post" action="${CreateSubject}">
        <div class="table">
            <table>
                <thead>
                <th colspan="2"><h1><spring:message code="subject.new"/></h1></th>
                </thead>
                <tbody>
                <tr>
                    <form:errors path="id" cssClass="error" element="p"/>
                    <td><spring:message code="subject.id"/></td>
                    <td><sl-input name="id" path="id" value="${SubjectForm.id}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.name"/></td>
                    <td><sl-input name="name" path="name" value="${SubjectForm.name}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.department"/></td>
                    <td><sl-input name="department" path="department" value="${SubjectForm.department}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.credits"/></td>
                    <td><sl-input name="credit" path="credit" value="${SubjectForm.credits}"></sl-input></td>
                </tr>
                <tr>
                    <td><spring:message code="subject.prerequisites"/></td>
                    <td>
                        <sl-button id="open-button">Agregar materias</sl-button>
                    </td>
                    <input type="hidden" name="requirementIds" id="hiddenInput"/>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="add-button">
            <sl-button type="submit" variant="success"><spring:message code="subject.create"/></sl-button>
        </div>
    </form:form>
    </main>
    <jsp:include page="../components/footer.jsp"/>
    <jsp:include page="../components/body_scripts.jsp"/>
</body>

<script>
    const dialog = document.querySelector('.dialog-header-actions');
    const openButton = document.querySelector('#open-button')
    const closeButton = dialog.querySelector('sl-button[slot="footer"]');
    //const newWindowButton = dialog.querySelector('.new-window');

    openButton.addEventListener('click', () => dialog.show());
    closeButton.addEventListener('click', () => dialog.hide());

    var requirementList = [];

    dialog.addEventListener('click', (event) => {
        event.stopPropagation();
    });

    function addRequirement() {
        const requirement = document.getElementById("requirement");
        const id = requirement.value.split(" - ")[0];
        if(!requirementList.includes(id)) {
            requirementList.push(id);
            document.getElementById('hiddenInput').value = JSON.stringify(requirementList);
            requirement.value = "";
        } else {
            requirement.value = "Error materia ya agregada";
        }

        console.log(requirementList);
    }


</script>
</html>


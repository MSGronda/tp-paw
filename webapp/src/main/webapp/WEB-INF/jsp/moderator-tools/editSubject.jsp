<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><spring:message code="subject.edit" /></title>
    <jsp:include page="../components/head_shared.jsp"/>

    <style>
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
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />
<main class="container-50">
    <form:form modelAttribute="SubjectForm" class="col s12" method="post" action="${Subject}">
        <div class="table">
            <table>
                <thead>
                <th colspan="2"><h1><spring:message code="subject.edit"/></h1></th>
                </thead>
                <tbody>
                <tr>
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
                    <td><spring:message code="subject.semester"/></td>
                    <td><sl-input name="semester" path="semester" value="${SubjectForm.semester}"></sl-input></td>
                </tr>

                </tbody>
            </table>
        </div>
    </form:form>
</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>
</html>
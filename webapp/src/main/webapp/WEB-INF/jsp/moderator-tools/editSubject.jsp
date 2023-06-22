<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title><spring:message code="subject.edit" /></title>
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
        li {
            list-style-type: none;
            font-size: 0.9rem;
        }
        .list-rm {
            display: flex;
            flex-direction: row;
            align-items: center;
            border: 0;
            padding: 0;
            margin: 0;
        }
        .error-message {
            color: red;
        }
        .professors-cell-width {
            max-width:20rem;
            overflow: hidden;
            white-space: normal;
            word-wrap: break-word;
            max-height:5rem;
        }
        .optional{
            color: grey;
            margin: 0;
        }
        .correlatives-div{
            display: flex;
            flex-direction: column;
        }
        .bottom-buttons{
            margin-left: 0.5rem;
            margin-right: 0.5rem;
        }
        p{
            margin: 0;
            font-size: medium;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp" />

<sl-dialog label="<spring:message code="subject.create.professor"/>" class="dialog-header-actions" style="--width: 40rem">
    <div class="table">
        <table>
            <tbody>
            <tr class="table-row">
                <td><spring:message code="professor.name"/></td>
                <td>
                    <sl-input autofocus id="new-prof-name"></sl-input>
                </td>
            </tr>
            <tr class="table-row">
                <td><spring:message code="professor.surname"/></td>
                <td>
                    <sl-input id="new-prof-surname"></sl-input>
                </td>
            </tr>
        </table>
    </div>
    <sl-button slot="footer" variant="success" onclick="createProfessor()"><spring:message code="subject.create"/></sl-button>
</sl-dialog>

<sl-dialog label="<spring:message code="subject.create.class.title"/>" class="dialog-header-actions2" style="--width: 45rem">
    <p slot="footer"><spring:message code="subject.create.class.hint"/></p>
    <div id="error-message">

    </div>
    <div class="table">
        <table>
            <tbody>
            <tr>
                <td><spring:message code="subject.classCode"/></td>
                <td><sl-input autofocus id="class-code"></sl-input></td>
            </tr>
            <tr>
                <td>
                    <spring:message code="subject.professors"/>
                </td>
                <td>
                    <sl-select multiple clearable id="class-professors">

                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.classDay"/></td>
                <td>
                    <sl-select id="class-day">
                        <sl-option value="<c:out value="1"/>"><spring:message code="subject.classDay1"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="subject.classDay2"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="subject.classDay3"/></sl-option>
                        <sl-option value="<c:out value="4"/>"><spring:message code="subject.classDay4"/></sl-option>
                        <sl-option value="<c:out value="5"/>"><spring:message code="subject.classDay5"/></sl-option>
                        <sl-option value="<c:out value="6"/>"><spring:message code="subject.classDay6"/></sl-option>
                        <sl-option value="<c:out value="7"/>"><spring:message code="subject.classDay7"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.time.start"/></td>
                <td><sl-input id="class-start-time" type="time"></sl-input></td>
            </tr>
            <tr>
                <td><spring:message code="subject.time.end"/></td>
                <td><sl-input id="class-end-time" type="time"></sl-input></td>
            </tr>
            <tr>
                <td><spring:message code="subject.classMode"/></td>
                <td>
                    <sl-select id="class-mode">
                        <sl-option value="<c:out value="1"/>"><spring:message code="subject.mode.inperson"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="subject.mode.virtual"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="subject.mode.lab"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="builder.building"/></td>
                <td>
                    <sl-select id="class-building">
                        <sl-option value="<c:out value="1"/>"><spring:message code="subject.building.rectorado"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="subject.building.tecnologico"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="subject.building.financiero"/></sl-option>
                        <sl-option value="<c:out value="4"/>"><spring:message code="subject.mode.virtual"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.classNumber"/></td>
                <td><sl-input id="classroom"></sl-input></td>
            </tr>
            </tbody>
        </table>
    </div>
    <sl-button slot="footer" variant="success" onclick="createClass()"><spring:message code="subject.add.short"/></sl-button>
</sl-dialog>

<sl-dialog label="<spring:message code="subject.add.degree"/>" class="dialog-header-actions3" >

    <div class="table degree-dialog">
        <table>
            <tbody>
            <tr>
                <td><spring:message code="degree"/></td>
                <td>
                    <sl-select id="select-degree" class="select-degree" hoist>
                        <c:forEach var="degree" items="${degrees}">
                            <sl-option value="${degree.id}"><c:out value="${degree.name}"/></sl-option>
                        </c:forEach>
                    </sl-select>
                </td>
            </tr>
            <tr>
                <td><spring:message code="subject.semester"/></td>
                <td>
                    <sl-select id="select-semester" class="select-semester" hoist>
                        <sl-option value="<c:out value="1"/>"><spring:message code="semester" arguments="1"/></sl-option>
                        <sl-option value="<c:out value="2"/>"><spring:message code="semester" arguments="2"/></sl-option>
                        <sl-option value="<c:out value="3"/>"><spring:message code="semester" arguments="3"/></sl-option>
                        <sl-option value="<c:out value="4"/>"><spring:message code="semester" arguments="4"/></sl-option>
                        <sl-option value="<c:out value="5"/>"><spring:message code="semester" arguments="5"/></sl-option>
                        <sl-option value="<c:out value="6"/>"><spring:message code="semester" arguments="6"/></sl-option>
                        <sl-option value="<c:out value="7"/>"><spring:message code="semester" arguments="7"/></sl-option>
                        <sl-option value="<c:out value="8"/>"><spring:message code="semester" arguments="8"/></sl-option>
                        <sl-option value="<c:out value="9"/>"><spring:message code="semester" arguments="9"/></sl-option>
                        <sl-option value="<c:out value="10"/>"><spring:message code="semester" arguments="10"/></sl-option>
                        <sl-option value="<c:out value="-1"/>"><spring:message code="elective"/></sl-option>
                    </sl-select>
                </td>
            </tr>
            </tbody>
        </table>
        <sl-button slot="footer" variant="success" onclick="addDegreeSemester()"><spring:message code="subject.add.short"/></sl-button>
    </div>
</sl-dialog>

<main class="container-50">
    <c:url value="/subject/${subject.id}/edit" var="EditSubject"/>
    <form:form modelAttribute="subjectForm" class="col s12" method="post" action="${EditSubject}">
        <div id="step1">
            <div class="table">
                <table>
                    <thead>
                    <th colspan="2"><h1><spring:message code="subject.edit"/></h1></th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <form:errors path="id" cssClass="error" element="p"/>
                            <c:if test="${subjectCodeRepeated == true}">
                                <p class="error"><spring:message code="subject.create.code-repeated"/></p>
                            </c:if>
                            <spring:message code="subject.id"/>
                        </td>
                        <td><sl-input name="id" path="id" value="${subject.id}" id="subject-id" onkeydown="return event.key !== 'Enter';"></sl-input></td>
                    </tr>
                    <tr>

                        <td>
                            <form:errors path="name" cssClass="error" element="p"/>
                            <spring:message code="subject.name"/>
                        </td>
                        <td><sl-input name="name" path="name" value="${subject.name}" id="subject-name" onkeydown="return event.key !== 'Enter';"></sl-input></td>
                    </tr>
                    <tr>

                        <td>
                            <form:errors path="department" cssClass="error" element="p"/>
                            <spring:message code="subject.department"/>
                        </td>
                        <td>
                            <sl-select id="department-select">
                                <sl-option id="department-select-1" value="<c:out value="1"/>"><c:out value="Ambiente y Movilidad"/></sl-option>
                                <sl-option id="department-select-2" value="<c:out value="2"/>"><c:out value="Ciencias Exactas y Naturales"/></sl-option>
                                <sl-option id="department-select-3" value="<c:out value="3"/>"><c:out value="Ciencias de la Vida"/></sl-option>
                                <sl-option id="department-select-4" value="<c:out value="4"/>"><c:out value="Economia y Negocios"/></sl-option>
                                <sl-option id="department-select-5" value="<c:out value="5"/>"><c:out value="Sistemas Complejos y Energía"/></sl-option>
                                <sl-option id="department-select-6" value="<c:out value="6"/>"><c:out value="Sistemas Digitales y Datos"/></sl-option>
                            </sl-select>
                            <input type="hidden" name="department" id="department-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>

                        <td>
                            <form:errors path="credits" cssClass="error" element="p"/>
                            <spring:message code="subject.credits"/>
                        </td>
                        <td><sl-input name="credits" path="credits" value="${subjectForm.credits}" type="number" id="subject-credits" onkeydown="return event.key !== 'Enter';"></sl-input></td>
                    </tr>
                    <tr>
                        <td>
                            <form:errors path="degreeIds" cssClass="error" element="p"/>
                            <form:errors path="semesters" cssClass="error" element="p"/>
                            <spring:message code="subject.degrees"/>
                        </td>
                        <td>
                            <sl-button id="open-button3"><spring:message code="subject.add.degree"/></sl-button>
                            <input name="degreeIds" type="hidden" id="degreeIds-hiddenInput"/>
                            <input name="semesters" type="hidden" id="semesters-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>
                        <td/>
                        <td>
                            <ul id="degreeSemesters"></ul>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="correlatives-div">
                                <form:errors path="requirementIds" cssClass="error" element="p"/>
                                <spring:message code="subject.prerequisites"/>
                                <h6 class="optional"><spring:message code="subject.optional"/></h6>
                            </div>
                        </td>
                        <td style="display: flex; flex-direction: row">

                            <input id="requirement" list="requirements" class="selection">
                            <datalist id="requirements">
                                <c:forEach items="${subjects}" var="subject">
                                    <option value="${subject.id} - ${subject.name}" id="${subject.id}"></option>
                                </c:forEach>
                            </datalist>
                            <sl-icon-button name="plus-lg" onclick="addRequirement()"></sl-icon-button>
                        </td>
                        <input type="hidden" name="requirementIds" id="hiddenInput"/>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <ul id="prerequisiteItems"></ul>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form:errors path="professors" cssClass="error" element="p"/>
                            <spring:message code="subject.professors"/>
                        </td>
                        <td style="display: flex; flex-direction: row">
                            <input id="professor" list="professors" class="selection">
                            <datalist id="professors">
                                <c:forEach items="${professors}" var="professor">
                                    <option value="${professor.name}" id="${professor.id}"></option>
                                </c:forEach>
                            </datalist>
                            <sl-icon-button name="plus-lg" onclick="addProfessor()"></sl-icon-button>
                            <input name="professors" type="hidden" id="professors-hiddenInput"/>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <ul id="professorItems"></ul>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <sl-button id="open-button"><spring:message code="subject.create.professor"/></sl-button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="add-button">
                <sl-button variant="success" onclick="nextStep()" id="nextStep1"><spring:message code="register.next"/></sl-button>
            </div>
        </div>
        <div id="step2" style="display: none">
            <div>
                <table>
                    <thead>
                    <th colspan="2"><h1><spring:message code="subject.new"/></h1></th>
                    </thead>
                    <tbody>
                    <tr>
                        <td><spring:message code="subject.class"/></td>
                        <td><sl-button id="open-button2"><spring:message code="subject.create.class"/></sl-button></td>
                    </tr>
                    </tbody>
                </table>
                <br/>
                <div>

                    <table>
                        <thead>
                        <tr>
                            <th>
                                <form:errors path="classCodes" cssClass="error" element="p"/>
                                <spring:message code="builder.class"/>
                            </th>
                            <th>
                                <form:errors path="classProfessors" cssClass="error" element="p"/>
                                <spring:message code="subject.add.professors"/>
                            </th>
                            <th>
                                <form:errors path="classDays" cssClass="error" element="p"/>
                                <spring:message code="subject.classDay"/>
                            </th>
                            <th>
                                <form:errors path="classStartTimes" cssClass="error" element="p"/>
                                <spring:message code="subject.time.start"/>
                            </th>
                            <th>
                                <form:errors path="classEndTimes" cssClass="error" element="p"/>
                                <spring:message code="subject.time.end"/>
                            </th>
                            <th>
                                <form:errors path="classBuildings" cssClass="error" element="p"/>
                                <spring:message code="builder.mode"/>
                            </th>
                            <th>
                                <form:errors path="classRooms" cssClass="error" element="p"/>
                                <spring:message code="builder.building"/>
                            </th>
                            <th>
                                <form:errors path="classModes" cssClass="error" element="p"/>
                                <spring:message code="subject.classroom"/>
                            </th>
                            <th> </th>
                        </tr>
                        </thead>
                        <tbody id="classItems">
                        </tbody>
                    </table>

                </div>
                <input type="hidden" name="classCodes" id="classCodes-hiddenInput"/>
                <input type="hidden" name="classProfessors" id="classProfessors-hiddenInput"/>
                <input type="hidden" name="classDays" id="classDays-hiddenInput"/>
                <input type="hidden" name="classStartTimes" id="classStartTimes-hiddenInput"/>
                <input type="hidden" name="classEndTimes" id="classEndTimes-hiddenInput"/>
                <input type="hidden" name="classBuildings" id="classBuildings-hiddenInput"/>
                <input type="hidden" name="classRooms" id="classRooms-hiddenInput"/>
                <input type="hidden" name="classModes" id="classModes-hiddenInput"/>
            </div>

            <div class="add-button">
                <sl-button variant="success" onclick="previousStep()" class="bottom-buttons"><spring:message code="register.previous"/></sl-button>
                <sl-button type="submit" variant="success" class="bottom-buttons" id="submit-button"><spring:message code="subject.create"/></sl-button>
            </div>
        </div>
        </div>
    </form:form>

</main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
</body>

<script>

    function setDepartment() {
        console.log("me llame");
        const departmentMap = {
            "<c:out value="1"/>": "<c:out value="Ambiente y Movilidad"/>",
            "<c:out value="2"/>": "<c:out value="Ciencias Exactas y Naturales"/>",
            "<c:out value="3"/>": "<c:out value="Ciencias de la Vida"/>",
            "<c:out value="4"/>": "<c:out value="Economia y Negocios"/>",
            "<c:out value="5"/>": "<c:out value="Sistemas Complejos y Energía"/>",
            "<c:out value="6"/>": "<c:out value="Sistemas Digitales y Datos"/>"
        }
        let departmentSelect = document.getElementById("department-select");
        for (let key in departmentMap) {
            if (departmentMap.hasOwnProperty(key)) {
                let value = departmentMap[key];
                if(value === "${subject.department}") {
                    console.log("entre");
                    departmentSelect.setAttribute("value", key);
                }
            }
        }
    }

    $(document).ready(function() {
        setDepartment();
    });
</script>

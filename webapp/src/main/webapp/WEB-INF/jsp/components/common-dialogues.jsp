<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<sl-dialog label="<spring:message code="subject.create.professor"/>" class="dialog-header-actions" style="--width: 40rem">
  <div class="table">

    <table>
      <tbody>
      <tr id="error-message2">
      </tr>
      <tr class="table-row">
        <td><spring:message code="professor.name"/></td>
        <td>
          <sl-input pattern="\p{L}(\p{L}|\s|')*" autofocus id="new-prof-name"></sl-input>
        </td>
      </tr>
      <tr class="table-row">
        <td><spring:message code="professor.surname"/></td>
        <td>
          <sl-input pattern="\p{L}(\p{L}|\s|')*" id="new-prof-surname"></sl-input>
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
          <sl-input id="class-mode"></sl-input>
        </td>
      </tr>
      <tr>
        <td><spring:message code="builder.building"/></td>
        <td>
          <sl-input id="class-building"></sl-input>
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

<sl-dialog label="<spring:message code="subject.edit.class.title"/>" class="dialog-header-actions4" style="--width: 45rem">
  <p slot="footer"><spring:message code="subject.create.class.hint"/></p>
  <div id="error-message4">

  </div>
  <div class="table">
    <table>
      <tbody>
      <tr>
        <td>
          <spring:message code="subject.professors"/>
        </td>
        <td>
          <sl-select multiple clearable id="class-professors4">

          </sl-select>
        </td>
      </tr>
      <tr>
        <td><spring:message code="subject.classDay"/></td>
        <td>
          <sl-select id="class-day4">
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
        <td><sl-input id="class-start-time4" type="time"></sl-input></td>
      </tr>
      <tr>
        <td><spring:message code="subject.time.end"/></td>
        <td><sl-input id="class-end-time4" type="time"></sl-input></td>
      </tr>
      <tr>
        <td><spring:message code="subject.classMode"/></td>
        <td>
          <sl-input id="class-mode4"></sl-input>
        </td>
      </tr>
      <tr>
        <td><spring:message code="builder.building"/></td>
        <td>
          <sl-input id="class-building4"></sl-input>
        </td>
      </tr>
      <tr>
        <td><spring:message code="subject.classNumber"/></td>
        <td><sl-input id="classroom4"></sl-input></td>
      </tr>
      </tbody>
    </table>
  </div>
  <sl-button slot="footer" variant="success" onclick="updateClass()"><spring:message code="subject.edit.short"/></sl-button>
</sl-dialog>



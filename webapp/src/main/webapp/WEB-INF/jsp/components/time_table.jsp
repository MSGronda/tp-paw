<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<style>
  /* Time table */
  .time-table {
    flex: 1;
    width: 100%;
    max-height: 100%;
    overflow-y: auto;
  }

  /*Table Attributes */
  table {
    margin-bottom: 0;
  }
  tbody td {
    font-size: 0.9rem;
    padding: 0.25rem !important;
    border-left: 1px solid #e9ecef;
  }

  tbody th {
    background-color: #f8f9fa;
    margin-right: 0;
    padding-right: 0;
  }

  thead th {
    background-color: #f8f9fa;
    position: sticky;
    top: 0
  }
</style>
<jsp:include page="../components/component-style/table_style.jsp"/>
<div id="time-table" class="time-table">
  <table>
    <thead>
    <tr>
      <th></th>
      <th><spring:message code="subject.classDay1"/></th>
      <th><spring:message code="subject.classDay2"/></th>
      <th><spring:message code="subject.classDay3"/></th>
      <th><spring:message code="subject.classDay4"/></th>
      <th><spring:message code="subject.classDay5"/></th>
      <th><spring:message code="subject.classDay6"/></th>
    </tr>
    </thead>
    <tbody id="weekly-schedule">
    </tbody>
  </table>
</div>
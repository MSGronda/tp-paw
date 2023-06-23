<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Params -->
<c:set var="totalPages" value="${requestScope.totalPages}"/>
<c:set var="currentPage" value="${requestScope.currentPage}"/>

<c:if test="${totalPages > 1}">
  <div class="pagination-buttons">
    <sl-radio-group name="pagination-radio" value="${currentPage}">
      <sl-radio-button id="prevPage" value="-1" <c:if test="${currentPage-1 <= 0}">disabled</c:if>>
        <sl-icon slot="prefix" name="chevron-left"></sl-icon>
        <spring:message code="subject.previousPage" />
      </sl-radio-button>
      <c:forEach var="pageNum" begin="1" end="${totalPages}">
        <c:choose>
          <c:when test="${(pageNum > currentPage -2 and pageNum < currentPage + 2) or (pageNum == 1 or pageNum == totalPages)}">
            <div class="active-pag-buttons">
              <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
            </div>
          </c:when>
          <c:when test="${pageNum < totalPages and pageNum == currentPage + 2}">
            <div class="active-pag-buttons">
              <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
              <c:if test="${totalPages > 4}">
                <sl-radio-button value="..." disabled>...</sl-radio-button>
              </c:if>
            </div>
          </c:when>
          <c:when test="${pageNum > 1 and pageNum == currentPage - 2}">
            <div class="active-pag-buttons">
              <c:if test="${totalPages > 4}">
                <sl-radio-button value="..." disabled>...</sl-radio-button>
              </c:if>
              <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
            </div>
          </c:when>
          <c:otherwise>
            <div class="hidden-pag-buttons">
              <sl-radio-button class="pageNumButton" value="${pageNum}">${pageNum}</sl-radio-button>
            </div>
          </c:otherwise>
        </c:choose>

      </c:forEach>
      <sl-radio-button id="nextPage" value="0" <c:if test="${currentPage >= totalPages}">disabled</c:if>>
        <sl-icon slot="suffix" name="chevron-right"></sl-icon>
        <spring:message code="subject.nextPage" />
      </sl-radio-button>
    </sl-radio-group>
  </div>
  <script src="<c:url value="/js/url-param-utils.js" />"></script>
  <script>
      let prevButton = document.getElementById("prevPage");
      prevButton.addEventListener('click',
          function (event) {
              event.preventDefault();
              let url = window.location.href;
              url = addOrUpdateParam(url, "pageNum", (${currentPage} - 1).toString());

              window.location.href = url;
          });
      let nextButton = document.getElementById("nextPage");
      nextButton.addEventListener('click',
          function (event) {
              event.preventDefault();
              let url = window.location.href;
              url = addOrUpdateParam(url, "pageNum", (${currentPage} + 1).toString());

              window.location.href = url;
          });

      let elements = document.getElementsByClassName("pageNumButton");
      for (let i = 0, len = elements.length; i < len; i++) {
          elements[i].addEventListener('click',
              function (event) {
                  event.preventDefault();
                  let url = window.location.href;
                  let pageNum = Number(elements[i].value);
                  url = addOrUpdateParam(url, "pageNum", pageNum.toString());
                  window.location.href = url;
              });
      }
      let activePaginationButtons = document.getElementsByClassName("active-pag-buttons");
      for (let i = 0, len = activePaginationButtons.length; i < len; i++) {
          activePaginationButtons[i].style.display = 'block';
      }
      let hiddenPaginationButtons = document.getElementsByClassName("hidden-pag-buttons");
      for (let i = 0, len = hiddenPaginationButtons.length; i < len; i++) {
          hiddenPaginationButtons[i].style.display = 'none';
      }
  </script>
</c:if>

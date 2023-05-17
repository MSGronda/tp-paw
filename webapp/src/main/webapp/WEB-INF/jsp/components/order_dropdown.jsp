<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<%--Params--%>
<c:set var="order" value="${requestScope.order}"/>
<c:set var="dir" value="${requestScope.dir}"/>

<style>
  .order-menu{
    display: flex;
    flex-direction: row;
    align-items: baseline;
  }
  #diffuclty-down{
    font-size: 0.8rem;
  }
  #diffuclty-up{
    font-size: 0.8rem;
  }
  #timedemand-down{
    font-size: 0.8rem;
  }
  #timedemand-up{
    font-size: 0.8rem;
  }
  #semester-up{
    font-size: 0.8rem;
  }
  #semester-down{
     font-size: 0.8rem;
  }
  .filter {
    width: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
</style>

<div class="filter">
  <h3><spring:message code="profile.reviews"/></h3>
  <div>
    <spring:message code="subject.actualFilter"/>
    <c:choose>
      <c:when test="${order == \"easy\" && dir == \"desc\"}">
        <spring:message code="subject.order.difficulty"/>
        <spring:message code="subject.directionDesc"/>
      </c:when>
      <c:when test="${order == \"timedemanding\" && dir == \"asc\"}">
        <spring:message code="subject.order.time"/>
        <spring:message code="subject.directionAsc"/>
      </c:when>
      <c:when test="${order == \"timedemanding\" && dir == \"desc\"}">
        <spring:message code="subject.order.time"/>
        <spring:message code="subject.directionDesc"/>
      </c:when>
      <c:when test="${order == \"semester\" && dir == \"asc\"}">
        <spring:message code="profile.order.semester"/>
        <spring:message code="subject.directionAsc"/>
      </c:when>
      <c:when test="${order == \"easy\" && dir == \"asc\"}">
        <spring:message code="subject.order.difficulty"/>
        <spring:message code="subject.directionAsc"/>
      </c:when>
      <c:otherwise>
        <spring:message code="profile.order.semester"/>
        <spring:message code="subject.directionDesc"/>
      </c:otherwise>
    </c:choose>
  </div>
  <div>
    <sl-dropdown>
      <sl-button variant="text" slot="trigger" size="large" caret>
        <spring:message code="subject.sort"/>
        <sl-icon slot="prefix" name="sort-down"></sl-icon>
      </sl-button>
      <sl-menu>
        <sl-menu-item id="difficulty-order">
          <div class="order-menu">
            <spring:message code="subject.order.difficulty"/>
            <section id="diffuclty-down">
              <sl-icon slot="suffix" name="arrow-down" ></sl-icon>
            </section>
            <section id="diffuclty-up">
              <sl-icon slot="suffix" name="arrow-up" ></sl-icon>
            </section>
          </div>
        </sl-menu-item>
        <sl-menu-item id="timedemand-order">
          <div class="order-menu">
            <spring:message code="subject.order.time"/>
            <section id="timedemand-down">
              <sl-icon slot="suffix" name="arrow-down" ></sl-icon>
            </section>
            <section id="timedemand-up">
              <sl-icon slot="suffix" name="arrow-up"></sl-icon>
            </section>
          </div>
        </sl-menu-item>
        <sl-menu-item id="semester-order">
          <div class="order-menu">
            <spring:message code="profile.order.semester"/>
            <section id="semester-down">
              <sl-icon slot="suffix" name="arrow-down" ></sl-icon>
            </section>
            <section id="semester-up">
              <sl-icon slot="suffix" name="arrow-up"></sl-icon>
            </section>
          </div>
        </sl-menu-item>
      </sl-menu>
    </sl-dropdown>
  </div>
</div>

<script>
  let dir = "${dir}";

  const orderBtns = [
    ['difficulty-order','easy','diffuclty-down','diffuclty-up'],
    ['timedemand-order','timedemanding','timedemand-down','timedemand-up'],
    ['semester-order','semester','semester-down','semester-up']
  ]
  const asc='asc'
  const desc = 'desc'
  for(let elem in orderBtns) {
    const btnElem = document.getElementById(orderBtns[elem][0])
    if(typeof btnElem !== null && elem !== 'undefined' ){
      btnElem.addEventListener('click',
              function(event) {
                event.preventDefault();
                let url = window.location.href;
                url = addOrUpdateParam(url,"order",orderBtns[elem][1]);
                url = addOrUpdateParam(url,"pageNum","0");
                if(dir !== null && dir === asc){
                  url = addOrUpdateParam(url,"dir",desc);
                } else {
                  url = addOrUpdateParam(url,"dir",asc);
                }
                window.location.href = url;
              });
      if(orderBtns[elem][0] === 'difficulty-order' && dir === desc){
        const section1 = document.getElementById(orderBtns[elem][2]);
        const section2 = document.getElementById(orderBtns[elem][3]);
        section1.style.display = "block";
        section2.style.display = "none";
      } else if(orderBtns[elem][0] === 'timedemand-order' && dir === desc) {
        const section1 = document.getElementById(orderBtns[elem][2]);
        const section2 = document.getElementById(orderBtns[elem][3]);
        section1.style.display = "block";
        section2.style.display = "none";
      }else if(orderBtns[elem][0] === 'difficulty-order' && dir === asc){
        const section1 = document.getElementById(orderBtns[elem][2]);
        const section2 = document.getElementById(orderBtns[elem][3]);
        section1.style.display = "none";
        section2.style.display = "block";
      } else if(orderBtns[elem][0] === 'timedemand-order' && dir === asc) {
        const section1 = document.getElementById(orderBtns[elem][2]);
        const section2 = document.getElementById(orderBtns[elem][3]);
        section1.style.display = "none";
        section2.style.display = "block";
      } else if(orderBtns[elem][0] === 'semester-order' && dir === asc) {
        const section1 = document.getElementById(orderBtns[elem][2]);
        const section2 = document.getElementById(orderBtns[elem][3]);
        section1.style.display = "none";
        section2.style.display = "block";
      } else {
        const section1 = document.getElementById(orderBtns[elem][2]);
        const section2 = document.getElementById(orderBtns[elem][3]);
        section1.style.display = "block";
        section2.style.display = "none";
      }
    }
  }
</script>

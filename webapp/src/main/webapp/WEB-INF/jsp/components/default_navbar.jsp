<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
  .nav-bar {
  background-color: white;
  width: 100%;
  min-height: 3.5rem;
  display: flex;
  justify-content: center;
  align-items: center
  }

  .nav-bar-area {
  width: 85%;
  display: flex;
  justify-content: left;
  align-items: center;
  padding-left: 2.5rem;
  }
</style>

<div class="nav-bar">
  <div class="nav-bar-area">

    <a href="${pageContext.request.contextPath}/">
      <img height="50px" src="${pageContext.request.contextPath}/img/uni.jpg" alt="UNI"/>
    </a>
  </div>
</div>

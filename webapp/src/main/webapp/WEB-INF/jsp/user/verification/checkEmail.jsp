<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><spring:message code="user.confirm.checkEmail.title"/></title>
  <jsp:include page="../../components/head_shared.jsp"/>
  <style>
    .card-basic {
      width: 100%;
    }
    .card-basic::part(body) {
      padding: 2rem;
    }
    .center2 {
      display: flex;
      flex-direction: column;
      width: 100%;
      justify-content: center;
      align-items: center;
    }
    h3 {
      font-size: 1.7rem;
      margin-bottom: 0;
      text-align: center;
    }
    .success {
      font-size: 5rem;
      color: #4a90e2;
    }
    h5 {
      font-weight: normal;
      font-size: 1rem;
      text-align: center;
    }

    #resendButton {
        display: inline;
    }
  </style>
  <script async>
    window.addEventListener('load', () => {
        setTimeout(() => {
            document.getElementById('resendButton').disabled = false;
        }, 10000);

        var timeleft = 9;
        var timer = setInterval(() => {
            if(timeleft <= 0) {
                document.getElementById('timer').innerHTML = "";
                clearInterval(timer);
                return;
            }

            document.getElementById('timer').innerHTML = ': ' + timeleft + 's';
            timeleft -= 1;
        }, 1000);
    });
  </script>
</head>
<body>
<jsp:include page="../../components/default_navbar.jsp"/>

<main class="container-50">
  <div class="title container-small">
    <h1><spring:message code="user.confirm.one_more_step"/></h1>
  </div>
  <div class="container-small">
    <sl-card class="card-basic">
      <div class="center2">
        <sl-icon class="success" name="envelope-exclamation"></sl-icon>
        <h3><spring:message code="user.confirm.checkEmail.title"/></h3>
        <h5><spring:message code="user.confirm.checkEmail.message"/></h5>
      </div>

      <c:url var="resendUrl" value="/verification/resend"/>
      <form method="post" action="${resendUrl}">
        <input type="hidden" name="email" value="${param.email}"/>
        <div class="center2">
          <sl-button disabled variant="primary" id="resendButton" type="submit">
            <spring:message code="user.confirm.checkEmail.resend.button"/>
            <span id="timer"></span>
          </sl-button>
        </div>
      </form>
    </sl-card>
  </div>
</main>

<jsp:include page="../../components/footer.jsp"/>
</body>
</html>


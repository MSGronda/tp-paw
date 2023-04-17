<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<style>
    .footer {background-color: white; width: 100%; ; display: flex; justify-content: center; align-items:center}
    .footer-area {width: 85%;  display: flex; flex-direction: column; justify-content: space-around; align-items:center}
    .content-row { display: flex; width: 100% }
    .content-column {display: flex; flex-direction: column; padding: 10px; width: 33%}
    .copyright-area { width: 100%; padding: 10px}
    .text-type {font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol'}
    ul {list-style-type: none; padding-left: 0; margin-left: 0}
</style>


<div class="footer">
    <div class="footer-area">


        <div class="content-row">
            <div class="content-column">
                <h4 class="text-type">UNI</h4>
                <sl-divider style="--spacing: 0rem;"></sl-divider>
                <p class="text-type"><spring:message code="footer.mission"/> </p>
            </div>

            <div class="content-column">
                <h4 class="text-type"><spring:message code="footer.explore"/></h4>
                <sl-divider style="--spacing: 0rem;"></sl-divider>
                <ul>
                    <li><sl-button variant="text" size="medium">Link 1</sl-button></li>
                    <li><sl-button variant="text" size="medium">Link 1</sl-button></li>
                </ul>
            </div>
            <div class="content-column">
                <h4 class="text-type"><spring:message code="footer.Learn"/></h4>
                <sl-divider style="--spacing: 0rem;"></sl-divider>
                <ul>
                    <li><sl-button variant="text" size="medium">Link 1</sl-button></li>
                    <li><sl-button variant="text" size="medium">Link 1</sl-button></li>
                </ul>
            </div>
        </div>
        <div class="copyright-area">
            <sl-divider style="--spacing: 0rem;"></sl-divider>
            <h6 class="text-type"><spring:message code="footer.rights"/></h6>
        </div>

    </div>

    </div>
</div>


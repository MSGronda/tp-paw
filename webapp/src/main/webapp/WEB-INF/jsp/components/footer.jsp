
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    .footer {background-color: white; width: 100%; ; display: flex; justify-content: center; align-items:center}
    .footer-area {width: 85%;  display: flex; flex-direction: column; justify-content: space-around; align-items:center}
    .content-row { display: flex; width: 100% }
    .content-column {display: flex; flex-direction: column; padding: 10px; width: 33%}
    .copyright-area { width: 100%; padding: 10px; padding-bottom: 1rem}
</style>


<div class="footer">
    <div class="footer-area">


        <div class="content-row">
            <div class="content-column">
                <h5 class="">UNI</h5>
                <sl-divider style="--spacing: 0rem;"></sl-divider>
                <p class="">Our mission is to provide a simple and intuitive way of share your opinions, tips and general knowledge for all of your college courses.</p>
            </div>

            <div class="content-column">
                <h5 class="">Explore</h5>
                <sl-divider style="--spacing: 0rem;"></sl-divider>
                <ul>
                    <li><a class="" href="#">Link 1</a></li>
                    <li><a class="" href="#">Link 2</a></li>
                </ul>
            </div>
            <div class="content-column">
                <h5 class="">Learn</h5>
                <sl-divider style="--spacing: 0rem;"></sl-divider>
                <ul>
                    <li><a class="" href="#">Link 1</a></li>
                    <li><a class="" href="#">Link 2</a></li>
                </ul>
            </div>
        </div>
        <div class="copyright-area">
            <sl-divider ></sl-divider>
            <h6> © 2023 Uni Team, All rights reserved.</h6>
        </div>

    </div>

    </div>
</div>


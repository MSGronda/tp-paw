<style>
    .nav-bar {background-color: white; width: 100%; height: 55px; display: flex; justify-content: center; align-items:center}
    .nav-bar-area {width: 85%;  display: flex; justify-content: space-around; align-items:center}
    .search-bar {width: 100% !important;}
    .form-area  {padding: 0; margin: 0; width: 40%}
</style>


<div class="nav-bar">
    <div class="nav-bar-area">

        <img height="50px" src="${pageContext.request.contextPath}/img/uni.jpg" alt="UNI"/>

        <form id="input-form" class="form-area">
            <sl-input id="input-text" class="search-bar" placeholder="Search">
                <sl-icon name="search" slot="prefix"></sl-icon>
            </sl-input>
        </form>


        <sl-button-group label="Alignment">
            <sl-button variant="text">Courses</sl-button>
            <sl-button variant="text">Subjects</sl-button>
            <sl-button variant="text">Semester Builder</sl-button>
        </sl-button-group>
        <sl-button variant="primary">Login</sl-button>
    </div>

</div>

<script src="${pageContext.request.contextPath}/js/search.js" defer></script>

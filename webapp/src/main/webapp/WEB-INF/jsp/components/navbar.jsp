<style>
    .nav-wrapper-width {width: 60%;}
    .uni_logo_font {font-weight: bold; font-size: 20px;}
    .nav-flex {display: flex; justify-content: space-between; align-items: center }
    .search-back-color {background-color: white; height: 80%}
    .search-height {height: 80% !important}
    .search-icon {color: #282727 !important; line-height: 52px  !important;}
    .profile-icon {font-size: 30px !important; display: inline !important;}
    .flex-center-center {display: flex; justify-content: center; align-items: center }
    /* Para alterar los valores de materialize, se debe usar !imporant */
</style>

<nav class="uni_back_color" role="navigation">
    <div class="nav-wrapper container nav-flex">
        <a id="logo-container" href="#" class="brand-logo uni_logo_font">UNI</a>
        <div></div>
        <div></div>

        <div class="nav-wrapper nav-wrapper-width search-back-color search-height">
            <form>
                <div class="input-field ">
                    <input id="search" type="search" required>
                    <label class="label-icon" for="search">
                        <i class="material-icons search-icon">search</i>
                    </label>
                    <i class="material-icons search-icon">close</i>
                </div>
            </form>
        </div>


        <a href="#" class="flex-center-center">
            <i class="material-icons profile-icon">person2</i>
        </a>
    </div>
</nav>
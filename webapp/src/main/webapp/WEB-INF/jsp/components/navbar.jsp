<style>
    .nav-wrapper-width {width: 60%;}
    .uni_logo_font {font-weight: bold; font-size: 20px;}
    .nav-flex {display: flex; justify-content: space-between; align-items: center }
    .search-height {height: 80% !important}
    .search-icon { line-height: 52px  !important;}
    .profile-icon { display: inline !important;}
    .flex-center-center {display: flex; justify-content: center; align-items: center }
    /* Para alterar los valores de materialize, se debe usar !imporant */
</style>

<nav class="uni_back_color" role="navigation">
    <div class="nav-wrapper container nav-flex">
        <a id="logo-container" href="#" class="brand-logo uni_logo_font">UNI</a>
        <div></div>
        <div></div>

        <div class="nav-wrapper nav-wrapper-width white search-height">
            <form>
                <div class="input-field ">
                    <input id="search" type="search" required>
                    <label class="label-icon" for="search">
                        <i class="material-icons search-icon grey-text text-darken-3">search</i>
                    </label>
                    <i class="material-icons search-icon grey-text text-darken-3">close</i>
                </div>
            </form>
        </div>


        <a href="#" class="flex-center-center">
            <i class="material-icons profile-icon">person2</i>
        </a>
    </div>
</nav>
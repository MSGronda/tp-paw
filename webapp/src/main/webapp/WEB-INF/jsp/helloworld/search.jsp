<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>${query} - Uni</title>
    <jsp:include page="../components/head_shared.jsp"/>
    <style>
        .search-area {
            min-height: 70%; width: 100%;
            display: grid;   grid-template-columns: auto auto auto; column-gap: 20px; row-gap: 20px;
            background-color: #f3f3f3; padding-top: 1rem;padding-bottom: 3rem;
        }
        .filter-area {
            width: 100%;
            display: flex; flex-direction: column; align-items: center;
            background-color: white; padding-top: 1rem;
        }
        .filter {
            width: 75%;
        }
        .filter-section {
            display: flex;     /* TODO CHANGE ME!! */
            justify-content: space-around;
        }
        .filter-option {
            display: flex; flex-direction: column; align-items: center;
            width: 20%;
        }
        .remove-filter {
            padding-top: .5rem;
        }
        .filter-button {
            --sl-input-border-width: 0px;
        }
        sl-button.filter-button::part(base):hover {
            background-color: white;
        }

        .test-card {
            max-width: 25rem;
        }
    </style>
</head>
<body>
    <jsp:include page="../components/navbar.jsp"/>

    <div class="filter-area">
        <div class="filter">

            <sl-button size="small" variant="default" id="toggle-filters">
                <sl-icon slot="prefix" name="filter"></sl-icon>
                Filter
            </sl-button>


            <section class="filter-section" id="filter-section">
                <div class="filter-option">
                    <h5>Department</h5>
                    <sl-button-group>
                        <sl-button class="filter-button" size="small" variant="default" id="sistemas-credit-filter" pill>Sistemas Digitales y Datos</sl-button>
                        <section id="remove-sistemas-param-section">
                            <sl-button class="filter-button" id="remove-sistemas-filter" variant="default" size="small" pill>
                                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
                            </sl-button>
                        </section>
                    </sl-button-group>
                </div>


                <div class="filter-option">
                    <h5>Credits</h5>

                    <sl-button-group>
                        <sl-button class="filter-button" size="small" variant="default" id="min-credit-filter" pill>1 - 3 credits</sl-button>
                        <section id="remove-min-credits-param-section">
                            <sl-button class="filter-button" id="remove-min-credits-filter" variant="default" size="small" pill>
                                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
                            </sl-button>
                        </section>
                    </sl-button-group>
                    <sl-button-group>
                        <sl-button class="filter-button" size="small" variant="default" id="med-credit-filter" pill>4 - 6 credits</sl-button>
                        <section id="remove-med-credits-param-section">
                            <sl-button class="filter-button" id="remove-med-credits-filter" variant="default" size="small" pill>
                                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
                            </sl-button>
                        </section>
                    </sl-button-group>

                    <sl-button-group>
                        <sl-button class="filter-button" size="small" variant="default" id="max-credit-filter" pill>More than 6 credits</sl-button>
                        <section id="remove-max-credits-param-section">
                            <sl-button class="filter-button" id="remove-max-credits-filter" variant="default" size="small" pill>
                                <sl-icon class="remove-filter" name="x-lg" label="Remove"></sl-icon>
                            </sl-button>
                        </section>
                    </sl-button-group>
                </div>
                <div class="filter-option">
                    <h5>Sort By</h5>
                    <sl-button class="filter-button" size="small" variant="default" id="order-by-name">A-Z</sl-button>
                    <sl-button class="filter-button" size="small" variant="default" id="order-by-credits">Credits</sl-button>
                    <sl-button class="filter-button" size="small" variant="default" id="order-by-id">ID</sl-button>
                </div>
            </section>


            <sl-divider></sl-divider>
        </div>
    </div>


    <div class="search-area">

        <c:forEach var="subject" items="${subjects}">
            <sl-card class="card-basic test-card">
                <div slot="header">
                        ${subject.name}
                </div>
                <p>Department<sl-divider vertical></sl-divider> ${subject.department}</p>
                <p>Credits <sl-divider vertical></sl-divider> ${subject.credits}</p>
            </sl-card>
        </c:forEach>
    </div>

    <jsp:include page="../components/footer.jsp"/>
    <%-- SCRIPT FOR SHOELACE --%>
    <script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.3.0/dist/shoelace-autoloader.js"></script>

    <script src="${pageContext.request.contextPath}/js/search-view.js" defer></script>
</body>
</html>

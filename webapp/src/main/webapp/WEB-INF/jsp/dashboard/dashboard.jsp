<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../components/head_shared.jsp"/>
    <title>Dashboard</title>
    <style>
        .tab{
            padding: 0.5rem;
            width: 100%;
        }
        .tab::part(base){
            height: 98%;
        }
        .tab::part(header){
            height: 3rem;
            display: flex;
            align-items: center;
        }
        .past-subjects-tab-area{
            width: 25%;
        }
        .current-subjects-tab-area{
            width: 35%;
        }
        .future-subjects-tab-area{
            width: 35%;
        }
        .dashboard-area {
            display: flex;
            flex-direction: row;
            align-items: stretch;
            height: 94vh;
            margin: auto 0;
        }
    </style>
</head>
<body>
<jsp:include page="../components/navbar.jsp"/>
    <main class="container-90">
        <div class="dashboard-area">

            <div id="past-subjects-tab" class="past-subjects-tab-area">
                <sl-card class="tab">
                    <div slot="header">
                        <h4>Past Subjects</h4>
                    </div>
                    <div></div>
                </sl-card>
            </div>

            <div id="current-subjects-tab" class="current-subjects-tab-area">
                <sl-card class="tab">
                    <div slot="header">
                        <h4>Current Semester</h4>
                    </div>
                </sl-card>
            </div>

            <div id="future-subjects-tab" class="future-subjects-tab-area">
                <sl-card class="tab">
                    <div slot="header">
                        <h4>Future Subjects</h4>
                    </div>
                </sl-card>
            </div>

        </div>
    </main>
<jsp:include page="../components/footer.jsp"/>
<jsp:include page="../components/body_scripts.jsp"/>
<script>

</script>
</body>

</html>

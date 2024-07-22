export const TRANSLATIONS_EN = {
    Landing: {
        title: "The best place to organize your semesters",
        get_started: "Get Started!",
        login: "Login",
        register: "Register",
        message1: "Keep track of your degree progression",
        message2: "Learn subject-specific tips from other students",
        message3: "Plan future semesters"
    },
    Register: {
        title: "Register",
        email: "Email",
        email_example: "username@gmail.com",
        email_error: "Invalid email",
        username: "Username",
        username_example: "username123",
        username_error: "Username must contain only letters and spaces",
        username_length_error: "Username must be less than 20 characters",
        username_empty_error: "Username cannot be empty",
        password: "Password",
        password_error: "Invalid password",
        confirm_password: "Repeat Password",
        confirm_password_error: "Passwords do not match",
        register: "Register",
        emailTaken: "Unable to register with this email!"
    },
    RegisterComplete: {
        title: "Registration successful",
        body: "You have successfully registered! We sent you an email to confirm your address. Please check your inbox."
    },
    ConfirmEmail: {
        title: "Confirm Email",
        InvalidToken: {
            title: "Invalid confirmation link",
            body: "The link you used to confirm your email is invalid or has already been used."
        },
        Confirmed: {
            title: "Email confirmed",
            body: "Your email has been confirmed! You can now login.",
            login: "Login"
        }
    },
    Onboarding: {
        back: "Back",
        next: "Next",
        finish: "Finish",
        DegreeSelection: {
            title: "What degree are you studying?",
            select: "Select your degree"
        },
        SubjectSelection: {
            title: "Select the subjects you already passed",
            semester: "Semester {{n}}",
            electives: "Electives"
        }
    },
    Login: {
        title: "Login",
        email: "Email",
        email_example: "username@gmail.com",
        email_error: "Invalid email",
        password: "Password",
        password_error: "Invalid password",
        login: "Login",
        rememberMe: "Remember me",
        noAccount: "Don't have an account?",
        register: "Register here",
        forgotPassword: "Don't have or forgot your password?",
        recover: "Recover",
        invalid_credentials: "The credentials provided are invalid"
    },
    Recover: {
        title: "Recover Password",
        email: "Email",
        error: "An error occurred, please try again later",
        error_alert_title: "Error",
        success: "An email has been sent to you with instructions to recover your password",
        success_alert_title: "Sent",
        invalid_email: "Invalid email",
        submit: "Submit",
        
        WithToken: {
            title: "Set New Password",
            error: "An error occurred, please try again later",
            error_alert_title: "Error",
            success: "Password successfully changed",
            success_alert_title: "Changed",
            invalid_password: "Must be between 8 and 25 characters",
            passwords_dont_match: "Passwords don't match",
            password: "New Password",
            password_confirm: "Confirm New Password",
            submit: "Submit",
            back_home: "Back to login"
        }
    },
    Home: {
        title: "Home",
        currentSemester: "Current Semester",
        overview: "Overview",
        futureSubjects: "Future Subjects",
        pastSubjects: "Past Subjects",
        completedCredits: "Completed Credits",
        totalCredits: "Total credits in your degree",
        overallProgress: "Overall Progress",
        completedCreditsByYear: "Progress by year",
        firstYear: "First Year",
        secondYear: "Second Year",
        thirdYear: "Third Year",
        fourthYear: "Fourth Year",
        fifthYear: "Fifth Year",
        thisSemester: "This semester you are taking",
        classDay1: "Monday",
        classDay2: "Tuesday",
        classDay3: "Wednesday",
        classDay4: "Thursday",
        classDay5: "Friday",
        classDay6: "Saturday",
        classDay: "Day",
        builderTime: "Time",
        builderClass: "Class",
        builderBuilding: "Building",
        builderMode: "Mode",
        emptySemester: "It seems like you don't have an active semester. You can build one using the",
        emptySemesterLink: " semester builder.",
        finishCurrentSemester: "Finish semester",
        editCurrentSemester: "Edit semester"
    },
    SubjectCard: {
        credits: "{{n}} credits",
        prerequisites: "{{n}} prerequisites",
        done_tooltip: "You have already completed this subject",
        easy: "Easy",
        normal: "Normal",
        hard: "Hard",
        low: "Not time demanding",
        medium: "Normal time demand",
        high: "Very time demanding",
        reviews: "{{n}} reviews",
        no_reviews: "No reviews",
        no_info: "No information"
    },
    Navbar: {
        home: "Home",
        curriculum: "Curriculum",
        semesterbuilder: "Semester Builder",
        profile: "Profile",
        degrees: "Degrees",
        createSubject: "Create new subject",
        createDegree: "Create new degree",
        editorTools: "Editor Tools",
        yourSemesters: "Your semesters"
    },
    ReviewCard: {
        showMore: "Show more",
        showLess: "Show less"
    },
    Footer: {
        mission: "Our mission is to provide a simple and intuitive way of sharing your opinions, tips and general knowledge for all of your college courses.",
        rights: "© 2023 Uni Team, All rights reserved."
    },
    Profile: {
        email: "Email:",
        username: "Username:",
        password: "Password:",
        degree: "Degree:",
        reviews:"User reviews:",
        loggeduser: "My Profile",
        logout: "Logout",
        moderator: "MODERATOR",
        newUsername: "New Username",
        change_password: "Change Password",
        change_degree: "Change Degree",
        change_username: "Change Username",
        change_picture: "Change Profile Picture",
        oldPass: "Current password",
        newPass: "New password",
        confirmNewPass: "Confirm new password",
        submit: "Submit",
        chooseDegree: "Choose your degree",
        changeDegreeWarning: "This will delete your current semester.",
        changeDegreeError: "There was an error changing your degree. Please try again later.",
        changePassError: "There was an error changing your password. Please try again later.",
        changeUsernameError: "There was an error changing your username. Please try again later.",
        warning: "Warning: ",
        samePassError: "The new password cannot be the same as the current one",
        wrongOldPass: "The current password is incorrect",
        pictureIdle: "Upload picture",
        pictureHint: "Drag and drop your image here. Max size 5MB.",
        pictureButton: "Select file",
        pictureReject: "Only .png and .jpeg less than 5MB."
    },
    Search: {
        title: "Search",
        not_found: "We could not find a subject that matched: {{query}}",
        filter: "Filter",
        filterCredits: "Credits",
        filterDepartment: "Department",
        filterDifficulty: "Difficulty",
        filterTimeDemand: "Time Demand",
        filterOrderBy: "Order By",
        filterAZ: "A-Z",
        filterId: "ID",
    },
    TimeTable: {
        day: "Day",
        time: "Time",
        class: "Class",
        building: "Building",
        mode: "Mode",
        day1: "Monday",
        day2: "Tuesday",
        day3: "Wednesday",
        day4: "Thursday",
        day5: "Friday",
        day6: "Saturday",
        day7: "Sunday"
    },
    Subject: {
        general: "General Information",
        times: "Class Times",
        classProf: "Class Professors",
        department: "Department",
        credits: "Credits",
        prerequisites: "Prerequisites",
        emptyPrerequisites: "None",
        professors: "Professors",
        emptyProfessors: "None",
        difficulty: "Difficulty",
        time: "Time Demanding",
        classCode: "Class Code",
        classDay: "Day",
        classTime: "Class Times",
        classMode: "Class Mode",
        classBuilding: "Class Building",
        classNumber: "Class Number",
        review: "Review Subject",
        electives: "Electives",
        year: "Year",
        progressTooltip: "Show your progress by indicating wheter you have passed a subject",
        progressDone: "Completed",
        progressPending: "Pending...",
        alreadyReviewed: "You already made a review",
        currentFilter: "Current filter",
        directionDesc: "descending",
        directionAsc: "ascending",
        orderDifficulty: "Difficulty",
        orderTimeDemand: "Time Demanding",
        sort: "Sort",
        noreviews: "No Reviews Yet!",
        ascDiff: "Ascending Difficulty",
        descDiff: "Descending Difficulty",
        ascTime: "Ascending Time Demand",
        descTime: "Descending Time Demand",
        editSubject: "Edit Subject",
    },
    Builder: {
        title: "Semester Builder",
        available: "Available Subjects",
        selectClass: "Select a class for",
        timeTable: "Your timetable",
        selected: "Selected subjects",
        overview: "Your semester overview",

        sortName: "Name",
        sortCredits: "Credits",
        sortDifficulty: "Difficulty",
        sortTimeDemand: "Time Demand",

        overviewCredits: "Number of credits",
        overviewDifficulty: "Time demand",
        overviewTimeDemand: "Overall difficulty",
        unlock: "By doing this semester you unlock",

        done: "Done",
        save: "Save",
        saveSuccessTitle: "Saved!",
        saveSuccessBody: "Your semester has been saved!",
        saveFailTitle: "Failed!",
        saveFailBody: "Your semester wasn't able to be saved!",
    },
    BuilderFinish: {
        title: "Finish Semester",
        select: "Select the subjects you passed",
        cancel: "Cancel",
        submit: "Submit",
        saveFailTitle: "Failed!",
        saveFailBody: "Failed to save subject progress!"
    },
    Review: {
        title: "Review {{subjectName}}",
        review: "Write your review",
        easy: "Easy",
        medium: "Medium",
        hard: "Hard",
        option: "Select an option",
        difficultyHelp: "Were the topics in the subject complex to understand?",
        lowTimeDemand: "Low time demanding",
        mediumTimeDemand: "Normal time demand",
        highTimeDemand: "Very time demanding",
        timeDemandHelp: "Were you required to put a lot of time every week to keep up with the topics?",
        public: "Public",
        anonymous: "Anonymous",
        publicHelp: "Other users will be able to identify that you made this review.",
        submit: "Submit!",
        editSubmit: "Edit review",
        editSuccess: "Review edited successfully",
        editFailure: "An error occured, please try again later",
        deleteSuccess: "Review deleted successfully",
        deleteFailure: "An error occured, please try again later",
    },
    MultiReview: {
        title: "Review subjects",
        progressText: "{{c}} of {{l}} reviews",
        skip: "Skip",
    },
    ReviewCards: {
        doyouwish: "This action cannot be undone. Do you wish to delete the review?",
        confirm: "Confirm",
        cancel: "Cancel",
    },
    CreateSubject: {
        title: "New Subject",
        pageTitle: "Create Subject",
        id: "Subject ID",
        idHelp: "Must be 4 numbers in the format: ##.##",
        name: "Subject Name",
        idError: "A subject with this code already exists",
        department: "Department",
        credits: "Credits",
        degree: "Degrees",
        addDegree: "Add degree",
        degreeModalDegree: "Degree",
        semesterOption: "Semester {{number}}",
        elective: "Elective",
        semester: "Semester",
        add: "Add",
        prerequisites: "Prerequisities",
        optional: "Optional",
        professor: "Professor",
        createProfessor: "Create Professor",
        professorName: "Professor's full name",
        next: "Next",
        previous: "Previous",
        generalInfo: "General Information",
        classes: "Classes",
        addClasses: "Add classes",
        class: "Class Code",
        professors: "Professors",
        day: "Day",
        timeStart: "Start time",
        timeEnd: "End time",
        mode: "Mode",
        building: "Building",
        classroom: "Classroom",
        day1: "Monday",
        day2: "Tuesday",
        day3: "Wednesday",
        day4: "Thursday",
        day5: "Friday",
        day6: "Saturday",
        day7: "Sunday",
        createSubject: "Create Subject",
        addPrereq: "Add Prerequisite",
        prereqAdvice: "Add a degree first!",
        professorLabel: "Search for a professor's name",
        editClasses: "Edit classes",
        edit: "Edit",
        addClassTimes: "Add Class times",
        missingFields: "Warning",
        completeFields: "Class Time won't be created until all fields are completed and the amount of credits aren't surpassed",
        createClassTime: "Add Class time",
        completeClassFields: "Must complete class name, professors fields and use only letters for the class name",
        emptySubjectName: "This field is required",
        emptySubjectNameError: "Subject name is required",
        wrongSubjectId: "Subject ID is required and needs to follow the format suggested",
        emptyDepartment: "Pick a department for the subject",
        emptyDegree: "Pick a degree and a semester for the subject",
        emptyClasses: "Classes are required for the subject",
        wrongCredits: "Make sure to match the amount of hours of class with the amount of credits for every class",
        disabledSubjectIdField: "Cannot edit subject ID",
        editSubjectTitle: "Edit subject",
    },
    Degrees: {
        title: "Degrees",
        createNew: "Create new degree",
        delete: "Delete",
        cancel: "Cancel",
        areYouSure: "Are you sure you want to delete this degree?",
    },
    User: {
        makeModerator: "Make moderator",
        moderator: "MODERATOR",
        noReviews: "No reviews yet!",
        userReviews: "User reviews",
        degree: "Degree",
        noDegree: "Not selected",
        currentSemester: "Current semester",
        completedCredits: "Completed credits",
        outOf: "out of",
    },
    CreateDegree: {
        title: "Create Degree",
        name: "Degree Name",
        credits: "Total Credits",
        generalInfo: "General Information",
        subjects: "Subjects",
        addSemester: "Add Semester",
        semester: "Semester",
        subjectNumber: "Subjects",
        nothingFound: "No subjects found",
        createDegree: "Create Degree",
        electives: "Electives",
    },
    Curriculum: {
        title: "Curriculum",
        semester: "Year {{year}} Semester {{semester}}",
        electives: "Electives",
        filter: "Filter",
        department: "Department",
        applyFilters: "Apply Filters",
        reset: "Reset",
        credits: "Credits",
        difficulty: "Difficulty",
        name: "Name",
        timeDemand: "Time Demand",
        code: "Code",
        easy: "Easy",
        medium: "Medium",
        hard: "Hard",
        low: "Low",
        high: "High",
        orderBy: "Order By:",
        ascending: "Ascending",
        descending: "Descending",
    },
    UserSemesters: {
        title: "Your Semesters",
        bodyTitle: "Your semesters:",
        cardTitleCurrent: "Current semester",
        cardTitle: "Semester {{n}}",
        active: "Active",
        completed: "Completed: {{n}}",
        credits: "{{n}} credits",
        emptySemester: "It seems like you don't have any semesters. You can build one using the",
        emptySemesterLink: " semester builder.",
    },
    Error :{
        title: "Error",
        NotFound: {
            header: "The page does not exist",
            subtitle: "The page you are trying to view does not exist.",
            returnHome: "Return home"
        },
        Unauthorized: {
            header: "Unauthorized",
            subtitle: "You do not have permission to view this page",
            returnHome: "Return"
        },
        ServerError: {
            header: "Server error",
            subtitle: "An error occurred on the server. Please try again later.",
            returnHome: "Return home"
        }
    }
    
}

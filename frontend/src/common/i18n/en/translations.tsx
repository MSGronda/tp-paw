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
        username_error: "Invalid username",
        password: "Password",
        password_error: "Invalid password",
        confirm_password: "Repeat Password",
        confirm_password_error: "Passwords do not match",
        register: "Register"
    },
    RegisterComplete: {
        title: "Registration successful",
        body: "You have successfully registered! We sent you an email to confirm your address. Please check your inbox."
    },
    ConfirmEmail: {
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
    Home: {
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
        createSubject: "Create new subject"
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
        username: "Username",
        reviews:"User reviews:",
        loggeduser: "My profile",
        logout: "Logout",
        change_password: "Change password",
        change_degree: "Change degree"
    },
    Search: {
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
        publicHelp: "Otros usuarios podran ver que esta reseña es tuya",
        submit: "Submit!",
        editSubmit: "Edit review",
        editSucces: "Review edited successfully",
        editFailure: "An error occured, please try again later",
        deleteSuccess: "Review deleted successfully",
        deleteFailure: "An error occured, please try again later",
    },
    MultiReview: {
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
        professorName: "Professor's name",
        professorLastName: "Professor's last name",
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
        createSubject: "Create Subject"
    },
    Degrees: {
        title: "Degrees",
        createNew: "Create new degree",
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
        semesterNumber: "Number {{n}} asd",
        subjectNumber: "Subjects",
        nothingFound: "No subjects found",
        createDegree: "Create Degree"

    }
}

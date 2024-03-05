
export const subject1 = {
    id: "31.08",
    subname: "Sistemas de Representación",
    department: "Ciencias exactas y Naturales",
    credits: 3
}

export const subject2 = {
    id: "31.09",
    subname: "Sistemas de la Cienca",
    department: "Ciencias exactas y Naturales",
    credits: 6
}

export const user1 = {
    id: 43,
    email: "pablo@gmail.com",
    username: "pablito",
    password: "123456789"
}

export const user2 = {
    id: 44,
    email: "esteban@gmail.com",
    username: "esteban"
}

export const review1 = {
    id: 1,
    subjectId: "31.08",
    userId: 43,
    review: "Muy buena materia",
    timeDemanding: 1,
    difficulty: 1,
    anonymous: false
}

export const review2 = {
    id: 2,
    subjectId: "31.08",
    userId: 42,
    review: "No me gusto nada, muy complicada",
    timeDemanding: 1,
    difficulty: 2,
    anonymous: true
}

const classTime1 = {
    day: 1,
    startTime: "08:00",
    endTime: "11:00",
    classNumber: "201T",
    building: "SDT",
    mode: "Presencial"
}

const classTime2 = {
    day: 3,
    startTime: "14:00",
    endTime: "17:00",
    classNumber: "702F",
    building: "SDF",
    mode: "Presencial"
}

export const class1 = {
    idSubject: "31.08",
    idClass: "201T",
    professors: ["Juan", "Carlos"],
    locations: [classTime1]
}

export const class2 = {
    idSubject: "31.00",
    idClass: "702F",
    professors: ["Maria", "Luz"],
    locations: [classTime2]
}

export const selectedSubject1 = {
    subject: subject1,
    selectedClass: class1
}

export const selectedSubject2 = {
    subject: subject2,
    selectedClass: class2
}
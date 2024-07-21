import initializeLocalStorageMock from '../mocks/localStorageMock';
import { expect, test } from 'vitest'
import {userService, } from '../../services/index.tsx';
import {afterEach, vi} from 'vitest';
import axiosInstance from '../../api.tsx';
import {user1, user2, class1, class2, selectedSubject1, selectedSubject2} from '../mocks/index';
import AuthService from "../../services/AuthService.ts";



initializeLocalStorageMock();

afterEach(() => {
    mockedGet.mockClear();
    mockedPost.mockClear();
    mockedDelete.mockClear();
    mockedPatch.mockClear();
    mockedPut.mockClear();
    localStorage.clear();
  })
  
  const mockedGet = vi.spyOn(axiosInstance, "get")
  const mockedPost = vi.spyOn(axiosInstance, "post")
  const mockedDelete = vi.spyOn(axiosInstance, "delete")
  const mockedPatch = vi.spyOn(axiosInstance, "patch")
  const mockedPut = vi.spyOn(axiosInstance, "put")

  test("It should return a list of users", async () => {
    mockedGet.mockResolvedValueOnce({ 
        status: 200,
        data: [user1, user2], 
        failure: false,
        headers: {}
    });

    const response = await userService.getUsersThatReviewedSubject("31.08", 1)

    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([user1, user2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
  });

test("It should create a user", async () => {
    mockedPost.mockResolvedValueOnce({ 
            status: 201,
            data: user1, 
            failure: false,
            headers: {}
    });

    const form = {
        email: user1.email,
        username: user1.username,
        password: user1.password,
        passwordConfirmation: user1.password
    }
    const response = await AuthService.register(form);
    expect(response).toBeTruthy();
});


test("It should fail to create a user", async () => {
    mockedPost.mockResolvedValueOnce({ 
            status: 400,
            failure: true,
            headers: {}
    });

    const form = {
        email: user1.email,
        username: user1.username,
        password: user1.password,
        passwordConfirmation: user1.password
    }
    const response = await AuthService.register(form);
    expect(response).toBeFalsy();
});

test("It should confirm an email", async () => {
    mockedPost.mockResolvedValueOnce({ 
            status: 200,
            failure: false,
            headers: {}
    });

    const response = await AuthService.confirmEmail("token");
    expect(response).toBeTruthy();
});

test("It should fail to confirm an email", async () => {
    mockedPost.mockResolvedValueOnce({ 
            status: 400,
            failure: false,
            headers: {}
    });

    const response = await AuthService.confirmEmail("token");
    expect(response).toBeFalsy();
});

test("It should return a user plan", async() =>{
    mockedGet.mockResolvedValueOnce({
        status: 200,
        data: [class1, class2],
        failure: false,
        headers: {}
    });

    const response = await userService.getUserPlan(43);
    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([class1, class2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    expect(mockedGet).toHaveBeenCalledWith("/users/43/plan", expect.any(Object));

});


test("It should set a user semester", async() =>{
    mockedPut.mockResolvedValueOnce({
        status: 200,
        failure: false,
        headers: {}
    });

    const selectedSubjects = [selectedSubject1, selectedSubject2];
    const response = await userService.setUserSemester(43, selectedSubjects);

    expect(response.failure).toBeFalsy();
    expect(mockedPut).toHaveBeenCalledTimes(1);

    //la funcion modifica el parametro
    const subs = [];
    const classes = [];
    selectedSubjects.forEach((subject) => {
        subs.push(subject.subject.id);
        classes.push(subject.selectedClass.idClass);
    })
    const data = {
        idSub: subs,
        idClass: classes
    }
    expect(mockedPut).toHaveBeenCalledWith("/users/43/plan", data, expect.any(Object));
});

test("It should complete a semester", async() => {
    mockedPatch.mockResolvedValueOnce({
        status: 200,
        failure: false,
        headers: {}
    });

    const response = await userService.completeSemester(43);
    expect(response.failure).toBeFalsy();
    expect(mockedPatch).toHaveBeenCalledTimes(1);
    const semesterData = {
        type: 3,
    }
    expect(mockedPatch).toHaveBeenCalledWith("/users/43/plan", semesterData, expect.any(Object));
})

test("It should set finished subjects", async() => {
    mockedPatch.mockResolvedValueOnce({
        status: 200,
        failure: false,
        headers: {}
    });

    const passed = ["31.08"];
    const notPassed = ["31.09"];

    const response = await userService.setFinishedSubjects(43, passed, notPassed);

    const progressData = {
        newPassedSubjects: passed,
        newNotPassedSubjects: notPassed
    }
    expect(response.failure).toBeFalsy();
    expect(mockedPatch).toHaveBeenCalledTimes(1);
    expect(mockedPatch).toHaveBeenCalledWith("/users/43/progress", progressData, expect.any(Object));
})

test("It should return a user progress", async() => {
    mockedGet.mockResolvedValueOnce({
        status: 200,
        data: {
            passed: ["31.08"],
            notPassed: ["31.09"]
        },
        failure: false,
        headers: {}
    });

    const response = await userService.getUserProgress(43);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    expect(mockedGet).toHaveBeenCalledWith("/users/43/progress", expect.any(Object));
    expect(response.data).toEqual({
        passed: ["31.08"],
        notPassed: ["31.09"]
    });
});

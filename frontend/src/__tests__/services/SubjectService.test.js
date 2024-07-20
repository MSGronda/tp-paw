import {subject1, subject2} from '../mocks/index.js';
import { expect, test } from 'vitest'
import { subjectService } from '../../services/index.tsx';
import {afterEach, vi} from 'vitest';
import axiosInstance from '../../api.tsx';
import initializeLocalStorageMock from '../mocks/localStorageMock';

initializeLocalStorageMock();


afterEach(() => {
  mockedGet.mockClear();
  mockedPost.mockClear();
  mockedDelete.mockClear();
  mockedPatch.mockClear();
  localStorage.clear();
})

const mockedGet = vi.spyOn(axiosInstance, "get")
const mockedPost = vi.spyOn(axiosInstance, "post")
const mockedDelete = vi.spyOn(axiosInstance, "delete")
const mockedPatch = vi.spyOn(axiosInstance, "patch")


test("It should return the subject with id = 31.08", async () => {

    mockedGet.mockResolvedValueOnce({ 
        status: 200,
        data: subject1, 
        failure: false,
        headers: {}
    });

    const response = await subjectService.getSubjectById("31.08")

    expect(response.data).toEqual(subject1);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    
});


test("It should return a list of subjects", async () => {

    mockedGet.mockResolvedValueOnce({ 
        status: 200,
        data: [subject1, subject2], 
        failure: false,
        headers: {}
    });

    const response = await subjectService.search("Sistemas", 1, null, null, null, null, null, null)


    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([subject1, subject2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    
});

test("It should return a list with subjects of 6 credits", async () => {

  mockedGet.mockResolvedValueOnce({ 
      status: 200,
      data: [subject2], 
      failure: false,
      headers: {}
  });

  const response = await subjectService.search("Sistemas", 1, 6, null, null, null, null, null)

  expect(response.data.length).toBe(1);
  expect(response.data).toEqual([subject2]);
  expect(response.failure).toBeFalsy();
  expect(mockedGet).toHaveBeenCalledTimes(1);
  
});


test("It should return a list of subjects that the user has available", async() => {
    
        mockedGet.mockResolvedValueOnce({ 
            status: 200,
            data: [subject1, subject2], 
            failure: false,
            headers: {}
        });
    
        const response = await subjectService.getAvailableSubjects(43)
   
        expect(response.data.length).toBe(2);
        expect(response.data).toEqual([subject1, subject2]);
        expect(response.failure).toBeFalsy();
        expect(mockedGet).toHaveBeenCalledTimes(1);
        expect(mockedGet).toHaveBeenCalledWith("/subjects", expect.objectContaining({params: {available: 43, page: 1}}))
})

test("It should return a list of subjects that the user has done", async() => {
    mockedGet.mockResolvedValueOnce({
        status: 200,
        data: [subject1, subject2],
        failure: false,
        headers: {}
    })


    const response = await subjectService.getDoneSubjects(43)
    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([subject1, subject2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    expect(mockedGet).toHaveBeenCalledWith("/subjects", expect.objectContaining({params: {done: 43, page: 1}}))
})


test("It should return a list of subjects that are unlockable", async() => {
    mockedGet.mockResolvedValueOnce({
        status: 200,
        data: [subject1, subject2],
        failure: false,
        headers: {}
    })

    const response = await subjectService.getUnlockableSubjects(43)
    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([subject1, subject2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    expect(mockedGet).toHaveBeenCalledWith("/subjects", expect.objectContaining({params: {unLockable: 43}}))
})

test("It should return a list of subjects that are in the user's plan", async() => {
    mockedGet.mockResolvedValueOnce({
        status: 200,
        data: [subject1, subject2],
        failure: false,
        headers: {}
    })

    const response = await subjectService.getUserPlanSubjects(43)
    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([subject1, subject2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    expect(mockedGet).toHaveBeenCalledWith("/subjects", expect.objectContaining({params: {plan: 43}}))
})




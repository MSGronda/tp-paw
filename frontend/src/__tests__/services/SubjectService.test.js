import {subject1} from '../mocks/index.js';
import { expect, test } from 'vitest'
import { subjectService } from '../../services/index.tsx';
import {afterEach, vi} from 'vitest';
import axiosInstance from '../../api.tsx';

// Define a mock for localStorage and sessionStorage
const localStorageMock = (() => {
    let store = {};
    return {
      getItem: key => store[key],
      setItem: (key, value) => { store[key] = value.toString() },
      removeItem: key => { delete store[key] },
      clear: () => { store = {} }
    };
  })();
  
global.localStorage = localStorageMock;
global.sessionStorage = localStorageMock;

const mockedGet = vi.spyOn(axiosInstance, "get")


test("It should return the subject with id = 31.08", async () => {

    mockedGet.mockResolvedValueOnce({ 
        status: 200,
        data: subject1, 
        failure: false,
        headers: {}
    });

    const response = await subjectService.getSubjectById("31.08")
    console.log(response);

    expect(response.data).toEqual(subject1);
    expect(response.failure).toBeFalsy();
    //expect().toHaveBeenCalledTimes(1);
    //expect(mock.history).toHaveBeenCalledWith("/subjects/31.08");
    
});




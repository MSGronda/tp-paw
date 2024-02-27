import initializeLocalStorageMock from '../mocks/localStorageMock';
import { expect, test } from 'vitest'
import {userService} from '../../services/index.tsx';
import {afterEach, vi} from 'vitest';
import axiosInstance from '../../api.tsx';
import {user1, user2} from '../mocks/index';


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

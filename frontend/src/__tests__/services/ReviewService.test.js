import { expect, test } from 'vitest'
import { reviewService } from '../../services/index.tsx';
import {afterEach, vi} from 'vitest';
import axiosInstance from '../../api.tsx';
import initializeLocalStorageMock from '../mocks/localStorageMock';
import { review1, review2 } from '../mocks/index.js';

initializeLocalStorageMock();

afterEach(() => {
    mockedGet.mockClear();
    mockedPost.mockClear();
    mockedDelete.mockClear();
    mockedPut.mockClear();
    localStorage.clear();
  })
  
  const mockedGet = vi.spyOn(axiosInstance, "get")
  const mockedPost = vi.spyOn(axiosInstance, "post")
  const mockedDelete = vi.spyOn(axiosInstance, "delete")
  const mockedPut = vi.spyOn(axiosInstance, "put")

  test("It should return a list of reviews", async () => {
    mockedGet.mockResolvedValueOnce({ 
        status: 200,
        data: [review1, review2], 
        failure: false,
        headers: {}
    });

    const response = await reviewService.getReviewsBySubject("31.08", 1, "difficulty", "desc")

    expect(response.data.length).toBe(2);
    expect(response.data).toEqual([review1, review2]);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);


  });

  test("It should return a failure", async () => {
    mockedGet.mockResolvedValueOnce({ 
        status: 400,
        failure: true,
    });

    const response = await reviewService.getReviewsBySubject("31.08", 1, "difficulty", "desc")
    expect(response.data).toBeUndefined();
    expect(response.failure).toBeTruthy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
    expect(response.status).toBe(400);
  });

  test("It should fail", async () => {
    mockedGet.mockRejectedValueOnce({});

    const response = await reviewService.getReviewsBySubject("31.08", 1, "difficulty", "desc")
    expect(response).toBeUndefined();

  });


  test("It should return a review", async () => {
    mockedGet.mockResolvedValueOnce({ 
        status: 200,
        data: review1, 
        failure: false,
        headers: {}
    });

    const response = await reviewService.getReviewFromSubjectAndUser("31.08", 43)

    expect(response.data).toEqual(review1);
    expect(response.failure).toBeFalsy();
    expect(mockedGet).toHaveBeenCalledTimes(1);
  });


  test("It should publish a review", async () => {
    mockedPost.mockResolvedValueOnce({ 
        status: 200,
        data: review1, 
        failure: false,
        headers: {}
    });

    const response = await reviewService.publishReview("31.08", 43, "Muy buena materia", 1, 1, false)
    expect(response.data).toEqual(review1);
    expect(response.failure).toBeFalsy();
    expect(mockedPost).toHaveBeenCalledTimes(1);
  });

  test("It should delete a review", async () => {
    mockedDelete.mockResolvedValueOnce({ 
        status: 200,
        failure: false,
        headers: {}
    });

    const response = await reviewService.deleteReview(1)
    expect(response.failure).toBeFalsy();
    expect(mockedDelete).toHaveBeenCalledTimes(1);
  });

  test("It should update a review", async () => {
    mockedPut.mockResolvedValueOnce({ 
        status: 200,
        data: review1, 
        failure: false,
        headers: {}
    });

    const response = await reviewService.editReview(43, "Muy buena materiaa", 1, 1, false, "31.08")
    expect(response.data).toEqual(review1);
    expect(response.failure).toBeFalsy();
    expect(mockedPut).toHaveBeenCalledTimes(1);
  });


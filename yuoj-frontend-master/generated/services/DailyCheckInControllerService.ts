/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_int_ } from '../models/BaseResponse_int_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Map_string_int_ } from '../models/BaseResponse_Map_string_int_';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class DailyCheckInControllerService {

    /**
     * checkIn
     * @param questionId questionId
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static checkInUsingPost(
questionId: number,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/daily_check_in/check_in',
            query: {
                'questionId': questionId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * makeupCheckIn
     * @param checkInDate checkInDate
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static makeupCheckInUsingPost(
checkInDate: string,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/daily_check_in/makeup',
            query: {
                'checkInDate': checkInDate,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getMakeupCardCount
     * @returns BaseResponse_int_ OK
     * @throws ApiError
     */
    public static getMakeupCardCountUsingGet(): CancelablePromise<BaseResponse_int_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/daily_check_in/makeup_card_count',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getCheckInRecord
     * @param month month
     * @param year year
     * @returns BaseResponse_Map_string_int_ OK
     * @throws ApiError
     */
    public static getCheckInRecordUsingGet(
month?: number,
year?: number,
): CancelablePromise<BaseResponse_Map_string_int_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/daily_check_in/record',
            query: {
                'month': month,
                'year': year,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getTodayQuestion
     * @returns BaseResponse_long_ OK
     * @throws ApiError
     */
    public static getTodayQuestionUsingGet(): CancelablePromise<BaseResponse_long_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/daily_check_in/today_question',
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}

/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_MockInterview_ } from '../models/BaseResponse_MockInterview_';
import type { BaseResponse_Page_MockInterview_ } from '../models/BaseResponse_Page_MockInterview_';
import type { BaseResponse_string_ } from '../models/BaseResponse_string_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { MockInterviewAddRequest } from '../models/MockInterviewAddRequest';
import type { MockInterviewEventRequest } from '../models/MockInterviewEventRequest';
import type { MockInterviewQueryRequest } from '../models/MockInterviewQueryRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class MockInterviewControllerService {

    /**
     * addMockInterview
     * @param mockInterviewAddRequest mockInterviewAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addMockInterviewUsingPost(
mockInterviewAddRequest: MockInterviewAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/mockInterview/add',
            body: mockInterviewAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteMockInterview
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteMockInterviewUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/mockInterview/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getMockInterviewById
     * @param id id
     * @returns BaseResponse_MockInterview_ OK
     * @throws ApiError
     */
    public static getMockInterviewByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_MockInterview_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/mockInterview/get',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * handleMockInterviewEvent
     * @param mockInterviewEventRequest mockInterviewEventRequest
     * @returns BaseResponse_string_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static handleMockInterviewEventUsingPost(
mockInterviewEventRequest: MockInterviewEventRequest,
): CancelablePromise<BaseResponse_string_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/mockInterview/handleEvent',
            body: mockInterviewEventRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMockInterviewByPage
     * @param mockInterviewQueryRequest mockInterviewQueryRequest
     * @returns BaseResponse_Page_MockInterview_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMockInterviewByPageUsingPost(
mockInterviewQueryRequest: MockInterviewQueryRequest,
): CancelablePromise<BaseResponse_Page_MockInterview_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/mockInterview/list/page',
            body: mockInterviewQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMyMockInterviewVOByPage
     * @param mockInterviewQueryRequest mockInterviewQueryRequest
     * @returns BaseResponse_Page_MockInterview_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyMockInterviewVoByPageUsingPost(
mockInterviewQueryRequest: MockInterviewQueryRequest,
): CancelablePromise<BaseResponse_Page_MockInterview_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/mockInterview/my/list/page/vo',
            body: mockInterviewQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}

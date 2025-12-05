/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_int_ } from '../models/BaseResponse_int_';
import type { BaseResponse_List_QuestionCommentVO_ } from '../models/BaseResponse_List_QuestionCommentVO_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { QuestionCommentAddRequest } from '../models/QuestionCommentAddRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class QuestionCommentControllerService {

    /**
     * addComment
     * @param questionCommentAddRequest questionCommentAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addCommentUsingPost(
questionCommentAddRequest: QuestionCommentAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/question_comment/add',
            body: questionCommentAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteComment
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteCommentUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/question_comment/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getCommentList
     * @param questionId questionId
     * @returns BaseResponse_List_QuestionCommentVO_ OK
     * @throws ApiError
     */
    public static getCommentListUsingGet(
questionId: number,
): CancelablePromise<BaseResponse_List_QuestionCommentVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/question_comment/list',
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
     * doThumb
     * @param commentId commentId
     * @returns BaseResponse_int_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doThumbUsingPost1(
commentId: number,
): CancelablePromise<BaseResponse_int_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/question_comment/thumb',
            query: {
                'commentId': commentId,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}

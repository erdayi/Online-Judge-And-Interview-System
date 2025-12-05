/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { UserVO } from './UserVO';

export type QuestionCommentVO = {
    children?: Array<QuestionCommentVO>;
    content?: string;
    createTime?: string;
    hasThumb?: boolean;
    id?: number;
    parentId?: number;
    questionId?: number;
    replyUser?: UserVO;
    replyUserId?: number;
    thumbNum?: number;
    updateTime?: string;
    user?: UserVO;
    userId?: number;
};

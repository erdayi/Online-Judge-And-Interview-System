/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { MockInterview } from './MockInterview';
import type { OrderItem } from './OrderItem';

export type Page_MockInterview_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<MockInterview>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};

# QuestionCommentControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addCommentUsingPOST**](#addcommentusingpost) | **POST** /api/question_comment/add | addComment|
|[**deleteCommentUsingPOST**](#deletecommentusingpost) | **POST** /api/question_comment/delete | deleteComment|
|[**doThumbUsingPOST1**](#dothumbusingpost1) | **POST** /api/question_comment/thumb | doThumb|
|[**getCommentListUsingGET**](#getcommentlistusingget) | **GET** /api/question_comment/list | getCommentList|

# **addCommentUsingPOST**
> BaseResponselong addCommentUsingPOST(questionCommentAddRequest)


### Example

```typescript
import {
    QuestionCommentControllerApi,
    Configuration,
    QuestionCommentAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionCommentControllerApi(configuration);

let questionCommentAddRequest: QuestionCommentAddRequest; //questionCommentAddRequest

const { status, data } = await apiInstance.addCommentUsingPOST(
    questionCommentAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionCommentAddRequest** | **QuestionCommentAddRequest**| questionCommentAddRequest | |


### Return type

**BaseResponselong**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |
|**201** | Created |  -  |
|**401** | Unauthorized |  -  |
|**403** | Forbidden |  -  |
|**404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteCommentUsingPOST**
> BaseResponseboolean deleteCommentUsingPOST(deleteRequest)


### Example

```typescript
import {
    QuestionCommentControllerApi,
    Configuration,
    DeleteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionCommentControllerApi(configuration);

let deleteRequest: DeleteRequest; //deleteRequest

const { status, data } = await apiInstance.deleteCommentUsingPOST(
    deleteRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **deleteRequest** | **DeleteRequest**| deleteRequest | |


### Return type

**BaseResponseboolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |
|**201** | Created |  -  |
|**401** | Unauthorized |  -  |
|**403** | Forbidden |  -  |
|**404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **doThumbUsingPOST1**
> BaseResponseint doThumbUsingPOST1()


### Example

```typescript
import {
    QuestionCommentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionCommentControllerApi(configuration);

let commentId: number; //commentId (default to undefined)

const { status, data } = await apiInstance.doThumbUsingPOST1(
    commentId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **commentId** | [**number**] | commentId | defaults to undefined|


### Return type

**BaseResponseint**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |
|**201** | Created |  -  |
|**401** | Unauthorized |  -  |
|**403** | Forbidden |  -  |
|**404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCommentListUsingGET**
> BaseResponseListQuestionCommentVO getCommentListUsingGET()


### Example

```typescript
import {
    QuestionCommentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionCommentControllerApi(configuration);

let questionId: number; //questionId (default to undefined)

const { status, data } = await apiInstance.getCommentListUsingGET(
    questionId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionId** | [**number**] | questionId | defaults to undefined|


### Return type

**BaseResponseListQuestionCommentVO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |
|**401** | Unauthorized |  -  |
|**403** | Forbidden |  -  |
|**404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)


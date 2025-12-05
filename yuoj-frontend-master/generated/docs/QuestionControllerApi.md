# QuestionControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addQuestionUsingPOST**](#addquestionusingpost) | **POST** /api/question/add | addQuestion|
|[**aiGenerateQuestionUsingPOST**](#aigeneratequestionusingpost) | **POST** /api/question/ai/generate/question | aiGenerateQuestion|
|[**deleteQuestionUsingPOST**](#deletequestionusingpost) | **POST** /api/question/delete | deleteQuestion|
|[**doQuestionSubmitUsingPOST**](#doquestionsubmitusingpost) | **POST** /api/question/question_submit/do | doQuestionSubmit|
|[**editQuestionUsingPOST**](#editquestionusingpost) | **POST** /api/question/edit | editQuestion|
|[**getQuestionByIdUsingGET**](#getquestionbyidusingget) | **GET** /api/question/get | getQuestionById|
|[**getQuestionVOByIdUsingGET**](#getquestionvobyidusingget) | **GET** /api/question/get/vo | getQuestionVOById|
|[**listMyQuestionVOByPageUsingPOST**](#listmyquestionvobypageusingpost) | **POST** /api/question/my/list/page/vo | listMyQuestionVOByPage|
|[**listQuestionByPageUsingPOST**](#listquestionbypageusingpost) | **POST** /api/question/list/page | listQuestionByPage|
|[**listQuestionSubmitByPageUsingPOST**](#listquestionsubmitbypageusingpost) | **POST** /api/question/question_submit/list/page | listQuestionSubmitByPage|
|[**listQuestionVOByPageUsingPOST**](#listquestionvobypageusingpost) | **POST** /api/question/list/page/vo | listQuestionVOByPage|
|[**updateQuestionUsingPOST**](#updatequestionusingpost) | **POST** /api/question/update | updateQuestion|

# **addQuestionUsingPOST**
> BaseResponselong addQuestionUsingPOST(questionAddRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionAddRequest: QuestionAddRequest; //questionAddRequest

const { status, data } = await apiInstance.addQuestionUsingPOST(
    questionAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionAddRequest** | **QuestionAddRequest**| questionAddRequest | |


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

# **aiGenerateQuestionUsingPOST**
> BaseResponseboolean aiGenerateQuestionUsingPOST(questionAIGenerateRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionAIGenerateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionAIGenerateRequest: QuestionAIGenerateRequest; //questionAIGenerateRequest

const { status, data } = await apiInstance.aiGenerateQuestionUsingPOST(
    questionAIGenerateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionAIGenerateRequest** | **QuestionAIGenerateRequest**| questionAIGenerateRequest | |


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

# **deleteQuestionUsingPOST**
> BaseResponseboolean deleteQuestionUsingPOST(deleteRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    DeleteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let deleteRequest: DeleteRequest; //deleteRequest

const { status, data } = await apiInstance.deleteQuestionUsingPOST(
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

# **doQuestionSubmitUsingPOST**
> BaseResponselong doQuestionSubmitUsingPOST(questionSubmitAddRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionSubmitAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionSubmitAddRequest: QuestionSubmitAddRequest; //questionSubmitAddRequest

const { status, data } = await apiInstance.doQuestionSubmitUsingPOST(
    questionSubmitAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionSubmitAddRequest** | **QuestionSubmitAddRequest**| questionSubmitAddRequest | |


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

# **editQuestionUsingPOST**
> BaseResponseboolean editQuestionUsingPOST(questionEditRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionEditRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionEditRequest: QuestionEditRequest; //questionEditRequest

const { status, data } = await apiInstance.editQuestionUsingPOST(
    questionEditRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionEditRequest** | **QuestionEditRequest**| questionEditRequest | |


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

# **getQuestionByIdUsingGET**
> BaseResponseQuestion getQuestionByIdUsingGET()


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let id: number; //id (optional) (default to undefined)

const { status, data } = await apiInstance.getQuestionByIdUsingGET(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | id | (optional) defaults to undefined|


### Return type

**BaseResponseQuestion**

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

# **getQuestionVOByIdUsingGET**
> BaseResponseQuestionVO getQuestionVOByIdUsingGET()


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let id: number; //id (optional) (default to undefined)

const { status, data } = await apiInstance.getQuestionVOByIdUsingGET(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | id | (optional) defaults to undefined|


### Return type

**BaseResponseQuestionVO**

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

# **listMyQuestionVOByPageUsingPOST**
> BaseResponsePageQuestionVO listMyQuestionVOByPageUsingPOST(questionQueryRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionQueryRequest: QuestionQueryRequest; //questionQueryRequest

const { status, data } = await apiInstance.listMyQuestionVOByPageUsingPOST(
    questionQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionQueryRequest** | **QuestionQueryRequest**| questionQueryRequest | |


### Return type

**BaseResponsePageQuestionVO**

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

# **listQuestionByPageUsingPOST**
> BaseResponsePageQuestion listQuestionByPageUsingPOST(questionQueryRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionQueryRequest: QuestionQueryRequest; //questionQueryRequest

const { status, data } = await apiInstance.listQuestionByPageUsingPOST(
    questionQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionQueryRequest** | **QuestionQueryRequest**| questionQueryRequest | |


### Return type

**BaseResponsePageQuestion**

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

# **listQuestionSubmitByPageUsingPOST**
> BaseResponsePageQuestionSubmitVO listQuestionSubmitByPageUsingPOST(questionSubmitQueryRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionSubmitQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionSubmitQueryRequest: QuestionSubmitQueryRequest; //questionSubmitQueryRequest

const { status, data } = await apiInstance.listQuestionSubmitByPageUsingPOST(
    questionSubmitQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionSubmitQueryRequest** | **QuestionSubmitQueryRequest**| questionSubmitQueryRequest | |


### Return type

**BaseResponsePageQuestionSubmitVO**

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

# **listQuestionVOByPageUsingPOST**
> BaseResponsePageQuestionVO listQuestionVOByPageUsingPOST(questionQueryRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionQueryRequest: QuestionQueryRequest; //questionQueryRequest

const { status, data } = await apiInstance.listQuestionVOByPageUsingPOST(
    questionQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionQueryRequest** | **QuestionQueryRequest**| questionQueryRequest | |


### Return type

**BaseResponsePageQuestionVO**

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

# **updateQuestionUsingPOST**
> BaseResponseboolean updateQuestionUsingPOST(questionUpdateRequest)


### Example

```typescript
import {
    QuestionControllerApi,
    Configuration,
    QuestionUpdateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionControllerApi(configuration);

let questionUpdateRequest: QuestionUpdateRequest; //questionUpdateRequest

const { status, data } = await apiInstance.updateQuestionUsingPOST(
    questionUpdateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionUpdateRequest** | **QuestionUpdateRequest**| questionUpdateRequest | |


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


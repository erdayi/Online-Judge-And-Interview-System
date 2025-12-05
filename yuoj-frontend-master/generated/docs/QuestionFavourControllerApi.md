# QuestionFavourControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**doQuestionFavourUsingPOST**](#doquestionfavourusingpost) | **POST** /api/question_favour/ | doQuestionFavour|
|[**listMyFavourQuestionByPageUsingPOST**](#listmyfavourquestionbypageusingpost) | **POST** /api/question_favour/my/list/page | listMyFavourQuestionByPage|

# **doQuestionFavourUsingPOST**
> BaseResponseint doQuestionFavourUsingPOST(questionFavourAddRequest)


### Example

```typescript
import {
    QuestionFavourControllerApi,
    Configuration,
    QuestionFavourAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionFavourControllerApi(configuration);

let questionFavourAddRequest: QuestionFavourAddRequest; //questionFavourAddRequest

const { status, data } = await apiInstance.doQuestionFavourUsingPOST(
    questionFavourAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionFavourAddRequest** | **QuestionFavourAddRequest**| questionFavourAddRequest | |


### Return type

**BaseResponseint**

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

# **listMyFavourQuestionByPageUsingPOST**
> BaseResponsePageQuestionVO listMyFavourQuestionByPageUsingPOST(questionQueryRequest)


### Example

```typescript
import {
    QuestionFavourControllerApi,
    Configuration,
    QuestionQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new QuestionFavourControllerApi(configuration);

let questionQueryRequest: QuestionQueryRequest; //questionQueryRequest

const { status, data } = await apiInstance.listMyFavourQuestionByPageUsingPOST(
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


# MockInterviewControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addMockInterviewUsingPOST**](#addmockinterviewusingpost) | **POST** /api/mockInterview/add | addMockInterview|
|[**deleteMockInterviewUsingPOST**](#deletemockinterviewusingpost) | **POST** /api/mockInterview/delete | deleteMockInterview|
|[**getMockInterviewByIdUsingGET**](#getmockinterviewbyidusingget) | **GET** /api/mockInterview/get | getMockInterviewById|
|[**handleMockInterviewEventUsingPOST**](#handlemockintervieweventusingpost) | **POST** /api/mockInterview/handleEvent | handleMockInterviewEvent|
|[**listMockInterviewByPageUsingPOST**](#listmockinterviewbypageusingpost) | **POST** /api/mockInterview/list/page | listMockInterviewByPage|
|[**listMyMockInterviewVOByPageUsingPOST**](#listmymockinterviewvobypageusingpost) | **POST** /api/mockInterview/my/list/page/vo | listMyMockInterviewVOByPage|

# **addMockInterviewUsingPOST**
> BaseResponselong addMockInterviewUsingPOST(mockInterviewAddRequest)


### Example

```typescript
import {
    MockInterviewControllerApi,
    Configuration,
    MockInterviewAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new MockInterviewControllerApi(configuration);

let mockInterviewAddRequest: MockInterviewAddRequest; //mockInterviewAddRequest

const { status, data } = await apiInstance.addMockInterviewUsingPOST(
    mockInterviewAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mockInterviewAddRequest** | **MockInterviewAddRequest**| mockInterviewAddRequest | |


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

# **deleteMockInterviewUsingPOST**
> BaseResponseboolean deleteMockInterviewUsingPOST(deleteRequest)


### Example

```typescript
import {
    MockInterviewControllerApi,
    Configuration,
    DeleteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new MockInterviewControllerApi(configuration);

let deleteRequest: DeleteRequest; //deleteRequest

const { status, data } = await apiInstance.deleteMockInterviewUsingPOST(
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

# **getMockInterviewByIdUsingGET**
> BaseResponseMockInterview getMockInterviewByIdUsingGET()


### Example

```typescript
import {
    MockInterviewControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MockInterviewControllerApi(configuration);

let id: number; //id (optional) (default to undefined)

const { status, data } = await apiInstance.getMockInterviewByIdUsingGET(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | id | (optional) defaults to undefined|


### Return type

**BaseResponseMockInterview**

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

# **handleMockInterviewEventUsingPOST**
> BaseResponsestring handleMockInterviewEventUsingPOST(mockInterviewEventRequest)


### Example

```typescript
import {
    MockInterviewControllerApi,
    Configuration,
    MockInterviewEventRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new MockInterviewControllerApi(configuration);

let mockInterviewEventRequest: MockInterviewEventRequest; //mockInterviewEventRequest

const { status, data } = await apiInstance.handleMockInterviewEventUsingPOST(
    mockInterviewEventRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mockInterviewEventRequest** | **MockInterviewEventRequest**| mockInterviewEventRequest | |


### Return type

**BaseResponsestring**

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

# **listMockInterviewByPageUsingPOST**
> BaseResponsePageMockInterview listMockInterviewByPageUsingPOST(mockInterviewQueryRequest)


### Example

```typescript
import {
    MockInterviewControllerApi,
    Configuration,
    MockInterviewQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new MockInterviewControllerApi(configuration);

let mockInterviewQueryRequest: MockInterviewQueryRequest; //mockInterviewQueryRequest

const { status, data } = await apiInstance.listMockInterviewByPageUsingPOST(
    mockInterviewQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mockInterviewQueryRequest** | **MockInterviewQueryRequest**| mockInterviewQueryRequest | |


### Return type

**BaseResponsePageMockInterview**

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

# **listMyMockInterviewVOByPageUsingPOST**
> BaseResponsePageMockInterview listMyMockInterviewVOByPageUsingPOST(mockInterviewQueryRequest)


### Example

```typescript
import {
    MockInterviewControllerApi,
    Configuration,
    MockInterviewQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new MockInterviewControllerApi(configuration);

let mockInterviewQueryRequest: MockInterviewQueryRequest; //mockInterviewQueryRequest

const { status, data } = await apiInstance.listMyMockInterviewVOByPageUsingPOST(
    mockInterviewQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mockInterviewQueryRequest** | **MockInterviewQueryRequest**| mockInterviewQueryRequest | |


### Return type

**BaseResponsePageMockInterview**

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


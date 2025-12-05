# PostFavourControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**doPostFavourUsingPOST**](#dopostfavourusingpost) | **POST** /api/post_favour/ | doPostFavour|
|[**listFavourPostByPageUsingPOST**](#listfavourpostbypageusingpost) | **POST** /api/post_favour/list/page | listFavourPostByPage|
|[**listMyFavourPostByPageUsingPOST**](#listmyfavourpostbypageusingpost) | **POST** /api/post_favour/my/list/page | listMyFavourPostByPage|

# **doPostFavourUsingPOST**
> BaseResponseint doPostFavourUsingPOST(postFavourAddRequest)


### Example

```typescript
import {
    PostFavourControllerApi,
    Configuration,
    PostFavourAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostFavourControllerApi(configuration);

let postFavourAddRequest: PostFavourAddRequest; //postFavourAddRequest

const { status, data } = await apiInstance.doPostFavourUsingPOST(
    postFavourAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postFavourAddRequest** | **PostFavourAddRequest**| postFavourAddRequest | |


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

# **listFavourPostByPageUsingPOST**
> BaseResponsePagePostVO listFavourPostByPageUsingPOST(postFavourQueryRequest)


### Example

```typescript
import {
    PostFavourControllerApi,
    Configuration,
    PostFavourQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostFavourControllerApi(configuration);

let postFavourQueryRequest: PostFavourQueryRequest; //postFavourQueryRequest

const { status, data } = await apiInstance.listFavourPostByPageUsingPOST(
    postFavourQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postFavourQueryRequest** | **PostFavourQueryRequest**| postFavourQueryRequest | |


### Return type

**BaseResponsePagePostVO**

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

# **listMyFavourPostByPageUsingPOST**
> BaseResponsePagePostVO listMyFavourPostByPageUsingPOST(postQueryRequest)


### Example

```typescript
import {
    PostFavourControllerApi,
    Configuration,
    PostQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostFavourControllerApi(configuration);

let postQueryRequest: PostQueryRequest; //postQueryRequest

const { status, data } = await apiInstance.listMyFavourPostByPageUsingPOST(
    postQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postQueryRequest** | **PostQueryRequest**| postQueryRequest | |


### Return type

**BaseResponsePagePostVO**

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


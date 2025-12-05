# PostThumbControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**doThumbUsingPOST**](#dothumbusingpost) | **POST** /api/post_thumb/ | doThumb|

# **doThumbUsingPOST**
> BaseResponseint doThumbUsingPOST(postThumbAddRequest)


### Example

```typescript
import {
    PostThumbControllerApi,
    Configuration,
    PostThumbAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostThumbControllerApi(configuration);

let postThumbAddRequest: PostThumbAddRequest; //postThumbAddRequest

const { status, data } = await apiInstance.doThumbUsingPOST(
    postThumbAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postThumbAddRequest** | **PostThumbAddRequest**| postThumbAddRequest | |


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


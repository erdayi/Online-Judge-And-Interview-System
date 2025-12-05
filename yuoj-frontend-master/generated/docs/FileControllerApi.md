# FileControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**uploadFileUsingPOST**](#uploadfileusingpost) | **POST** /api/file/upload | uploadFile|

# **uploadFileUsingPOST**
> BaseResponsestring uploadFileUsingPOST()


### Example

```typescript
import {
    FileControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FileControllerApi(configuration);

let biz: string; // (optional) (default to undefined)
let file: File; // (optional) (default to undefined)

const { status, data } = await apiInstance.uploadFileUsingPOST(
    biz,
    file
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **biz** | [**string**] |  | (optional) defaults to undefined|
| **file** | [**File**] |  | (optional) defaults to undefined|


### Return type

**BaseResponsestring**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
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


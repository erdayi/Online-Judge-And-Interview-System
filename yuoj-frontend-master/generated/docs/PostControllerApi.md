# PostControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addPostUsingPOST**](#addpostusingpost) | **POST** /api/post/add | addPost|
|[**deletePostUsingPOST**](#deletepostusingpost) | **POST** /api/post/delete | deletePost|
|[**editPostUsingPOST**](#editpostusingpost) | **POST** /api/post/edit | editPost|
|[**getPostVOByIdUsingGET**](#getpostvobyidusingget) | **GET** /api/post/get/vo | getPostVOById|
|[**listMyPostVOByPageUsingPOST**](#listmypostvobypageusingpost) | **POST** /api/post/my/list/page/vo | listMyPostVOByPage|
|[**listPostVOByPageUsingPOST**](#listpostvobypageusingpost) | **POST** /api/post/list/page/vo | listPostVOByPage|
|[**searchPostVOByPageUsingPOST**](#searchpostvobypageusingpost) | **POST** /api/post/search/page/vo | searchPostVOByPage|
|[**updatePostUsingPOST**](#updatepostusingpost) | **POST** /api/post/update | updatePost|

# **addPostUsingPOST**
> BaseResponselong addPostUsingPOST(postAddRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    PostAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let postAddRequest: PostAddRequest; //postAddRequest

const { status, data } = await apiInstance.addPostUsingPOST(
    postAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postAddRequest** | **PostAddRequest**| postAddRequest | |


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

# **deletePostUsingPOST**
> BaseResponseboolean deletePostUsingPOST(deleteRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    DeleteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let deleteRequest: DeleteRequest; //deleteRequest

const { status, data } = await apiInstance.deletePostUsingPOST(
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

# **editPostUsingPOST**
> BaseResponseboolean editPostUsingPOST(postEditRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    PostEditRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let postEditRequest: PostEditRequest; //postEditRequest

const { status, data } = await apiInstance.editPostUsingPOST(
    postEditRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postEditRequest** | **PostEditRequest**| postEditRequest | |


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

# **getPostVOByIdUsingGET**
> BaseResponsePostVO getPostVOByIdUsingGET()


### Example

```typescript
import {
    PostControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let id: number; //id (optional) (default to undefined)

const { status, data } = await apiInstance.getPostVOByIdUsingGET(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | id | (optional) defaults to undefined|


### Return type

**BaseResponsePostVO**

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

# **listMyPostVOByPageUsingPOST**
> BaseResponsePagePostVO listMyPostVOByPageUsingPOST(postQueryRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    PostQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let postQueryRequest: PostQueryRequest; //postQueryRequest

const { status, data } = await apiInstance.listMyPostVOByPageUsingPOST(
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

# **listPostVOByPageUsingPOST**
> BaseResponsePagePostVO listPostVOByPageUsingPOST(postQueryRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    PostQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let postQueryRequest: PostQueryRequest; //postQueryRequest

const { status, data } = await apiInstance.listPostVOByPageUsingPOST(
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

# **searchPostVOByPageUsingPOST**
> BaseResponsePagePostVO searchPostVOByPageUsingPOST(postQueryRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    PostQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let postQueryRequest: PostQueryRequest; //postQueryRequest

const { status, data } = await apiInstance.searchPostVOByPageUsingPOST(
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

# **updatePostUsingPOST**
> BaseResponseboolean updatePostUsingPOST(postUpdateRequest)


### Example

```typescript
import {
    PostControllerApi,
    Configuration,
    PostUpdateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PostControllerApi(configuration);

let postUpdateRequest: PostUpdateRequest; //postUpdateRequest

const { status, data } = await apiInstance.updatePostUsingPOST(
    postUpdateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **postUpdateRequest** | **PostUpdateRequest**| postUpdateRequest | |


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


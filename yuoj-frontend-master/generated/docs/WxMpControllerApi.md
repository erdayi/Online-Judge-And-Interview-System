# WxMpControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**checkUsingGET**](#checkusingget) | **GET** /api/ | check|
|[**receiveMessageUsingPOST**](#receivemessageusingpost) | **POST** /api/ | receiveMessage|
|[**setMenuUsingGET**](#setmenuusingget) | **GET** /api/setMenu | setMenu|

# **checkUsingGET**
> string checkUsingGET()


### Example

```typescript
import {
    WxMpControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new WxMpControllerApi(configuration);

let echostr: string; //echostr (optional) (default to undefined)
let nonce: string; //nonce (optional) (default to undefined)
let signature: string; //signature (optional) (default to undefined)
let timestamp: string; //timestamp (optional) (default to undefined)

const { status, data } = await apiInstance.checkUsingGET(
    echostr,
    nonce,
    signature,
    timestamp
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **echostr** | [**string**] | echostr | (optional) defaults to undefined|
| **nonce** | [**string**] | nonce | (optional) defaults to undefined|
| **signature** | [**string**] | signature | (optional) defaults to undefined|
| **timestamp** | [**string**] | timestamp | (optional) defaults to undefined|


### Return type

**string**

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

# **receiveMessageUsingPOST**
> receiveMessageUsingPOST()


### Example

```typescript
import {
    WxMpControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new WxMpControllerApi(configuration);

const { status, data } = await apiInstance.receiveMessageUsingPOST();
```

### Parameters
This endpoint does not have any parameters.


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |
|**201** | Created |  -  |
|**401** | Unauthorized |  -  |
|**403** | Forbidden |  -  |
|**404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **setMenuUsingGET**
> string setMenuUsingGET()


### Example

```typescript
import {
    WxMpControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new WxMpControllerApi(configuration);

const { status, data } = await apiInstance.setMenuUsingGET();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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


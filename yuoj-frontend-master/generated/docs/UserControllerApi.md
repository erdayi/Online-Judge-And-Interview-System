# UserControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addUserUsingPOST**](#adduserusingpost) | **POST** /api/user/add | addUser|
|[**deleteUserUsingPOST**](#deleteuserusingpost) | **POST** /api/user/delete | deleteUser|
|[**getLoginUserUsingGET**](#getloginuserusingget) | **GET** /api/user/get/login | getLoginUser|
|[**getUserByIdUsingGET**](#getuserbyidusingget) | **GET** /api/user/get | getUserById|
|[**getUserVOByIdUsingGET**](#getuservobyidusingget) | **GET** /api/user/get/vo | getUserVOById|
|[**listUserByPageUsingPOST**](#listuserbypageusingpost) | **POST** /api/user/list/page | listUserByPage|
|[**listUserVOByPageUsingPOST**](#listuservobypageusingpost) | **POST** /api/user/list/page/vo | listUserVOByPage|
|[**updateMyUserUsingPOST**](#updatemyuserusingpost) | **POST** /api/user/update/my | updateMyUser|
|[**updateUserUsingPOST**](#updateuserusingpost) | **POST** /api/user/update | updateUser|
|[**userLoginByWxOpenUsingGET**](#userloginbywxopenusingget) | **GET** /api/user/login/wx_open | userLoginByWxOpen|
|[**userLoginUsingPOST**](#userloginusingpost) | **POST** /api/user/login | userLogin|
|[**userLogoutUsingPOST**](#userlogoutusingpost) | **POST** /api/user/logout | userLogout|
|[**userRegisterUsingPOST**](#userregisterusingpost) | **POST** /api/user/register | userRegister|

# **addUserUsingPOST**
> BaseResponselong addUserUsingPOST(userAddRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserAddRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userAddRequest: UserAddRequest; //userAddRequest

const { status, data } = await apiInstance.addUserUsingPOST(
    userAddRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userAddRequest** | **UserAddRequest**| userAddRequest | |


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

# **deleteUserUsingPOST**
> BaseResponseboolean deleteUserUsingPOST(deleteRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    DeleteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let deleteRequest: DeleteRequest; //deleteRequest

const { status, data } = await apiInstance.deleteUserUsingPOST(
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

# **getLoginUserUsingGET**
> BaseResponseLoginUserVO getLoginUserUsingGET()


### Example

```typescript
import {
    UserControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

const { status, data } = await apiInstance.getLoginUserUsingGET();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**BaseResponseLoginUserVO**

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

# **getUserByIdUsingGET**
> BaseResponseUser getUserByIdUsingGET()


### Example

```typescript
import {
    UserControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let id: number; //id (optional) (default to undefined)

const { status, data } = await apiInstance.getUserByIdUsingGET(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | id | (optional) defaults to undefined|


### Return type

**BaseResponseUser**

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

# **getUserVOByIdUsingGET**
> BaseResponseUserVO getUserVOByIdUsingGET()


### Example

```typescript
import {
    UserControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let id: number; //id (optional) (default to undefined)

const { status, data } = await apiInstance.getUserVOByIdUsingGET(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | id | (optional) defaults to undefined|


### Return type

**BaseResponseUserVO**

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

# **listUserByPageUsingPOST**
> BaseResponsePageUser listUserByPageUsingPOST(userQueryRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userQueryRequest: UserQueryRequest; //userQueryRequest

const { status, data } = await apiInstance.listUserByPageUsingPOST(
    userQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userQueryRequest** | **UserQueryRequest**| userQueryRequest | |


### Return type

**BaseResponsePageUser**

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

# **listUserVOByPageUsingPOST**
> BaseResponsePageUserVO listUserVOByPageUsingPOST(userQueryRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserQueryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userQueryRequest: UserQueryRequest; //userQueryRequest

const { status, data } = await apiInstance.listUserVOByPageUsingPOST(
    userQueryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userQueryRequest** | **UserQueryRequest**| userQueryRequest | |


### Return type

**BaseResponsePageUserVO**

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

# **updateMyUserUsingPOST**
> BaseResponseboolean updateMyUserUsingPOST(userUpdateMyRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserUpdateMyRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userUpdateMyRequest: UserUpdateMyRequest; //userUpdateMyRequest

const { status, data } = await apiInstance.updateMyUserUsingPOST(
    userUpdateMyRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userUpdateMyRequest** | **UserUpdateMyRequest**| userUpdateMyRequest | |


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

# **updateUserUsingPOST**
> BaseResponseboolean updateUserUsingPOST(userUpdateRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserUpdateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userUpdateRequest: UserUpdateRequest; //userUpdateRequest

const { status, data } = await apiInstance.updateUserUsingPOST(
    userUpdateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userUpdateRequest** | **UserUpdateRequest**| userUpdateRequest | |


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

# **userLoginByWxOpenUsingGET**
> BaseResponseLoginUserVO userLoginByWxOpenUsingGET()


### Example

```typescript
import {
    UserControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let code: string; //code (default to undefined)

const { status, data } = await apiInstance.userLoginByWxOpenUsingGET(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] | code | defaults to undefined|


### Return type

**BaseResponseLoginUserVO**

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

# **userLoginUsingPOST**
> BaseResponseLoginUserVO userLoginUsingPOST(userLoginRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserLoginRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userLoginRequest: UserLoginRequest; //userLoginRequest

const { status, data } = await apiInstance.userLoginUsingPOST(
    userLoginRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userLoginRequest** | **UserLoginRequest**| userLoginRequest | |


### Return type

**BaseResponseLoginUserVO**

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

# **userLogoutUsingPOST**
> BaseResponseboolean userLogoutUsingPOST()


### Example

```typescript
import {
    UserControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

const { status, data } = await apiInstance.userLogoutUsingPOST();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**BaseResponseboolean**

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

# **userRegisterUsingPOST**
> BaseResponselong userRegisterUsingPOST(userRegisterRequest)


### Example

```typescript
import {
    UserControllerApi,
    Configuration,
    UserRegisterRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new UserControllerApi(configuration);

let userRegisterRequest: UserRegisterRequest; //userRegisterRequest

const { status, data } = await apiInstance.userRegisterUsingPOST(
    userRegisterRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userRegisterRequest** | **UserRegisterRequest**| userRegisterRequest | |


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


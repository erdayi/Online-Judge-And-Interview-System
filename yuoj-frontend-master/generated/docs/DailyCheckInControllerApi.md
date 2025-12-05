# DailyCheckInControllerApi

All URIs are relative to *http://localhost:8121*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**checkInUsingPOST**](#checkinusingpost) | **POST** /api/daily_check_in/check_in | checkIn|
|[**getCheckInRecordUsingGET**](#getcheckinrecordusingget) | **GET** /api/daily_check_in/record | getCheckInRecord|
|[**getMakeupCardCountUsingGET**](#getmakeupcardcountusingget) | **GET** /api/daily_check_in/makeup_card_count | getMakeupCardCount|
|[**getTodayQuestionUsingGET**](#gettodayquestionusingget) | **GET** /api/daily_check_in/today_question | getTodayQuestion|
|[**makeupCheckInUsingPOST**](#makeupcheckinusingpost) | **POST** /api/daily_check_in/makeup | makeupCheckIn|

# **checkInUsingPOST**
> BaseResponseboolean checkInUsingPOST()


### Example

```typescript
import {
    DailyCheckInControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DailyCheckInControllerApi(configuration);

let questionId: number; //questionId (default to undefined)

const { status, data } = await apiInstance.checkInUsingPOST(
    questionId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **questionId** | [**number**] | questionId | defaults to undefined|


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

# **getCheckInRecordUsingGET**
> BaseResponseMapstringint getCheckInRecordUsingGET()


### Example

```typescript
import {
    DailyCheckInControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DailyCheckInControllerApi(configuration);

let month: number; //month (optional) (default to undefined)
let year: number; //year (optional) (default to undefined)

const { status, data } = await apiInstance.getCheckInRecordUsingGET(
    month,
    year
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **month** | [**number**] | month | (optional) defaults to undefined|
| **year** | [**number**] | year | (optional) defaults to undefined|


### Return type

**BaseResponseMapstringint**

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

# **getMakeupCardCountUsingGET**
> BaseResponseint getMakeupCardCountUsingGET()


### Example

```typescript
import {
    DailyCheckInControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DailyCheckInControllerApi(configuration);

const { status, data } = await apiInstance.getMakeupCardCountUsingGET();
```

### Parameters
This endpoint does not have any parameters.


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
|**401** | Unauthorized |  -  |
|**403** | Forbidden |  -  |
|**404** | Not Found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getTodayQuestionUsingGET**
> BaseResponselong getTodayQuestionUsingGET()


### Example

```typescript
import {
    DailyCheckInControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DailyCheckInControllerApi(configuration);

const { status, data } = await apiInstance.getTodayQuestionUsingGET();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**BaseResponselong**

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

# **makeupCheckInUsingPOST**
> BaseResponseboolean makeupCheckInUsingPOST()


### Example

```typescript
import {
    DailyCheckInControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DailyCheckInControllerApi(configuration);

let checkInDate: string; //checkInDate (default to undefined)

const { status, data } = await apiInstance.makeupCheckInUsingPOST(
    checkInDate
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **checkInDate** | [**string**] | checkInDate | defaults to undefined|


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


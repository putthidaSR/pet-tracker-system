<a name="top"></a>
# Acme project v0.0.0

REST Api

# Table of contents

- [LocationProvider](#LocationProvider)
  - [Request latest location information](#Request-latest-location-information)
  - [Request locations information](#Request-locations-information)
- [PetRegistration](#PetRegistration)
  - [Add Pet information](#Add-Pet-information)
  - [Delete Pet information](#Delete-Pet-information)
  - [Delete Pets information](#Delete-Pets-information)
  - [Request Pet information](#Request-Pet-information)
  - [Update Pet information](#Update-Pet-information)
- [UserRegistration](#UserRegistration)
  - [Add User information](#Add-User-information)
  - [Delete User information](#Delete-User-information)
  - [Delete Users information](#Delete-Users-information)
  - [Request User information](#Request-User-information)
  - [Update User information](#Update-User-information)

___


# <a name='LocationProvider'></a> LocationProvider

## <a name='Request-latest-location-information'></a> Request latest location information
[Back to top](#top)

```
GET /pets/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Pets unique ID.</p> |

### Success response

#### Success response - `Success 200`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Location record unique ID.</p> |
| rfidNumber | `String` | <p>Related rfidNumber unique ID.</p> |
| longitude | `Number` | <p>Pet longitude.</p> |
| latitude | `Number` | <p>Pet latitude.</p> |
| createTime | `Number` | <p>Location create time.</p> |
| active | `String` | <p>Location data status.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
{
    "id": 0,
    "rfidNumber": "1020391293",
    "longitude": 12.923432,
    "latitude": 5.66666,
    "createTime": 1293811200000,
    "active": "Y"
}
```

### Error response

#### Error response - `Error 404`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| UserNotFound |  | <p>The <code>id</code> of the Pet was not found.</p> |

## <a name='Request-locations-information'></a> Request locations information
[Back to top](#top)

```
GET /pets/:id/locations?currentPage=:currentPage&amp;pageSize=:pageSize
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Pets unique ID.</p> |
| currentPage | `Number` | <p>current page.</p> |
| pageSize | `Number` | <p>page size.</p> |

### Success response

#### Success response - `Success 200`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Location record unique ID.</p> |
| rfidNumber | `String` | <p>Related rfidNumber unique ID.</p> |
| longitude | `Number` | <p>Pet longitude.</p> |
| latitude | `Number` | <p>Pet latitude.</p> |
| createTime | `Number` | <p>Location create time.</p> |
| active | `String` | <p>Location data status.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
[
	{
    	"id": 0,
    	"rfidNumber": "1020391293",
    	"longitude": 12.923432,
    	"latitude": 5.66666,
    	"createTime": 1293811200000,
    	"active": "Y"
	}
	{
    "id": 1,
    "rfidNumber": "1020391293",
    "longitude": 13.923432,
    "latitude": 4.66666,
    "createTime": 1293811200001,
    "active": "Y"
	}
]
```

### Error response

#### Error response - `Error 404`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| UserNotFound |  | <p>The <code>id</code> of the Pet was not found.</p> |

# <a name='PetRegistration'></a> PetRegistration

## <a name='Add-Pet-information'></a> Add Pet information
[Back to top](#top)

```
POST /pets
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| userId | `Number` | <p>Related user unique ID.</p> |
| rfidNumber | `String` | <p>Pet rfid number.</p> |
| name | `String` | <p>Pet name.</p> |
| category | `String` | <p>Pet category.</p> |
| year | `Number` | <p>Pet category.</p> |

### Parameters examples
`json` - Request-Example:

```json
{
    "userId": 1,
    "rfidNumber": "0000000",
    "name": "john",
    "category": "test",
    "year": 1
}
```

### Success response

#### Success response - `Success 200`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
    HTTP/1.1 200 OK
	{
	    "id": 1
	}
```

## <a name='Delete-Pet-information'></a> Delete Pet information
[Back to top](#top)

```
DELETE /pets/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Pet unique ID.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
```

## <a name='Delete-Pets-information'></a> Delete Pets information
[Back to top](#top)

```
DELETE /pets
```

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
```

## <a name='Request-Pet-information'></a> Request Pet information
[Back to top](#top)

```
GET /pets/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |

### Success response

#### Success response - `Success 200`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Pet unique ID.</p> |
| userId | `Number` | <p>Related user unique ID.</p> |
| rfidNumber | `String` | <p>Pet rfid number.</p> |
| name | `String` | <p>Pet name.</p> |
| category | `String` | <p>Pet category.</p> |
| year | `Number` | <p>Pet category.</p> |
| createTime | `Number` | <p>Pet create time.</p> |
| modifyTime | `Number` | <p>Pet modify time.</p> |
| active | `String` | <p>Pet data status.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
{
    "id": 0,
    "userId": 1,
    "rfidNumber": "0000000",
    "name": "john",
    "category": "test",
    "year": 1,
    "createTime": 1293811200000,
    "modifyTime": null,
    "active": "Y"
}
```

### Error response

#### Error response - `Error 404`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| UserNotFound |  | <p>The <code>id</code> of the User was not found.</p> |

## <a name='Update-Pet-information'></a> Update Pet information
[Back to top](#top)

```
PUT /pets/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| userId | `Number` | <p>Related user unique ID.</p> |
| rfidNumber | `String` | <p>Pet rfid number.</p> |
| name | `String` | <p>Pet name.</p> |
| category | `String` | <p>Pet category.</p> |
| year | `Number` | <p>Pet category.</p> |

### Parameters examples
`json` - Request-Example:

```json
{
    "userId": 1,
    "rfidNumber": "0000000",
    "name": "john",
    "category": "test",
    "year": 1
}
```

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
```

# <a name='UserRegistration'></a> UserRegistration

## <a name='Add-User-information'></a> Add User information
[Back to top](#top)

```
POST /users
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| loginName | `String` | <p>User login name.</p> |
| loginPassword | `String` | <p>User login password.</p> |
| address | `String` | <p>User address.</p> |
| contact | `String` | <p>User contact.</p> |

### Parameters examples
`json` - Request-Example:

```json
 {
	   "loginName": "test",
	   "loginPassword": "test",
	   "address": "test",
	   "contact": "test",
 }
```

### Success response

#### Success response - `Success 200`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
    HTTP/1.1 200 OK
	{
	    "id": 1
	}
```

## <a name='Delete-User-information'></a> Delete User information
[Back to top](#top)

```
DELETE /users/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
```

## <a name='Delete-Users-information'></a> Delete Users information
[Back to top](#top)

```
DELETE /users
```

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
```

## <a name='Request-User-information'></a> Request User information
[Back to top](#top)

```
GET /users/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |

### Success response

#### Success response - `Success 200`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |
| address | `String` | <p>User address.</p> |
| contact | `String` | <p>User contact.</p> |
| createTime | `Number` | <p>User create time.</p> |
| modifyTime | `Number` | <p>User modify time.</p> |

### Success response example

#### Success response example - `Success-Response:`

```json
    HTTP/1.1 200 OK
	{
	    "id": 1,
	    "address": "123456",
	    "contact": "1",
	    "createTime": 1638633710000,
	    "modifyTime": 1638633710000
	}
```

### Error response

#### Error response - `Error 404`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| UserNotFound |  | <p>The <code>id</code> of the User was not found.</p> |

## <a name='Update-User-information'></a> Update User information
[Back to top](#top)

```
PUT /users/:id
```

### Parameters - `Parameter`

| Name     | Type       | Description                           |
|----------|------------|---------------------------------------|
| id | `Number` | <p>Users unique ID.</p> |
| address | `String` | <p>User address.</p> |
| contact | `String` | <p>User contact.</p> |

### Parameters examples
`json` - Request-Example:

```json
{
  "address": "4711"
  "contact": "4711"
}
```

### Success response example

#### Success response example - `Success-Response:`

```json
HTTP/1.1 200 OK
```


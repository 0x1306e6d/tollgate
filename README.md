# tollgate

![Build master](https://github.com/ghkim3221/tollgate/workflows/Build%20master/badge.svg)
![Build pull request](https://github.com/ghkim3221/tollgate/workflows/Build%20pull%20request/badge.svg)

Tollgate is an application-level API gateway for your microservice. You can expose your microservice to the 
world by just deploying and configuring. We can do other basic thing like logging and will do authentication, 
authorization, monitoring, reconstructing request (response) body and sanitizing response status code.

The primary goal of Tollgate is to minimize coding and replace it with settings. To do it, we support 
file-based configuration and will support dynamic configurations via CentralDogma or something else.

Tollgate is running based on the [Armeria](https://armeria.dev). You can use the Armeria's decorator when need 
some customizing. Then register your decorator and use it in settings.

## Getting Started

**TBW**

## Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `port` | `int` | `required` | Port that server listens on | |
| `healthCheckPath` | `string` | `optional` | An endpoint for health check service | Default: `/health` |
| `endpoints` | `list` | `required` | A list of [Endpoint Configuration](#endpoint-configuration) | |

#### Configuration Example

```json
{
  "port": 8080,
  "healthCheckPath": "/health",
  "endpoints": [
    {
      "method": "GET",
      "path": "/api/v2/berry/{idOrName}",
      "upstream": {
        "service": {
          "uri": "http://berry:8080"
        },
        "endpoint": {
          "method": "GET",
          "path": "/berry/{idOrName}"
        }
      }
    }
  ]
}
```

### Endpoint Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `method` | `string` | `required` | HTTP method which this endpoint expose as | |
| `path` | `string` | `required` | URI path which this endpoint expose to | |
| `upstream` | `object` | `required` | A [Upstream Configuration](#upstream-configuration) to proxy requests from this endpoint | |

#### Endpoint Configuration Example

```json
{
  "method": "GET",
  "path": "/api/v2/berry/{idOrName}",
  "upstream": {
    "service": {
      "uri": "http://berry:8080"
    },
    "endpoint": {
      "method": "GET",
      "path": "/berry/{idOrName}"
    }
  }
}
```

### Upstream Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `service` | `object` | `required` | A [Service Configuration](#service-configuration) on your microservice | |
| `endpoint` | `object` | `required` | A [Upstream Endpoint Configuration](#upstream-endpoint-configuration) which represents an endpoint of upstream to proxy | |

#### Upstream Configuration

```json
{
  "service": {
    "uri": "http://berry:8080"
  },
  "endpoint": {
    "method": "GET",
    "path": "/berry/{idOrName}"
  }
}
```

### Service Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `scheme` | `string` | `optional` | A scheme of an URI of the service | |
| `authorities` | `list` | `optional` | A list of [Authority Configuration](#authority-configuration) of an URI of the service | |
| `uri` | `string` | `optional` | An URI of the service | |

> **Note** One of `scheme` and `authorities` pair and `uri` MUST be required.

#### Service Configuration Example

 - Using `scheme` and `authorities` pair
 
    ```json
    {
     "scheme": "http",
     "authorities": [
       {
         "host": "berry",
         "port": 8080
       }
     ]
   }
   ```
 
 - Using `uri`

   ```json
   {
     "uri": "http://berry:8080"
   }
   ```

### Authority Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `host` | `string` | `requried` | A host of an URI | |
| `port` | `int` | `required` | A port of an URI | |

#### Authority Configuration Example

```json
{
  "host": "berry",
  "port": 8080
}
```

### Upstream Endpoint Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `method` | `string` | `required` | HTTP method to use when proxying requests | |
| `path` | `string` | `required` | URI path to use when proxying requests | |

#### Upstream Endpoint Configuration Example

```json
{
  "method": "GET",
  "path": "/berry/{idOrName}"
}
```

## License

```
MIT License

Copyright (c) 2020 Gihwan Kim

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

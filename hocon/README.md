# HOCON

An extension to build `Gateway` using a HOCON (Human-Optimized Config Object Notation) configuration.

## Getting Started

It provides `HoconGatewayBuilder` which is a `Gateway` builder using
a [lightbend/config](https://github.com/lightbend/config):

```java
final Config config = ...;
final Gateway gateway = HoconGatewayBuilder.of().build(config);
```

If you want to customize `Gateway` in your code, pass `GatewayBuilder` as parameter:

```java
final GatewayBuilder builder = ...; // your own builder

final Config config = ...;
final Gateway gateway = HoconGatewayBuilder.of(builder).build(config);
```

If you want to customize configuration format, set your own `HoconGatewayConfigurator`:

```java
final Config config = ...;
final HoconGatewayConfigurator configurator = new HoconGatewayConfigurator() {
    @Override
    public void configure(GatewayBuilder builder, Config config) {
        ...
    }
};
final Gateway gateway = HoconGatewayBuilder.of()
                                           .gatewayConfigurator(configurator)
                                           .build(config);
```

If not set, [default configuration format](#default-configuration-format) is used by default.

## Default Configuration Format

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `port` | `int` | `required` | Port that server listens on | |
| `healthCheckPath` | `string` | `optional` | An endpoint for health check service | Default: `/health` |
| `routing` | `object` | `required` | A map of [Routing Configuration](#routing-configuration) | |

#### Configuration Example

```hocon
tollgate {
  port = 8080
  healthCheckPath = "/health"
  routing {
    getBerry {
      method = "GET"
      path = "/api/v2/berry/{idOrName}"
      logging {
        logger = "BerryLogger"
        requestLogLevel = "INFO"
        successfulResponseLogLevel = "INFO"
        failureResponseLogLevel = "WARN"
      }
      upstream {
        uri = "http://berry:8080"
        logging {
          logger = "BerryLogger"
          requestLogLevel = "INFO"
          successfulResponseLogLevel = "INFO"
          failureResponseLogLevel = "WARN"
        }
      }
    }
  }
}
```

### Routing Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `method` | `string` | `required` | HTTP method which this endpoint expose as | |
| `path` | `string` | `required` | URI path which this endpoint expose to | |
| `logging` | `object` | `optional` | A [Logging Configuration](#logging-configuration) to log requests and responses from this endpoint | |
| `upstream` | `object` | `required` | A [Upstream Configuration](#upstream-configuration) to proxy requests from this endpoint | |

### Upstream Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `uri` | `string` | `optional` | An URI of the upstream | |
| `scheme` | `string` | `optional` | A scheme of an URI of the upstream | |
| `path` | `string` | `optional` | Request path to the upstream | |
| `status` | `list` | `optional` | List of [Status Function Configuration](#status-function-configuration) to apply to response status from the upstream | |
| `endpoints` | `list` | `optional` | A list of [Endpoint Configuration](#endpoint-configuration) of an URI of the upstream | |
| `remapping` | `object` | `optional` | A [Remapping Upstream Configuration](#remapping-upstream-configuration) to remap request or response | |
| `logging` | `object` | `optional` | A [Logging Configuration](#logging-configuration) to log requests and responses to the upstream | |

> **Note** One of `uri` or `scheme` and `endpoints` pair MUST be required.

### Status Function Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `from` | `list` | `required` | List of HTTP status (code) to transform | |
| `to` | `string` or `int` | `required` | Transformed HTTP status (code) | |

### Endpoint Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `host` | `string` | `requried` | A host of the endpoint | |
| `port` | `int` | `required` | A port of the endpoint | |

### Remapping Upstream Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `path` | `string` | `optional` | Remaps request path | |

### Logging Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `logger` | `string` | `optional` | Name to use when logging. | |
| `requestLogLevel` | `string` | `optional` | `LogLevel` to use when logging requests. | If unset, will use `DEBUG`. |
| `successfulResponseLogLevel` | `string` | `optional` | `LogLevel` to use when logging successful responses (e.g., no unhandled exception). | If unset, will use `DEBUG`. |
| `failureResponseLogLevel` | `string` | `optional` | `LogLevel` to use when logging failure responses (e.g., failed with an exception). | It unset, will use `WARN`. |
| `samplingRate` | `double` | `optional` | Rate to sample requests to log. | |

> **Note** The `LogLevel` should be one of `OFF`, `TRACE`, `DEBUG`, `INFO`, `WARN` and `ERROR`.

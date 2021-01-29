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
      upstream {
        uri = "http://berry:8080"
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
| `upstream` | `object` | `required` | A [Upstream Configuration](#upstream-configuration) to proxy requests from this endpoint | |

### Upstream Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `uri` | `string` | `optional` | An URI of the upstream | |
| `scheme` | `string` | `optional` | A scheme of an URI of the upstream | |
| `endpoints` | `list` | `optional` | A list of [Endpoint Configuration](#endpoint-configuration) of an URI of the upstream | |
| `remapping` | `object` | `optional` | A [Remapping Upstream Configuration](#remapping-upstream-configuration) to remap request or response | |

> **Note** One of `uri` or `scheme` and `endpoints` pair MUST be required.

### Endpoint Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `host` | `string` | `requried` | A host of the endpoint | |
| `port` | `int` | `required` | A port of the endpoint | |

### Remapping Upstream Configuration

| Name | Type | Mandatory | Description | Note |
|------|------|-----------|-------------|------|
| `path` | `string` | `optional` | Remaps request path | |

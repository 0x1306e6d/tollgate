# exception-mapping

A `HttpClient` decorator which maps a `Throwable` which is thrown while forwarding to an appropriate 
`HttpResponse`.

## Getting Started

It provides `ExceptionMappingClient` and `ExceptionMappingFunction`:

```java
// use default mapping function
Upstream.builder("http://10.0.1.1")
        .decorator(ExceptionMappingClient.newDecorator())
        .build();

// use customized mapping function
Upstream.builder("http://10.0.1.1")
        .decorator(ExceptionMappingClient.newDecorator(cause -> {
            ... // maps the given cause to a HttpResponse
        }))
```

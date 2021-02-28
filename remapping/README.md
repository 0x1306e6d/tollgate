# remapping

A `HttpClient` decorator which remaps `HttpRequest` or `HttpResponse`.

## Getting Started

It provides `RemappingClient`, `RemappingRequestStrategy` and `RemappingResponseStrategy`:

```java
Upstream.of("http://10.0.1.1")
        .decorate(RemappingClient.newDecorator(
            new RemappingRequestStrategy() {
                @Override
                public HttpRequest remap(ClientRequestContext ctx, HttpRequest req) {
                    ... // remaps HTTP request to upstream server
                }
            }, new RemappingResponseStrategy() {
                @Override
                public HttpResponse remap(ClientRequestContext ctx, HttpResponse res) {
                    ... // remaps HTTP response from upstream server
                }
            }));
```

Or you can use builder:

```java
Upstream.of("http://10.0.1.1")
        .decorate(RemappingClient.builder()
                                 .requestStrategy(new RemappingRequestStrategy() {
                                     @Override
                                     public HttpRequest remap(ClientRequestContext ctx, HttpRequest req) {
                                         ... // remaps HTTP request to upstream server
                                     }
                                 })
                                 .responseStrategy(new RemappingResponseStrategy() {
                                     @Override
                                     public HttpResponse remap(ClientRequestContext ctx, HttpResponse res) {
                                         ... // remaps HTTP response from upstream server
                                     }
                                 })
                                 .newDecorator());
```

It also provides useful implementation:

```java
Upstream.of("http://10.0.1.1")
        .decorate(RemappingClient.builder()
                                 .requestPath("/foo/{bar}") // automatically remaps URI path to `/foo/{bar}`, `bar` is path parameter
                                 .newDecorator());
```

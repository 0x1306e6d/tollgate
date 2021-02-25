# tollgate

![Build master](https://github.com/ghkim3221/tollgate/workflows/Build%20master/badge.svg)
![Build pull request](https://github.com/ghkim3221/tollgate/workflows/Build%20pull%20request/badge.svg)

Tollgate is an application-level API gateway for your microservice.

## Getting Started

```java
final Gateway gateway = Gateway.builder()
                               .http(8080)
                               .upstream("/foo", Upstream.of("http://10.0.1.1"))
                               .upstream("/bar", Upstream.of("http://10.0.1.2"))
                               .build();
gateway.start().join();
```

- `Gateway` is listening on HTTP 8080.
- All requests routing to `/foo` will be forwarded to `http://10.0.1.1`.
- All requests routing to `/bar` will be forwarded to `http://10.0.1.2`.

## Features

- [exception-mapping](/exception-mapping)
    - Maps a `Throwable` which is thrown while forwarding to an appropriate `HttpResponse`.
- [HOCON](/hocon)
    - Builds `Gateway` using a HOCON (Human-Optimized Config Object Notation) configuration.
- [remapping](/remapping)
    - Remaps `HttpRequest` or `HttpResponse`.

## License

```
MIT License

Copyright (c) 2020 - 2021 Gihwan Kim

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

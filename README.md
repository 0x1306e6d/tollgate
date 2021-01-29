# tollgate

![Build master](https://github.com/ghkim3221/tollgate/workflows/Build%20master/badge.svg)
![Build pull request](https://github.com/ghkim3221/tollgate/workflows/Build%20pull%20request/badge.svg)

Tollgate is an application-level API gateway for your microservice. You can expose your microservice to the
world by just deploying and configuring. We can do other basic thing like logging and will do authentication,
authorization, monitoring, reconstructing request (response) body and sanitizing response status code.

The primary goal of Tollgate is to minimize coding and replace it with settings. To do it, we support file-based
configuration and will support dynamic configurations via CentralDogma or something else.

Tollgate is running based on the [Armeria](https://armeria.dev). You can use the Armeria's decorator when need
some customizing. Then register your decorator and use it in settings.

## Getting Started

**TBW**

## Extensions

- [HOCON](/hocon)
    - to build `Gateway` using a HOCON (Human-Optimized Config Object Notation) configuration.

## How to Build

**TBW**

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

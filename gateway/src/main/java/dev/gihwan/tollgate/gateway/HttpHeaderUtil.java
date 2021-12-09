/*
 * MIT License
 *
 * Copyright (c) 2020 - 2021 Gihwan Kim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.gihwan.tollgate.gateway;

import java.util.Set;

import com.linecorp.armeria.common.HttpHeaderNames;

import io.netty.util.AsciiString;

final class HttpHeaderUtil {

    // https://datatracker.ietf.org/doc/html/rfc7540#section-8.1.2.3
    private static final Set<AsciiString> REQUEST_PSEUDO_HEADERS = Set.of(HttpHeaderNames.METHOD,
                                                                          HttpHeaderNames.SCHEME,
                                                                          HttpHeaderNames.AUTHORITY,
                                                                          HttpHeaderNames.PATH);

    // https://datatracker.ietf.org/doc/html/rfc7540#section-8.1.2.4
    private static final Set<AsciiString> RESPONSE_PSEUDO_HEADERS = Set.of(HttpHeaderNames.STATUS);

    static boolean isRequestPseudoHeader(AsciiString name) {
        return REQUEST_PSEUDO_HEADERS.contains(name);
    }

    static boolean isResponsePseudoHeader(AsciiString name) {
        return RESPONSE_PSEUDO_HEADERS.contains(name);
    }

    private HttpHeaderUtil() {}
}

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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.common.ResponseHeaders;

class RemappingStatusFunctionTest {
    @Test
    void remapStatus() {
        final RemappingStatusFunction function =
                new RemappingStatusFunction(HttpStatusFunction.when(HttpStatus::isSuccess)
                                                              .to(HttpStatus.OK));

        final HttpResponse res = HttpResponse.of(HttpStatus.CREATED);
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        assertThat(applied.status()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldNotChangeExceptStatus() {
        final RemappingStatusFunction function =
                new RemappingStatusFunction(HttpStatusFunction.when(HttpStatus::isSuccess)
                                                              .to(HttpStatus.OK));

        final HttpResponse res = HttpResponse.of(ResponseHeaders.builder(HttpStatus.CREATED)
                                                                .contentType(MediaType.PLAIN_TEXT)
                                                                .contentLength(13)
                                                                .add("foo", "this is foo")
                                                                .add("bar", "this is bar")
                                                                .build(),
                                                 HttpData.ofUtf8("Hello, World!"));
        final AggregatedHttpResponse applied = function.apply(res).aggregate().join();
        assertThat(applied.status()).isEqualTo(HttpStatus.OK);
        assertThat(applied.contentType()).isEqualTo(MediaType.PLAIN_TEXT);
        assertThat(applied.contentUtf8()).isEqualTo("Hello, World!");
        assertThat(applied.headers().contentLength()).isEqualTo(13);
        assertThat(applied.headers().get("foo")).isEqualTo("this is foo");
        assertThat(applied.headers().get("bar")).isEqualTo("this is bar");
    }
}

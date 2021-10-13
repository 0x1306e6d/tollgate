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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.linecorp.armeria.common.HttpStatus;

class HttpStatusNamesTest {
    @ParameterizedTest
    @CsvSource({
            "CONTINUE,100",
            "OK,200",
            "MULTIPLE_CHOICES,300",
            "BAD_REQUEST,400",
            "INTERNAL_SERVER_ERROR,500",
    })
    void of(String name, int statusCode) {
        final HttpStatus status = HttpStatusNames.of(name);
        assertThat(status.code()).isEqualTo(statusCode);
    }

    @Test
    void ofWithUnknownNameShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> HttpStatusNames.of("UNKNOWN"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "CONTINUE,100",
            "OK,200",
            "MULTIPLE_CHOICES,300",
            "BAD_REQUEST,400",
            "INTERNAL_SERVER_ERROR,500",
    })
    void ofNullable(String name, int statusCode) {
        final HttpStatus status = HttpStatusNames.ofNullable(name);
        assertThat(status).isNotNull();
        assertThat(status.code()).isEqualTo(statusCode);
    }

    @Test
    void ofNullableWithUnknownNameShouldReturnNull() {
        assertThat(HttpStatusNames.ofNullable("UNKNOWN")).isNull();
    }
}

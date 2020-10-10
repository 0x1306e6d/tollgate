/*
 * MIT License
 *
 * Copyright (c) 2020 Gihwan Kim
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

package dev.gihwan.tollgate.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.Scheme;

@SuppressWarnings("ConstantConditions")
class ServiceConfigTest {

    @Test
    void ofUri() {
        assertThatThrownBy(() -> ServiceConfig.of(null))
                .isInstanceOf(NullPointerException.class);

        final ServiceConfig config = ServiceConfig.of("http://example.com");
        assertThat(config.uri()).isEqualTo("http://example.com");
        assertThat(config.scheme()).isNull();
        assertThat(config.authorities()).isNull();
    }

    @Test
    void ofSchemeAndAuthorities() {
        assertThatThrownBy(() -> ServiceConfig.of(null, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ServiceConfig.of("http", null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> ServiceConfig.of("http", Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ServiceConfig.of(null, List.of(Authority.of("127.0.0.1", 8080))))
                .isInstanceOf(NullPointerException.class);

        final ServiceConfig config = ServiceConfig.of("http",
                                                      List.of(Authority.of("127.0.0.1", 8080),
                                                              Authority.of("127.0.0.1", 8081)));
        assertThat(config.uri()).isNull();
        assertThat(config.scheme()).isEqualTo(Scheme.parse("http"));
        assertThat(config.authorities())
                .contains(Authority.of("127.0.0.1", 8080), Authority.of("127.0.0.1", 8081));
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    void equalsWithUri() {
        final ServiceConfig config = ServiceConfig.of("http://example.com");

        final ServiceConfig same = ServiceConfig.of("http://example.com");
        final ServiceConfig differentUri = ServiceConfig.of("http://example.org");
        final ServiceConfig differentSchemeAndAuthorities =
                ServiceConfig.of("http", List.of(Authority.of("127.0.0.1", 8080)));

        assertThat(config.equals(same)).isTrue();
        assertThat(config.equals(differentUri)).isFalse();
        assertThat(config.equals(differentSchemeAndAuthorities)).isFalse();

        assertThat(config.equals(null)).isFalse();
        assertThat(config.equals(new Dummy())).isFalse();
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    void equalsWithSchemeAndAuthorities() {
        final ServiceConfig config = ServiceConfig.of("http", List.of(Authority.of("127.0.0.1", 8080)));

        final ServiceConfig same = ServiceConfig.of("http", List.of(Authority.of("127.0.0.1", 8080)));
        final ServiceConfig differentScheme =
                ServiceConfig.of("https", List.of(Authority.of("127.0.0.1", 8080)));
        final ServiceConfig differentAuthorities =
                ServiceConfig.of("http", List.of(Authority.of("127.0.0.1", 8081)));
        final ServiceConfig differentUri = ServiceConfig.of("http://example.com");

        assertThat(config.equals(same)).isTrue();
        assertThat(config.equals(differentScheme)).isFalse();
        assertThat(config.equals(differentAuthorities)).isFalse();
        assertThat(config.equals(differentUri)).isFalse();

        assertThat(config.equals(null)).isFalse();
        assertThat(config.equals(new Dummy())).isFalse();
    }

    private static class Dummy {}
}

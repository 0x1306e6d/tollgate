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

package dev.gihwan.tollgate.core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

@SuppressWarnings("ConstantConditions")
class AuthorityTest {

    @Test
    void of() {
        assertThatThrownBy(() -> Authority.of(null, 0))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Authority.of("127.0.0.1", -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Authority.of("127.0.0.1", 65536))
                .isInstanceOf(IllegalArgumentException.class);

        final Authority authority = Authority.of("127.0.0.1", 8080);
        assertThat(authority.host()).isEqualTo("127.0.0.1");
        assertThat(authority.port()).isEqualTo(8080);
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    void equals() {
        final Authority authority = Authority.of("127.0.0.1", 8080);

        final Authority same = Authority.of("127.0.0.1", 8080);
        final Authority differentHost = Authority.of("127.0.0.2", 8080);
        final Authority differentPort = Authority.of("127.0.0.1", 8081);

        assertThat(authority.equals(same)).isTrue();
        assertThat(authority.equals(differentHost)).isFalse();
        assertThat(authority.equals(differentPort)).isFalse();

        assertThat(authority.equals(null)).isFalse();
        assertThat(authority.equals(new Dummy())).isFalse();
    }

    private static class Dummy {}
}

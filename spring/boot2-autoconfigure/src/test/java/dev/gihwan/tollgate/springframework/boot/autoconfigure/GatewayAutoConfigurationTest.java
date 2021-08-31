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

package dev.gihwan.tollgate.springframework.boot.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.Upstream;

class GatewayAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GatewayAutoConfiguration.class));

    @Test
    void shouldNotCreateGatewayBeanByDefault() {
        contextRunner.run(context -> {
            assertThat(context).doesNotHaveBean(Gateway.class);
            assertThat(context).doesNotHaveBean(GatewayStartStopLifecycle.class);
        });
    }

    @Test
    void createGatewayBean() {
        contextRunner.withUserConfiguration(CustomGatewayConfiguration.class)
                     .run(context -> {
                         assertThat(context).hasSingleBean(Gateway.class);
                         assertThat(context).hasSingleBean(GatewayStartStopLifecycle.class);
                     });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomGatewayConfiguration {

        @Bean
        GatewayCustomizer gatewayCustomizer() {
            return builder -> builder.upstream("/", Upstream.of("https://example.com"));
        }
    }
}

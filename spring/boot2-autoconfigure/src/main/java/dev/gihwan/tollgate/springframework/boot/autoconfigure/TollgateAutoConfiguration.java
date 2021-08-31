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

import static dev.gihwan.tollgate.springframework.boot.autoconfigure.GatewayServerConfigurationUtils.configureServer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.gihwan.tollgate.gateway.Gateway;
import dev.gihwan.tollgate.gateway.GatewayBuilder;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Tollgate.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(GatewayCustomizer.class)
@EnableConfigurationProperties(TollgateProperties.class)
public class TollgateAutoConfiguration {

    @Bean
    public Gateway gateway(TollgateProperties properties, ObjectProvider<GatewayCustomizer> customizers) {
        final GatewayBuilder builder = Gateway.builder();
        builder.server(serverBuilder -> configureServer(serverBuilder, properties.getServer()));
        customizers.forEach(customizer -> customizer.customize(builder));
        return builder.build();
    }

    @Bean
    public SmartLifecycle gatewayStartStopLifecycle(Gateway gateway) {
        return new GatewayStartStopLifecycle(gateway);
    }
}

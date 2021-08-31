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

import java.util.List;

import org.junit.jupiter.api.Test;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.ServerConfig;
import com.linecorp.armeria.server.ServerPort;

import dev.gihwan.tollgate.springframework.boot.autoconfigure.GatewayProperties.Server.Port;

class GatewayServerConfigurationUtilsTest {

    @Test
    void configurePort() {
        final GatewayProperties.Server properties = new GatewayProperties.Server();
        properties.setPorts(List.of(newPort(8080, List.of(SessionProtocol.HTTP))));

        final ServerBuilder builder = newServerBuilder();
        GatewayServerConfigurationUtils.configureServer(builder, properties);
        final Server server = builder.build();
        final ServerConfig serverConfig = server.config();

        assertThat(serverConfig.ports())
                .containsOnly(new ServerPort(8080, SessionProtocol.HTTP));
    }

    @Test
    void configurePortAndProtocols() {
        final GatewayProperties.Server properties = new GatewayProperties.Server();
        properties.setPorts(List.of(newPort(8080, List.of(SessionProtocol.HTTP, SessionProtocol.HTTPS))));

        final ServerBuilder builder = newServerBuilder().tlsSelfSigned();
        GatewayServerConfigurationUtils.configureServer(builder, properties);
        final Server server = builder.build();
        final ServerConfig serverConfig = server.config();

        assertThat(serverConfig.ports())
                .containsOnly(new ServerPort(8080, SessionProtocol.HTTP, SessionProtocol.HTTPS));
    }

    @Test
    void configurePorts() {
        final GatewayProperties.Server properties = new GatewayProperties.Server();
        properties.setPorts(List.of(newPort(8080, List.of(SessionProtocol.HTTP)),
                                    newPort(8443, List.of(SessionProtocol.HTTPS))));

        final ServerBuilder builder = newServerBuilder().tlsSelfSigned();
        GatewayServerConfigurationUtils.configureServer(builder, properties);
        final Server server = builder.build();
        final ServerConfig serverConfig = server.config();

        assertThat(serverConfig.ports())
                .containsOnly(new ServerPort(8080, SessionProtocol.HTTP),
                              new ServerPort(8443, SessionProtocol.HTTPS));
    }

    @Test
    void configureDefaultPorts() {
        final GatewayProperties.Server properties = new GatewayProperties.Server();

        final ServerBuilder builder = newServerBuilder();
        GatewayServerConfigurationUtils.configureServer(builder, properties);
        final Server server = builder.build();
        final ServerConfig serverConfig = server.config();

        assertThat(serverConfig.ports())
                .containsOnly(new ServerPort(0, SessionProtocol.HTTP));
    }

    private static ServerBuilder newServerBuilder() {
        return Server.builder()
                     .service("/", (ctx, req) -> HttpResponse.of(HttpStatus.OK));
    }

    private static GatewayProperties.Server.Port newPort(int port, List<SessionProtocol> protocols) {
        final GatewayProperties.Server.Port properties = new Port();
        properties.setPort(port);
        properties.setProtocols(protocols);
        return properties;
    }
}

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

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.ServerPort;

import dev.gihwan.tollgate.gateway.Gateway;

/**
 * {@link ConfigurationProperties @ConfigurationProperties} for Tollgate.
 */
@ConfigurationProperties("tollgate")
public class TollgateProperties {

    /**
     * {@link com.linecorp.armeria.server.Server} which serves the {@link Gateway}.
     */
    private Server server = new Server();

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = requireNonNull(server, "server");
    }

    /**
     * {@link com.linecorp.armeria.server.Server} properties of {@link Gateway}.
     */
    public static class Server {

        /**
         * {@link Port}s which the server listens to.
         */
        private List<Port> ports = emptyList();

        public List<Port> getPorts() {
            return ports;
        }

        public void setPorts(List<Port> ports) {
            this.ports = requireNonNull(ports, "ports");
        }

        /**
         * {@link ServerPort} properties.
         */
        public static class Port {

            /**
             * Port which the server listens to.
             */
            private int port;

            /**
             * {@link SessionProtocol}s which the server listens using.
             * By default, {@link SessionProtocol#HTTP} is used.
             */
            private List<SessionProtocol> protocols = List.of(SessionProtocol.HTTP);

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public List<SessionProtocol> getProtocols() {
                return protocols;
            }

            public void setProtocols(List<SessionProtocol> protocols) {
                this.protocols = requireNonNull(protocols, "protocols");
            }
        }
    }
}

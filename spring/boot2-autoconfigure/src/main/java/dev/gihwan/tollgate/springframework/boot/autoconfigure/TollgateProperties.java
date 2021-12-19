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

import java.net.URI;
import java.util.List;

import javax.annotation.Nullable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.linecorp.armeria.common.Scheme;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.ServerPort;

import dev.gihwan.tollgate.gateway.Gateway;

/**
 * {@link ConfigurationProperties @ConfigurationProperties} for Tollgate.
 */
@ConfigurationProperties("tollgate.gateway")
public class TollgateProperties {

    /**
     * {@link com.linecorp.armeria.server.Server} properties of this {@link Gateway}.
     */
    private Server server = new Server();

    /**
     * Routes of this {@link Gateway}.
     */
    private List<Route> routes = emptyList();

    /**
     * Returns the {@link com.linecorp.armeria.server.Server} properties of this {@link Gateway}.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Sets the {@link com.linecorp.armeria.server.Server} properties of this {@link Gateway} as the given
     * {@link Server}.
     */
    public void setServer(Server server) {
        this.server = requireNonNull(server, "server");
    }

    /**
     * Returns the routes of this {@link Gateway}.
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Sets the routes of this {@link Gateway} as the given {@link Route}s.
     */
    public void setRoutes(List<Route> routes) {
        this.routes = requireNonNull(routes, "routes");
    }

    /**
     * {@link com.linecorp.armeria.server.Server} properties of {@link Gateway}.
     */
    public static class Server {

        /**
         * {@link Port}s which the server listens to.
         */
        private List<Port> ports = emptyList();

        /**
         * Returns the {@link Port}s which the server listens to.
         */
        public List<Port> getPorts() {
            return ports;
        }

        /**
         * Sets the {@link Port}s which the server listens to as the given {@link Port}s.
         */
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

            /**
             * Returns the port which the server listens to.
             */
            public int getPort() {
                return port;
            }

            /**
             * Sets the port which the server listens to as the given {@code port}.
             */
            public void setPort(int port) {
                this.port = port;
            }

            /**
             * Returns the {@link SessionProtocol}s which the server listens using.
             */
            public List<SessionProtocol> getProtocols() {
                return protocols;
            }

            /**
             * Sets the {@link SessionProtocol}s which the server listens using as the given
             * {@link SessionProtocol}s.
             */
            public void setProtocols(List<SessionProtocol> protocols) {
                this.protocols = requireNonNull(protocols, "protocols");
            }
        }
    }

    /**
     * Route properties.
     */
    public static class Route {

        /**
         * Path pattern of this route.
         */
        private String path;

        /**
         * {@link dev.gihwan.tollgate.gateway.Upstream} properties of this route.
         */
        private Upstream upstream;

        /**
         * Returns the path pattern of this route.
         */
        public String getPath() {
            return path;
        }

        /**
         * Sets the path pattern of this route as the given {@code path}.
         */
        public void setPath(String path) {
            this.path = requireNonNull(path, "path");
        }

        /**
         * Returns the {@link dev.gihwan.tollgate.gateway.Upstream} properties of this route.
         */
        public Upstream getUpstream() {
            return upstream;
        }

        /**
         * Sets the {@link dev.gihwan.tollgate.gateway.Upstream} properties of this route as the given
         * {@link Upstream}.
         */
        public void setUpstream(Upstream upstream) {
            this.upstream = requireNonNull(upstream, "upstream");
        }

        /**
         * {@link dev.gihwan.tollgate.gateway.Upstream} properties.
         */
        public static class Upstream {

            /**
             * {@link URI} of this {@link dev.gihwan.tollgate.gateway.Upstream}.
             */
            @Nullable
            private URI uri;

            /**
             * {@link Scheme} of this {@link dev.gihwan.tollgate.gateway.Upstream}.
             */
            @Nullable
            private String scheme;

            /**
             * {@link Endpoint}s of this {@link dev.gihwan.tollgate.gateway.Upstream}.
             */
            @Nullable
            private List<Endpoint> endpoints;

            /**
             * Returns the {@link URI} of this {@link dev.gihwan.tollgate.gateway.Upstream}.
             */
            @Nullable
            public URI getUri() {
                return uri;
            }

            /**
             * Sets the {@link URI} of this {@link dev.gihwan.tollgate.gateway.Upstream} as the given
             * {@link URI}.
             */
            public void setUri(URI uri) {
                this.uri = requireNonNull(uri, "uri");
            }

            /**
             * Returns the scheme of this {@link dev.gihwan.tollgate.gateway.Upstream}.
             */
            @Nullable
            public String getScheme() {
                return scheme;
            }

            /**
             * Sets the scheme of this {@link dev.gihwan.tollgate.gateway.Upstream} as the given {@code scheme}.
             */
            public void setScheme(String scheme) {
                this.scheme = requireNonNull(scheme, "scheme");
            }

            /**
             * Returns the {@link Endpoint}s of this {@link dev.gihwan.tollgate.gateway.Upstream}.
             */
            @Nullable
            public List<Endpoint> getEndpoints() {
                return endpoints;
            }

            /**
             * Sets the {@link Endpoint}s of this {@link dev.gihwan.tollgate.gateway.Upstream} as the given
             * {@link Endpoint}s.
             */
            public void setEndpoints(List<Endpoint> endpoints) {
                this.endpoints = requireNonNull(endpoints, "endpoints");
            }

            /**
             * {@link com.linecorp.armeria.client.Endpoint} properties.
             */
            public static class Endpoint {

                /**
                 * Host of this {@link com.linecorp.armeria.client.Endpoint}.
                 */
                private String host;

                /**
                 * Port of this {@link com.linecorp.armeria.client.Endpoint}.
                 */
                private int port;

                /**
                 * Returns the host of this {@link com.linecorp.armeria.client.Endpoint}.
                 */
                public String getHost() {
                    return host;
                }

                /**
                 * Sets the host of this {@link com.linecorp.armeria.client.Endpoint} as the given
                 * {@code host}.
                 */
                public void setHost(String host) {
                    this.host = requireNonNull(host, "host");
                }

                /**
                 * Returns the port of this {@link com.linecorp.armeria.client.Endpoint}.
                 */
                public int getPort() {
                    return port;
                }

                /**
                 * Sets the port of this {@link com.linecorp.armeria.client.Endpoint} as the given
                 * {@code port}.
                 */
                public void setPort(int port) {
                    this.port = port;
                }
            }
        }
    }
}

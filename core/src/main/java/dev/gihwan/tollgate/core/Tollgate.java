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

package dev.gihwan.tollgate.core;

import java.net.InetSocketAddress;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerPort;

public final class Tollgate {

    private static final Logger logger = LoggerFactory.getLogger(Tollgate.class);

    public static DefaultTollgateBuilder builder() {
        return new DefaultTollgateBuilder();
    }

    private final Server server;

    Tollgate(Server server) {
        this.server = server;
    }

    /**
     * @see Server#activePorts()
     */
    public Map<InetSocketAddress, ServerPort> activePorts() {
        return server.activePorts();
    }

    /**
     * @see Server#activePort()
     */
    @Nullable
    public ServerPort activePort() {
        return server.activePort();
    }

    /**
     * @see Server#activePort(SessionProtocol)
     */
    @Nullable
    public ServerPort activePort(SessionProtocol protocol) {
        return server.activePort(protocol);
    }

    /**
     * @see Server#activeLocalPort()
     */
    public int activeLocalPort() {
        return server.activeLocalPort();
    }

    /**
     * @see Server#activeLocalPort(SessionProtocol)
     */
    public int activeLocalPort(SessionProtocol protocol) {
        return server.activeLocalPort(protocol);
    }

    public void start() {
        logger.info("Starting the Tollgate server.");
        server.start().join();
        logger.info("Started the Tollgate server at {}.", activePorts());
    }

    public void stop() {
        logger.info("Stopping the Tollgate server.");
        server.stop().join();
        logger.info("Stopped the Tollgate server.");
    }
}

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

import static java.util.Objects.requireNonNull;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import com.linecorp.armeria.common.HttpStatus;

/**
 * Mappings between HTTP status name and {@link HttpStatus}.
 */
public final class HttpStatusNames {

    private static final Map<String, HttpStatus> nameToStatus;

    static {
        final ImmutableMap.Builder<String, HttpStatus> builder = ImmutableMap.builder();

        // 1xx Informational
        builder.put("CONTINUE", HttpStatus.CONTINUE);
        builder.put("SWITCHING_PROTOCOLS", HttpStatus.SWITCHING_PROTOCOLS);
        builder.put("PROCESSING", HttpStatus.PROCESSING);

        // 2xx Success
        builder.put("OK", HttpStatus.OK);
        builder.put("CREATED", HttpStatus.CREATED);
        builder.put("ACCEPTED", HttpStatus.ACCEPTED);
        builder.put("NON_AUTHORITATIVE_INFORMATION", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        builder.put("NO_CONTENT", HttpStatus.NO_CONTENT);
        builder.put("RESET_CONTENT", HttpStatus.RESET_CONTENT);
        builder.put("PARTIAL_CONTENT", HttpStatus.PARTIAL_CONTENT);
        builder.put("MULTI_STATUS", HttpStatus.MULTI_STATUS);

        // 3xx Redirection
        builder.put("MULTIPLE_CHOICES", HttpStatus.MULTIPLE_CHOICES);
        builder.put("MOVED_PERMANENTLY", HttpStatus.MOVED_PERMANENTLY);
        builder.put("FOUND", HttpStatus.FOUND);
        builder.put("SEE_OTHER", HttpStatus.SEE_OTHER);
        builder.put("NOT_MODIFIED", HttpStatus.NOT_MODIFIED);
        builder.put("USE_PROXY", HttpStatus.USE_PROXY);
        builder.put("TEMPORARY_REDIRECT", HttpStatus.TEMPORARY_REDIRECT);

        // 4xx Client Error
        builder.put("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        builder.put("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        builder.put("PAYMENT_REQUIRED", HttpStatus.PAYMENT_REQUIRED);
        builder.put("FORBIDDEN", HttpStatus.FORBIDDEN);
        builder.put("NOT_FOUND", HttpStatus.NOT_FOUND);
        builder.put("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED);
        builder.put("NOT_ACCEPTABLE", HttpStatus.NOT_ACCEPTABLE);
        builder.put("PROXY_AUTHENTICATION_REQUIRED", HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        builder.put("REQUEST_TIMEOUT", HttpStatus.REQUEST_TIMEOUT);
        builder.put("CONFLICT", HttpStatus.CONFLICT);
        builder.put("GONE", HttpStatus.GONE);
        builder.put("LENGTH_REQUIRED", HttpStatus.LENGTH_REQUIRED);
        builder.put("PRECONDITION_FAILED", HttpStatus.PRECONDITION_FAILED);
        builder.put("REQUEST_ENTITY_TOO_LARGE", HttpStatus.REQUEST_ENTITY_TOO_LARGE);
        builder.put("REQUEST_URI_TOO_LONG", HttpStatus.REQUEST_URI_TOO_LONG);
        builder.put("UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        builder.put("REQUESTED_RANGE_NOT_SATISFIABLE", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        builder.put("EXPECTATION_FAILED", HttpStatus.EXPECTATION_FAILED);
        builder.put("MISDIRECTED_REQUEST", HttpStatus.MISDIRECTED_REQUEST);
        builder.put("UNPROCESSABLE_ENTITY", HttpStatus.UNPROCESSABLE_ENTITY);
        builder.put("LOCKED", HttpStatus.LOCKED);
        builder.put("FAILED_DEPENDENCY", HttpStatus.FAILED_DEPENDENCY);
        builder.put("UNORDERED_COLLECTION", HttpStatus.UNORDERED_COLLECTION);
        builder.put("UPGRADE_REQUIRED", HttpStatus.UPGRADE_REQUIRED);
        builder.put("PRECONDITION_REQUIRED", HttpStatus.PRECONDITION_REQUIRED);
        builder.put("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS);
        builder.put("REQUEST_HEADER_FIELDS_TOO_LARGE", HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
        builder.put("CLIENT_CLOSED_REQUEST", HttpStatus.CLIENT_CLOSED_REQUEST);

        // 5xx Server Error
        builder.put("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        builder.put("NOT_IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED);
        builder.put("BAD_GATEWAY", HttpStatus.BAD_GATEWAY);
        builder.put("SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE);
        builder.put("GATEWAY_TIMEOUT", HttpStatus.GATEWAY_TIMEOUT);
        builder.put("HTTP_VERSION_NOT_SUPPORTED", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        builder.put("VARIANT_ALSO_NEGOTIATES", HttpStatus.VARIANT_ALSO_NEGOTIATES);
        builder.put("INSUFFICIENT_STORAGE", HttpStatus.INSUFFICIENT_STORAGE);
        builder.put("NOT_EXTENDED", HttpStatus.NOT_EXTENDED);
        builder.put("NETWORK_AUTHENTICATION_REQUIRED", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);

        nameToStatus = builder.build();
    }

    /**
     * Returns the {@link HttpStatus} represented by the given {@code name}.
     *
     * @throws IllegalArgumentException if not found.
     */
    public static HttpStatus of(String name) {
        final HttpStatus nullableStatus = ofNullable(name);
        if (nullableStatus == null) {
            throw new IllegalArgumentException("unknown HTTP status: " + name);
        }
        return nullableStatus;
    }

    /**
     * Returns the {@link HttpStatus} represented by the given {@code name}.
     */
    @Nullable
    public static HttpStatus ofNullable(String name) {
        requireNonNull(name, "name");
        return nameToStatus.get(name);
    }

    private HttpStatusNames() {}
}

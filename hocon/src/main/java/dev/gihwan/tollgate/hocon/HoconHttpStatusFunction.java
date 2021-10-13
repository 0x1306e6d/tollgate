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

package dev.gihwan.tollgate.hocon;

import java.util.Set;
import java.util.stream.Collectors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import com.linecorp.armeria.common.HttpStatus;

import dev.gihwan.tollgate.gateway.HttpStatusFunction;
import dev.gihwan.tollgate.gateway.HttpStatusNames;

final class HoconHttpStatusFunction {

    static HttpStatusFunction of(Config config) {
        final Set<HttpStatus> from = config.getList("from")
                                           .stream()
                                           .map(HoconHttpStatusFunction::toHttpStatus)
                                           .collect(Collectors.toUnmodifiableSet());
        final HttpStatus to = toHttpStatus(config.getValue("to"));
        return HttpStatusFunction.from(from).to(to);
    }

    private static HttpStatus toHttpStatus(ConfigValue value) {
        if (value.valueType() == ConfigValueType.NUMBER) {
            final Number unwrapped = (Number) value.unwrapped();
            return HttpStatus.valueOf(unwrapped.intValue());
        }
        if (value.valueType() == ConfigValueType.STRING) {
            final String unwrapped = (String) value.unwrapped();
            return HttpStatusNames.of(unwrapped);
        }
        throw new IllegalArgumentException("should be number or string type");
    }

    private HoconHttpStatusFunction() {}
}

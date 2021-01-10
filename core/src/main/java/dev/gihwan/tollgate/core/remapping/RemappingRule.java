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

package dev.gihwan.tollgate.core.remapping;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;

@FunctionalInterface
public interface RemappingRule {

    static RemappingRule path(String pathPattern) {
        return new RemappingPathRule(requireNonNull(pathPattern, "pathPattern"));
    }

    static RemappingRule of(RemappingRule... remappingRules) {
        return of(ImmutableList.copyOf(requireNonNull(remappingRules, "remappingRules")));
    }

    static RemappingRule of(Iterable<? extends RemappingRule> remappingRules) {
        requireNonNull(remappingRules, "remappingRules");

        final List<RemappingRule> cast = ImmutableList.copyOf(remappingRules);
        checkState(!cast.isEmpty(), "should have at least one remapping rule");

        if (cast.size() == 1) {
            return cast.get(0);
        } else {
            return cast.stream().reduce(RemappingRule::andThen).get();
        }
    }

    HttpRequest remap(ServiceRequestContext ctx, HttpRequest req);

    default RemappingRule andThen(RemappingRule after) {
        requireNonNull(after, "after");
        return (ctx, req) -> after.remap(ctx, remap(ctx, req));
    }
}

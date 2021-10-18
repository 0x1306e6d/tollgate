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

rootProject.name = "tollgate"

include("gateway")

include("hocon")
include("junit5")
include("spring:boot2-autoconfigure")
include("spring:boot2-starter")
include("standalone")
include("testing")
include("util")

include(":examples:helloworld")
include(":examples:pokeapi:pokeapi-berry")
include(":examples:pokeapi:pokeapi-contest")
include(":examples:pokeapi:pokeapi-encounter")
include(":examples:pokeapi:pokeapi-evolution")
include(":examples:pokeapi:pokeapi-game")
include(":examples:pokeapi:pokeapi-gateway")
include(":examples:pokeapi:pokeapi-item")
include(":examples:pokeapi:pokeapi-location")
include(":examples:pokeapi:pokeapi-machine")
include(":examples:pokeapi:pokeapi-move")
include(":examples:pokeapi:pokeapi-pokemon")
include(":examples:spring:boot2")
include(":examples:streaming")

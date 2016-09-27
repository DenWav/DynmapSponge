/*
 * DynmapSponge
 *
 * Copyright 2016 Kyle Wood (DemonWav)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demonwav.dynmapsponge

import java.util.Optional

/**
 * Due to Kotlin's nullability system, it's best to just get null from an Optional and
 * use Kotlin's system instead. Rather than always calling #orElse(null), this is a
 * more convenient way of calling that method.
 */
val <V> Optional<V>.get: V?
    get() {
        return orElse(null)
    }

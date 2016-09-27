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

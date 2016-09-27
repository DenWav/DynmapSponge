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

package com.demonwav.dynmapsponge.util

import org.spongepowered.api.Sponge
import org.spongepowered.api.world.biome.BiomeType

fun getBlockNames(): Array<String> {
    TODO()
}

fun getBlockMaterialMap(): IntArray {
    TODO()
}

// Ensure ordering stays the same
val orderedList = Sponge.getRegistry().getAllOf(BiomeType::class.java).toList()

fun getBiomeNames(): Array<String> {
    return orderedList.map { it.name }.toTypedArray()
}

fun getBiomeBaseList(): Collection<BiomeType> {
    return orderedList
}

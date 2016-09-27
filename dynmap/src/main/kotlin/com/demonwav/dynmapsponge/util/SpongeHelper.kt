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

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import org.spongepowered.api.Sponge
import org.spongepowered.api.world.biome.BiomeType

object SpongeHelper {
    // Ensure ordering stays the same
    private lateinit var orderedList: List<BiomeType>

    fun init() {
        orderedList = Sponge.getRegistry().getAllOf(BiomeType::class.java).toList()
    }

    fun getBlockNames(): Array<String?> {
        return Array(4096) { i -> getBlockUnlocalizedName(Block.getBlockById(i)) }
    }

    private fun getBlockUnlocalizedName(b: Block?): String? {
        var s = b?.unlocalizedName
        if (s?.startsWith("tile.") ?: false) {
            s = s?.substring(5)
        }
        return s
    }

    fun getBlockMaterialMap(): IntArray {
        val map = IntArray(4096) { -1 }
        val mats = arrayListOf<Material>()

        for (i in map.indices) {
            val b = Block.getBlockById(i)
            if (b != null) {
                val mat = b.blockState.baseState.material
                if (mat != null) {
                    map[i] = mats.indexOf(mat)
                    if (map[i] < 0) {
                        map[i] = mats.size
                        mats.add(mat)
                    }
                }
            }
        }

        return map
    }

    fun getBiomeNames(): Array<String> {
        return orderedList.map { it.name }.toTypedArray()
    }

    fun getBiomeBaseList(): Collection<BiomeType> {
        return orderedList
    }
}

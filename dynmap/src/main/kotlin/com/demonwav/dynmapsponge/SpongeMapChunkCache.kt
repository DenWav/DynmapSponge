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

import org.dynmap.DynmapChunk
import org.dynmap.DynmapWorld
import org.dynmap.common.BiomeMap
import org.dynmap.utils.BlockStep
import org.dynmap.utils.DynIntHashMap
import org.dynmap.utils.MapChunkCache
import org.dynmap.utils.MapIterator
import org.dynmap.utils.VisibilityLimit
import org.spongepowered.api.world.World
import org.spongepowered.api.world.extent.BlockVolume
import org.spongepowered.api.world.extent.ImmutableBlockVolume
import java.util.ArrayList

private var init = false
private val nullBiomeMap = arrayOf(BiomeMap.NULL)
private val unstep = arrayOf(BlockStep.X_MINUS, BlockStep.Y_MINUS, BlockStep.Z_MINUS, BlockStep.X_PLUS, BlockStep.Y_PLUS, BlockStep.Z_PLUS)

private var biomeTobmap: Array<BiomeMap>? = null

class SpongeMapChunkCache : MapChunkCache() {

    private lateinit var world: World
    private lateinit var spongeWorld: SpongeWorld
    private var nsect: Int = 0
    private val chunks = ArrayList<DynmapChunk>()
    private val iterator = chunks.iterator()

    var empty = true

    private var snapcnt = 0
    private lateinit var snaparray: Array<ImmutableBlockVolume>
    private lateinit var snaptile: Array<DynIntHashMap>
    private lateinit var sameNeighborBiomeCnt: Array<ByteArray>
    private lateinit var biomeMap: Array<Array<BiomeMap>>
    private lateinit var isSectionNotEmpty: Array<BooleanArray>
    private lateinit var inhabitedTicks: LongArray

    private var x_dim: Int? = null
    private var x_min: Int? = null
    private var x_max: Int? = null

    private var z_min: Int? = null

    private var z_max: Int? = null

    override fun loadChunks(maxToLoad: Int): Int {
        TODO("not implemented")
    }

    override fun setVisibleRange(limit: VisibilityLimit?) {
        TODO("not implemented")
    }

    override fun isEmptySection(sx: Int, sy: Int, sz: Int): Boolean {
        TODO("not implemented")
    }

    override fun isEmpty(): Boolean {
        return empty
    }

    override fun getIterator(x: Int, y: Int, z: Int): MapIterator {
        TODO("not implemented")
    }

    override fun unloadChunks() {
        TODO("not implemented")
    }

    override fun isDoneLoading(): Boolean {
        TODO("not implemented")
    }

    override fun setHiddenRange(limit: VisibilityLimit?) {
        TODO("not implemented")
    }

    override fun setHiddenFillStyle(style: HiddenChunkStyle?) {
        TODO("not implemented")
    }

    override fun getWorld(): DynmapWorld {
        return spongeWorld
    }

    override fun setChunkDataTypes(blockdata: Boolean, biome: Boolean, highestblocky: Boolean, rawbiome: Boolean): Boolean {
        TODO("not implemented")
    }
}

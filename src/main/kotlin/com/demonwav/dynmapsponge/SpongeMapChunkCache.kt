package com.demonwav.dynmapsponge

import org.dynmap.DynmapWorld
import org.dynmap.utils.MapChunkCache
import org.dynmap.utils.MapIterator
import org.dynmap.utils.VisibilityLimit
import org.spongepowered.api.world.World

class SpongeMapChunkCache : MapChunkCache() {

    private lateinit var world: World
    private lateinit var spongeWorld: SpongeWorld

    private var x_min: Int? = null
    private var x_max: Int? = null
    private var z_min: Int? = null
    private var z_max: Int? = null

    private var x_dim: Int? = null

    var empty = true

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

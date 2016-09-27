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

import net.minecraft.world.World as NmsWorld
import org.dynmap.DynmapChunk
import org.dynmap.DynmapLocation
import org.dynmap.DynmapWorld
import org.dynmap.utils.MapChunkCache
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.property.block.LightEmissionProperty
import org.spongepowered.api.data.property.block.SkyLuminanceProperty
import org.spongepowered.api.world.DimensionType
import org.spongepowered.api.world.DimensionTypes
import org.spongepowered.api.world.World

class SpongeWorld(name: String, height: Int, sealevel: Int, env: DimensionType) : DynmapWorld(name, height, sealevel) {

    private var world: World? = null

    init {
        when (env) {
            DimensionTypes.NETHER -> {
                val f = 0.1F
                for (i in 0..15) {
                    val f1 = 1F - i.toFloat() / 15F
                    // What is even going on here
                    setBrightnessTableEntry(i, (1F - f1) / (f1 * 3F + 1F) * (1F - f) + f)
                }
            }
        }
    }

    constructor(world: World) : this(world.name, Sponge.getServer().chunkLayout.spaceSize.y, (world as NmsWorld).seaLevel, world.dimension.type) {
        setWorldLoaded(world)
    }

    override fun getLightLevel(x: Int, y: Int, z: Int): Int {
        return world?.getLocation(x, y, z)?.getProperty(LightEmissionProperty::class.java)?.get?.value ?: 0
    }

    override fun getSkyLightLevel(x: Int, y: Int, z: Int): Int {
        return world?.getBlock(x, y, z)?.getProperty(SkyLuminanceProperty::class.java)?.get?.value?.toInt() ?: 0
    }

    override fun canGetSkyLightLevel(): Boolean {
        return when (world?.dimension?.type) {
            DimensionTypes.NETHER, DimensionTypes.THE_END -> false
            else -> true
        }
    }

    override fun isThundering(): Boolean {
        return world?.properties?.isThundering ?: false
    }

    override fun getHighestBlockYAt(x: Int, z: Int): Int {
        for (y in Sponge.getServer().chunkLayout.spaceMax.y..Sponge.getServer().chunkLayout.spaceMin.y) {
            if (world?.getBlockType(x, y, z) != BlockTypes.AIR) {
                return y
            }
        }
        return 0
    }

    override fun isLoaded(): Boolean {
        return world?.isLoaded ?: false
    }

    override fun isNether(): Boolean {
        return world?.dimension?.type == DimensionTypes.NETHER
    }

    override fun getTime(): Long {
        return world?.properties?.worldTime ?: 0
    }

    override fun getChunkCache(chunks: MutableList<DynmapChunk>?): MapChunkCache {
        TODO("not implemented")
    }

    override fun getEnvironment(): String {
        return when (world?.dimension?.type) {
            DimensionTypes.NETHER -> "nether"
            DimensionTypes.THE_END -> "the_end"
            // Dynmap can't handle any other type, so just call it normal
            else -> "normal"
        }
    }

    override fun hasStorm(): Boolean {
        return world?.properties?.isRaining ?: false
    }

    override fun getSpawnLocation(): DynmapLocation? {
        val l = world?.spawnLocation ?: return null
        return DynmapLocation(world?.name, l.x, l.y, l.z)
    }

    fun setWorldLoaded(world: World) {
        this.world = world
        Sponge.getServer().loadWorld(world.uniqueId)
    }

    override fun setWorldUnloaded() {
        Sponge.getServer().unloadWorld(world)
    }

}

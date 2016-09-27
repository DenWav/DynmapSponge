package com.demonwav.dynmapsponge

import org.dynmap.DynmapChunk
import org.dynmap.DynmapLocation
import org.dynmap.DynmapWorld
import org.dynmap.utils.MapChunkCache
import org.spongepowered.api.Server
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.property.block.LightEmissionProperty
import org.spongepowered.api.data.property.block.SkyLuminanceProperty
import org.spongepowered.api.world.DimensionTypes
import org.spongepowered.api.world.World

class SpongeWorld(val world: World, val server: Server) : DynmapWorld(world.name, server.chunkLayout.spaceSize.y, world.properties.sealevel/*todo*/) {

    override fun getLightLevel(x: Int, y: Int, z: Int): Int {
        return world.getLocation(x, y, z).getProperty(LightEmissionProperty::class.java).get?.value ?: 0
    }

    override fun getSkyLightLevel(x: Int, y: Int, z: Int): Int {
        return world.getBlock(x, y, z).getProperty(SkyLuminanceProperty::class.java).get?.value?.toInt() ?: 0
    }

    override fun canGetSkyLightLevel(): Boolean {
        return when (world.dimension.type) {
            DimensionTypes.NETHER, DimensionTypes.THE_END -> false
            else -> true
        }
    }

    override fun isThundering(): Boolean {
        return world.properties.isThundering
    }

    override fun getHighestBlockYAt(x: Int, z: Int): Int {
        for (y in server.chunkLayout.spaceMax.y..server.chunkLayout.spaceMin.y) {
            if (world.getBlockType(x, y, z) != BlockTypes.AIR) {
                return y
            }
        }
        return 0
    }

    override fun isLoaded(): Boolean {
        return world.isLoaded
    }

    override fun isNether(): Boolean {
        return world.dimension.type == DimensionTypes.NETHER
    }

    override fun getTime(): Long {
        return world.properties.worldTime
    }

    override fun getChunkCache(chunks: MutableList<DynmapChunk>?): MapChunkCache {
        TODO("not implemented")
    }

    override fun getEnvironment(): String {
        return when (world.dimension.type) {
            DimensionTypes.NETHER -> "nether"
            DimensionTypes.THE_END -> "the_end"
            // Dynmap can't handle any other type, so just call it normal
            else -> "normal"
        }
    }

    override fun hasStorm(): Boolean {
        return world.properties.isRaining
    }

    override fun getSpawnLocation(): DynmapLocation {
        val l = world.spawnLocation
        return DynmapLocation(world.name, l.x, l.y, l.z)
    }

    override fun setWorldUnloaded() {
        server.unloadWorld(world)
    }

}

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

import com.demonwav.dynmapsponge.listeners.PlayerListeners
import com.demonwav.dynmapsponge.util.getBiomeBaseHumidity
import com.demonwav.dynmapsponge.util.getBiomeBaseIdString
import com.demonwav.dynmapsponge.util.getBiomeBaseList
import com.demonwav.dynmapsponge.util.getBiomeBaseTemperature
import com.demonwav.dynmapsponge.util.getBiomeNames
import com.demonwav.dynmapsponge.util.getBlockMaterialMap
import com.demonwav.dynmapsponge.util.getBlockNames
import com.google.inject.Inject
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener
import org.dynmap.DynmapCore
import org.dynmap.MapManager
import org.dynmap.common.BiomeMap
import org.dynmap.markers.MarkerAPI
import org.dynmap.modsupport.ModSupportImpl
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.event.game.state.GameStoppingEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.world.World
import java.nio.file.Path
import java.util.HashMap

const val id = "dynmap"
const val name = "DynmapSponge"
const val version = "1.0-SNAPSHOT"

@Plugin(
    id = id,
    name = name,
    version = version,
    description = "Dynmap implementation for Sponge",
    authors = arrayOf("DemonWav")
)
class DynmapSponge : DynmapCommonAPI {

    @Inject
    private lateinit var logger: Logger

    @Inject
    @DefaultConfig(sharedRoot = false)
    private lateinit var defaultConfig: Path

    private lateinit var pluginContainer: PluginContainer
    private lateinit var mapManager: MapManager

    val core = DynmapCore()
    private val worldMap = HashMap<String, SpongeWorld>()

    private var lastWorld: World? = null
    private var lastSpongeWorld: SpongeWorld? = null

    // TPS calculator
    var tps = 20.0
    private var lastTick = 0L
    private var perTickLimit = 0L
    private var currentTickStartTime = 0L
    private var avgTickLength = 50000000L

    private var chunksInCurrentTick = 0
    private var currentTick = 0L
    private var prevTick = 0L


    fun getWorldByName(name: String): SpongeWorld? {
        if ((lastWorld != null) && (lastSpongeWorld != null) && (lastWorld?.name == name)) {
            return lastSpongeWorld
        }
        return worldMap[name]
    }

    fun getWorld(world: World): SpongeWorld {
        if (lastWorld === world && lastSpongeWorld != null) {
            return lastSpongeWorld!!
        }

        var spongeWorld = worldMap[world.name]
        if (spongeWorld == null) {
            spongeWorld = SpongeWorld(world)
            worldMap[world.name] = spongeWorld
        } else if (!spongeWorld.isLoaded) {
            spongeWorld.setWorldLoaded(world)
        }

        lastWorld = world
        lastSpongeWorld = spongeWorld

        return spongeWorld
    }

    @Listener
    fun onServerPreStart(event: GamePreInitializationEvent) {
        ModSupportImpl.init()

        pluginContainer = Sponge.getPluginManager().getPlugin(id).get!!
    }

    @Listener
    fun onServerStart(event: GameInitializationEvent) {
        val mcVer = Sponge.getPlatform().minecraftVersion.name

        loadExtraBiomes(mcVer)

        createListeners()

        core.pluginJarFile
        core.setPluginVersion(version, "Sponge")
        core.setMinecraftVersion(mcVer)
        core.dataFolder = defaultConfig.toFile()
        core.server = SpongeServer(Sponge.getServer(), this)
        // SpongeBlocks functions
        core.blockNames = getBlockNames()
        core.blockMaterialMap = getBlockMaterialMap()
        core.biomeNames = getBiomeNames()

        // enable core
        if (!core.enableCore()) {
            // don't enable the plugin
            return
        }

        mapManager = core.mapManager

        DynmapCommonAPIListener.apiInitialized(this)

        lastTick = System.nanoTime()
        perTickLimit = core.maxTickUseMS.toLong() * 1000000L

        Sponge.getScheduler().createTaskBuilder().intervalTicks(1).delayTicks(1).execute(this::processTick).submit(this)

        logger.info("Enabled")
    }

    @Listener
    fun onServerStop(event: GameStoppingEvent) {
        DynmapCommonAPIListener.apiTerminated()

        core.disableCore()

        // TODO sscache cleanup
        logger.info("Disabled")
    }

    fun loadExtraBiomes(mcVer: String) {
        BiomeMap.loadWellKnownByVersion(mcVer)

        val biomeList = getBiomeBaseList()
        var count = 0

        for ((i, biomeBase) in biomeList.withIndex()) {
            val temp = getBiomeBaseTemperature(biomeBase)
            val humidity = getBiomeBaseHumidity(biomeBase)

            val biomeMap = BiomeMap.byBiomeID(i)
            if (biomeMap.isDefault) {
                var id = getBiomeBaseIdString(biomeBase)

                if (id == null) {
                    id = "BIOME_" + i
                }

                val map = BiomeMap(i, id, temp, humidity)
                logger.debug("Add custom biome [${map.toString()}]($i)")
                count++
            } else {
                biomeMap.temperature = temp
                biomeMap.rainfall = humidity
            }
        }

        if (count > 0) {
            logger.info("Added $count custom biome mappings")
        }
    }

    fun createListeners() {
        Sponge.getEventManager().registerListeners(PlayerListeners(this), this)
    }

    private fun processTick() {
        val now = System.nanoTime()
        val elapsed = now - lastTick
        lastTick = now
        avgTickLength = ((avgTickLength * 99) / 100) + (elapsed / 100)
        tps = 1E9 / avgTickLength.toDouble()

        chunksInCurrentTick = mapManager.maxChunkLoadsPerTick
        currentTick++

        core.serverTick(tps)
    }

    // Dynmap common API (just pass-through to core)
    override fun getMarkerAPI(): MarkerAPI = core.markerAPI
    override fun sendBroadcastToWeb(sender: String?, msg: String?) = core.sendBroadcastToWeb(sender, msg)
    override fun markerAPIInitialized() = core.markerAPIInitialized()
    override fun getPlayerVisbility(player: String?) = core.getPlayerVisbility(player)
    override fun setDisableChatToWebProcessing(disable: Boolean) = core.setDisableChatToWebProcessing(disable)
    override fun testIfPlayerInfoProtected() = core.testIfPlayerInfoProtected()
    override fun testIfPlayerVisibleToPlayer(player: String?, player_to_see: String?) = core.testIfPlayerVisibleToPlayer(player, player_to_see)
    override fun getPauseFullRadiusRenders() = core.pauseFullRadiusRenders
    override fun getPauseUpdateRenders() = core.pauseUpdateRenders
    override fun getDynmapCoreVersion(): String = core.dynmapCoreVersion
    override fun triggerRenderOfBlock(wid: String?, x: Int, y: Int, z: Int) = core.triggerRenderOfBlock(wid, x, y, z)
    override fun triggerRenderOfVolume(wid: String?, minx: Int, miny: Int, minz: Int, maxx: Int, maxy: Int, maxz: Int) =
        core.triggerRenderOfVolume(wid, minx, miny, minz, maxx, maxy, maxz)
    override fun assertPlayerInvisibility(player: String?, is_invisible: Boolean, plugin_id: String?) {
        core.assertPlayerInvisibility(player, is_invisible, plugin_id)
    }
    override fun setPauseUpdateRenders(dopause: Boolean) {
        core.pauseUpdateRenders = dopause
    }
    override fun postPlayerMessageToWeb(playerid: String?, playerdisplay: String?, message: String?) {
        core.postPlayerMessageToWeb(playerid, playerdisplay, message)
    }
    override fun setPlayerVisiblity(player: String?, is_visible: Boolean) {
        core.setPlayerVisiblity(player, is_visible)
    }
    override fun setPauseFullRadiusRenders(dopause: Boolean) {
        core.pauseFullRadiusRenders = dopause
    }
    override fun processSignChange(blkid: Int, world: String?, x: Int, y: Int, z: Int, lines: Array<out String>?, playerid: String?) {
        core.processSignChange(blkid, world, x, y, z, lines, playerid)
    }
    override fun assertPlayerVisibility(player: String?, is_visible: Boolean, plugin_id: String?) {
        core.assertPlayerVisibility(player, is_visible, plugin_id)
    }
    override fun postPlayerJoinQuitToWeb(playerid: String?, playerdisplay: String?, isjoin: Boolean) {
        core.postPlayerJoinQuitToWeb(playerid, playerdisplay, isjoin)
    }
}

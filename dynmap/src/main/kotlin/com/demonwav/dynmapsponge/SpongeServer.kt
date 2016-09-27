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

import net.minecraft.block.Block
import org.dynmap.DynmapChunk
import org.dynmap.DynmapWorld
import org.dynmap.common.BiomeMap
import org.dynmap.common.DynmapListenerManager
import org.dynmap.common.DynmapPlayer
import org.dynmap.common.DynmapServerInterface
import org.dynmap.utils.MapChunkCache
import org.spongepowered.api.Server
import org.spongepowered.api.Sponge
import org.spongepowered.api.service.ban.BanService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class SpongeServer(private val server: Server, private val plugin: DynmapSponge) : DynmapServerInterface() {

    override fun scheduleServerTask(run: Runnable?, delay: Long) {
        if (run == null) {
            return
        }

        Sponge.getScheduler().createTaskBuilder().delayTicks(delay).execute(run).submit(plugin)
    }

    override fun createMapChunkCache(w: DynmapWorld,
                                     chunks: MutableList<DynmapChunk>,
                                     blockdata: Boolean,
                                     highesty: Boolean,
                                     biome: Boolean,
                                     rawbiome: Boolean): MapChunkCache {
        TODO()
    }

    override fun reload() {
        plugin.disableCore()
        plugin.enableCore()
    }

    override fun getWorldByName(wname: String?): SpongeWorld? {
        if (wname == null) {
            return null
        }
        return plugin.getWorldByName(wname)
    }

    override fun <T : Any?> callSyncMethod(task: Callable<T>?): Future<T>? {
        if (task == null) {
            return null
        }

        val future = CompletableFuture<T>()
        Sponge.getScheduler().createTaskBuilder().execute { ->
            future.complete(task.call())
        }
        return future
    }

    override fun getCurrentPlayers(): Int {
        return server.onlinePlayers.size
    }

    override fun getServerIP(): String? {
        return server.boundAddress.get?.address?.hostAddress
    }

    override fun getOnlinePlayers(): Array<out DynmapPlayer?> {
        val list = server.onlinePlayers.toList()
        val array = Array(list.size) { i -> SpongePlayer(list[i]) }
        return array
    }

    override fun stripChatColor(s: String?): String {
        return TextSerializers.FORMATTING_CODE.stripCodes(s)
    }

    override fun requestEventNotification(type: DynmapListenerManager.EventType?): Boolean {
        // TODO sponge dynmap api
        TODO()
    }

    override fun getMaxPlayers(): Int {
        return server.maxPlayers
    }

    override fun getPlayer(name: String?): DynmapPlayer? {
        val player = server.getPlayer(name)
        if (player.isPresent) {
            return SpongePlayer(player.get())
        }
        return null
    }

    override fun getServerTPS(): Double {
        return plugin.tps
    }

    override fun resetCacheStats() {
    }

    override fun sendWebChatEvent(source: String, name: String, msg: String): Boolean {
        val event = DynmapWebChatEvent(source, name, msg)
        Sponge.getEventManager().post(event)
        return event.isCancelled
    }

    override fun getIPBans(): MutableSet<String>? {
        return Sponge.getServiceManager().getRegistration(BanService::class.java).get?.provider?.ipBans?.map { it.address.hostAddress }?.toMutableSet()
    }

    override fun getCacheHitRate(): Double {
        TODO()
    }

    override fun getBiomeIDs(): Array<out String> {
        val map = BiomeMap.values()
        return Array(map.size) { i -> map[i].toString() }
    }

    override fun getOfflinePlayer(name: String?): DynmapPlayer? {
        // TODO don't..do this...
        val profile = server.gameProfileManager[name].get()
        if (profile.isFilled) {
            val player = server.getPlayer(profile.uniqueId).get ?: return null
            return SpongePlayer(player)
        }
        return null
    }

    override fun checkPlayerPermissions(name: String?, perms: MutableSet<String>?): MutableSet<String>? {
        val player = server.getPlayer(name).get ?: return mutableSetOf()
        val banService = Sponge.getServiceManager().getRegistration(BanService::class.java).get?.provider ?: return mutableSetOf()

        val profile = server.gameProfileManager[player.uniqueId].get()
        if (banService.isBanned(profile)) {
            return mutableSetOf()
        }

        TODO()
    }

    override fun getBlockIDAt(wname: String?, x: Int, y: Int, z: Int): Int {
        return Block.getIdFromBlock(Sponge.getServer().getWorld(wname).get().getBlock(x, y, z).type as Block)
    }

    override fun broadcastMessage(msg: String?) {
        server.broadcastChannel.send(Text.of(msg))
    }

    override fun checkPlayerPermission(player: String?, perm: String?): Boolean {
        TODO()
    }

    override fun getServerName(): String {
        return "Sponge"
    }

    override fun isPlayerBanned(pid: String?): Boolean {
        val banService = Sponge.getServiceManager().getRegistration(BanService::class.java).get?.provider ?: return false
        val profile = server.gameProfileManager[pid].get()
        return banService.isBanned(profile)
    }
}

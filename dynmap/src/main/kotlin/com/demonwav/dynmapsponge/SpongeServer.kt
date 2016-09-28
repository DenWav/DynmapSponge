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
import org.dynmap.common.DynmapListenerManager.EventType
import org.dynmap.common.DynmapPlayer
import org.dynmap.common.DynmapServerInterface
import org.dynmap.utils.MapChunkCache
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.action.SleepingEvent
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.service.ban.BanService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers
import java.util.HashSet
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

class SpongeServer(private val plugin: DynmapSponge) : DynmapServerInterface() {

    private val registered = HashSet<DynmapListenerManager.EventType>()

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
        return Sponge.getServer().onlinePlayers.size
    }

    override fun getServerIP(): String? {
        return Sponge.getServer().boundAddress.get?.address?.hostAddress
    }

    override fun getOnlinePlayers(): Array<out DynmapPlayer?> {
        val list = Sponge.getServer().onlinePlayers.toList()
        val array = Array(list.size) { i -> SpongePlayer(list[i]) }
        return array
    }

    override fun stripChatColor(s: String?): String {
        return TextSerializers.FORMATTING_CODE.stripCodes(s)
    }

    override fun requestEventNotification(type: DynmapListenerManager.EventType): Boolean {
        if (registered.contains(type)) {
            return true
        }

        when (type) {
            EventType.WORLD_LOAD, EventType.WORLD_UNLOAD -> {

            }
            EventType.WORLD_SPAWN_CHANGE -> {
                // TODO
            }
            EventType.PLAYER_JOIN, EventType.PLAYER_QUIT -> {
                // Already handled
            }
            EventType.PLAYER_BED_LEAVE -> {
                Sponge.getEventManager().registerListeners(plugin, object {
                    @Listener
                    fun onBedLeave(event: SleepingEvent.Finish, @Getter("getTargetEntity") player: Player) {
                        val dynmapPlayer = SpongePlayer(player)
                        plugin.core.listenerManager.processPlayerEvent(EventType.PLAYER_BED_LEAVE, dynmapPlayer)
                    }
                })
            }
            EventType.PLAYER_CHAT -> {
                Sponge.getEventManager().registerListeners(plugin, object {
                    @Listener
                    fun onPlayerChat(event: MessageChannelEvent.Chat, @First player: Player, @Getter("getRawMessage") text: Text) {
                        val dynmapPlayer = SpongePlayer(player)
                        plugin.core.listenerManager.processChatEvent(EventType.PLAYER_CHAT, dynmapPlayer, text.toPlain())
                    }
                })
            }
            EventType.BLOCK_BREAK -> {
                Sponge.getEventManager().registerListeners(plugin, object {
                    @Listener
                    fun onBlockBreak(event: ChangeBlockEvent.Break, @First player: Player) {
                        event.transactions.forEach { transaction ->
                            val final = transaction.final
                            val loc = final.location.get ?: return
                            val block = loc.block.type as Block

                            plugin.core.listenerManager.processBlockEvent(
                                EventType.BLOCK_BREAK,
                                Block.getIdFromBlock(block),
                                loc.extent.name,
                                loc.blockX,
                                loc.blockY,
                                loc.blockZ
                            )
                        }
                    }
                })
            }
            EventType.SIGN_CHANGE -> {
                Sponge.getEventManager().registerListeners(plugin, object {
                    @Listener
                    fun onSignChange(event: ChangeSignEvent, @First player: Player, @Getter("getText") text: SignData) {
                        val loc = event.targetTile.location
                        val block = loc.block.type as Block
                        val dynmapPlayer = SpongePlayer(player)

                        plugin.core.listenerManager.processSignChangeEvent(
                            EventType.SIGN_CHANGE,
                            Block.getIdFromBlock(block),
                            loc.extent.name,
                            loc.blockX,
                            loc.blockY,
                            loc.blockZ,
                            text.lines().map { it.toPlain() }.toTypedArray(),
                            dynmapPlayer
                        )
                    }
                })
            }
            else -> {
                plugin.logger.error("Unhandled event type: $type")
                return false
            }
        }
        registered.add(type)
        return true
    }

    override fun getMaxPlayers(): Int {
        return Sponge.getServer().maxPlayers
    }

    override fun getPlayer(name: String?): DynmapPlayer? {
        val player = Sponge.getServer().getPlayer(name)
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
        val profile = Sponge.getServer().gameProfileManager[name].get()
        if (profile.isFilled) {
            val player = Sponge.getServer().getPlayer(profile.uniqueId).get ?: return null
            return SpongePlayer(player)
        }
        return null
    }

    override fun checkPlayerPermissions(name: String, perms: Set<String>): Set<String>? {
        val player = Sponge.getServer().getPlayer(name).get ?: return mutableSetOf()
        val banService = Sponge.getServiceManager().getRegistration(BanService::class.java).get?.provider ?: return setOf()

        val profile = Sponge.getServer().gameProfileManager[player.uniqueId].get()
        if (banService.isBanned(profile)) {
            return setOf()
        }

        return perms.filter { player.hasPermission(it) }.toSet()
    }

    override fun getBlockIDAt(wname: String?, x: Int, y: Int, z: Int): Int {
        return Block.getIdFromBlock(Sponge.getServer().getWorld(wname).get().getBlock(x, y, z).type as Block)
    }

    override fun broadcastMessage(msg: String?) {
        Sponge.getServer().broadcastChannel.send(Text.of(msg))
    }

    override fun checkPlayerPermission(name: String?, perm: String?): Boolean {
        val player = Sponge.getServer().getPlayer(name).get ?: return false
        if (!player.isOnline) {
            return false
        }
        return player.hasPermission(perm)
    }

    override fun getServerName(): String {
        return "Sponge"
    }

    override fun isPlayerBanned(pid: String?): Boolean {
        val banService = Sponge.getServiceManager().getRegistration(BanService::class.java).get?.provider ?: return false
        val profile = Sponge.getServer().gameProfileManager[pid].get()
        return banService.isBanned(profile)
    }
}

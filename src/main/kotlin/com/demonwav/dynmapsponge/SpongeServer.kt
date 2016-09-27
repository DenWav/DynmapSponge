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
import org.dynmap.common.DynmapListenerManager
import org.dynmap.common.DynmapPlayer
import org.dynmap.common.DynmapServerInterface
import org.dynmap.utils.MapChunkCache
import java.util.concurrent.Callable
import java.util.concurrent.Future

class SpongeServer : DynmapServerInterface() {
    override fun scheduleServerTask(run: Runnable?, delay: Long) {
        TODO("not implemented")
    }

    override fun createMapChunkCache(w: DynmapWorld?, chunks: MutableList<DynmapChunk>?, blockdata: Boolean, highesty: Boolean, biome: Boolean, rawbiome: Boolean): MapChunkCache {
        TODO("not implemented")
    }

    override fun reload() {
        TODO("not implemented")
    }

    override fun getWorldByName(wname: String?): DynmapWorld {
        TODO("not implemented")
    }

    override fun <T : Any?> callSyncMethod(task: Callable<T>?): Future<T> {
        TODO("not implemented")
    }

    override fun getCurrentPlayers(): Int {
        TODO("not implemented")
    }

    override fun getServerIP(): String {
        TODO("not implemented")
    }

    override fun getOnlinePlayers(): Array<out DynmapPlayer> {
        TODO("not implemented")
    }

    override fun stripChatColor(s: String?): String {
        TODO("not implemented")
    }

    override fun requestEventNotification(type: DynmapListenerManager.EventType?): Boolean {
        TODO("not implemented")
    }

    override fun getMaxPlayers(): Int {
        TODO("not implemented")
    }

    override fun getPlayer(name: String?): DynmapPlayer {
        TODO("not implemented")
    }

    override fun getServerTPS(): Double {
        TODO("not implemented")
    }

    override fun resetCacheStats() {
        TODO("not implemented")
    }

    override fun sendWebChatEvent(source: String?, name: String?, msg: String?): Boolean {
        TODO("not implemented")
    }

    override fun getIPBans(): MutableSet<String>? {
        TODO("not implemented")
    }

    override fun getCacheHitRate(): Double {
        TODO("not implemented")
    }

    override fun getBiomeIDs(): Array<out String> {
        TODO("not implemented")
    }

    override fun getOfflinePlayer(name: String?): DynmapPlayer {
        TODO("not implemented")
    }

    override fun checkPlayerPermissions(player: String?, perms: MutableSet<String>?): MutableSet<String>? {
        TODO("not implemented")
    }

    override fun getBlockIDAt(wname: String?, x: Int, y: Int, z: Int): Int {
        TODO("not implemented")
    }

    override fun broadcastMessage(msg: String?) {
        TODO("not implemented")
    }

    override fun checkPlayerPermission(player: String?, perm: String?): Boolean {
        TODO("not implemented")
    }

    override fun getServerName(): String {
        TODO("not implemented")
    }

    override fun isPlayerBanned(pid: String?): Boolean {
        TODO("not implemented")
    }
}

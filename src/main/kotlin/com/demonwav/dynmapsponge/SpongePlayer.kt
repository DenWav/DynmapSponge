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

import org.dynmap.DynmapLocation
import org.dynmap.common.DynmapPlayer
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.text.serializer.TextSerializers
import java.net.InetSocketAddress
import java.util.HashMap

class SpongePlayer(player: Player) : SpongeCommandSender(player), DynmapPlayer {

    private var player: Player?
    private val user: User = player

    private val sortWeights = HashMap<String, Int>()

    init {
        this.player = player
    }

    override fun isSneaking(): Boolean {
        TODO("not implemented")
    }

    override fun getName(): String {
        return user.name
    }

    override fun getFirstLoginTime(): Long {
        return user.get(Keys.FIRST_DATE_PLAYED).get?.toEpochMilli() ?: 0
    }

    override fun getLastLoginTime(): Long {
        return user.get(Keys.LAST_DATE_PLAYED).get?.toEpochMilli() ?: 0
    }

    override fun getHealth(): Double {
        return player?.health()?.get() ?: 0.0
    }

    override fun getArmorPoints(): Int {
        TODO("not implemented")
    }

    override fun getWorld(): String? {
        return player?.world?.name
    }

    override fun isOnline(): Boolean {
        return player?.isOnline == true
    }

    override fun isInvisible(): Boolean {
        if (player == null) {
            return false
        }
        return player?.get(Keys.INVISIBLE)?.get == true
    }

    override fun getLocation(): DynmapLocation? {
        if (player != null) {
            val l = player!!.location
            return DynmapLocation(player!!.world.name, l.x, l.y, l.z)
        }
        return null
    }

    override fun getBedSpawnLocation(): DynmapLocation? {
        if (player == null) {
            return null
        }
        val l = player?.run { get(Keys.RESPAWN_LOCATIONS)?.get?.get(world?.uniqueId)?.position } ?: return null
        return DynmapLocation(player?.world?.name, l.x, l.y, l.z)
    }

    override fun getDisplayName(): String {
        if (player != null) {
            // TODO implement this manually to avoid the deprecated method
            return TextSerializers.LEGACY_FORMATTING_CODE.serialize(player!!.displayNameData.displayName().get())
        } else {
            return user.name
        }
    }

    override fun getSortWeight(): Int {
        return sortWeights[name] ?: 0
    }

    override fun setSortWeight(wt: Int) {
        if (wt == 0) {
            sortWeights.remove(name)
        } else {
            sortWeights[name] = wt
        }
    }

    override fun getAddress(): InetSocketAddress? {
        return player?.connection?.address
    }
}

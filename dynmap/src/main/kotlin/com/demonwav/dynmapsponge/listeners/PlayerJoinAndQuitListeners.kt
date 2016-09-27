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

package com.demonwav.dynmapsponge.listeners

import com.demonwav.dynmapsponge.DynmapSponge
import com.demonwav.dynmapsponge.SpongePlayer
import org.dynmap.common.DynmapListenerManager.EventType
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.network.ClientConnectionEvent

class PlayerJoinAndQuitListeners(private val plugin: DynmapSponge) {
    @Listener
    fun onPlayerLogin(event: ClientConnectionEvent.Join) {
        val player = SpongePlayer(event.targetEntity)
        // Give time for other plugins to interact with player
        Sponge.getScheduler().createTaskBuilder().delayTicks(2).execute { ->
            plugin.core.listenerManager.processPlayerEvent(EventType.PLAYER_JOIN, player)
        }.submit(plugin)
    }

    @Listener
    fun onPlayerQuit(event: ClientConnectionEvent.Disconnect) {
        val player = SpongePlayer(event.targetEntity)
        plugin.core.listenerManager.processPlayerEvent(EventType.PLAYER_QUIT, player)
    }
}


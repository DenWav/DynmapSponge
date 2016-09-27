package com.demonwav.dynmapsponge.listeners

import com.demonwav.dynmapsponge.DynmapSponge
import com.demonwav.dynmapsponge.SpongePlayer
import org.dynmap.common.DynmapListenerManager.EventType
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.network.ClientConnectionEvent

class PlayerListeners(private val plugin: DynmapSponge) {
    init {
        Sponge.getEventManager().registerListeners(plugin, this)
    }

    @Listener
    fun onPlayerLogin(event: ClientConnectionEvent.Join) {
        val player = SpongePlayer(event.targetEntity)
        // Give time for other plugins to interact with player
        Sponge.getScheduler().createTaskBuilder().delayTicks(2).execute { task ->
            plugin.core.listenerManager.processPlayerEvent(EventType.PLAYER_JOIN, player)
        }.submit(plugin)
    }

    @Listener
    fun onPlayerQuit(event: ClientConnectionEvent.Disconnect) {
        val player = SpongePlayer(event.targetEntity)
        plugin.core.listenerManager.processPlayerEvent(EventType.PLAYER_QUIT, player)
    }
}


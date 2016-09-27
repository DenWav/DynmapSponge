package com.demonwav.dynmapsponge

import com.google.inject.Inject
import org.dynmap.modsupport.ModSupportImpl
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.plugin.Plugin

@Plugin(
    id = "dynmapsponge",
    name = "Dynmapsponge",
    description = "Dynmap implementation for Sponge",
    authors = arrayOf("DemonWav")
)
class DynmapSponge {

    @Inject
    private val logger: Logger? = null

    @Listener
    fun onServerPreStart(event: GamePreInitializationEvent) {
        ModSupportImpl.init()
    }

    @Listener
    fun onServerStart(event: GameInitializationEvent) {
    }
}

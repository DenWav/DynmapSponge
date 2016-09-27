package com.demonwav.dynmapsponge

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import org.spongepowered.api.text.format.TextStyles

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
    fun onServerStart(event: GameStartedServerEvent) {
    }

    @Listener
    fun onPlayerJoin(event: ClientConnectionEvent.Join, @Getter("getTargetEntity") player: Player) {
        // The text message could be configurable, check the docs on how to do so!
        player.sendMessage(Text.of(TextColors.AQUA, TextStyles.BOLD, "Hi " + player.name))
    }

}

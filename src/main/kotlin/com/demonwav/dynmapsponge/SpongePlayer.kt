package com.demonwav.dynmapsponge

import org.dynmap.DynmapLocation
import org.dynmap.common.DynmapPlayer
import org.spongepowered.api.entity.living.player.Player
import java.net.InetSocketAddress

class SpongePlayer(private val player: Player) : DynmapPlayer {
    override fun isSneaking(): Boolean {
        TODO("not implemented")
    }

    override fun getName(): String {
        TODO("not implemented")
    }

    override fun isOp(): Boolean {
        TODO("not implemented")
    }

    override fun isConnected(): Boolean {
        TODO("not implemented")
    }

    override fun getFirstLoginTime(): Long {
        TODO("not implemented")
    }

    override fun getLastLoginTime(): Long {
        TODO("not implemented")
    }

    override fun hasPrivilege(privid: String?): Boolean {
        TODO("not implemented")
    }

    override fun getHealth(): Double {
        TODO("not implemented")
    }

    override fun hasPermissionNode(node: String?): Boolean {
        TODO("not implemented")
    }

    override fun getArmorPoints(): Int {
        TODO("not implemented")
    }

    override fun getWorld(): String {
        TODO("not implemented")
    }

    override fun isOnline(): Boolean {
        TODO("not implemented")
    }

    override fun sendMessage(msg: String?) {
        TODO("not implemented")
    }

    override fun isInvisible(): Boolean {
        TODO("not implemented")
    }

    override fun getLocation(): DynmapLocation {
        TODO("not implemented")
    }

    override fun getBedSpawnLocation(): DynmapLocation {
        TODO("not implemented")
    }

    override fun getDisplayName(): String {
        TODO("not implemented")
    }

    override fun getSortWeight(): Int {
        TODO("not implemented")
    }

    override fun setSortWeight(wt: Int) {
        TODO("not implemented")
    }

    override fun getAddress(): InetSocketAddress {
        TODO("not implemented")
    }
}

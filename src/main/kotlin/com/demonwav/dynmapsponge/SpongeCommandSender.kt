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

import org.dynmap.common.DynmapCommandSender
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.text.Text

open class SpongeCommandSender(val source: CommandSource) : DynmapCommandSender {
    override fun sendMessage(msg: String?) {
        source.sendMessage(Text.of(msg))
    }

    override fun isOp(): Boolean {
        return source.hasPermission("dynmap.op")
    }

    override fun isConnected(): Boolean {
        return (source as? User)?.isOnline ?: false
    }

    override fun hasPrivilege(privid: String?): Boolean {
        // TODO support perms plugins
        return source.hasPermission(privid)
    }

    override fun hasPermissionNode(node: String?): Boolean {
        return source.hasPermission(node)
    }
}

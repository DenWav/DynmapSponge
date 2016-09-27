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

package com.demonwav.dynmapsponge;

import org.dynmap.DynmapCommonAPI;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * This is the interface representing the published API for the Dynmap plugin for Sponge. Public methods
 * of the DynmapSponge class are subject to change without notice, so be careful with forming dependencies
 * beyond these.
 *
 * This interface is Sponge specific.
 */
public interface DynmapAPI extends DynmapCommonAPI {

    /**
     * Trigger update on tiles associated with given locations.  If two locations provided,
     * the volume is the rectangular prism ("cuboid") with the two locations on opposite corners.
     *
     * @param l0 - first location
     * @param l1 - second location
     * @return number of tiles queued to be rerendered  (@deprecated return value - just returns 1)
     */
    int triggerRenderOfVolume(Location<World> l0, Location<World> l1);

    /**
     * Set player visibility
     * @param player - player
     * @param isVisible - true if visible, false if hidden
     */
    void setPlayerVisibility(Player player, boolean isVisible);

    /**
     * Test if player is visible
     * @return true if visible, false if not
     */
    boolean getPlayerVisibility(Player player);

    /**
     * Post message from player to web
     * @param player - player
     * @param message - message text
     */
    void postPlayerMessageToWeb(Player player, String message);

    /**
     * Post join/quit message for player to web
     * @param player - player
     * @param isJoin - if true, join message; if false, quit message
     */
    void postPlayerJoinQuitToWeb(Player player, boolean isJoin);

    /**
     * Get version of dynmap plugin
     * @return version - format is "major.minor-build" or "major.minor.patch-build"
     */
    String getDynmapVersion();

    /**
     * Set player visibility (transient - if player is configured to be visible, they are hidden if one or more plugins assert their invisiblity)
     * @param player - player ID
     * @param isInvisible - true if asserting player should be invisible, false if no assertion
     * @param plugin - asserting plugin
     */
    void assertPlayerInvisibility(Player player, boolean isInvisible, Object plugin);

    /**
     * Set player visibility (transient - if player is configured to be hidden, they are made visible if one or more plugins assert their visibility))
     * @param player - player
     * @param isVisible - true if asserting that hidden player should be visible, false if no assertion
     * @param plugin - asserting plugin
     */
    void assertPlayerVisibility(Player player, boolean isVisible, Object plugin);
}

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

import org.dynmap.common.BiomeMap
import org.dynmap.renderer.RenderPatchFactory
import org.dynmap.utils.BlockStep
import org.dynmap.utils.MapIterator

open class SpongeMapIterator : MapIterator {
    override fun isEmptySection(): Boolean {
        TODO("not implemented")
    }

    override fun getPatchFactory(): RenderPatchFactory {
        TODO("not implemented")
    }

    override fun getY(): Int {
        TODO("not implemented")
    }

    override fun getSmoothWaterColorMultiplier(): Int {
        TODO("not implemented")
    }

    override fun getSmoothWaterColorMultiplier(colormap: IntArray?): Int {
        TODO("not implemented")
    }

    override fun getBlockTypeIDAt(s: BlockStep?): Int {
        TODO("not implemented")
    }

    override fun getBlockTypeIDAt(xoff: Int, yoff: Int, zoff: Int): Int {
        TODO("not implemented")
    }

    override fun getLastStep(): BlockStep {
        TODO("not implemented")
    }

    override fun getBlockTypeID(): Int {
        TODO("not implemented")
    }

    override fun getBlockKey(): Long {
        TODO("not implemented")
    }

    override fun getSmoothFoliageColorMultiplier(colormap: IntArray?): Int {
        TODO("not implemented")
    }

    override fun getBlockData(): Int {
        TODO("not implemented")
    }

    override fun getBlockDataAt(xoff: Int, yoff: Int, zoff: Int): Int {
        TODO("not implemented")
    }

    override fun getSmoothGrassColorMultiplier(colormap: IntArray?): Int {
        TODO("not implemented")
    }

    override fun getBiome(): BiomeMap {
        TODO("not implemented")
    }

    override fun stepPosition(step: BlockStep?) {
        TODO("not implemented")
    }

    override fun getBlockTileEntityField(fieldId: String?): Any {
        TODO("not implemented")
    }

    override fun getBlockSkyLight(): Int {
        TODO("not implemented")
    }

    override fun getX(): Int {
        TODO("not implemented")
    }

    override fun getZ(): Int {
        TODO("not implemented")
    }

    override fun getSmoothColorMultiplier(colormap: IntArray?, swampcolormap: IntArray?): Int {
        TODO("not implemented")
    }

    override fun setY(y: Int) {
        TODO("not implemented")
    }

    override fun unstepPosition(step: BlockStep?) {
        TODO("not implemented")
    }

    override fun unstepPosition(): BlockStep {
        TODO("not implemented")
    }

    override fun getBlockEmittedLight(): Int {
        TODO("not implemented")
    }

    override fun getWorldHeight(): Int {
        TODO("not implemented")
    }

    override fun getInhabitedTicks(): Long {
        TODO("not implemented")
    }

    override fun initialize(x0: Int, y0: Int, z0: Int) {
        TODO("not implemented")
    }

    override fun getBlockTileEntityFieldAt(fieldId: String?, xoff: Int, yoff: Int, zoff: Int): Any {
        TODO("not implemented")
    }
}

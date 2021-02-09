package org.eln2.mod.cable

import net.minecraft.util.ResourceLocation
import org.eln2.mod.misc.UtilsClient.bindTexture

class CableRenderDescriptor(modName: String, cableTexture: String, var widthPixel: Float, var heightPixel: Float) {
    @JvmField
    var width: Float = widthPixel / 16
    @JvmField
    var height: Float = heightPixel / 16
    var widthDiv2: Float = widthPixel / 16 / 2
    var cableTexture: ResourceLocation = ResourceLocation(modName, cableTexture)

    fun bindCableTexture() {
        bindTexture(cableTexture)
    }
}
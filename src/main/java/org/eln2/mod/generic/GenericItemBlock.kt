package org.eln2.mod.generic

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

class GenericItemBlock(b: Block, var textureIdOffset: Int, ItemName: String, subNames: Array<String>) : ItemBlock(b) {
    var subNames = arrayOf("Copper", "Silver", "Gold")

    override fun getMetadata(damageValue: Int): Int {
        return damageValue
    }

    init {
        this.subNames = subNames
        setHasSubtypes(true)
        unlocalizedName = "wireItemBlock"
    }
}
package org.eln2.mod.node

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock
import org.eln2.mod.node.NodeBlock

class NodeBlockItem(b: Block) : ItemBlock(b) {

    override fun getMetadata(damageValue: Int): Int {
        return damageValue
    }

    val block: NodeBlock
        get() = Block.getBlockFromItem(this) as NodeBlock

    init {
        unlocalizedName = "NodeBlockItem"
    }
}

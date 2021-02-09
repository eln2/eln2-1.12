package org.eln2.mod.gui

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import org.eln2.mod.gui.ISlotSkin.SlotSkin

open class SlotWithSkinAndComment(
    par1iInventory: IInventory,
    par2: Int,
    par3: Int,
    par4: Int,
    override var slotSkin: SlotSkin,
    var comment: Array<String>
) : Slot(par1iInventory, par2, par3, par4), ISlotSkin, ISlotWithComment {

    override fun getComment(list: MutableList<String>) {
        list.addAll(comment)
    }
}
package mods.eln.gui

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import org.eln2.mod.gui.IItemStackFilter
import org.eln2.mod.gui.ISlotSkin.SlotSkin
import org.eln2.mod.gui.SlotWithSkinAndComment

class SlotFilter(
    par1iInventory: IInventory,
    slot: Int,
    x: Int,
    y: Int,
    var stackLimit: Int,
    var itemStackFilter: Array<IItemStackFilter>,
    skin: SlotSkin,
    comment: Array<String>
) : SlotWithSkinAndComment(par1iInventory, slot, x, y, skin, comment) {

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * the armor slots.
     */
    override fun isItemValid(itemStack: ItemStack): Boolean {
        for (filter in itemStackFilter) {
            if (filter.tryItemStack(itemStack)) return true
        }
        return false
    }

    override fun getSlotStackLimit(): Int {
        // return super.getSlotStackLimit();
        return stackLimit
    }

}
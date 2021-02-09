package mods.eln.misc

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.text.ITextComponent

class FakeSideInventory : ISidedInventory {
    override fun getName(): String {
        return "FakeSideInventory"
    }

    override fun hasCustomName(): Boolean {
        return false
    }

    override fun getDisplayName(): ITextComponent {
        TODO("Not yet implemented")
    }

    override fun getSizeInventory(): Int {
        return 0
    }

    override fun isEmpty(): Boolean {
        return true
    }

    override fun getStackInSlot(var1: Int): ItemStack? {
        return null
    }

    override fun decrStackSize(var1: Int, var2: Int): ItemStack? {
        return null
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        return null
    }

    override fun setInventorySlotContents(var1: Int, var2: ItemStack) {}

    override fun getInventoryStackLimit(): Int {
        return 0
    }

    override fun markDirty() {}
    override fun isUsableByPlayer(player: EntityPlayer): Boolean {
        return false
    }

    override fun openInventory(player: EntityPlayer) {}

    override fun closeInventory(player: EntityPlayer) {}

    override fun isItemValidForSlot(var1: Int, var2: ItemStack): Boolean {
        return false
    }

    override fun getField(id: Int): Int = 0

    override fun setField(id: Int, value: Int) {}

    override fun getFieldCount(): Int = 0

    override fun clear() {}

    override fun getSlotsForFace(side: EnumFacing): IntArray {
        return intArrayOf()
    }

    override fun canInsertItem(index: Int, itemStackIn: ItemStack, direction: EnumFacing): Boolean {
        return false
    }

    override fun canExtractItem(index: Int, stack: ItemStack, direction: EnumFacing): Boolean {
        return false
    }

    companion object {
        @JvmStatic
        val instance = FakeSideInventory()
    }
}

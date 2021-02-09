package org.eln2.mod.generic

import mods.eln.gui.SlotWithSkin
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import org.eln2.mod.gui.ISlotSkin
import org.eln2.mod.gui.ISlotWithComment

class GenericItemUsingDamageSlot : SlotWithSkin, ISlotWithComment {
    var comment: Array<String>
    var descriptorClassList: Array<Class<*>>

    override fun getSlotStackLimit(): Int = slotStackLimitField

    var slotStackLimitField: Int = 0

    constructor(
            inventory: IInventory,
            slot: Int,
            x: Int,
            y: Int,
            stackLimit: Int,
            descriptorClassList: Array<Class<*>>,
            skin: ISlotSkin.SlotSkin,
            comment: Array<String>
    ) : super(inventory, slot, x, y, skin) {
        slotStackLimitField = stackLimit
        this.descriptorClassList = descriptorClassList
        this.comment = comment
    }

    constructor(
            inventory: IInventory,
            slot: Int,
            x: Int,
            y: Int,
            stackLimit: Int,
            descriptorClassList: Class<*>,
            skin: ISlotSkin.SlotSkin,
            comment: Array<String>
    ) : super(inventory, slot, x, y, skin) {
        slotStackLimitField = stackLimit
        this.descriptorClassList = arrayOf(descriptorClassList)
        this.comment = comment
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    override fun isItemValid(itemStack: ItemStack): Boolean {
        if (itemStack.item !is GenericItemUsingDamage<*>) return false
        val descriptor: GenericItemUsingDamageDescriptor = (itemStack.item as GenericItemUsingDamage<*>).getDescriptor(itemStack)?: return false
        for (classFilter in descriptorClassList) {
            var c: Class<*>? = descriptor.javaClass
            while (c != null) {
                if (c == classFilter) return true
                c = c.superclass
            }
        }
        return false
    }

    override fun getComment(list: MutableList<String>) {
        list.addAll(comment)
    }
}
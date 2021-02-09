package org.eln2.mod.gui

import net.minecraft.item.ItemStack

interface IItemStackFilter {
    fun tryItemStack(itemStack: ItemStack?): Boolean
}

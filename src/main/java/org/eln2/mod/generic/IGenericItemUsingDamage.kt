package org.eln2.mod.generic

import net.minecraft.item.ItemStack

interface IGenericItemUsingDamage {
    fun getDescriptor(damage: Int): GenericItemUsingDamageDescriptor?
    fun getDescriptor(itemStack: ItemStack?): GenericItemUsingDamageDescriptor?
}
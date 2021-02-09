package org.eln2.mod.generic

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class GenericCreativeTab(label: String, var item: Item) : CreativeTabs(label) {

    @SideOnly(Side.CLIENT)
    override fun getTabIconItem(): ItemStack {
        return item.defaultInstance.copy()
    }
}
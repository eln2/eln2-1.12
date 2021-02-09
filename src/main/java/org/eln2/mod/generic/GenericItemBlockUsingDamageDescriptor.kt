package org.eln2.mod.generic

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.eln2.mod.Eln2Config
import java.util.*

open class GenericItemBlockUsingDamageDescriptor @JvmOverloads constructor(name: String, iconName: String = name) {
    var iconName: String? = null
    //var iconIndex: IIcon? = null
    var name: String
    var parentItem: Item? = null
    var parentItemDamage = 0
    fun setDefaultIcon(name: String) {
        val iconName = name.replace(" ".toRegex(), "").toLowerCase()
        //Utils.println("Icon Name: " + iconName);
        if (Eln2Config.noSymbols &&
                javaClass.classLoader.getResource("assets/eln/textures/blocks/$iconName-ni.png") != null) {
            this.iconName = "$iconName-ni"
        } else {
            this.iconName = iconName
        }
    }

    val defaultNBT: NBTTagCompound?
        get() = null

    open fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag, list: MutableList<String>) {}

    open fun onItemUseFirst(player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, hand: EnumHand): EnumActionResult {
        return EnumActionResult.FAIL
    }
/*
    @SideOnly(value = Side.CLIENT)
    fun updateIcons(iconRegister: IIconRegister) {
        iconIndex = iconRegister.registerIcon("eln:$iconName")
    }

    val icon: IIcon?
        get() = iconIndex*/

    fun getName(stack: ItemStack?): String {
        return name
    }

    fun setParent(item: Item?, damage: Int) {
        parentItem = item
        parentItemDamage = damage
    }

    fun onEntityItemUpdate(entityItem: EntityItem?): Boolean {
        return false
    }

    companion object {
        var INVALID_NAME = "\$NO_DESCRIPTOR"
        var byName = HashMap<String, GenericItemBlockUsingDamageDescriptor>()
        fun getByName(name: String): GenericItemBlockUsingDamageDescriptor? {
            return byName[name]
        }

        fun getDescriptor(stack: ItemStack?): GenericItemBlockUsingDamageDescriptor? {
            if (stack == null) return null
            val item: Item = stack.item
            if (item !is GenericItemBlockUsingDamage<*>) return null
            val genItem: GenericItemBlockUsingDamage<*> = item
            return genItem.getDescriptor(stack)
        }

        fun getDescriptor(stack: ItemStack?, extendClass: Class<*>): GenericItemBlockUsingDamageDescriptor? {
            val desc = getDescriptor(stack) ?: return null
            return if (extendClass.isAssignableFrom(desc.javaClass)) desc else null
        }
    }

    init {
        setDefaultIcon(iconName)
        this.name = name
        byName[name] = this
    }
}
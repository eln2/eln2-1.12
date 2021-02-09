package org.eln2.mod.generic

import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
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
import java.util.*

open class GenericItemUsingDamageDescriptor {

    @JvmOverloads
    constructor(name: String, iconName: String = name) {
        setDefaultIcon(iconName)
        this.name = name
        byName[name] = this
    }

    var IconName: String? = null
    //open var icon: IIcon? = null
    @JvmField
    var name: String
    @JvmField
    var voltageLevelColor = VoltageLevelColor.None
    @JvmField
    var parentItem: Item? = null
    @JvmField
    var parentItemDamage = 0
    fun setDefaultIcon(name: String) {
        IconName = "eln:" + name.replace(" ".toRegex(), "").toLowerCase()
    }

    open fun getDefaultNBT(): NBTTagCompound? = null

    open fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag, list: MutableList<String>) {}

    open fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack>? {
        return null
    }

    open fun onItemRightClick(s: ItemStack, w: World, p: EntityPlayer): ItemStack {
        return s
    }

    fun getSubItems(list: MutableList<ItemStack>) = list.add(newItemStack(1))

/*
    @SideOnly(value = Side.CLIENT)
    fun updateIcons(iconRegister: IIconRegister) {
        icon = iconRegister.registerIcon(IconName)
    }*/

    open fun getName(stack: ItemStack): String? {
        return name
    }

    open fun setParent(item: Item?, damage: Int) {
        parentItem = item
        parentItemDamage = damage
    }

    open fun newItemStack(size: Int): ItemStack {
        val stack = ItemStack(parentItem, size, parentItemDamage)
        stack.tagCompound = getDefaultNBT()
        return stack
    }

    fun newItemStack(): ItemStack {
        return newItemStack(1)
    }

    fun checkSameItemStack(stack: ItemStack?): Boolean {
        if (stack == null) return false
        return !(stack.item !== this.parentItem || stack.itemDamage != parentItemDamage)
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    open fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        return EnumActionResult.FAIL
    }

/*
    open fun handleRenderType(item: ItemStack?, type: ItemRenderType?): Boolean {
        return voltageLevelColor != VoltageLevelColor.None
    }

    open fun shouldUseRenderHelper(type: ItemRenderType?, item: ItemStack?, helper: ItemRendererHelper?): Boolean {
        return false
    }

    open fun renderItem(type: ItemRenderType?, item: ItemStack?, vararg data: Any?) {
        if (icon == null) return
        voltageLevelColor.drawIconBackground(type!!)
        // remove "eln:" to add the full path replace("eln:", "textures/blocks/") + ".png";
        val icon = icon!!.iconName.substring(4)
        UtilsClient.drawIcon(type, ResourceLocation("eln", "textures/items/$icon.png"))
    }

    open fun onUpdate(stack: ItemStack, world: World, entity: Entity, par4: Int, par5: Boolean) {}
    protected fun getNbt(stack: ItemStack): NBTTagCompound {
        var nbt = stack.tagCompound
        if (nbt == null) {
            stack.tagCompound = getDefaultNBT().also { nbt = it }
        }
        return nbt
    }
*/
    open fun onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        return false
    }

    open fun onDroppedByPlayer(item: ItemStack, player: EntityPlayer?): Boolean {
        return true
    }

    open fun onEntitySwing(entityLiving: EntityLivingBase?, stack: ItemStack?): Boolean {
        return false
    }

    open fun onBlockStartBreak(itemstack: ItemStack, pos: BlockPos, player: EntityPlayer): Boolean {
        return false
    }

    open fun onUpdate(stack: ItemStack, world: World, entity: Entity, par4: Int, par5: Boolean) {}

    companion object {
        var byName = HashMap<String, GenericItemUsingDamageDescriptor>()
        @JvmField
        var INVALID_NAME = "\$NO_DESCRIPTOR"
        @JvmStatic
        fun getByName(name: String?): GenericItemUsingDamageDescriptor? {
            return byName[name]
        }

        @JvmStatic
        fun getDescriptor(stack: ItemStack?): GenericItemUsingDamageDescriptor? {
            if (stack == null) return null
            return if (stack.item !is GenericItemUsingDamage<*>) null else (stack.item as GenericItemUsingDamage<*>).getDescriptor(stack)
        }

        @JvmStatic
        fun getDescriptor(stack: ItemStack?, extendClass: Class<*>): GenericItemUsingDamageDescriptor? {
            val desc = getDescriptor(stack) ?: return null
            return if (!extendClass.isAssignableFrom(desc.javaClass)) null else desc
        }
    }

}

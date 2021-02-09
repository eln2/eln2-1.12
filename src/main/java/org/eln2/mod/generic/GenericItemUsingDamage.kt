package org.eln2.mod.generic

import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.eln2.mod.misc.UtilsClient
import java.util.*

open class GenericItemUsingDamage<Descriptor : GenericItemUsingDamageDescriptor> : Item(), IGenericItemUsingDamage {
    var subItemList = Hashtable<Int, Descriptor>()
    var orderList = ArrayList<Int>()
    var defaultElement: Descriptor? = null

    fun addWithoutRegistry(damage: Int, descriptor: Descriptor) {
        subItemList[damage] = descriptor
        val stack = ItemStack(this, 1, damage)
        //LanguageRegistry.addName(stack, descriptor!!.name)
        descriptor.setParent(this, damage)
    }

    fun addElement(damage: Int, descriptor: Descriptor) {
        subItemList[damage] = descriptor
        val stack = ItemStack(this, 1, damage)
        //LanguageRegistry.addName(stack, descriptor!!.name)
        orderList.add(damage)
        descriptor.setParent(this, damage)
        ForgeRegistries.ITEMS.register(descriptor.newItemStack(1).item)
        //GameRegistry.registerCustomItemStack(descriptor.name, descriptor.newItemStack(1))
    }

    override fun getDescriptor(damage: Int): Descriptor? {
        return subItemList[damage]
    }

    override fun getDescriptor(itemStack: ItemStack?): Descriptor? {
        if (itemStack == null) return defaultElement
        return if (itemStack.getItem() !== this) defaultElement else getDescriptor(itemStack.getItemDamage())
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack>? {
        return getDescriptor(playerIn.heldItemMainhand)?.onItemRightClick(worldIn, playerIn, handIn)
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        return getDescriptor(stack)?.name?.replace(" ", "_")?: this.javaClass.name
    }
    /*
    fun getIconFromDamage(damage: Int): IIcon? {
        val desc: GenericItemUsingDamageDescriptor = getDescriptor(damage)
        return if (desc != null) {
            getDescriptor(damage).icon
        } else null
    }

    @SideOnly(value = Side.CLIENT)
    fun registerIcons(iconRegister: IIconRegister?) {
        for (descriptor in subItemList.values) {
            descriptor.updateIcons(iconRegister)
        }
    }*/

    @SideOnly(Side.CLIENT)
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        // You can also take a more direct approach and do each one individual but I prefer the lazy / right way
        for (id in orderList) {
            subItemList[id]!!.getSubItems(items)
        }
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        val desc: Descriptor = getDescriptor(stack) ?: return
        val list = mutableListOf<String>()
        desc.addInformation(stack, worldIn, tooltip, flagIn, list)
        UtilsClient.showItemTooltip(list, tooltip)
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val d: GenericItemUsingDamageDescriptor = getDescriptor(player.heldItemMainhand) ?: return EnumActionResult.FAIL
        //val d2: GenericItemBlockUsingDamageDescriptor = getDescriptor(player.heldItemOffhand) ?: return EnumActionResult.FAIL
        return d.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ)
    }

    override fun onEntitySwing(entityLiving: EntityLivingBase, stack: ItemStack): Boolean {
        val d: GenericItemUsingDamageDescriptor = getDescriptor(stack)
                ?: return super.onEntitySwing(entityLiving, stack)
        return d.onEntitySwing(entityLiving, stack)
    }

    override fun onBlockStartBreak(itemstack: ItemStack, pos: BlockPos, player: EntityPlayer): Boolean {
        val d: GenericItemUsingDamageDescriptor = getDescriptor(itemstack)
                ?: return super.onBlockStartBreak(itemstack, pos, player)
        return d.onBlockStartBreak(itemstack, pos, player)
    }

    override fun onUpdate(stack: ItemStack, world: World, entity: Entity, par4: Int, par5: Boolean) {
        if (world.isRemote) {
            return
        }
        val d: GenericItemUsingDamageDescriptor = getDescriptor(stack) ?: return
        d.onUpdate(stack, world, entity, par4, par5)
    }

    override fun canHarvestBlock(blockIn: IBlockState): Boolean {
        return true
    }

    override fun onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (worldIn.isRemote) {
            return false
        }
        val d: GenericItemUsingDamageDescriptor = getDescriptor(stack) ?: return true
        return d.onBlockDestroyed(stack, worldIn, state, pos, entityLiving)
    }

    override fun onDroppedByPlayer(item: ItemStack, player: EntityPlayer): Boolean {
        val d: GenericItemUsingDamageDescriptor = getDescriptor(item) ?: return super.onDroppedByPlayer(item, player)
        return d.onDroppedByPlayer(item, player)
    }

    init {
        hasSubtypes = true
    }
}
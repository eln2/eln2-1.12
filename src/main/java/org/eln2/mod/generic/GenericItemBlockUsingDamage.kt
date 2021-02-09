package org.eln2.mod.generic

import net.minecraft.block.Block
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.eln2.mod.misc.Utils
import org.eln2.mod.misc.UtilsClient
import java.util.*
import kotlin.collections.ArrayList

class GenericItemBlockUsingDamage<Descriptor : GenericItemBlockUsingDamageDescriptor>(b: Block) : ItemBlock(b) {
    var subItemList = Hashtable<Int, Descriptor>()
    var orderList = ArrayList<Int>()
    var descriptors = ArrayList<Descriptor>()
    var defaultElement: Descriptor? = null

    fun doubleEntry(src: Int, dst: Int) {
        subItemList[dst] = subItemList[src]
    }

    fun addDescriptor(damage: Int, descriptor: Descriptor) {
        subItemList[damage] = descriptor
        val stack = ItemStack(this, 1, damage)
        stack.tagCompound = descriptor.defaultNBT
        //LanguageRegistry.addName(stack, descriptor.name);
        orderList.add(damage)
        descriptors.add(descriptor)
        descriptor.setParent(this, damage)
        ForgeRegistries.ITEMS.register(this)
        //GameRegistry.registerCustomItemStack(descriptor.name, descriptor.newItemStack(1))
    }

    fun addWithoutRegistry(damage: Int, descriptor: Descriptor) {
        subItemList[damage] = descriptor
        val stack = ItemStack(this, 1, damage)
        stack.tagCompound = descriptor.defaultNBT
        descriptor.setParent(this, damage)
    }

    fun getDescriptor(damage: Int): Descriptor? {
        return subItemList[damage]
    }

    fun getDescriptor(itemStack: ItemStack): Descriptor? {
        if (itemStack == null) return defaultElement
        return if (itemStack.item !== this) defaultElement else getDescriptor(itemStack.itemDamage)
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        return getDescriptor(stack)?.name?.replace(" ","_")?: return this.javaClass.name
    }

    /*
    fun getIconFromDamage(damage: Int): IIcon? {
        val desc: Descriptor = getDescriptor(damage) ?: return null
        return desc.icon
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
        //for(Entry<Integer, Descriptor> entry : subItemList.entrySet())
        for (id in orderList) {
            val stack: ItemStack = Utils.newItemStack(id, 1, id)
            stack.tagCompound = subItemList[id]?.defaultNBT
            items.add(stack)
        }
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        val desc: Descriptor = getDescriptor(stack) ?: return
        val list = mutableListOf<String>()
        desc.addInformation(stack, worldIn, tooltip, flagIn, list)
        UtilsClient.showItemTooltip(list, tooltip)
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        val desc: Descriptor = getDescriptor(entityItem.item)?: return false
        return desc.onEntityItemUpdate(entityItem)
    }

    override fun onItemUseFirst(player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, hand: EnumHand): EnumActionResult {
        val desc: Descriptor = getDescriptor(player.heldItemMainhand)?: return EnumActionResult.FAIL
        return desc.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand)
    }

    init {
        hasSubtypes = true
    }
}
package org.eln2.mod.generic

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.common.ISpecialArmor

class SharedItem : GenericItemUsingDamage<GenericItemUsingDamageDescriptor>(), ISpecialArmor {
    /*
    fun handleRenderType(item: ItemStack?, type: ItemRenderType?): Boolean {
        val d: GenericItemUsingDamageDescriptor = getDescriptor(item) ?: return false
        return d.handleRenderType(item, type)
    }

    fun shouldUseRenderHelper(type: ItemRenderType?, item: ItemStack?, helper: ItemRendererHelper?): Boolean {
        val d: GenericItemUsingDamageDescriptor = getDescriptor(item) ?: return false
        return d.shouldUseRenderHelper(type, item, helper)
    }*/

    /*
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
    	return true;
    }
    
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer) {
		return "eln:textures/armor/copper_layer_1.png";
	}

    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return new ModelBiped();
    }*//*
    fun renderItem(type: ItemRenderType?, item: ItemStack?, vararg data: Any?) {
        Minecraft.getMinecraft().mcProfiler.startSection("SharedItem")
        when (type) {
            ENTITY -> {
            }
            EQUIPPED -> {
            }
            FIRST_PERSON_MAP -> {
            }
            INVENTORY -> {
            }
            else -> {
            }
        }
        val d: GenericItemUsingDamageDescriptor = getDescriptor(item)
        if (d != null) {
            d.renderItem(type, item, data)
        }
        Minecraft.getMinecraft().mcProfiler.endSection()
    }*/

    override fun getProperties(player: EntityLivingBase,
                               armor: ItemStack, source: DamageSource, damage: Double, slot: Int): ISpecialArmor.ArmorProperties {
        return ISpecialArmor.ArmorProperties(10, 1.0, 10000)
    }

    override fun getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int): Int {
        return 4
    }

    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack,
                             source: DamageSource, damage: Int, slot: Int) {
    }
}
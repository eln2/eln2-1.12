package org.eln2.mod.node.transparent

import org.eln2.mod.node.transparent.TransparentNode.FrontType
import net.minecraft.block.Block
import net.minecraft.block.BlockHopper
import net.minecraft.entity.EntityLivingBase
import org.eln2.mod.generic.GenericItemBlockUsingDamageDescriptor
import org.eln2.mod.misc.Coordinate
import org.eln2.mod.misc.Direction
import org.eln2.mod.misc.Obj3D
import org.eln2.mod.misc.Utils.entityLivingHorizontalViewDirection
import org.eln2.mod.misc.Utils.entityLivingViewDirection
import org.lwjgl.opengl.GL11

open class TransparentNodeDescriptor @JvmOverloads constructor(
    name: String,
    var ElementClass: Class<*>,
    var RenderClass: Class<*>,
    val tileEntityMetaTag: EntityMetaTag = EntityMetaTag.Basic) : GenericItemBlockUsingDamageDescriptor(name) { //}, IItemRenderer {
    @JvmField
    protected var voltageLevelColor = VoltageLevelColor.None
    //var ghostGroup: GhostGroup? = null
/*
    override fun handleRenderType(item: ItemStack, type: ItemRenderType): Boolean {
        return voltageLevelColor !== VoltageLevelColor.None
    }

    override fun shouldUseRenderHelper(type: ItemRenderType, item: ItemStack, helper: ItemRendererHelper): Boolean {
        return false
    }

    open fun shouldUseRenderHelperEln(type: ItemRenderType?, item: ItemStack?, helper: ItemRendererHelper?): Boolean {
        return false
    }*//*

    override fun renderItem(type: ItemRenderType, item: ItemStack, vararg data: Any) {
        if (icon == null) return
        voltageLevelColor.drawIconBackground(type)

        // remove "eln:" to add the full path replace("eln:", "textures/blocks/") + ".png";
        val icon = icon.iconName.substring(4)
        drawIcon(type, ResourceLocation("eln", "textures/blocks/$icon.png"))
    }*/

    fun objItemScale(obj: Obj3D?) {
        if (obj == null) return
        var factor = obj.yDim * 0.6f
        factor = factor.coerceAtLeast((obj.zMax.coerceAtLeast(-obj.xMin) + Math.max(obj.xMax, -obj.zMin)) * 0.7f)
        factor = 1f / factor
        GL11.glScalef(factor, factor, factor)
        GL11.glTranslatef((obj.zMin.coerceAtMost(obj.xMin) + obj.xMax.coerceAtLeast(obj.zMax)) / 2 - (obj.xMax + obj.xMin) / 2, 1.0f - (obj.xMax + obj.xMin) / 2 - (obj.zMax + obj.zMin) / 2 - (obj.yMax + obj.yMin) / 2, 0.0f)
    }

    open val frontType: FrontType?
        get() = FrontType.PlayerViewHorizontal

    open fun mustHaveFloor(): Boolean {
        return true
    }

    open fun mustHaveCeiling(): Boolean {
        return false
    }

    open fun mustHaveWall(): Boolean {
        return false
    }

    open fun mustHaveWallFrontInverse(): Boolean {
        return false
    }

    open fun checkCanPlace(coord: Coordinate?, front: Direction): String? {
        val no = "Can't place block at side"

        var block: Block
        if (mustHaveFloor()) {
            val temp = Coordinate(coord!!)
            temp.move(Direction.YN)
            block = temp.block
            if (!block.isOpaqueCube(temp.block.defaultState) && block !is BlockHopper) return no//I18N.tr("You can't place this block at this side")
        }
        if (mustHaveCeiling()) {
            val temp = Coordinate(coord!!)
            temp.move(Direction.YP)
            block = temp.block
            if (!block.isOpaqueCube(temp.block.defaultState)) return no//I18N.tr("You can't place this block at this side")
        }
        if (mustHaveWallFrontInverse()) {
            val temp = Coordinate(coord!!)
            temp.move(front.inverse)
            block = temp.block
            if (!block.isOpaqueCube(temp.block.defaultState)) return no//I18N.tr("You can't place this block at this side")
        }
        if (mustHaveWall()) {
            var wall = false
            var temp = Coordinate(coord!!)
            temp.move(Direction.XN)
            block = temp.block
            if (block.isOpaqueCube(temp.block.defaultState)) wall = true
            temp = Coordinate(coord)
            temp.move(Direction.XP)
            block = temp.block
            if (block.isOpaqueCube(temp.block.defaultState)) wall = true
            temp = Coordinate(coord)
            temp.move(Direction.ZN)
            block = temp.block
            if (block.isOpaqueCube(temp.block.defaultState)) wall = true
            temp = Coordinate(coord)
            temp.move(Direction.ZP)
            block = temp.block
            if (block.isOpaqueCube(temp.block.defaultState)) wall = true
            if (!wall) return no//I18N.tr("You can't place this block at this side")
        }
        //val ghostGroup = getGhostGroupFront(front)
        //if (ghostGroup != null && !ghostGroup.canBePloted(coord!!)) I18N.tr("Not enough space for this block") else null
        return null
    }

    open fun getFrontFromPlace(side: Direction, entityLiving: EntityLivingBase?): Direction? {
        var front = Direction.XN
        when (frontType) {
            FrontType.BlockSide -> front = side
            FrontType.BlockSideInv -> front = side.inverse
            FrontType.PlayerView -> front = entityLivingViewDirection(entityLiving!!).inverse
            FrontType.PlayerViewHorizontal -> front = entityLivingHorizontalViewDirection(entityLiving!!).inverse
        }
        return front
    }
/*
    fun getGhostGroupFront(front: Direction?): GhostGroup? {
        return if (ghostGroup == null) null else ghostGroup!!.newRotate(front)
    }
*/
    val ghostGroupUuid: Int
        get() = -1
    open val spawnDeltaX: Int
        get() = 0
    open val spawnDeltaY: Int
        get() = 0
    open val spawnDeltaZ: Int
        get() = 0

}

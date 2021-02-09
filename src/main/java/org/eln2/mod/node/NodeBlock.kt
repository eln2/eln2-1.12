package org.eln2.mod.node

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import org.eln2.mod.misc.Direction
import org.eln2.mod.misc.Utils.isRemote

abstract class NodeBlock(material: Material, tileEntityClass: Class<*>, blockItemNbr: Int) : Block(material) {

    var blockItemNbr: Int
    var tileEntityClass: Class<*>

    override fun setHardness(hardness: Float): Block {
        this.blockHardness = hardness
        return this
    }

    /*
    override fun isProvidingWeakPower(block: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Int {
        val entity = block.getTileEntity(x, y, z) as NodeBlockEntity
        return entity.isProvidingWeakPower(fromIntMinecraftSide(side))
    }*/

    override fun canConnectRedstone(state: IBlockState, world: IBlockAccess, pos: BlockPos, side: EnumFacing?): Boolean {
        val entity = world.getTileEntity(pos) as NodeBlockEntity
        return entity.canConnectRedstone(Direction.XN)
    }

    override fun isOpaqueCube(state: IBlockState): Boolean {
        return true
    }

    override fun isNormalCube(state: IBlockState): Boolean {
        return true
    }

    override fun getRenderType(state: IBlockState): EnumBlockRenderType {
        return EnumBlockRenderType.INVISIBLE
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos): Int {
        val entity: TileEntity? = world.getTileEntity(pos)
        if (entity is NodeBlockEntity) {
            return entity.lightValue
        }
        return 0
    }

    //client server
    open fun onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, front: Direction?, entityLiving: EntityLivingBase?, metadata: Int): Boolean {
        val tileEntity = world.getTileEntity(BlockPos(x, y, z)) as NodeBlockEntity
        tileEntity.onBlockPlacedBy(front, entityLiving, metadata)
        return true
    }

    //server
    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        if (!worldIn.isRemote) {
            val entity = worldIn.getTileEntity(pos) as NodeBlockEntity
            entity.onBlockAdded()
        }
    }

    //server
    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val entity = worldIn.getTileEntity(pos) as NodeBlockEntity
        entity.onBreakBlock()
        super.breakBlock(worldIn, pos, state)
    }

    override fun onNeighborChange(world: IBlockAccess, pos: BlockPos, neighbor: BlockPos) {
        if (!isRemote(world)) {
            val entity = world.getTileEntity(pos) as NodeBlockEntity
            entity.onNeighborBlockChange()
        }
    }

    // TODO: This is the new block state code for 1.12. We could store stuff here (this is on drop), but I think not.
    override fun damageDropped(state: IBlockState): Int {
        return 0
    }
    /*
    override fun damageDropped(metadata: Int): Int {
        return metadata
    }
     */

    //client server
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val entity = worldIn.getTileEntity(pos) as NodeBlockEntity
        return false//entity.onBlockActivated()
    }

    override fun hasTileEntity(state: IBlockState): Boolean {
        return true
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return tileEntityClass.getConstructor().newInstance() as TileEntity
    }

    init {
        unlocalizedName = "NodeBlock"
        this.tileEntityClass = tileEntityClass
        useNeighborBrightness = true
        this.blockItemNbr = blockItemNbr
        blockHardness = 1.0f
        blockResistance = 1.0f
    }
}

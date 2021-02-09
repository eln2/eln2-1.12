package org.eln2.mod.misc

import jdk.nashorn.internal.ir.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import org.lwjgl.opengl.GL11

/**
 * Represents the 6 possible directions along the axis of a block.
 */
enum class Direction(var int: Int) {
    /**
     * -X
     */
    XN(0),

    /**
     * +X
     */
    XP(1),

    /**
     * -Y
     */
    YN(2),  //MC-Code starts with 0 here

    /**
     * +Y
     */
    YP(3),  // 1...

    /**
     * -Z
     */
    ZN(4),

    /**
     * +Z
     */
    ZP(5);

    val isNotY: Boolean
        get() = this != YP && this != YN
    val isY: Boolean
        get() = this == YP || this == YN

    fun applyTo(vector: DoubleArray, distance: Double) {
        if (int == 0) vector[0] -= distance
        if (int == 1) vector[0] += distance
        if (int == 2) vector[1] -= distance
        if (int == 3) vector[1] += distance
        if (int == 4) vector[2] -= distance
        if (int == 5) vector[2] += distance
    }

    fun applyTo(vector: IntArray, distance: Int) {
        if (int == 0) vector[0] -= distance
        if (int == 1) vector[0] += distance
        if (int == 2) vector[1] -= distance
        if (int == 3) vector[1] += distance
        if (int == 4) vector[2] -= distance
        if (int == 5) vector[2] += distance
    }

    val horizontalIndex: Int
        get() = when (this) {
            XN -> 0
            XP -> 1
            YN -> 0
            YP -> 0
            ZN -> 2
            ZP -> 3
            else -> 0
        }
    /*public CoordinateTuple ApplyToCoordinates(CoordinateTuple coordinates) {
		CoordinateTuple ret = new CoordinateTuple(coordinates);

		ret.coords[dir / 2] += GetSign();

		return ret;
	}*/
    /**
     * Get the tile entity next to a tile entity following this direction.
     *
     * @param tileEntity tile entity to check
     * @return Adjacent tile entity or null if none exists
     */
    fun applyToTileEntity(tileEntity: TileEntity?): TileEntity? {
        if (tileEntity == null) return null

        val coords = intArrayOf(tileEntity.pos.x, tileEntity.pos.y, tileEntity.pos.z)
        coords[int / 2] += sign
        val crd = Coordinate(coords[0], coords[1], coords[2], tileEntity.world)
        //tileEntity.world.blockExists(coords[0], coords[1], coords[2])
        return if (crd.blockExist) {
            val blockPos = BlockPos(coords[0], coords[1], coords[2])
            tileEntity.world.getTileEntity(blockPos)
        } else {
            null
        }
    }

    /*
    fun applyToTileEntityAndSameClassThan(tileEntity: TileEntity?, c: Class<*>?): TileEntity? {
        if (tileEntity == null) return null
        val findedEntity = applyToTileEntity(tileEntity) ?: return null
        return if (!isTheClass(findedEntity, c!!)) null else findedEntity
    }*/

    /**
     * Get the inverse of this direction (XN -> XP, XP -> XN, etc.)
     *
     * @return Inverse direction
     */
    val inverse: Direction
        get() {
            val inverseDir = int - sign
            for (direction in values()) {
                if (direction.int == inverseDir) return direction
            }
            return this
        }

    /**
     * Convert this direction to a Minecraft side value.
     *
     * @return Minecraft side value
     */
    fun toSideValue(): Int {
        return (int + 4) % 6
    }

    /**
     * Determine direction sign (N for negative or P for positive).
     *
     * @return -1 if the direction is negative, +1 if the direction is positive
     */
    private val sign: Int
        get() = int % 2 * 2 - 1

    fun renderBlockFace(x: Int, y: Int, spriteDim: Float) {
        when (this) {
            Direction.XN -> {
                GL11.glNormal3f(-1.0f, 0.0f, 0.0f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(-0.5f, 0.5f, 0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(-0.5f, 0.5f, -0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(-0.5f, -0.5f, -0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(-0.5f, -0.5f, 0.5f)
            }
            XP -> {
                GL11.glNormal3f(1.0f, 0.0f, 0.0f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(0.5f, 0.5f, -0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(0.5f, 0.5f, 0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(0.5f, -0.5f, 0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(0.5f, -0.5f, -0.5f)
            }
            Direction.YN -> {
                GL11.glNormal3f(0.0f, -1.0f, 0.0f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(0.5f, -0.5f, -0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(0.5f, -0.5f, 0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(-0.5f, -0.5f, 0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(-0.5f, -0.5f, -0.5f)
            }
            YP -> {
                GL11.glNormal3f(0.0f, 1.0f, 0.0f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(-0.5f, 0.5f, -0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(-0.5f, 0.5f, 0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(0.5f, 0.5f, 0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(0.5f, 0.5f, -0.5f)
            }
            ZN -> {
                GL11.glNormal3f(0.0f, 0.0f, -1.0f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(-0.5f, 0.5f, -0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(0.5f, 0.5f, -0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(0.5f, -0.5f, -0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(-0.5f, -0.5f, -0.5f)
            }
            ZP -> {
                GL11.glNormal3f(0.0f, 0.0f, 1.0f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(0.5f, 0.5f, 0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 0) * spriteDim)
                GL11.glVertex3f(-0.5f, 0.5f, 0.5f)
                GL11.glTexCoord2f((x + 0) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(-0.5f, -0.5f, 0.5f)
                GL11.glTexCoord2f((x + 1) * spriteDim, (y + 1) * spriteDim)
                GL11.glVertex3f(0.5f, -0.5f, 0.5f)
            }
            else -> {
            }
        }
    }

    fun right(): Direction {
        return when (this) {
            XN -> ZP
            XP -> ZN
            YN -> ZN
            YP -> ZP
            ZN -> XN
            ZP -> XP
        }
    }

    fun left(): Direction {
        return right().inverse
    }

    fun up(): Direction {
        return when (this) {
            XN -> YP
            XP -> YP
            YN -> XP
            YP -> XP
            ZN -> YP
            ZP -> YP
        }
    }

    fun down(): Direction {
        return up().inverse
    }

    fun back(): Direction {
        return inverse
    }

    fun applyLRDU(lrdu: LRDU): Direction {
        return when (lrdu) {
            LRDU.Down -> down()
            LRDU.Left -> left()
            LRDU.Right -> right()
            LRDU.Up -> up()
        }
    }

    fun getLRDUGoingTo(target: Direction): LRDU? {
        for (lrdu in LRDU.values()) {
            if (target == applyLRDU(lrdu)) return lrdu
        }
        return null
    }

    fun glRotateXnRef() {
        //toCheck
        when (this) {
            XN -> {
            }
            XP ->                 //GL11.glScalef(-1f, 1, -1f);
                GL11.glRotatef(180f, 0f, 1f, 0f)
            YN -> {
                GL11.glRotatef(90f, 0f, 0f, 1f)
                GL11.glScalef(1f, -1f, -1f)
            }
            YP -> GL11.glRotatef(90f, 0f, 0f, -1f)
            ZN -> GL11.glRotatef(270f, 0f, 1f, 0f)
            ZP -> GL11.glRotatef(90f, 0f, 1f, 0f)
            else -> {
            }
        }
    }

    fun glRotateXnRefInv() {
        //toCheck
        when (this) {
            XN -> {
            }
            XP ->                 //GL11.glScalef(-1f, 1, -1f);
                GL11.glRotatef(180f, 0f, -1f, 0f)
            YN -> {
                GL11.glScalef(1f, -1f, -1f)
                GL11.glRotatef(90f, 0f, 0f, -1f)
            }
            YP -> GL11.glRotatef(90f, 0f, 0f, 1f)
            ZN -> GL11.glRotatef(270f, 0f, -1f, 0f)
            ZP -> GL11.glRotatef(90f, 0f, -1f, 0f)
            else -> {
            }
        }
    }

    fun glRotateZnRef() {
        //toCheck
        when (this) {
            XN -> GL11.glRotatef(90f, 0f, 1f, 0f)
            XP -> GL11.glRotatef(90f, 0f, -1f, 0f)
            YN -> {
                GL11.glRotatef(90f, 1f, 0f, 0f)
                GL11.glScalef(1f, -1f, 1f)
            }
            YP -> {
                GL11.glRotatef(90f, 1f, 0f, 0f)
                GL11.glScalef(1f, 1f, 1f)
            }
            ZN -> {
            }
            ZP -> GL11.glRotatef(180f, 0f, 1f, 0f)
            else -> {
            }
        }
    }

    fun getTileEntity(coordinate: Coordinate): TileEntity {
        var x = coordinate.x
        var y = coordinate.y
        var z = coordinate.z
        when (this) {
            XN -> x--
            XP -> x++
            YN -> y--
            YP -> y++
            ZN -> z--
            ZP -> z++
            else -> {
            }
        }
        return coordinate.world().getTileEntity(BlockPos(x, y, z))!!
    }

    fun writeToNBT(nbt: NBTTagCompound, name: String) {
        nbt.setByte(name, int.toByte())
    }

    fun rotateFromXN(p: DoubleArray) {
        val x = p[0]
        val y = p[1]
        val z = p[2]
        when (this) {
            XN -> {
            }
            XP -> {
                p[0] = -x
                p[2] = -z
            }
            YN -> {
                p[0] = y
                p[1] = x
                p[2] = -z
            }
            YP -> {
                p[0] = y
                p[1] = -x
                p[2] = z
            }
            ZN -> {
                p[0] = -z
                p[2] = x
            }
            ZP -> {
                p[0] = z
                p[2] = -x
            }
            else -> {
            }
        }
    }

    fun rotateFromXN(p: IntArray) {
        val x = p[0]
        val y = p[1]
        val z = p[2]
        when (this) {
            XN -> {
            }
            XP -> {
                p[0] = -x
                p[2] = -z
            }
            YN -> {
                p[0] = y
                p[1] = x
                p[2] = -z
            }
            YP -> {
                p[0] = y
                p[1] = -x
                p[2] = z
            }
            ZN -> {
                p[0] = -z
                p[2] = x
            }
            ZP -> {
                p[0] = z
                p[2] = -x
            }
            else -> {
            }
        }
    }
    /*
    fun rotateFromXN(p: Vec3i) {
        val x = p.x
        val y = p.y
        val z = p.z
        when (this) {
            XN -> {
            }
            XP -> {
                p.x = -x
                p.z = -z
            }
            YN -> {
                p.x = y
                p.y = x
                p.z = -z
            }
            YP -> {
                p.x = y
                p.y = -x
                p.z = z
            }
            ZN -> {
                p.x = -z
                p.z = x
            }
            ZP -> {
                p.x = z
                p.z = -x
            }
        }
    }*/

    fun rotateFromXN(p: Coordinate) {
        val x = p.x
        val y = p.y
        val z = p.z
        when (this) {
            XN -> {
            }
            XP -> {
                p.x = -x
                p.z = -z
            }
            YN -> {
                p.x = y
                p.y = x
                p.z = -z
            }
            YP -> {
                p.x = y
                p.y = -x
                p.z = z
            }
            ZN -> {
                p.x = -z
                p.z = x
            }
            ZP -> {
                p.x = z
                p.z = -x
            }
        }
    }

    fun glTranslate(v: Float) {
        when (this) {
            XN -> GL11.glTranslatef(-v, 0f, 0f)
            XP -> GL11.glTranslatef(v, 0f, 0f)
            YN -> GL11.glTranslatef(0f, -v, 0f)
            YP -> GL11.glTranslatef(0f, v, 0f)
            ZN -> GL11.glTranslatef(0f, 0f, -v)
            ZP -> GL11.glTranslatef(0f, 0f, v)
        }
    }

    fun toEnumFacing(): EnumFacing {
        return when (this) {
            YN -> EnumFacing.DOWN
            XP -> EnumFacing.EAST
            ZN -> EnumFacing.NORTH
            ZP -> EnumFacing.SOUTH
            YP -> EnumFacing.UP
            XN -> EnumFacing.WEST
        }
    }

    fun fromEnumFacing(direction: EnumFacing): Direction {
        return when (direction) {
            EnumFacing.DOWN -> YN
            EnumFacing.EAST -> XP
            EnumFacing.NORTH -> ZN
            EnumFacing.SOUTH -> ZP
            EnumFacing.UP -> YP
            EnumFacing.WEST -> XN
        }
    }

    fun glRotateZnRefInv() {
        when (this) {
            XN -> GL11.glRotatef(-90f, 0f, 1f, 0f)
            XP -> GL11.glRotatef(-90f, 0f, -1f, 0f)
            YN -> {
                GL11.glRotatef(-90f, 1f, 0f, 0f)
                GL11.glScalef(1f, -1f, 1f)
            }
            YP -> {
                GL11.glRotatef(-90f, 1f, 0f, 0f)
                GL11.glScalef(1f, 1f, 1f)
            }
            ZN -> {
            }
            ZP -> GL11.glRotatef(-180f, 0f, 1f, 0f)
            else -> {
            }
        }
    }

    companion object {
        val intToDir = arrayOf(XN, XP, YN, YP, ZN, ZP)
        val all = arrayOf(XN, XP, YN, YP, ZN, ZP)
        val axes = arrayOf(arrayOf(XN, XP), arrayOf(YN, YP), arrayOf(ZN, ZP))
        @JvmStatic
        fun fromHorizontalIndex(nbr: Int): Direction {
            return when (nbr) {
                0 -> XN
                1 -> XP
                2 -> ZN
                3 -> ZP
                else -> XN
            }
        }

        @JvmStatic
        fun fromInt(idx: Int): Direction? {
            for (direction in values()) {
                if (direction.int == idx) return direction
            }
            return null
        }

        @JvmStatic
        fun fromIntMinecraftSide(idx: Int): Direction? {
            var idx = idx
            idx = (idx + 2) % 6
            for (direction in values()) {
                if (direction.int == idx) return direction
            }
            return null
        }

        @JvmStatic
        fun readFromNBT(nbt: NBTTagCompound, name: String): Direction? {
            return fromInt(nbt.getByte(name).toInt())
        }
    }
}

package org.eln2.mod.cable

import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import org.eln2.mod.misc.Direction
import org.eln2.mod.misc.LRDU
import org.eln2.mod.misc.LRDUMask
import org.eln2.mod.node.NodeBase.Companion.isBlockWrappable
import org.eln2.mod.node.NodeBlockEntity
import org.lwjgl.opengl.GL11

object CableRender {
    /*
	static final int connectionStandard = 0;	
	static final int connectionInternal = 1;
	static final int connectionWrappeHalf = 2;
	static final int connectionWrappeFull = 3;
	static final int connectionExtend = 5;
	*/
    fun connectionType(entity: NodeBlockEntity, connectedSide: LRDUMask, side: Direction): CableRenderType {
        var block: Block
        var x2: Int
        var y2: Int
        var z2: Int
        val connectionTypeBuild = CableRenderType()
        var otherTileEntity: TileEntity?
        for (lrdu in LRDU.values()) {
            //noConnection
            if (!connectedSide[lrdu]) continue
            val sideLrdu = side.applyLRDU(lrdu)
            x2 = entity.pos.x
            y2 = entity.pos.y
            z2 = entity.pos.z
            when (sideLrdu) {
                Direction.XN -> x2--
                Direction.XP -> x2++
                Direction.YN -> y2--
                Direction.YP -> y2++
                Direction.ZN -> z2--
                Direction.ZP -> z2++
                else -> {
                }
            }

            //standardConnection
            otherTileEntity = entity.world.getTileEntity(BlockPos(x2, y2, z2))
            /*if (otherTileEntity instanceof SixNodeEntity) {
                SixNodeEntity sixNodeEntity = (SixNodeEntity) otherTileEntity;
                if (sixNodeEntity.elementRenderList[side.getInt()] != null) {
                    Direction otherSide = side.applyLRDU(lrdu);
                    connectionTypeBuild.otherdry[lrdu.getDir()] = sixNodeEntity.getCableDry(otherSide, otherSide.getLRDUGoingTo(side));
                    connectionTypeBuild.otherRender[lrdu.getDir()] = sixNodeEntity.getCableRender(otherSide, otherSide.getLRDUGoingTo(side));
                    continue;
                }
            }
*/
            //no wrappeConection ?
            if (isBlockWrappable(entity.world.getBlockState(BlockPos(x2, y2, z2)).block, entity.world, x2, y2, z2)) {
                when (side) {
                    Direction.XN -> x2--
                    Direction.XP -> x2++
                    Direction.YN -> y2--
                    Direction.YP -> y2++
                    Direction.ZN -> z2--
                    Direction.ZP -> z2++
                    else -> {
                    }
                }
                otherTileEntity = entity.world.getTileEntity(BlockPos(x2, y2, z2))
                if (otherTileEntity is NodeBlockEntity) {
                    /*
					Direction otherDirection = side.getInverse();
					LRDU otherLRDU = otherDirection.getLRDUGoingTo(sideLrdu).inverse();
					CableRenderDescriptor render = entity.getCableRender(sideLrdu,sideLrdu.getLRDUGoingTo(side));
					//CableRenderDescriptor render = entity.getCableRender(side,lrdu);
					NodeBlockEntity otherNode =  ((NodeBlockEntity)otherTileEntity);
					CableRenderDescriptor otherRender = otherNode.getCableRender(otherDirection, otherLRDU);*/
                    /*	Direction otherDirection = side.getInverse();
					LRDU otherLRDU = otherDirection.getLRDUGoingTo(sideLrdu).inverse();
					CableRenderDescriptor render = entity.getCableRender(sideLrdu,sideLrdu.getLRDUGoingTo(side));
					NodeBlockEntity otherNode =  ((NodeBlockEntity)otherTileEntity);
					CableRenderDescriptor otherRender = otherNode.getCableRender(otherDirection, otherLRDU);
					*/
                    val otherDirection = side.inverse
                    val otherLRDU = otherDirection.getLRDUGoingTo(sideLrdu)!!.inverse()
                    val render: CableRenderDescriptor? = entity.getCableRender(sideLrdu, sideLrdu.getLRDUGoingTo(side)!!)
                    val otherNode = otherTileEntity
                    val otherRender: CableRenderDescriptor? = otherNode.getCableRender(otherDirection, otherLRDU)
                    if (render == null) {
                        //Utils.println("ASSERT cableRender missing");
                        continue
                    }
                    if (otherRender == null) {
                        connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Etend
                        connectionTypeBuild.endAt[lrdu.dir] = render.heightPixel
                        connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                        connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                        //	=  += (connectionExtend + (render.heightPixel<<4))<<(lrdu.dir*8) ;
                        continue
                    }
                    //	if(element.tileEntity.hashCode() > otherTileEntity.hashCode())
                    if (render.width == otherRender.width) {
                        if (sideLrdu.int > otherDirection.int) {
                            connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Etend
                            connectionTypeBuild.endAt[lrdu.dir] = otherRender.heightPixel
                            //connectionTypeBuild += (connectionExtend + (otherRender.heightPixel<<4))<<(lrdu.dir*8);
                        }
                        connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                        connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                        continue
                    }
                    if (render.width < otherRender.width) {
                        connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Etend
                        connectionTypeBuild.endAt[lrdu.dir] = otherRender.heightPixel
                        connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                        connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                        //connectionTypeBuild += (connectionExtend + (otherRender.heightPixel<<4))<<(lrdu.dir*8);
                        continue
                    }
                }
            }
        }
        return connectionTypeBuild
    }

    /*
    fun connectionType(element: SixNodeElementRender, side: Direction): CableRenderType {
        var block: Block
        var x2: Int
        var y2: Int
        var z2: Int
        val connectionTypeBuild = CableRenderType()
        var otherTileEntity: TileEntity
        for (lrdu in LRDU.values()) {
            //noConnection
            if (!element.connectedSide.get(lrdu)) continue
            val sideLrdu = side.applyLRDU(lrdu)
            if (element.tileEntity.elementRenderList.get(sideLrdu.int) != null) {
                val otherLRDU = sideLrdu.getLRDUGoingTo(side)
                val render: CableRenderDescriptor = element.getCableRender(lrdu)
                val otherElement: SixNodeElementRender = element.tileEntity.elementRenderList.get(sideLrdu.int)
                val otherRender: CableRenderDescriptor = otherElement.getCableRender(otherLRDU)
                if (otherRender == null || render == null) {
                    continue
                }
                if (render.width === otherRender.width) {
                    if (side.int > sideLrdu.int) {
                        connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Internal
                        connectionTypeBuild.endAt[lrdu.dir] = otherRender.heightPixel
                        //connectionTypeBuild += (connectionInternal + (otherRender.heightPixel<<4))<<(lrdu.dir*8);
                    }
                    connectionTypeBuild.otherdry[lrdu.dir] = otherElement.getCableDry(otherLRDU)
                    connectionTypeBuild.otherRender[lrdu.dir] = otherElement.getCableRender(otherLRDU)
                    continue
                }
                if (render.width < otherRender.width) {
                    connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Internal
                    connectionTypeBuild.endAt[lrdu.dir] = otherRender.heightPixel
                    connectionTypeBuild.otherdry[lrdu.dir] = otherElement.getCableDry(otherLRDU)
                    connectionTypeBuild.otherRender[lrdu.dir] = otherElement.getCableRender(otherLRDU)
                    //connectionTypeBuild += (connectionInternal + (otherRender.heightPixel<<4))<<(lrdu.dir*8);
                    continue
                }
                connectionTypeBuild.otherdry[lrdu.dir] = otherElement.getCableDry(otherLRDU)
                connectionTypeBuild.otherRender[lrdu.dir] = otherElement.getCableRender(otherLRDU)
                continue
            }
            x2 = element.tileEntity.xCoord
            y2 = element.tileEntity.yCoord
            z2 = element.tileEntity.zCoord
            when (sideLrdu) {
                Direction.XN -> x2--
                Direction.XP -> x2++
                Direction.YN -> y2--
                Direction.YP -> y2++
                Direction.ZN -> z2--
                Direction.ZP -> z2++
                else -> {
                }
            }

            //standardConnection
            otherTileEntity = element.tileEntity.getWorldObj().getTileEntity(x2, y2, z2)
            if (otherTileEntity is SixNodeEntity) {
                val sixNodeEntity: SixNodeEntity = otherTileEntity as SixNodeEntity
                if (sixNodeEntity.elementRenderList.get(side.int) != null) {
                    connectionTypeBuild.otherdry[lrdu.dir] = sixNodeEntity.elementRenderList.get(side.int).getCableDry(lrdu.inverse())
                    connectionTypeBuild.otherRender[lrdu.dir] = sixNodeEntity.elementRenderList.get(side.int).getCableRender(lrdu.inverse())
                    continue
                }
            }

            //no wrappeConection ?
            if (!isBlockWrappable(element.tileEntity.getWorldObj().getBlock(x2, y2, z2), element.tileEntity.getWorldObj(), x2, y2, z2)) {
                continue
            } else {
                when (side) {
                    Direction.XN -> x2--
                    Direction.XP -> x2++
                    Direction.YN -> y2--
                    Direction.YP -> y2++
                    Direction.ZN -> z2--
                    Direction.ZP -> z2++
                    else -> {
                    }
                }
                otherTileEntity = element.tileEntity.getWorldObj().getTileEntity(x2, y2, z2)
                if (otherTileEntity is NodeBlockEntity) {
                    //Direction otherDirection = side.getInverse();
                    /*Direction otherDirection = side.applyLRDU(lrdu).getInverse();
					LRDU otherLRDU = otherDirection.getLRDUGoingTo(side.getInverse());
					CableRenderDescriptor render = element.getCableRender(lrdu);
					NodeBlockEntity otherNode = ((NodeBlockEntity)otherTileEntity);
					CableRenderDescriptor otherRender = otherNode.getCableRender(side.getInverse(), lrdu//.inverse());
				*/
                    val otherDirection = side.inverse
                    val otherLRDU = otherDirection.getLRDUGoingTo(sideLrdu)!!.inverse()
                    val render: CableRenderDescriptor = element.getCableRender(lrdu) ?: continue
                    val otherNode = otherTileEntity
                    val otherRender: CableRenderDescriptor? = otherNode.getCableRender(otherDirection, otherLRDU)
                    if (otherRender == null) {
                        connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Etend
                        connectionTypeBuild.endAt[lrdu.dir] = render.heightPixel
                        connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                        connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                        //connectionTypeBuild += (connectionExtend + (render.heightPixel<<4))<<(lrdu.dir*8) ;
                        continue
                    }
                    //	if(element.tileEntity.hashCode() > otherTileEntity.hashCode())
                    if (render.width === otherRender.width) {
                        if (sideLrdu.int > otherDirection.int) {
                            connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Etend
                            connectionTypeBuild.endAt[lrdu.dir] = otherRender.heightPixel
                            ///connectionTypeBuild += (connectionExtend + (otherRender.heightPixel<<4))<<(lrdu.dir*8);
                        }
                        connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                        connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                        continue
                    }
                    if (render.width < otherRender.width) {
                        connectionTypeBuild.method[lrdu.dir] = CableRenderTypeMethodType.Etend
                        connectionTypeBuild.endAt[lrdu.dir] = otherRender.heightPixel
                        connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                        connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                        //connectionTypeBuild += (connectionExtend + (otherRender.heightPixel<<4))<<(lrdu.dir*8);
                        continue
                    }
                    connectionTypeBuild.otherdry[lrdu.dir] = otherNode.getCableDry(otherDirection, otherLRDU)
                    connectionTypeBuild.otherRender[lrdu.dir] = otherNode.getCableRender(otherDirection, otherLRDU)
                    continue
                }

                /*
				if(otherTileEntity instanceof SixNodeEntity) {
				//	SixNodeEntity sixNodeEntity = (SixNodeEntity) otherTileEntity;
					//sixNodeEntity.elementRenderList[0].
					connectionTypeBuild += connectionWrappeHalf<<(lrdu.dir*8);
					continue;					
				}
				else {
					connectionTypeBuild += connectionWrappeFull<<(lrdu.dir*8);
					continue;
				}*/
            }
        }
        return connectionTypeBuild
    }*/

    fun drawCable(cable: CableRenderDescriptor, connection: LRDUMask, connectionType: CableRenderType) {
        drawCable(cable, connection, connectionType, cable.widthDiv2 / 2f)
    }

    fun drawCable(cable: CableRenderDescriptor?, connection: LRDUMask, connectionType: CableRenderType, deltaStart: Float) {
        if (cable == null) return
        //GL11.glDisable(GL11.GL_TEXTURE);
        //if(connection.mask != 0) return;
        var tx: Float
        var ty: Float
        run {
            var endLeft = -deltaStart
            var endRight = deltaStart
            var endUp = deltaStart
            var endDown = -deltaStart
            val startdLeft = -connectionType.startAt[0]
            val startRight = connectionType.startAt[1]
            val startUp = connectionType.startAt[2]
            val startDown = -connectionType.startAt[3]
            if (connection.mask == 0 && deltaStart.toInt() >= 0f) {
                endLeft = -cable.widthDiv2 - 3.0f / 16.0f
                endRight = cable.widthDiv2 + 3.0f / 16.0f
                endDown = -cable.widthDiv2 - 3.0f / 16.0f
                endUp = cable.widthDiv2 + 3.0f / 16.0f
            } else {
                if (connection[LRDU.Left]) {
                    endLeft = -0.5f
                }
                if (connection[LRDU.Right]) {
                    endRight = 0.5f
                }
                if (connection[LRDU.Down]) {
                    endDown = -0.5f
                }
                if (connection[LRDU.Up]) {
                    endUp = 0.5f
                }
            }
            when (connectionType.method[0]) {
                CableRenderTypeMethodType.Internal -> endLeft += (connectionType.endAt[0] / 16.0).toFloat()
                CableRenderTypeMethodType.Etend -> endLeft -= (connectionType.endAt[0] / 16.0).toFloat()
                else -> {
                }
            }
            when (connectionType.method[1]) {
                CableRenderTypeMethodType.Internal -> endRight -= (connectionType.endAt[1] / 16.0).toFloat()
                CableRenderTypeMethodType.Etend -> endRight += (connectionType.endAt[1] / 16.0).toFloat()
                else -> {
                }
            }
            when (connectionType.method[2]) {
                CableRenderTypeMethodType.Internal -> endDown += (connectionType.endAt[2] / 16.0).toFloat()
                CableRenderTypeMethodType.Etend -> endDown -= (connectionType.endAt[2] / 16.0).toFloat()
                else -> {
                }
            }
            when (connectionType.method[3]) {
                CableRenderTypeMethodType.Internal -> endUp -= (connectionType.endAt[3] / 16.0).toFloat()
                CableRenderTypeMethodType.Etend -> endUp += (connectionType.endAt[3] / 16.0).toFloat()
                else -> {
                }
            }
            val height = cable.height
            tx = 0.25f
            ty = 0.5f

            //	Utils.bindTextureByName(cable.cableTexture);
            if (endLeft < startdLeft) {
                GL11.glBegin(GL11.GL_QUAD_STRIP)
                GL11.glNormal3f(0f, 1f, 0f)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + endLeft)
                GL11.glVertex3f(0f, cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + startdLeft)
                GL11.glVertex3f(0f, cable.widthDiv2, startdLeft)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endLeft)
                GL11.glVertex3f(height, cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + startdLeft)
                GL11.glVertex3f(height, cable.widthDiv2, startdLeft)
                GL11.glNormal3f(1f, 0f, 0f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endLeft)
                GL11.glVertex3f(height, -cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + startdLeft)
                GL11.glVertex3f(height, -cable.widthDiv2, startdLeft)
                GL11.glNormal3f(0f, -1f, 0f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f - height, ty + endLeft)
                GL11.glVertex3f(0f, -cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f - height, ty + startdLeft)
                GL11.glVertex3f(0f, -cable.widthDiv2, startdLeft)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_QUADS)
                GL11.glNormal3f(0f, 0f, -1f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endLeft - height)
                GL11.glVertex3f(0f, -cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endLeft - height)
                GL11.glVertex3f(0f, cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endLeft)
                GL11.glVertex3f(height, cable.widthDiv2, endLeft)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endLeft)
                GL11.glVertex3f(height, -cable.widthDiv2, endLeft)
                GL11.glEnd()
            }
            if (endRight > startRight) {
                GL11.glBegin(GL11.GL_QUAD_STRIP)
                GL11.glNormal3f(0f, 1f, 0f)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + startRight)
                GL11.glVertex3f(0f, cable.widthDiv2, startRight)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + endRight)
                GL11.glVertex3f(0f, cable.widthDiv2, endRight)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + startRight)
                GL11.glVertex3f(height, cable.widthDiv2, startRight)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endRight)
                GL11.glVertex3f(height, cable.widthDiv2, endRight)
                GL11.glNormal3f(1f, 0f, 0f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + startRight)
                GL11.glVertex3f(height, -cable.widthDiv2, startRight)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endRight)
                GL11.glVertex3f(height, -cable.widthDiv2, endRight)
                GL11.glNormal3f(0f, -1f, 0f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f - height, ty + startRight)
                GL11.glVertex3f(0f, -cable.widthDiv2, startRight)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f - height, ty + endRight)
                GL11.glVertex3f(0f, -cable.widthDiv2, endRight)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_QUADS)
                GL11.glNormal3f(0f, 0f, 1f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endRight)
                GL11.glVertex3f(height, -cable.widthDiv2, endRight)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endRight)
                GL11.glVertex3f(height, cable.widthDiv2, endRight)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endRight + height)
                GL11.glVertex3f(0f, cable.widthDiv2, endRight)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endRight + height)
                GL11.glVertex3f(0f, -cable.widthDiv2, endRight)
                GL11.glEnd()
            }
            if (endDown < startDown) {
                GL11.glBegin(GL11.GL_QUAD_STRIP)
                GL11.glNormal3f(0f, 0f, -1f)
                GL11.glTexCoord2f(tx - (cable.widthDiv2 - height) * 0.5f, ty + endDown)
                GL11.glVertex3f(0f, endDown, -cable.widthDiv2)
                GL11.glTexCoord2f(tx - (cable.widthDiv2 - height) * 0.5f, ty + startDown)
                GL11.glVertex3f(0f, startDown, -cable.widthDiv2)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endDown)
                GL11.glVertex3f(height, endDown, -cable.widthDiv2)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + startDown)
                GL11.glVertex3f(height, startDown, -cable.widthDiv2)
                GL11.glNormal3f(1f, 0f, 0f)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endDown)
                GL11.glVertex3f(height, endDown, cable.widthDiv2)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + startDown)
                GL11.glVertex3f(height, startDown, cable.widthDiv2)
                GL11.glNormal3f(0f, 0f, 1f)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + endDown)
                GL11.glVertex3f(0f, endDown, cable.widthDiv2)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + startDown)
                GL11.glVertex3f(0f, startDown, cable.widthDiv2)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_QUADS)
                GL11.glNormal3f(0f, -1f, 0f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endDown)
                GL11.glVertex3f(height, endDown, -cable.widthDiv2)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endDown)
                GL11.glVertex3f(height, endDown, cable.widthDiv2)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endDown - height)
                GL11.glVertex3f(0f, endDown, cable.widthDiv2)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endDown - height)
                GL11.glVertex3f(0f, endDown, -cable.widthDiv2)
                GL11.glEnd()
            }
            if (endUp > startUp) {
                GL11.glBegin(GL11.GL_QUAD_STRIP)
                GL11.glNormal3f(0f, 0f, -1f)
                GL11.glTexCoord2f(tx - (cable.widthDiv2 - height) * 0.5f, ty + startUp)
                GL11.glVertex3f(0f, startUp, -cable.widthDiv2)
                GL11.glTexCoord2f(tx - (cable.widthDiv2 - height) * 0.5f, ty + endUp)
                GL11.glVertex3f(0f, endUp, -cable.widthDiv2)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + startUp)
                GL11.glVertex3f(height, startUp, -cable.widthDiv2)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endUp)
                GL11.glVertex3f(height, endUp, -cable.widthDiv2)
                GL11.glNormal3f(1f, 0f, 0f)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + startUp)
                GL11.glVertex3f(height, startUp, cable.widthDiv2)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endUp)
                GL11.glVertex3f(height, endUp, cable.widthDiv2)
                GL11.glNormal3f(0f, 0f, 1f)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + startUp)
                GL11.glVertex3f(0f, startUp, cable.widthDiv2)
                GL11.glTexCoord2f(tx + (cable.widthDiv2 + height) * 0.5f, ty + endUp)
                GL11.glVertex3f(0f, endUp, cable.widthDiv2)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_QUADS)
                GL11.glNormal3f(0f, 1f, 0f)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endUp + height)
                GL11.glVertex3f(0f, endUp, -cable.widthDiv2)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endUp + height)
                GL11.glVertex3f(0f, endUp, cable.widthDiv2)
                GL11.glTexCoord2f(tx + cable.widthDiv2 * 0.5f, ty + endUp)
                GL11.glVertex3f(height, endUp, cable.widthDiv2)
                GL11.glTexCoord2f(tx - cable.widthDiv2 * 0.5f, ty + endUp)
                GL11.glVertex3f(height, endUp, -cable.widthDiv2)
                GL11.glEnd()
            }
        }
    }

    fun drawNode(cable: CableRenderDescriptor, connection: LRDUMask, connectionType: CableRenderType?) {
        if (connection.mask == 0 || (connection[LRDU.Left] || connection[LRDU.Right]) && (connection[LRDU.Down] || connection[LRDU.Up]) || connection.mask == 1 || connection.mask == 2 || connection.mask == 4 || connection.mask == 8) {
            val widthDiv2 = cable.widthDiv2 + 1.0f / 16.0f
            val height = cable.height + 1.0f / 16.0f
            val tx = 0.75f
            val ty = 0.5f
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
            //	Utils.bindTextureByName(ClientProxy.CABLENODE_PNG);
            GL11.glBegin(GL11.GL_QUAD_STRIP)
            GL11.glNormal3f(0f, 1f, 0f)
            GL11.glTexCoord2f(tx + (widthDiv2 + cable.height + 1.0f / 16.0f) * 0.5f, ty - widthDiv2)
            GL11.glVertex3f(0f, widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx + (widthDiv2 + cable.height + 1.0f / 16.0f) * 0.5f, ty + widthDiv2)
            GL11.glVertex3f(0f, widthDiv2, widthDiv2)
            GL11.glTexCoord2f(tx + widthDiv2 * 0.5f, ty - widthDiv2)
            GL11.glVertex3f(height, widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx + widthDiv2 * 0.5f, ty + widthDiv2)
            GL11.glVertex3f(height, widthDiv2, widthDiv2)
            GL11.glNormal3f(1f, 0f, 0f)
            GL11.glTexCoord2f(tx - widthDiv2 * 0.5f, ty - widthDiv2)
            GL11.glVertex3f(height, -widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx - widthDiv2 * 0.5f, ty + widthDiv2)
            GL11.glVertex3f(height, -widthDiv2, widthDiv2)
            GL11.glNormal3f(0f, -1f, 0f)
            GL11.glTexCoord2f(tx - (widthDiv2 + cable.height + 1.0f / 16.0f) * 0.5f, ty - widthDiv2)
            GL11.glVertex3f(0f, -widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx - (widthDiv2 + cable.height + 1.0f / 16.0f) * 0.5f, ty + widthDiv2)
            GL11.glVertex3f(0f, -widthDiv2, widthDiv2)
            GL11.glEnd()
            GL11.glBegin(GL11.GL_QUADS)
            GL11.glNormal3f(0f, 0f, -1f)
            GL11.glTexCoord2f(tx - widthDiv2 * 0.5f, ty - widthDiv2 - cable.height - 1.0f / 16.0f)
            GL11.glVertex3f(0f, -widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx + widthDiv2 * 0.5f, ty - widthDiv2 - cable.height - 1.0f / 16.0f)
            GL11.glVertex3f(0f, widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx + widthDiv2 * 0.5f, ty - widthDiv2)
            GL11.glVertex3f(height, widthDiv2, -widthDiv2)
            GL11.glTexCoord2f(tx - widthDiv2 * 0.5f, ty - widthDiv2)
            GL11.glVertex3f(height, -widthDiv2, -widthDiv2)
            GL11.glNormal3f(0f, 0f, 1f)
            GL11.glTexCoord2f(tx - widthDiv2 * 0.5f, ty + widthDiv2)
            GL11.glVertex3f(height, -widthDiv2, widthDiv2)
            GL11.glTexCoord2f(tx + widthDiv2 * 0.5f, ty + widthDiv2)
            GL11.glVertex3f(height, widthDiv2, widthDiv2)
            GL11.glTexCoord2f(tx + widthDiv2 * 0.5f, ty + widthDiv2 + cable.height + 1.0f / 16.0f)
            GL11.glVertex3f(0f, widthDiv2, widthDiv2)
            GL11.glTexCoord2f(tx - widthDiv2 * 0.5f, ty + widthDiv2 + cable.height + 1.0f / 16.0f)
            GL11.glVertex3f(0f, -widthDiv2, widthDiv2)
            GL11.glEnd()
        }
        //	GL11.glEnable(GL11.GL_TEXTURE);
    }
}
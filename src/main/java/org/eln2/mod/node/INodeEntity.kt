package org.eln2.mod.node

import net.minecraft.client.gui.GuiScreen
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.eln2.mod.misc.Direction
import java.io.DataInputStream

interface INodeEntity {
    val nodeUuid: String
    fun serverPublishUnserialize(stream: DataInputStream)
    fun serverPacketUnserialize(stream: DataInputStream)

    @SideOnly(Side.CLIENT)
    fun newGuiDraw(side: Direction, player: EntityPlayer): GuiScreen?
    fun newContainer(side: Direction, player: EntityPlayer): Container?
}

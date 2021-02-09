package org.eln2.mod.node.transparent

import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import org.eln2.mod.node.transparent.TransparentNodeEntity
import org.lwjgl.opengl.GL11

class TransparentNodeRender<T: TileEntity> : TileEntitySpecialRenderer<T>() {

    override fun render(te: T, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val tileEntity = te as TransparentNodeEntity
        if (tileEntity.elementRender == null) return
        GL11.glPushMatrix()
        GL11.glTranslatef(x.toFloat() + .5f, y.toFloat() + .5f, z.toFloat() + .5f)
        tileEntity.elementRender!!.draw()
        GL11.glPopMatrix()
    }
}

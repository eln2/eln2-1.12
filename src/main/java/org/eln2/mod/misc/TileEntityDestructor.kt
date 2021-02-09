package org.eln2.mod.misc

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

class TileEntityDestructor {
    var destroyList = ArrayList<TileEntity>()
    fun clear() {
        destroyList.clear()
    }

    fun add(tile: TileEntity) {
        destroyList.add(tile)
    }

    @SubscribeEvent
    fun tick(event: TickEvent.ServerTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        for (t in destroyList) {
            if (t.world.getTileEntity(t.pos) === t) {
                t.world.setBlockToAir(t.pos)
                Utils.println("destroy light at $t")
            }
        }
        destroyList.clear()
    }

    init {
        FMLCommonHandler.instance().bus().register(this)
    }
}

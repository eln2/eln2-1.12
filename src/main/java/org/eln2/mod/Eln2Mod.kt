package org.eln2.mod

import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger
import org.eln2.mod.Eln2Object.logger
import org.eln2.sim.Simulator

@Mod(modid = Eln2Mod.MODID, name = Eln2Mod.NAME, version = Eln2Mod.VERSION)
class Eln2Mod {
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.registryName)
    }

    companion object {
        const val MODID = "examplemod"
        const val NAME = "Example Mod"
        const val VERSION = "1.0"
    }
}

object Eln2Object {
    var debug = false
    lateinit var logger: Logger
    val SV = 5.0
    val LV = 50.0
    val MV = 200.0
    val HV = 800.0
    val VHV = 3_200.0

    val LP = LV * 20.0
    val MP = MV * 10.0
    val HP = HV * 6.25
    val VHP = VHV * 4.275

    val cableResistance = 0.01

    val gateOutputCurrent = 0.05

    val sim = Simulator(0.05, 0.05, 50, 400.0)
}
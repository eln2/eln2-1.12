package mods.eln.gui

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import org.eln2.mod.gui.ISlotSkin
import org.eln2.mod.gui.ISlotSkin.SlotSkin

open class SlotWithSkin(
        inventory: IInventory,
        slot: Int,
        xPosition: Int,
        yPosition: Int,
        override var slotSkin: SlotSkin
) : Slot(inventory, slot, xPosition, yPosition), ISlotSkin
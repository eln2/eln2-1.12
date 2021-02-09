package org.eln2.mod.gui

interface ISlotSkin {
    enum class SlotSkin {
        none, medium, big
    }

    val slotSkin: SlotSkin?
}
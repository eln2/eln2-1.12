package org.eln2.mod.node

interface IThermalDestructorDescriptor {
    val thermalDestructionMax: Double
    val thermalDestructionStart: Double
    val thermalDestructionPerOverflow: Double
    val thermalDestructionProbabilityPerOverflow: Double
}

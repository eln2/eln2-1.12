package org.eln2.mod.node

interface IVoltageDestructorDescriptor {
    val voltageDestructionMax: Double
    val voltageDestructionStart: Double
    val voltageDestructionPerOverflow: Double
    val voltageDestructionProbabilityPerOverflow: Double
}

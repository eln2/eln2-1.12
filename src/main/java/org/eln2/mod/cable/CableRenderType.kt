package org.eln2.mod.cable

class CableRenderType {
    @JvmField
    var method = arrayOfNulls<CableRenderTypeMethodType>(4)
    @JvmField
    var endAt = FloatArray(4)
    var startAt = FloatArray(4)
    @JvmField
    var otherdry = IntArray(4)
    @JvmField
    var otherRender = arrayOfNulls<CableRenderDescriptor>(4)

    init {
        for (idx in 0..3) {
            method[idx] = CableRenderTypeMethodType.Standard
            endAt[idx] = 0f
            startAt[idx] = 0f
            otherdry[idx] = 0
            otherRender[idx] = null
        }
    }
}

enum class CableRenderTypeMethodType {
    Standard, Internal, WrapperHalf, WrapperFull, Etend
}
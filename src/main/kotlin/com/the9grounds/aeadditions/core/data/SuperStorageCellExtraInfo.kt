package com.the9grounds.aeadditions.core.data

import java.util.*

data class SuperStorageCellExtraInfo(var usedBytes: Long = 0) {
    override fun hashCode(): Int {
        return Objects.hash(usedBytes)
    }

    override fun equals(other: Any?): Boolean {
        return if (other === this) {
            true
        } else {
            other is SuperStorageCellExtraInfo && other.usedBytes === usedBytes
        }
    }

    fun hasDefaultValues(): Boolean {
        return usedBytes == 0L
    }
}

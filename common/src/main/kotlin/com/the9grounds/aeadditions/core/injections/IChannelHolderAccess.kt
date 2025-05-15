package com.the9grounds.aeadditions.core.injections

import com.the9grounds.aeadditions.util.ChannelHolder

interface IChannelHolderAccess {
    fun getChannelHolder(): ChannelHolder {
        throw AssertionError()
    }
}
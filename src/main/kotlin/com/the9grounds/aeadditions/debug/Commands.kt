package com.the9grounds.aeadditions.debug

import com.the9grounds.aeadditions.debug.commands.BroadcastToChannel
import com.the9grounds.aeadditions.debug.commands.ICommand
import com.the9grounds.aeadditions.debug.commands.SubscribeToChannel
import com.the9grounds.aeadditions.debug.commands.TestChannelCreate

enum class Commands(val level: Int, val command: ICommand) {
    ChannelCreate(4, TestChannelCreate),
    Subscribe(4, SubscribeToChannel),
    Broadcast(4, BroadcastToChannel)
}
package com.the9grounds.aeadditions.debug

import com.the9grounds.aeadditions.debug.commands.ICommand
import com.the9grounds.aeadditions.debug.commands.TestExtract
import com.the9grounds.aeadditions.debug.commands.TestInsert

enum class Commands(val level: Int, val command: ICommand) {
    TESTINSERT(4, TestInsert()),
    TESTEXTRACT(4, TestExtract())
}
package com.the9grounds.aeadditions.integration.opencomputers

import net.minecraft.item.ItemStack

object ResultWrapper {
    fun result(vararg args: Any): Array<Any?> {
        fun unwrap(arg: Any): Any? = when(arg) {
            is ItemStack -> if (arg.isEmpty) null else arg
            else -> arg
        }

        return args.map { unwrap(it) }.toTypedArray()
    }
}
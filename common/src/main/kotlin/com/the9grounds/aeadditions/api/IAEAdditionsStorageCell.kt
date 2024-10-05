package com.the9grounds.aeadditions.api

import appeng.api.stacks.AEKey
import appeng.api.stacks.AEKeyType
import net.minecraft.world.item.ItemStack
import javax.annotation.Nonnull

interface IAEAdditionsStorageCell {

    /**
     * Basic cell items are limited to a single [AEKeyType].
     */
    fun getKeyType(): AEKeyType?
    
    /**
     * It wont work if the return is not a multiple of 8. The limit is ([Integer.MAX_VALUE] + 1) / 8.
     *
     * @param cellItem item
     *
     * @return number of bytes
     */
    fun getBytes(@Nonnull cellItem: ItemStack): Int

    /**
     * Determines the number of bytes used for any type included on the cell.
     *
     * @param cellItem item
     *
     * @return number of bytes
     */
    fun getBytesPerType(@Nonnull cellItem: ItemStack): Int

    /**
     * Allows you to fine tune which items are allowed on a given cell, if you don't care, just return false; As the
     * handler for this type of cell is still the default cells, the normal AE black list is also applied.
     *
     * @param cellItem          item
     * @param requestedAddition requested addition
     * @return true to preventAdditionOfItem
     */
    fun isBlackListed(cellItem: ItemStack?, requestedAddition: AEKey?): Boolean {
        return false
    }

    /**
     * Allows you to specify if this storage cell can be stored inside other storage cells, only set this for special
     * items like the matter cannon that are not general purpose storage.
     *
     * @return true if the storage cell can be stored inside other storage cells, this is generally false, except for
     * certain situations such as the matter cannon.
     */
    fun storableInStorageCell(): Boolean {
        return false
    }

    /**
     * Allows an item to selectively enable or disable its status as a storage cell.
     *
     * @param i item
     * @return if the ItemStack should currently be usable as a storage cell.
     */
    fun isStorageCell(i: ItemStack?): Boolean {
        return true
    }

    /**
     * Must be between 1 and 63, indicates how many types you want to store on the item.
     *
     * @param cellItem item
     *
     * @return number of types
     */
    fun getTotalTypes(@Nonnull cellItem: ItemStack): Int

    /**
     * @return drain in ae/t this storage cell will use.
     */
    fun getIdleDrain(): Double
}
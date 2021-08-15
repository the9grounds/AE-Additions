package com.the9grounds.aeadditions.parts

import appeng.api.implementations.parts.IStorageMonitorPart
import appeng.api.networking.security.IActionSource
import appeng.api.networking.storage.IStackWatcher
import appeng.api.networking.storage.IStackWatcherHost
import appeng.api.parts.IPartModel
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IAEStack
import appeng.api.storage.data.IItemList
import appeng.client.render.TesrRenderHelper
import com.the9grounds.aeadditions.integration.appeng.AppEng
import appeng.fluids.util.AEFluidStack
import appeng.me.GridAccessException
import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.core.PlayerMessages
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import com.the9grounds.aeadditions.util.StorageChannels
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.util.Hand
import net.minecraft.util.Util
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.vector.Vector3d

abstract class AbstractMonitorPart<T : IAEStack<T>>(itemStack: ItemStack) : AbstractDisplayPart(itemStack), IStackWatcherHost, IStorageMonitorPart {
    var locked = false
    var watchedItem: T? = null
    var lastText: String? = null
    var myWatcher: IStackWatcher? = null

    override fun readFromNBT(data: CompoundNBT?) {
        super.readFromNBT(data)
        locked = data!!.getBoolean("locked")
        
        val type = data.getString("itemType")
        
        val compound = data.getCompound("watchedItem")
        watchedItem = when(type) {
            "fluid" -> AEFluidStack.fromNBT(compound) as? T
            "chemical" -> AEChemicalStack(compound) as? T
            else -> throw RuntimeException("Invalid type for storage/conversion monitor")
        }
    }

    override fun writeToNBT(data: CompoundNBT?) {
        super.writeToNBT(data)
        data!!.putBoolean("locked", locked)
        
        val watchedItemCompound = CompoundNBT()
        if (watchedItem != null) {
            val type = getTypeForWatchedItem()

            data.putString("itemType", type)
            
            watchedItem!!.writeToNBT(watchedItemCompound)
        }
        
        data.put("watchedItem", watchedItemCompound)
        
    }

    private fun getTypeForWatchedItem(): String {
        val type = when (watchedItem) {
            is IAEFluidStack -> "fluid"
            is IAEChemicalStack -> "chemical"
            null -> ""
            else -> throw RuntimeException("Invalid type for storage/conversion monitor")
        }
        return type
    }

    override fun writeToStream(data: PacketBuffer?) {
        super.writeToStream(data)
        data!!.writeBoolean(locked)
        val type = getTypeForWatchedItem()
        
        data.writeString(type)
        
        if (watchedItem != null) {
            watchedItem!!.writeToPacket(data)
        }
    }

    override fun readFromStream(data: PacketBuffer?): Boolean {
        super.readFromStream(data)
        
        val isLocked = data!!.readBoolean()

        val needRedraw = isLocked != locked
        
        locked = isLocked
        
        val type = data.readString()
        
        watchedItem = when(type) {
            "" -> null
            "fluid" -> AEFluidStack.fromPacket(data) as? T
            "chemical" -> AEChemicalStack.fromPacket(data) as? T
            else -> throw RuntimeException("Invalid type for storage/conversion monitor")
        }
        
        return needRedraw
    }

    override fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (isRemote) {
            return true
        }
        
        if (!proxy.isActive) { 
            return false
        }
        
        if (!Utils.hasPermissions(location, player!!)) {
            return false
        }
        
        if (!locked) {
            val stack = getStackFromHeldItem(player, hand)
            
            if (stack == null) {
                return false
            }
            
            watchedItem = stack
            
            configureWatcher()
            host?.markForSave()
            host?.markForUpdate()
        } else {
            return super.onPartActivate(player, hand, pos)
        }
        
        return true
    }

    override fun onShiftClicked(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (isRemote) {
            return true
        }

        if (!proxy.isActive) {
            return false
        }

        if (!Utils.hasPermissions(location, player!!)) {
            return false
        }
        
        if (locked || watchedItem == null) {
            return false
        }
        
        if (myWatcher != null) {
            myWatcher!!.remove(watchedItem)
        }
        
        watchedItem = null
        
        host?.markForSave()
        host?.markForUpdate()
        
        return true
    }

    override fun onPartShiftActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (isRemote) {
            return true
        }

        if (!proxy.isActive) {
            return false
        }

        if (!Utils.hasPermissions(location, player!!)) {
            return false
        }
        
        if (player.getHeldItem(hand).isEmpty) {
            locked = !locked
            
            val message = if (locked) {
                PlayerMessages.isNowLocked
            } else {
                PlayerMessages.isNowUnlocked
            }
            
            player.sendMessage(message.get(), Util.DUMMY_UUID)
            host?.markForSave()
            host?.markForUpdate()
        }
        
        return true
    }

    override fun renderDynamic(
        partialTicks: Float,
        matrixStack: MatrixStack?,
        buffers: IRenderTypeBuffer?,
        combinedLightIn: Int,
        combinedOverlayIn: Int
    ) {
        
        if (clientFlags.and(POWERED_FLAG or CHANNEL_FLAG) != POWERED_FLAG or CHANNEL_FLAG) {
            return
        }
        
        if (watchedItem == null) {
            return
        }
        
        matrixStack!!.push()
        matrixStack.translate(.5, .5, .5)
        
        Utils.rotateToFace(matrixStack, side!!.facing!!, rotation)
        
        matrixStack.translate(0.0, 0.05, .5)
        
        Utils.renderStack2dWithAmount(matrixStack, buffers, watchedItem!!, getWatchedItemName(), .4f, -.23f, 15728880, combinedOverlayIn)
        
        matrixStack.pop()
    }
    
    abstract fun getWatchedItemName(): String

    override fun requireDynamicRender(): Boolean = true
    
    abstract fun getStackFromHeldItem(player: PlayerEntity?, hand: Hand?) : T?

    fun selectModel(on: IPartModel, off: IPartModel, hasChannel: IPartModel, lockedOn: IPartModel, lockedOff: IPartModel, lockedHasChannel: IPartModel): IPartModel {
        return when {
            isActive -> if (locked) lockedHasChannel else hasChannel
            isPowered -> if (locked) lockedOn else on
            else -> if (locked) lockedOff else off
        }
    }

    override fun updateWatcher(newWatcher: IStackWatcher?) {
        myWatcher = newWatcher
        configureWatcher()
    }

    private fun configureWatcher() {
        if (myWatcher != null) {
            myWatcher!!.reset()
        }

        try {
            if (watchedItem != null) {
                if (myWatcher != null) {
                    myWatcher!!.add(watchedItem)
                }

                val stack = this.proxy.storage.getInventory(getStorageChannel()).storageList.findPrecise(watchedItem)

                if (stack == null) {
                    watchedItem!!.stackSize = 0L
                } else {
                    watchedItem!!.stackSize = stack.stackSize
                }
            }
        } catch (e: GridAccessException) {
            //
        }
    }

    override fun onStackChange(
        o: IItemList<*>?,
        fullStack: IAEStack<*>?,
        diffStack: IAEStack<*>?,
        src: IActionSource?,
        chan: IStorageChannel<*>?
    ) {
        if (watchedItem != null) {
            if (fullStack != null) {
                watchedItem!!.stackSize = fullStack.getStackSize()
                
                val text = Utils.getAmountTextForStack(watchedItem!!)
                
                if (text != lastText) {
                    lastText = text
                    host!!.markForUpdate()
                }
            }
        }
    }
    
    abstract fun getStorageChannel(): IStorageChannel<T>
    
    override fun showNetworkInfo(where: RayTraceResult?): Boolean = false

    override fun getDisplayed(): T? = watchedItem

    override fun isLocked(): Boolean = locked
}
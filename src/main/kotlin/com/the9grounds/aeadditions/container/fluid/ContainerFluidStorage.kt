package com.the9grounds.aeadditions.container.fluid

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.FluidStack
import com.the9grounds.aeadditions.container.ContainerStorage
import net.minecraft.util.EnumHand
import appeng.api.storage.IMEMonitor
import appeng.api.storage.data.IAEFluidStack
import com.the9grounds.aeadditions.api.IPortableFluidStorageCell
import com.the9grounds.aeadditions.api.IWirelessFluidTermHandler
import com.the9grounds.aeadditions.util.AEUtils
import appeng.api.config.Actionable
import com.the9grounds.aeadditions.container.StorageType
import com.the9grounds.aeadditions.util.FluidHelper
import com.the9grounds.aeadditions.util.PlayerSource

class ContainerFluidStorage : ContainerStorage {
    constructor(player: EntityPlayer?, hand: EnumHand?) : super(StorageType.FLUID, player, hand) {}
    constructor(
        monitor: IMEMonitor<IAEFluidStack>,
        player: EntityPlayer?,
        storageCell: IPortableFluidStorageCell?,
        hand: EnumHand?
    ) : super(
        StorageType.FLUID, monitor, player, storageCell, hand
    ) {
    }

    constructor(
        monitor: IMEMonitor<IAEFluidStack>,
        player: EntityPlayer?,
        handler: IWirelessFluidTermHandler?,
        hand: EnumHand?
    ) : super(
        StorageType.FLUID, monitor, player, handler, hand
    ) {
    }

    constructor(monitor: IMEMonitor<IAEFluidStack>, player: EntityPlayer?, hand: EnumHand?) : super(
        StorageType.FLUID,
        monitor,
        player,
        hand
    ) {
    }

    override fun doWork() {
        val secondSlot = inventory.getStackInSlot(1)
        if (secondSlot != null && !secondSlot.isEmpty && secondSlot.count >= secondSlot.maxStackSize) {
            return
        }
        var container = inventory.getStackInSlot(0)
        if (!FluidHelper.isFluidContainer(container)) {
            return
        }
        if (monitor == null) {
            return
        }
        container = container.copy()
        container.count = 1
        if (FluidHelper.isDrainableFilledContainer(container)) {
            val containerFluid = FluidHelper.getFluidFromContainer(container)

            //Tries to inject fluid to network.
            val notInjected = monitor.injectItems(
                AEUtils.createFluidStack(containerFluid),
                Actionable.SIMULATE, PlayerSource(player, null)
            )
            if (notInjected != null) {
                return
            }
            val handItem = player.getHeldItem(hand)
            if (handler != null) {
                if (!handler.hasPower(
                        player, 20.0,
                        handItem
                    )
                ) {
                    return
                }
                handler.usePower(
                    player, 20.0,
                    handItem
                )
            } else if (storageCell != null) {
                if (!storageCell.hasPower(
                        player, 20.0,
                        handItem
                    )
                ) {
                    return
                }
                storageCell.usePower(
                    player, 20.0,
                    handItem
                )
            }
            val drainedContainer = FluidHelper
                .drainStack(container, containerFluid)
            if (fillSecondSlot(drainedContainer.right)) {
                monitor.injectItems(
                    AEUtils.createFluidStack(containerFluid),
                    Actionable.MODULATE,
                    PlayerSource(player, null)
                )
                decreaseFirstSlot()
            }
        } else if (FluidHelper.isFillableContainerWithRoom(container)) {
            if (selectedFluid == null) {
                return
            }
            val capacity = FluidHelper.getCapacity(container, selectedFluid)
            //Tries to simulate the extraction of fluid from storage.
            val result = monitor.extractItems(
                AEUtils.createFluidStack(selectedFluid, capacity.toLong()), Actionable.SIMULATE, PlayerSource(
                    player, null
                )
            )

            //Calculates the amount of fluid to fill container with.
            val proposedAmount = if (result == null) 0 else Math.min(capacity.toLong(), result.stackSize)
                .toInt()
            if (proposedAmount == 0) {
                return
            }

            //Tries to fill the container with fluid.
            val filledContainer = FluidHelper.fillStack(
                container, FluidStack(
                    selectedFluid, proposedAmount
                )
            )

            //Moves it to second slot and commits extraction to grid.
            if (fillSecondSlot(filledContainer.right)) {
                monitor.extractItems(
                    AEUtils.createFluidStack(
                        selectedFluid, filledContainer.left.toLong()
                    ),
                    Actionable.MODULATE,
                    PlayerSource(player, null)
                )
                decreaseFirstSlot()
            }
        }
    }
}
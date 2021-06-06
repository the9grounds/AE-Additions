package extracells.part.gas

import appeng.api.networking.storage.IStackWatcher
import appeng.api.networking.storage.IStorageGrid
import extracells.integration.Integration
import extracells.part.fluid.PartFluidStorageMonitor
import extracells.util.GasUtil
import extracells.util.StorageChannels
import extracells.util.WrenchUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.common.Optional

class PartGasStorageMonitor: PartFluidStorageMonitor() {

    val isMekanismEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        if (isMekanismEnabled) {
            return onActivateGas(player, hand, pos)
        }

        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    fun onActivateGas(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        if (player?.world == null || hand == null || pos == null) {
            return true
        }

        if (player.world.isRemote) {
            return true
        }

        val itemStack: ItemStack? = player.getHeldItem(hand)

        if (itemStack == null) {
            if (locked) {
                return false
            }

            if (this.fluid == null) {
                return true
            }

            if (this.watcher != null) {
                this.watcher.remove(StorageChannels.GAS!!.createStack(fluid))
            }

            fluid = null
            amount = 0L
            host?.markForUpdate()
            return true
        }

        val rayTraceResult = RayTraceResult(pos, facing, location!!.pos)
        val wrenchHandler = WrenchUtil.getHandler(itemStack, player, rayTraceResult, hand)

        if (wrenchHandler != null) {
            locked = !locked
            wrenchHandler.wrenchUsed(itemStack, player, rayTraceResult, hand)

            host?.markForUpdate()

            if (locked) {
                player.sendMessage(TextComponentTranslation("chat.appliedenergistics2.isNowLocked"))
            } else {
                player.sendMessage(TextComponentTranslation("chat.appliedenergistics2.isNowUnlocked"))
            }

            return true
        }

        if (locked) {
            return false
        }

        if (GasUtil.isFilled(itemStack)) {
            if (fluid != null && watcher != null) {
                watcher.remove(StorageChannels.GAS!!.createStack(fluid))
            }

            val gas = GasUtil.getGasFromContainer(itemStack)
            val fluidStack = GasUtil.getFluidStack(gas)

            this.fluid = fluidStack?.fluid

            if (watcher != null) {
                watcher.add(StorageChannels.GAS!!.createStack(fluid))
            }

            host?.markForUpdate()
            onStackChange()
            return true
        }

        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun onStackChange() {
        if (fluid != null) {
            val node = gridNode ?: return
            val grid = node.grid ?: return

            val storage = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return

            val fluids = gasStorage ?: return

            val gas = GasUtil.getGas(fluid)

            for (s in fluids.storageList) {
                if (s.gas == gas) {
                    amount = s.stackSize
                    host?.markForUpdate()

                    return
                }
            }

            amount = 0L

            host?.markForUpdate()
        }
    }

    override fun updateWatcher(w: IStackWatcher?) {
        watcher = w ?: return
        if (fluid != null) {
            w.add(StorageChannels.GAS!!.createStack(fluid))
        }

        onStackChange(null, null, null, null, null)
    }
}
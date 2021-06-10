package extracells.part.gas

import appeng.api.config.Actionable
import appeng.api.networking.storage.IStackWatcher
import appeng.api.networking.storage.IStorageGrid
import appeng.api.parts.IPartHost
import appeng.api.parts.IPartModel
import appeng.api.storage.IMEMonitor
import extracells.api.gas.IAEGasStack
import extracells.integration.Integration
import extracells.models.PartModels
import extracells.util.*
import mekanism.api.gas.GasStack
import mekanism.api.gas.IGasItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.common.Optional
import org.apache.commons.lang3.tuple.MutablePair

class PartGasConversionMonitor : PartGasStorageMonitor() {

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        if (isMekanismEnabled) {
            return onActivateGas(player, hand, pos)
        }

        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    fun wasActivated(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        if (player == null || player.world == null) {
            return true
        }
        if (player.world.isRemote) return true
        val s: ItemStack? = player.getHeldItem(hand)
        if (s == null) {
            if (this.locked) return false
            if (this.fluid == null) return true
            if (this.watcher != null) this.watcher!!.remove(StorageChannels.GAS!!.createStack(this.fluid!!))
            this.fluid = null
            this.amount = 0L
            val host: IPartHost = host
            if (host != null) {
                host.markForUpdate()
            }
            return true
        }
        val rayTraceResult = RayTraceResult(pos, getFacing(), location!!.pos)
        val wrenchHandler = WrenchUtil.getHandler(s, player, rayTraceResult, hand)
        if (wrenchHandler != null) {
            this.locked = !this.locked
            wrenchHandler.wrenchUsed(s, player, rayTraceResult, hand)
            val host: IPartHost = host
            if (host != null) host.markForUpdate()
            if (this.locked) player.sendMessage(TextComponentTranslation("chat.appliedenergistics2.isNowLocked"))
            else player.sendMessage(TextComponentTranslation("chat.appliedenergistics2.isNowUnlocked"))
            return true
        }
        if (this.locked) return false
        if (GasUtil.isFilled(s)) {
            if (this.fluid != null && this.watcher != null) this.watcher!!.remove(StorageChannels.GAS!!.createStack(this.fluid!!))
            val gas = GasUtil.getGasFromContainer(s)
            val fluidStack = GasUtil.getFluidStack(gas)
            this.fluid = if (fluidStack == null) null else fluidStack.fluid
            if (this.watcher != null) this.watcher!!.add(StorageChannels.GAS!!.createStack(this.fluid!!))
            val host: IPartHost = host
            if (host != null) host.markForUpdate()
            onStackChange()
            return true
        }
        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun onActivateGas(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        val b: Boolean = wasActivated(player, hand, pos)
        if (b) {
            return b
        }
        if (player == null || player.world == null || hand == null || pos == null) {
            return true
        }
        if (player.world.isRemote) return true
        val s: ItemStack = player.getHeldItem(hand)
        val mon: IMEMonitor<IAEGasStack?>? = gasStorage
        if (this.locked && s != null && (!s.isEmpty) && mon != null) {
            val s2: ItemStack = s.copy()
            s2.count = 1
            if (GasUtil.isFilled(s2)) {
                val g = GasUtil.getGasFromContainer(s2)
                if (g == null) return true
                val g1 = StorageChannels.GAS!!.createStack(g)
                val not: IAEGasStack? = mon.injectItems(g1!!.copy(), Actionable.SIMULATE, MachineSource(this))
                if (mon.canAccept(g1) && (not == null || not.stackSize == 0L)) {
                    mon.injectItems(g1, Actionable.MODULATE, MachineSource(this))
                    val empty1: MutablePair<Int, ItemStack> = GasUtil.drainStack(s2, g)
                    val empty: ItemStack = empty1.right
                    if (empty != null && !empty.isEmpty) {
                        dropItems(host.tile.world, host.tile.pos.offset(facing), empty)
                    }
                    val s3: ItemStack = s.copy()
                    s3.setCount(s3.count - 1)
                    if (s3.count == 0) player.setHeldItem(hand, ItemStack.EMPTY)
                    else player.setHeldItem(hand, s3)
                }
                return true
            }
            else if (GasUtil.isEmpty(s2)) {
                if (this.fluid == null) return true
                var extract: IAEGasStack? = null
                val item = s2.item
                if (item is IGasItem) {
                    extract = mon.extractItems(StorageChannels.GAS!!.createStack(GasStack(GasUtil.getGas(this.fluid), item.getMaxGas(s2))), Actionable.SIMULATE, MachineSource(this))
                }
                else
                    return true
                if (extract != null) {
                    mon.extractItems(StorageChannels.GAS.createStack(GasStack(GasUtil.getGas(this.fluid), extract.stackSize.toInt())), Actionable.MODULATE, MachineSource(this))
                    val empty1: MutablePair<Int, ItemStack> = GasUtil.fillStack(s2, extract.gasStack as GasStack)
                    if (empty1.left == 0) {
                        mon.injectItems(StorageChannels.GAS.createStack(GasStack(GasUtil.getGas(this.fluid), extract.stackSize.toInt())), Actionable.MODULATE, MachineSource(this))
                        return true
                    }
                    val empty: ItemStack = empty1.right
                    if (empty != null && !empty.isEmpty) {
                        dropItems(host.tile.world, host.tile.pos.offset(facing), empty)
                    }
                    val s3: ItemStack = s.copy()
                    s3.setCount(s3.count - 1)
                    if (s3.count == 0) player.setHeldItem(hand, ItemStack.EMPTY)
                    else player.setHeldItem(hand, s3)
                }
                return true
            }
        }
        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    fun storageMonitor(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d): Boolean {
        if (player?.world == null) {
            return true
        }
        if (player.world.isRemote) {
            return true
        }
        val s: ItemStack? = player.getHeldItem(hand)
        if (s == null) {
            if (this.locked) return false
            if (this.fluid == null) return true
            if (this.watcher != null) this.watcher!!.remove(AEUtils.createFluidStack(this.fluid))
            this.fluid = null
            this.amount = 0L
            this.host?.markForUpdate()
            return true
        }
        val rayTraceResult = RayTraceResult(pos, facing, location!!.pos)
        val wrenchHandler = WrenchUtil.getHandler(s, player, rayTraceResult, hand)
        if (wrenchHandler != null) {
            this.locked = !this.locked
            wrenchHandler.wrenchUsed(s, player, rayTraceResult, hand)
            this.host?.markForUpdate()
            if (this.locked) player.sendMessage(TextComponentTranslation("chat.appliedenergistics2.isNowLocked"))
            else player.sendMessage(TextComponentTranslation("chat.appliedenergistics2.isNowUnlocked"))
            return true
        }
        if (this.locked) return false
        if (GasUtil.isFilled(s)) {
            if (this.fluid != null && this.watcher != null) this.watcher!!.remove(AEUtils.createFluidStack(this.fluid))
            val gas = GasUtil.getGasFromContainer(s)
            this.fluid = GasUtil.getFluidStack(gas)?.fluid
            if (this.watcher != null) this.watcher!!.add(AEUtils.createFluidStack(this.fluid))
            this.host?.markForUpdate()
            return true
        }
        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun onStackChange() {
        if (this.fluid != null) {
            val node = gridNode ?: return

            val grid = node.grid ?: return

            val storage = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return

            val fluids = gasStorage ?: return

            val gas = GasUtil.getGas(this.fluid)

            for (s in fluids.storageList) {
                if (s!!.gas == gas) {
                    this.amount = s.stackSize
                    host?.markForUpdate()

                    return
                }

            }

            this.amount = 0L
            host?.markForUpdate()
        }
    }

    override fun getStaticModels(): IPartModel {
        return if (isActive && isPowered) {
            PartModels.CONVERSION_MONITOR_HAS_CHANNEL
        } else if (isPowered) {
            PartModels.CONVERSION_MONITOR_ON
        } else {
            PartModels.CONVERSION_MONITOR_OFF
        }
    }

    override fun updateWatcher(w: IStackWatcher?) {
        this.watcher = w

        if (this.fluid != null) {
            w!!.add(StorageChannels.GAS!!.createStack(this.fluid!!))
        }

        onStackChange(null, null, null, null, null)
    }
}
package extracells.integration.mekanism.gas

import appeng.api.config.FuzzyMode
import appeng.api.storage.IStorageChannel
import extracells.api.gas.IAEGasStack
import extracells.item.ItemGas
import extracells.registries.ItemEnum
import extracells.util.StorageChannels
import io.netty.buffer.ByteBuf
import mekanism.api.gas.Gas
import mekanism.api.gas.GasStack
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.math.min

class AEGasStack : IAEGasStack {

    private var canCraft = false
    private var _stackSize = 0L
    private var _countRequestable = 0L
    private var gas: Gas? = null

    constructor(oldStack: AEGasStack) {
        this.gas = oldStack.gas
        this.stackSize = oldStack.stackSize
        this.isCraftable = oldStack.isCraftable
        this.countRequestable = oldStack.countRequestable
    }

    constructor(gasStack: GasStack?) {

        if (gasStack == null || gasStack.gas == null) {
            throw IllegalArgumentException("Gas is null")
        }

        this.gas = gasStack.gas
        this.stackSize = gasStack.amount.toLong()
        this.isCraftable = false
        this.countRequestable = 0
    }

    constructor(nbt: NBTTagCompound) {
        this.gas = Gas.readFromNBT(nbt)
        this.stackSize = nbt.getLong("amount")
        this.isCraftable = nbt.getBoolean("isCraftable")
        this.countRequestable = nbt.getLong("countRequestable")
    }

    constructor(data: ByteBuf) {
        val length = data.readInt()

        var bytes = ByteArray(length)
        data.readBytes(bytes)

        val inputStream = DataInputStream(ByteArrayInputStream(bytes))

        val nbt = CompressedStreamTools.readCompressed(inputStream)

        this.gas = Gas.readFromNBT(nbt)
        this.stackSize = nbt.getLong("amount")
        this.isCraftable = nbt.getBoolean("isCraftable")
        this.countRequestable = nbt.getLong("countRequestable")
    }

    override fun add(stack: IAEGasStack?) {
        if (stack == null) {
            return
        }

        this.incStackSize(stack.stackSize)
        this.countRequestable = this.countRequestable + stack.countRequestable
        this.isCraftable = isCraftable || stack.isCraftable
    }

    override fun getStackSize(): Long = this._stackSize

    override fun setStackSize(stackSize: Long): IAEGasStack {
        this._stackSize = stackSize

        return this
    }

    override fun getCountRequestable(): Long {
        return this._countRequestable
    }

    override fun setCountRequestable(amount: Long): IAEGasStack {
        _countRequestable = amount

        return this
    }

    override fun isCraftable(): Boolean {
        return canCraft
    }

    override fun setCraftable(isCraftable: Boolean): IAEGasStack {
        this.canCraft = isCraftable

        return this
    }

    override fun reset(): IAEGasStack {
        stackSize = 0
        _countRequestable = 0
        canCraft = false

        return this
    }

    override fun isMeaningful(): Boolean = this.stackSize.toInt() != 0 || this.countRequestable > 0 || isCraftable

    override fun incStackSize(amount: Long) {
        this._stackSize += amount
    }

    override fun decStackSize(amount: Long) {
        this._stackSize -= amount
    }

    override fun incCountRequestable(amount: Long) {
        this._countRequestable += amount
    }

    override fun decCountRequestable(amount: Long) {
        this._countRequestable -= amount
    }

    override fun writeToNBT(nbt: NBTTagCompound?) {
        if (gas == null || nbt == null) {
            return
        }

        gas!!.write(nbt)

        nbt.setLong("amount", stackSize)
        nbt.setBoolean("isCraftable", isCraftable)
        nbt.setLong("countRequestable", countRequestable)
    }

    override fun fuzzyComparison(gasStack: IAEGasStack?, fuzzyMode: FuzzyMode?): Boolean {
        return when(gasStack) {
            is Gas -> gasStack == gas
            // TODO: Confirm works
            is IAEGasStack -> gasStack.gas == gas
            is AEGasStack -> gasStack.gas == gas
            else -> false
        }
    }

    override fun writeToPacket(byteBuffer: ByteBuf?) {
        if (byteBuffer == null) {
            return
        }

        val byteOutputStream = ByteArrayOutputStream()
        val outputStream = DataOutputStream(byteOutputStream)
        val nbt = NBTTagCompound()

        writeToNBT(nbt)

        CompressedStreamTools.writeCompressed(nbt, outputStream)

        val bytes = byteOutputStream.toByteArray()
        val length = bytes.size

        byteBuffer.writeInt(length)
        byteBuffer.writeBytes(bytes)
    }

    override fun copy(): IAEGasStack = AEGasStack(this)

    override fun empty(): IAEGasStack {
        val newStack = copy()
        newStack.reset()
        return newStack
    }

    override fun isItem(): Boolean = false

    override fun isFluid(): Boolean = false

    override fun getChannel(): IStorageChannel<IAEGasStack> = StorageChannels.GAS!!

    override fun asItemStackRepresentation(): ItemStack {
        val stack = ItemEnum.GASITEM.getSizedStack(1)

        ItemGas.setGasName(stack, gas!!.name)

        return stack
    }

    override fun getGasStack(): GasStack = GasStack(gas, min(Int.MAX_VALUE, stackSize.toInt()))

    override fun getGas(): Any? = gas

    override fun hashCode(): Int = gas.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AEGasStack

        if (canCraft != other.canCraft) return false
        if (_countRequestable != other._countRequestable) return false
        if (gas != other.gas) return false

        return true
    }


}
package com.the9grounds.aeadditions.network.packet

import com.the9grounds.aeadditions.container.gas.ContainerGasInterface
import com.the9grounds.aeadditions.gui.gas.GuiGasInterface
import com.the9grounds.aeadditions.integration.mekanism.gas.MekanismGas
import com.the9grounds.aeadditions.util.GasUtil
import com.the9grounds.aeadditions.util.GuiUtil
import mekanism.api.gas.Gas
import mekanism.api.gas.GasRegistry
import mekanism.api.gas.GasTank
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PacketGasInterface(val gasTanks: List<GasTank>, val gasConfig: List<Gas?>) : Packet() {
    override fun getPacketId(): PacketId = PacketId.GAS_INTERFACE

    override fun writeData(data: PacketBufferEC) {
        val tag = NBTTagCompound()
        gasTanks.forEachIndexed { index, gasTank ->
            tag.setTag("tank#${index}", gasTank.write(NBTTagCompound()))
        }

        gasConfig.forEachIndexed { index, gas ->
            if (gas != null) {
                tag.setString("gasConfig#${index}", gas.name)
            }
        }

        data.writeCompoundTag(tag)
    }

    @SideOnly(Side.CLIENT)
    class Handler : IPacketHandlerClient {
        override fun onPacketData(data: PacketBufferEC, player: EntityPlayer) {
            val tag = data.readCompoundTag()!!
            val tanks = mutableListOf<GasTank>()
            val gasConfig = mutableListOf<Gas?>()

            for (i in 0 until 6) {
                tanks.add(i, GasTank.readFromNBT(tag.getCompoundTag("tank#${i}")))

                if (tag.hasKey("gasConfig#${i}")) {
                    gasConfig.add(i, GasRegistry.getGas(tag.getString("gasConfig#${i}")))
                } else {
                    gasConfig.add(i, null)
                }
            }

            val gui = GuiUtil.getGui(GuiGasInterface::class.java)
            val container = GuiUtil.getContainer(gui, ContainerGasInterface::class.java)
                ?: return

            for (i in 0 until 6) {
                val tank = tanks[i]
                val gas = gasConfig[i]

                val existingTank = container.gasInterface.gasTanks[i]

                existingTank.gas = tank.gas
                gui!!.filter[i]!!.fluid = if (gas == null) null else MekanismGas.fluidGas[gas]
            }
        }

    }
}
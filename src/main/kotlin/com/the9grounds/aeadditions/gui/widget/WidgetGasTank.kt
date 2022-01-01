package com.the9grounds.aeadditions.gui.widget

import com.the9grounds.aeadditions.integration.mekanism.gas.MekanismGas
import mekanism.api.gas.GasTank
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.EnumFacing
import net.minecraft.util.text.translation.I18n

class WidgetGasTank @JvmOverloads constructor(
    widgetManager: WidgetManager?,
    var tank: GasTank,
    xPos: Int,
    yPos: Int,
    var index: Int = 0
) : AbstractWidget(widgetManager, xPos, yPos) {
    override fun draw(mouseX: Int, mouseY: Int) {
        if (tank == null) {
            return
        }
        val textureManager = manager.mc.textureManager
        GlStateManager.disableLighting()

        GlStateManager.color(1.0f, 1.0f, 1.0f)

        val gas = tank!!.gas
        if (gas != null && gas.amount > 0) {
            textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            val sprite = Minecraft.getMinecraft().textureMapBlocks.getAtlasSprite(MekanismGas.fluidGas.get(gas.gas)?.still.toString())

            val color = gas.gas.tint

            GlStateManager.color(getRed(color), getGreen(color), getBlue(color))

            val scaledHeight = (this.height * (tank.getStored().toFloat() / tank.maxGas.toFloat() )).toInt()

            val iconHeightRemainder = scaledHeight % 16

            if (iconHeightRemainder > 0) {
                manager.gui.drawTexturedModalRect(xPos, yPos + height - iconHeightRemainder, sprite, 16, iconHeightRemainder)
            }
            for (i in 0 until scaledHeight / 16) {
                manager.gui.drawTexturedModalRect(xPos, yPos + this.height - iconHeightRemainder - ( i + 1 ) * 16, sprite, 16, 16)
            }
        }
        GlStateManager.enableLighting()
    }

    override fun getToolTip(mouseX: Int, mouseY: Int): List<String> {
        val description: MutableList<String> = ArrayList()
        if (tank == null || tank!!.gas == null) {
            description.add(I18n.translateToLocal("com.the9grounds.aeadditions.tooltip.empty1"))
            description.add("Side: ${EnumFacing.byIndex(index).name}")
        } else {
            if (tank!!.gas!!.amount > 0
                && tank!!.gas != null
            ) {
                val amountToText = tank!!.gas!!.amount.toString() + "mB"
                description.add(tank!!.gas!!.gas.localizedName)
                description.add(amountToText)
                description.add("Side: ${EnumFacing.byIndex(index).name}")
            }
        }
        return description
    }

    private fun getRed(color: Int): Float {
        return (color shr 16 and 0xFF) / 255.0f
    }

    private fun getGreen(color: Int): Float {
        return (color shr 8 and 0xFF) / 255.0f
    }

    private fun getBlue(color: Int): Float {
        return (color and 0xFF) / 255.0f
    }

    init {
        width = 16
        height = 68
    }
}
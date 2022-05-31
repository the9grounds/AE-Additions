package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.button.AccessButton
import com.the9grounds.aeadditions.client.gui.button.TransceiverTypeButton
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class MEWirelessTransceiverScreen(menu: MEWirelessTransceiverMenu, inventory: Inventory, title: Component) : AbstractContainerScreen<MEWirelessTransceiverMenu>(
    menu, inventory, title
) {
    val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/me_wireless_transceiver.png"))
    val channelListBackground = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/channel-list-background.png"))
    private val accessBtn = AccessButton(this, false)
    private val typeButton = TransceiverTypeButton(this)
    
    init {

        imageHeight = 227 
        
        titleLabelX = 5
        titleLabelY = 5
        inventoryLabelY = 135
        inventoryLabelX = 8
    }
    override fun renderBg(posStack: PoseStack, ticks: Float, p_97789_: Int, p_97790_: Int) {
        texture.dest(guiLeft, guiTop).src(0, 0, xSize, ySize).blit(posStack, 0)
        channelListBackground.dest(guiLeft + 16, guiTop + 70, 144, 50).src(0, 0, 2, 2).blit(posStack, -3)
    }

    override fun render(posStack: PoseStack, p_97796_: Int, p_97797_: Int, p_97798_: Float) {
        super.render(posStack, p_97796_, p_97797_, p_97798_)
        accessBtn.x = guiLeft + imageWidth + 2
        accessBtn.y = guiTop + 3
        accessBtn.render(posStack, p_97796_, p_97797_, p_97798_)
        typeButton.x = guiLeft + imageWidth + 2
        typeButton.y = guiTop + 16 + 12
        typeButton.render(posStack, p_97796_, p_97797_, p_97798_)
        
        posStack.pushPose()
        posStack.translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)
        val initialPaddingX = 1.toDouble()
        val initialPaddingY = 1.toDouble()
        posStack.translate(16.0 + initialPaddingX, 70.0 + initialPaddingY, -1.0)
        
        // All Channels
        val filtered = menu.channelInfos.filter { it.isPrivate == menu.isPrivate }
        for (channelInfo in filtered) {
            val text = TextComponent(channelInfo.name)
            
            this.font.draw(posStack, text, 0f, 0f, Color(58, 201, 178, 1).toARGB())
        }
    }

    override fun mouseClicked(p_97748_: Double, p_97749_: Double, p_97750_: Int): Boolean {
        accessBtn.mouseClicked(p_97748_, p_97749_, p_97750_)
        typeButton.mouseClicked(p_97748_, p_97749_, p_97750_)
        return super.mouseClicked(p_97748_, p_97749_, p_97750_)
    }
    
    fun accessButtonPressed(button: Button) {
        menu.isPrivate = !menu.isPrivate
        
        accessBtn.isPrivate = menu.isPrivate
    }
    
    fun transceiverTypeButtonPressed(button: Button) {
        menu.isSubscriber = !menu.isSubscriber
        
        typeButton.isSubscriber = menu.isSubscriber
    }
}
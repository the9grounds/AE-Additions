package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.button.*
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
    private val createButton = CreateButton(this)
    private var textField: TextField? = null
    private var clearChannelButton: ClearChannelButton? = null
    private var setChannelButton: SetChannelButton? = null
    
    init {

        imageHeight = 227 
        
        titleLabelX = 5
        titleLabelY = 5
        inventoryLabelY = 135
        inventoryLabelX = 8
    }

    override fun init() {
        super.init()

        textField = TextField(font, guiLeft + 16, guiTop + 33, 126, 14)
        setChannelButton = SetChannelButton(this, font)
        clearChannelButton = ClearChannelButton(this, font)
    }
    
    override fun renderBg(posStack: PoseStack, ticks: Float, p_97789_: Int, p_97790_: Int) {
        texture.dest(guiLeft, guiTop).src(0, 0, xSize, ySize).blit(posStack, 0)
        channelListBackground.dest(guiLeft + 16, guiTop + 70, 144, 50).src(0, 0, 2, 2).blit(posStack, -3)
    }

    override fun render(posStack: PoseStack, mouseX: Int, mouseY: Int, p_97798_: Float) {
        super.render(posStack, mouseX, mouseY, p_97798_)
        accessBtn.x = guiLeft + imageWidth + 2
        accessBtn.y = guiTop + 3
        accessBtn.render(posStack, mouseX, mouseY, p_97798_)
        typeButton.x = guiLeft + imageWidth + 2
        typeButton.y = guiTop + 16 + 12
        typeButton.render(posStack, mouseX, mouseY, p_97798_)
        font.draw(posStack, TextComponent("Channel Name"), (guiLeft + 16).toFloat(), (guiTop + 22).toFloat(), Color(0, 0, 0, 1).toARGB())
        textField?.render(posStack, mouseX, mouseY, p_97798_)
        createButton.x = guiLeft + 16 + textField!!.width + 2
        createButton.y = guiTop + 22 + 10
        createButton.render(posStack, mouseX, mouseY, p_97798_)
        clearChannelButton?.x = guiLeft + 75
        clearChannelButton?.y = guiTop + 125
        clearChannelButton?.renderButton(posStack, mouseX, mouseY, p_97798_)
        setChannelButton?.x = guiLeft + 120
        setChannelButton?.y = guiTop + 125
        setChannelButton?.renderButton(posStack, mouseX, mouseY, p_97798_)

        clearChannelButton?.active = !menu.isOnChannel
        
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
        
        posStack.popPose()
        
        this.renderTooltips(posStack, mouseX, mouseY)
    }

    override fun mouseClicked(p_97748_: Double, p_97749_: Double, p_97750_: Int): Boolean {
        accessBtn.mouseClicked(p_97748_, p_97749_, p_97750_)
        typeButton.mouseClicked(p_97748_, p_97749_, p_97750_)
        createButton.mouseClicked(p_97748_, p_97749_, p_97750_)
        clearChannelButton?.mouseClicked(p_97748_, p_97749_, p_97750_)
        setChannelButton?.mouseClicked(p_97748_, p_97749_, p_97750_)
        textField?.mouseClicked(p_97748_, p_97749_, p_97750_)
        return super.mouseClicked(p_97748_, p_97749_, p_97750_)
    }

    override fun keyPressed(p_97765_: Int, p_97766_: Int, p_97767_: Int): Boolean {
        textField?.keyPressed(p_97765_, p_97766_, p_97767_)
        return super.keyPressed(p_97765_, p_97766_, p_97767_)
    }
    
    private fun renderTooltips(posStack: PoseStack, mouseX: Int, mouseY: Int) {
        renderTooltip(posStack, mouseX, mouseY)
        
        if (accessBtn.isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
            accessBtn.renderToolTip(posStack, mouseX, mouseY)
        }
        
        if (typeButton.isMouseOver(mouseX.toDouble(), mouseY.toDouble())) {
            typeButton.renderToolTip(posStack, mouseX, mouseY)
        }
    }
    
    override fun charTyped(p_94683_: Char, p_94684_: Int): Boolean {
        textField?.charTyped(p_94683_, p_94684_)
        return super.charTyped(p_94683_, p_94684_)
    }
    
    fun accessBtnTooltip(button: Button, posStack: PoseStack, mouseX: Int, mouseY: Int) {
        this.renderTooltip(posStack, TextComponent(if (accessBtn.isPrivate) "Private" else "Public"), mouseX, mouseY)
    }
    
    fun transceiverTypeButtonTooltip(button: Button, posStack: PoseStack, mouseX: Int, mouseY: Int) {
        val label = if (typeButton.isSubscriber) {
            "Subscriber"
        } else {
            "Broadcaster"
        }
        this.renderTooltip(posStack, TextComponent(label), mouseX, mouseY)
    }
    
    fun accessButtonPressed(button: Button) {
        menu.isPrivate = !menu.isPrivate
        
        accessBtn.isPrivate = menu.isPrivate
    }
    
    fun transceiverTypeButtonPressed(button: Button) {
        menu.isSubscriber = !menu.isSubscriber
        
        typeButton.isSubscriber = menu.isSubscriber
    }
    
    fun createButtonPressed(button: Button) {
        
    }
    
    fun clearChannelButtonPressed(button: Button) {
        
    }
    
    fun setChannelButtonPressed(button: Button) {
        
    }
}
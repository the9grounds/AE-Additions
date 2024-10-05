package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.button.*
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.packet.CreateChannelPacket
import com.the9grounds.aeadditions.core.network.packet.DeleteChannelPacket
import com.the9grounds.aeadditions.core.network.packet.TransceiverDataChange
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory


class MEWirelessTransceiverScreen(menu: MEWirelessTransceiverMenu, inventory: Inventory, title: Component) : AbstractContainerScreen<MEWirelessTransceiverMenu>(
    menu, inventory, title
) {
    val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/me_wireless_transceiver.png"))
    val channelListBackground = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/channel-list-background.png"))
    val accessBtn = AccessButton(this, false)
    val typeButton = TransceiverTypeButton(this)
    private val createButton = CreateButton(this)
    private var extraDataPane: ExtraDataPane? = null
    private var textField: TextField? = null
    private var scrollBar: ScrollBar? = null
    private var deleteChannelButton: DeleteChannelButton? = null
    private var setChannelButton: SaveChangesButton? = null
    private var channelFields : MutableMap<ChannelInfo, ChannelField> = mutableMapOf()
    private var selectedChannel: ChannelInfo? = null
    
    private var channels = listOf<ChannelInfo>()
    
    var currentCursor = 0
    
    init {

        imageHeight = 227 
        
        titleLabelX = 5
        titleLabelY = 5
        inventoryLabelY = 135
        inventoryLabelX = 8
        
        menu.screen = this
    }

    override fun init() {
        super.init()

        textField = TextField(font, guiLeft + 16, guiTop + 33, 126, 14)
        setChannelButton = SaveChangesButton(this, font)
        deleteChannelButton = DeleteChannelButton(this, font)
        extraDataPane = ExtraDataPane(guiLeft - 128, guiTop, this, font)
        scrollBar = ScrollBar(guiLeft + 16 + 138, guiTop + 70, this)
    }
    
    override fun renderBg(guiGraphics: GuiGraphics, ticks: Float, p_97789_: Int, p_97790_: Int) {
        this.renderBackground(guiGraphics)
        texture.dest(guiLeft, guiTop).src(0, 0, xSize, ySize).blit(guiGraphics)
        // TODO: Understand implication of removed z value
        channelListBackground.dest(guiLeft + 16, guiTop + 70, 144, 50).src(0, 0, 2, 2).blit(guiGraphics)
    }


    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, p_97798_: Float) {
        super.render(guiGraphics, mouseX, mouseY, p_97798_)
        accessBtn.x = guiLeft + imageWidth + 2
        accessBtn.y = guiTop + 3
        accessBtn.render(guiGraphics, mouseX, mouseY, p_97798_)
        typeButton.x = guiLeft + imageWidth + 2
        typeButton.y = guiTop + 16 + 12
        typeButton.render(guiGraphics, mouseX, mouseY, p_97798_)
        font.drawInBatch("Channel Name", (guiLeft + 16).toFloat(), (guiTop + 22).toFloat(), Color(0, 0, 0, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)
        textField?.render(guiGraphics, mouseX, mouseY, p_97798_)
        createButton.x = guiLeft + 16 + textField!!.width + 2
        createButton.y = guiTop + 22 + 10
        createButton.render(guiGraphics, mouseX, mouseY, p_97798_)
        deleteChannelButton?.x = guiLeft + 75
        deleteChannelButton?.y = guiTop + 125
        deleteChannelButton?.render(guiGraphics, mouseX, mouseY, p_97798_)
        setChannelButton?.x = guiLeft + 120
        setChannelButton?.y = guiTop + 125
        setChannelButton?.render(guiGraphics, mouseX, mouseY, p_97798_)
        extraDataPane?.render(guiGraphics, mouseX, mouseY, p_97798_)
        scrollBar?.render(guiGraphics, mouseX, mouseY, p_97798_)
        
        val channelName = if (menu.isOnChannel) {
            menu.currentChannel?.name ?: "None"
        } else {
            "None"
        }
        
        font.drawInBatch(Component.literal("Current Channel:"), (guiLeft + 16).toFloat(), (guiTop + 50).toFloat(), Color(0, 0, 0, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)
        font.drawInBatch(Component.literal(channelName), (guiLeft + 104).toFloat(), (guiTop + 50).toFloat(), Color(0, 88, 12, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)
        
        var mode = "None"
        
        if (menu.isOnChannel) {
            mode = if (menu.isCurrentlySubscribing) {
                "Subscribing"
            } else {
                "Broadcasting"
            }
        }
        
        font.drawInBatch(Component.literal("Current Mode:"), (guiLeft + 16).toFloat(), (guiTop + 60).toFloat(), Color(0, 0, 0, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)
        font.drawInBatch(Component.literal(mode), (guiLeft + 90).toFloat(), (guiTop + 60).toFloat(), Color(0, 88, 12, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)

        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)
        val initialPaddingX = 1.toDouble()
        val initialPaddingY = 1.toDouble()
        guiGraphics.pose().translate(16.0 + initialPaddingX, 70.0 + initialPaddingY, 0.0)
        
        // All Channels
        var channelX = 0
        var channelY =  0
        guiGraphics.pose().pushPose()
        guiGraphics.pose().scale(.8f, .8f, .8f)
        channels.forEachIndexed { index, channelInfo ->
            val channelField = this.channelFields[channelInfo]
            if (channelField != null) {
                val shouldShow = shouldShowChannel(index)
                channelField.updateValues(shouldShow, selectedChannel == channelInfo, channelY)
                channelField.render(guiGraphics, 0, 0, 0f)

                if (shouldShow) {
                    channelY += 12
                }
            }
        }

        guiGraphics.pose().popPose()
        guiGraphics.pose().popPose()
    }
    
    fun onChannelListChanged() {
        channelFields.clear()
        
        channels = menu.channelInfos.filter { it.isPrivate == menu.isPrivate }
        
        channels.forEach { 
            channelFields.put(it, ChannelField(font, it, guiLeft, guiTop))
        }
    }

    override fun mouseClicked(x: Double, y: Double, p_97750_: Int): Boolean {
        accessBtn.mouseClicked(x, y, p_97750_)
        typeButton.mouseClicked(x, y, p_97750_)
        createButton.mouseClicked(x, y, p_97750_)
        deleteChannelButton?.mouseClicked(x, y, p_97750_)
        setChannelButton?.mouseClicked(x, y, p_97750_)
        val wasClicked = textField?.mouseClicked(x, y, p_97750_)

        if (wasClicked !== null) {
            if (wasClicked) {
                textField?.isFocused = true
            } else {
                if (textField!!.isFocused) {
                    textField?.isFocused = false
                }
            }
        }

        val filtered = menu.channelInfos.filter { it.isPrivate == menu.isPrivate }
        
        filtered.forEach { 
            if (channelFields[it]?.mouseClicked(x, y, p_97750_) == true) {
                selectedChannel = it
            }
        }
        
        return super.mouseClicked(x, y, p_97750_)
    }

    override fun keyPressed(key: Int, p_97766_: Int, p_97767_: Int): Boolean {
        textField?.keyPressed(key, p_97766_, p_97767_)
        if (key !== 256 && textField?.canConsumeInput() == true) {
            return true;
        }
        return super.keyPressed(key, p_97766_, p_97767_)
    }

    override fun mouseReleased(p_97812_: Double, p_97813_: Double, p_97814_: Int): Boolean {
        scrollBar?.mouseReleased(p_97812_, p_97813_, p_97814_)
        return super.mouseReleased(p_97812_, p_97813_, p_97814_)
    }

    override fun mouseDragged(
        p_97752_: Double,
        p_97753_: Double,
        p_97754_: Int,
        p_97755_: Double,
        p_97756_: Double
    ): Boolean {
        scrollBar?.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_)
        return super.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_)
    }
    
    override fun charTyped(p_94683_: Char, p_94684_: Int): Boolean {
        textField?.charTyped(p_94683_, p_94684_)
        return super.charTyped(p_94683_, p_94684_)
    }
    
    fun accessBtnTooltip(button: Button): Tooltip {
        return Tooltip.create(Component.literal("Access Mode\n${if (accessBtn.isPrivate) "Private" else "Public"}"))
    }
    
    fun transceiverTypeButtonTooltip(button: Button): Tooltip {
        val label = if (typeButton.isSubscriber) {
            "Subscriber"
        } else {
            "Broadcaster"
        }

        return Tooltip.create(Component.literal("Transceiver Mode\n${label}"))
    }
    
    fun accessButtonPressed(button: Button) {
        menu.isPrivate = !menu.isPrivate
        
        accessBtn.isPrivate = menu.isPrivate
        
        onChannelListChanged()
    }
    
    fun transceiverTypeButtonPressed(button: Button) {
        menu.isSubscriber = !menu.isSubscriber
        
        typeButton.isSubscriber = menu.isSubscriber
    }
    
    fun createButtonPressed(button: Button) {
        val channelName = textField?.value
        val private = menu.isPrivate
        
        if (channelName != null && channelName.length > 0) {
            NetworkManager.sendToServer(CreateChannelPacket(private, channelName, menu.isSubscriber))
        }
        
        textField?.value = ""
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, wheelDelta: Double): Boolean {
        
        if (mouseX > guiLeft + 16 && mouseX < guiLeft + 160 && mouseY > guiTop + 70 && mouseY < guiTop + 120) {
            scrollBar?.mouseScrolled(mouseX, mouseY, wheelDelta)
        }
        
        return super.mouseScrolled(mouseX, mouseY, wheelDelta)
    }
    
    fun deleteChannelButtonPressed(button: Button) {
        if (selectedChannel != null) {
            NetworkManager.sendToServer(DeleteChannelPacket(selectedChannel!!))
            selectedChannel = null
            menu.currentChannel = null
        }
    }
    
    fun saveChangesButtonPressed(button: Button) {
        if (selectedChannel !== null || menu.currentChannel !== null) {
            NetworkManager.sendToServer(
                TransceiverDataChange(
                    menu.isSubscriber,
                    Minecraft.getInstance().level!!,
                    selectedChannel ?: menu.currentChannel!!
                )
            )
        }
    }
    
    fun shouldShowChannel(index: Int): Boolean {
        return index >= currentCursor && index < currentCursor + 5
    }
    
    fun scrollNumber(): Int {
        return (channels.size - 5).coerceAtLeast(0)
    }
    
    fun scrollUnit(): Double {
        return 45.0 / (scrollNumber() + 1)
    }
}
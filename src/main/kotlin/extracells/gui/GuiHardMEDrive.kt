package extracells.gui

import extracells.container.ContainerHardMEDrive
import extracells.registries.BlockEnum
import extracells.tileentity.TileEntityHardMeDrive
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation

class GuiHardMEDrive(val inventory: InventoryPlayer, val tile: TileEntityHardMeDrive): GuiContainer(ContainerHardMEDrive(inventory, tile)) {

    private val guiTexture = ResourceLocation("extracells", "textures/gui/hardmedrive.png")

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(guiTexture);
        val posX = (width - xSize) / 2;
        val posY = (height - ySize) / 2;
        drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);

        for (s in this.inventorySlots.inventorySlots) {
            renderBackground(s as Slot)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(BlockEnum.BLASTRESISTANTMEDRIVE.statName, 5, 5, 0x000000)
    }

    private fun renderBackground(slot: Slot) {
        if ((slot.stack == null || slot.stack.isEmpty) && slot.slotNumber < 3) {
            GlStateManager.disableLighting()
            GlStateManager.enableBlend()
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F)
            this.mc.textureManager.bindTexture(ResourceLocation("appliedenergistics2", "textures/guis/states.png"))
            this.drawTexturedModalRect(this.guiLeft + slot.xPos, this.guiTop + slot.yPos, 240, 0, 16, 16)
            GlStateManager.disableBlend()
            GlStateManager.enableLighting()
        }
    }
}
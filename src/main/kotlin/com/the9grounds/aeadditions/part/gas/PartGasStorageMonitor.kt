package com.the9grounds.aeadditions.part.gas

import appeng.api.networking.security.IActionSource
import appeng.api.networking.storage.IStackWatcher
import appeng.api.networking.storage.IStackWatcherHost
import appeng.api.networking.storage.IStorageGrid
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.storage.IMEMonitor
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IAEStack
import appeng.api.storage.data.IItemList
import appeng.api.util.AECableType
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.models.PartModels
import com.the9grounds.aeadditions.part.PartECBase
import com.the9grounds.aeadditions.util.AEUtils
import com.the9grounds.aeadditions.util.GasUtil
import com.the9grounds.aeadditions.util.StorageChannels
import com.the9grounds.aeadditions.util.WrenchUtil
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import java.io.IOException

open class PartGasStorageMonitor: PartECBase(), IStackWatcherHost {

    var fluid: Fluid? = null
    var amount = 0L
    var dspList: Any? = null
    var locked = false
    var watcher: IStackWatcher? = null

    val gasStorage: IMEMonitor<IAEGasStack?>?
    get() {
        val n = gridNode ?: return null
        val g = n.grid ?: return null
        val storage = g.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return null
        return storage.getInventory(StorageChannels.GAS)
    }

    override fun getCableConnectionLength(aeCableType: AECableType?): Float {
        return 1.0f
    }

    fun dropItems(world: World?, pos: BlockPos, stack: ItemStack?) {
        if (world == null) {
            return
        }
        if (!world.isRemote) {
            val f = 0.7f
            val d0 = world.rand.nextFloat() * f + (1.0f - f) * 0.5
            val d1 = world.rand.nextFloat() * f + (1.0f - f) * 0.5
            val d2 = world.rand.nextFloat() * f + (1.0f - f) * 0.5
            val entityitem = EntityItem(world, pos.x + d0, pos.y + d1, pos.z + d2, stack)
            entityitem.setPickupDelay(10)
            world.spawnEntity(entityitem)
        }
    }

    override fun getBoxes(bch: IPartCollisionHelper) {
        bch.addBox(2.0, 2.0, 14.0, 14.0, 14.0, 16.0)
        bch.addBox(4.0, 4.0, 13.0, 12.0, 12.0, 14.0)
        bch.addBox(5.0, 5.0, 12.0, 11.0, 11.0, 13.0)
    }

    override fun getPowerUsage(): Double = 1.0

    override fun getWailaBodey(data: NBTTagCompound, list: MutableList<String?>): List<String?>? {
        super.getWailaBodey(data, list)
        var amount = 0L
        var fluid: Fluid? = null
        if (data.hasKey("locked") && data.getBoolean("locked")) {
            list.add(
                I18n
                    .translateToLocal("waila.appliedenergistics2.Locked")
            )
        } else {
            list.add(
                I18n
                    .translateToLocal("waila.appliedenergistics2.Unlocked")
            )
        }
        if (data.hasKey("amount")) {
            amount = data.getLong("amount")
        }
        if (data.hasKey("fluid")) {
            val fluidName = data.getString("fluid")
            if (!fluidName.isEmpty()) {
                fluid = FluidRegistry.getFluid(fluidName)
            }
        }
        if (fluid != null) {
            list.add(
                I18n.translateToLocal("com.the9grounds.aeadditions.tooltip.fluid")
                        + ": "
                        + fluid.getLocalizedName(
                    FluidStack(
                        fluid,
                        Fluid.BUCKET_VOLUME
                    )
                )
            )
            if (isActive) {
                list.add(
                    (I18n
                        .translateToLocal("com.the9grounds.aeadditions.tooltip.amount")
                            + ": "
                            + amount + "mB")
                )
            } else {
                list.add(
                    (I18n
                        .translateToLocal("com.the9grounds.aeadditions.tooltip.amount")
                            + ": 0mB")
                )
            }
        } else {
            list.add(
                (I18n.translateToLocal("com.the9grounds.aeadditions.tooltip.fluid")
                        + ": "
                        + I18n
                    .translateToLocal("com.the9grounds.aeadditions.tooltip.empty1"))
            )
            list.add(
                I18n
                    .translateToLocal("com.the9grounds.aeadditions.tooltip.amount") + ": 0mB"
            )
        }
        return list
    }

    override fun getWailaTag(tag: NBTTagCompound): NBTTagCompound? {
        super.getWailaTag(tag)
        tag.setBoolean("locked", this.locked)
        tag.setLong("amount", this.amount)
        if (fluid == null) {
            tag.setString("fluid", "")
        } else {
            tag.setString("fluid", fluid!!.getName())
        }
        return tag
    }

    override fun readFromNBT(data: NBTTagCompound) {
        super.readFromNBT(data)
        if (data.hasKey("amount")) {
            amount = data.getLong("amount")
        }
        if (data.hasKey("fluid")) {
            val name = data.getString("fluid")
            if (name.isEmpty()) {
                fluid = null
            } else {
                fluid = FluidRegistry.getFluid(name)
            }
        }
        if (data.hasKey("locked")) {
            locked = data.getBoolean("locked")
        }
    }

    @Throws(IOException::class)
    override fun readFromStream(data: ByteBuf): Boolean {
        super.readFromStream(data)
        amount = data.readLong()
        val name = ByteBufUtils.readUTF8String(data)
        if (name.isEmpty()) {
            fluid = null
        } else {
            fluid = FluidRegistry.getFluid(name)
        }
        locked = data.readBoolean()
        return true
    }

    val isMekanismEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        if (isMekanismEnabled) {
            return onActivateGas(player, hand, pos)
        }

        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    open fun onActivateGas(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
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
                this.watcher!!.remove(StorageChannels.GAS!!.createStack(fluid!!))
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
                watcher!!.remove(StorageChannels.GAS!!.createStack(fluid!!))
            }

            val gas = GasUtil.getGasFromContainer(itemStack)
            val fluidStack = GasUtil.getFluidStack(gas)

            this.fluid = fluidStack?.fluid

            if (watcher != null) {
                watcher!!.add(StorageChannels.GAS!!.createStack(fluid!!))
            }

            host?.markForUpdate()
            onStackChange()
            return true
        }

        return false
    }

    override fun onStackChange(
        p0: IItemList<*>?,
        p1: IAEStack<*>?,
        p2: IAEStack<*>?,
        p3: IActionSource?,
        p4: IStorageChannel<*>?
    ) {
        onStackChange()
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    open fun onStackChange() {
        if (fluid != null) {
            val node = gridNode ?: return
            val grid = node.grid ?: return

            val storage = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return

            val fluids = gasStorage ?: return

            val gas = GasUtil.getGas(fluid)

            for (s in fluids.storageList) {
                if (s!!.gas == gas) {
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
            w.add(StorageChannels.GAS!!.createStack(fluid!!))
        }

        onStackChange(null, null, null, null, null)
    }

    @SideOnly(Side.CLIENT)
    override fun renderDynamic(x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {
        if (fluid == null) {
            return
        }
        if (dspList == null) {
            dspList = GLAllocation.generateDisplayLists(1)
        }
        if (!isActive) {
            return
        }
        val aeFluidStack = AEUtils.createFluidStack(fluid)
        aeFluidStack!!.stackSize = amount
        if (aeFluidStack != null) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5)
            GlStateManager.glNewList((dspList as Int?)!!, GL11.GL_COMPILE_AND_EXECUTE)
            val tess = Tessellator.getInstance()
            renderFluid(tess, aeFluidStack)
            GlStateManager.glEndList()
            GlStateManager.popMatrix()
        }
    }

    @SideOnly(Side.CLIENT)
    private fun renderFluid(tess: Tessellator, fluidStack: IAEFluidStack) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
        val facing = side.facing
        moveToFace(facing)
        rotateToFace(facing, 1.toByte())
        GlStateManager.pushMatrix()
        try {
            val br = 16 shl 20 or 16 shl 4
            val var11 = br % 65536
            val var12 = br / 65536
            OpenGlHelper.setLightmapTextureCoords(
                OpenGlHelper.lightmapTexUnit,
                var11 * 0.8f, var12 * 0.8f
            )
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            GlStateManager.disableLighting()
            GlStateManager.disableRescaleNormal()
            // RenderHelper.enableGUIStandardItemLighting();
            val mc = Minecraft.getMinecraft()
            val fluidStill = fluid!!.still
            if (fluidStill != null) {
                val textureMap = mc.textureMapBlocks
                val fluidIcon = textureMap.getAtlasSprite(fluidStill.toString())
                if (fluidIcon != null) {
                    GL11.glTranslatef(0.0f, 0.14f, -0.24f)
                    GL11.glScalef(1.0f / 62.0f, 1.0f / 62.0f, 1.0f / 62.0f)
                    GL11.glTranslated(-8.6, -16.3, -1.2)
                    mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
                    val buffer = tess.buffer
                    buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
                    try {
                        buffer.pos(0.0, 16.0, 0.0).tex(fluidIcon.minU.toDouble(), fluidIcon.maxV.toDouble())
                            .color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
                        buffer.pos(16.0, 16.0, 0.0).tex(fluidIcon.maxU.toDouble(), fluidIcon.maxV.toDouble())
                            .color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
                        buffer.pos(16.0, 0.0, 0.0).tex(fluidIcon.maxU.toDouble(), fluidIcon.minV.toDouble())
                            .color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
                        buffer.pos(0.0, 0.0, 0.0).tex(fluidIcon.minU.toDouble(), fluidIcon.minV.toDouble())
                            .color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
                    } finally {
                        tess.draw()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GlStateManager.popMatrix()
        GlStateManager.translate(0.0f, 0.14f, -0.24f)
        GlStateManager.scale(1.0f / 62.0f, 1.0f / 62.0f, 1.0f / 62.0f)
        var qty = fluidStack.stackSize
        if (qty > 999999999999L) {
            qty = 999999999999L
        }
        var msg = java.lang.Long.toString(qty) + "mB"
        if (qty > 1000000000) {
            msg = java.lang.Long.toString(qty / 1000000000) + "MB"
        } else if (qty > 1000000) {
            msg = java.lang.Long.toString(qty / 1000000) + "KB"
        } else if (qty > 9999) {
            msg = java.lang.Long.toString(qty / 1000) + 'B'
        }
        val fr = Minecraft.getMinecraft().fontRenderer
        val width = fr.getStringWidth(msg)
        GlStateManager.translate(-0.5f * width, 0.0f, -1.0f)
        fr.drawString(msg, 0, 0, 0)
        GlStateManager.popAttrib()
    }

    private fun moveToFace(face: EnumFacing) {
        GlStateManager.translate(face.xOffset * 0.77, face.yOffset * 0.77, face.zOffset * 0.77)
    }

    private fun rotateToFace(face: EnumFacing, spin: Byte) {
        when (face) {
            EnumFacing.UP -> {
                GlStateManager.scale(1.0f, -1.0f, 1.0f)
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f)
                GlStateManager.rotate(spin * 90.0f, 0f, 0f, 1f)
            }
            EnumFacing.DOWN -> {
                GlStateManager.scale(1.0f, -1.0f, 1.0f)
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                GlStateManager.rotate(spin * -90.0f, 0f, 0f, 1f)
            }
            EnumFacing.EAST -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f)
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f)
            }
            EnumFacing.WEST -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f)
                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f)
            }
            EnumFacing.NORTH -> GlStateManager.scale(-1.0f, -1.0f, -1.0f)
            EnumFacing.SOUTH -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f)
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
            }
            else -> {
            }
        }
    }

    override fun getStaticModels(): IPartModel {
        return if (isActive && isPowered) {
            PartModels.STORAGE_MONITOR_HAS_CHANNEL
        } else if (isPowered) {
            PartModels.STORAGE_MONITOR_ON
        } else {
            PartModels.STORAGE_MONITOR_OFF
        }
    }

    override fun requireDynamicRender(): Boolean = true

    override fun writeToNBT(data: NBTTagCompound) {
        super.writeToNBT(data)
        data.setLong("amount", amount)
        if (fluid == null) {
            data.setInteger("fluid", -1)
        } else {
            data.setString("fluid", fluid!!.name)
        }
        data.setBoolean("locked", locked)
    }

    @Throws(IOException::class)
    override fun writeToStream(data: ByteBuf) {
        super.writeToStream(data)
        data.writeLong(amount)
        if (fluid == null) {
            ByteBufUtils.writeUTF8String(data, "")
        } else {
            ByteBufUtils.writeUTF8String(data, fluid!!.name)
        }
        data.writeBoolean(locked)
    }
}
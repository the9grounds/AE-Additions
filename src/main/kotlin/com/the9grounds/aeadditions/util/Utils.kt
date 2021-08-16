package com.the9grounds.aeadditions.util

import appeng.api.storage.data.IAEStack
import appeng.api.util.DimensionalCoord
import appeng.client.render.TesrRenderHelper
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3f
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import kotlin.random.Random

object Utils {
    fun spawnDrops(w: World, pos: BlockPos, drops: List<ItemStack>) {
        if (!w.isRemote()) {
            for (itemStack in drops) {
                if (!itemStack.isEmpty && itemStack.count > 0) {
                    val offset_x = ((Random.nextInt() % 32 - 16) / 82).toDouble()
                    val offset_y = ((Random.nextInt() % 32 - 16) / 82).toDouble()
                    val offset_z = ((Random.nextInt() % 32 - 16) / 82).toDouble()
                    val itemEntity = ItemEntity(
                        w, 0.5 + offset_x + pos.x,
                        0.5 + offset_y + pos.y, 0.2 + offset_z + pos.z, itemStack.copy()
                    )
                    w.addEntity(itemEntity)
                }
            }
        }
    }

    fun hasPermissions(dc: DimensionalCoord, player: PlayerEntity): Boolean {
        return if (!dc.isInWorld(player.world)) {
            false
        } else player.world.isBlockModifiable(player, dc.pos)
    }
    
    fun <T: IAEStack<T>> getAmountTextForStack(stack: T): String {
        return when {
            stack.stackSize < 9999L -> "${stack.stackSize}"
            stack.stackSize < 1000000 -> "${(stack.stackSize / 1000).toInt()}K"
            stack.stackSize < 1000000000 -> "${(stack.stackSize / 1000000).toInt()}M"
            else -> "${(stack.stackSize / 1000000000).toInt()}B"
        }
    }
    
    fun rotateToFace(matrixStack: MatrixStack, face: Direction, spin: Byte) {
        when (face) {
            Direction.UP -> {
                matrixStack.rotate(Vector3f.XP.rotationDegrees(270f))
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(-spin * 90.0f))
            }
            Direction.DOWN -> {
                matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0f))
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(spin * -90.0f))
            }
            Direction.EAST -> matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0f))
            Direction.WEST -> matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0f))
            Direction.NORTH -> matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0f))
            Direction.SOUTH -> {
            }
            else -> {
            }
        }
    }

    /**
     * Render an item in 2D and the given text below it.
     *
     * @param matrixStack
     * @param buffers
     * @param spacing           Specifies how far apart the item and the item stack amount are rendered.
     * @param combinedLightIn
     * @param combinedOverlayIn
     */
    fun <T: IAEStack<T>> renderStack2dWithAmount(
        matrixStack: MatrixStack, buffers: IRenderTypeBuffer?,
        itemStack: T, itemName: String, itemScale: Float, spacing: Float, combinedLightIn: Int, combinedOverlayIn: Int
    ) {
        val renderStack = itemStack.asItemStackRepresentation()
        TesrRenderHelper.renderItem2d(matrixStack, buffers, renderStack, itemScale, combinedLightIn, combinedOverlayIn)
        val renderedStackSize = getAmountTextForStack(itemStack)

        // Render the item count & name
        val fr = Minecraft.getInstance().fontRenderer
        
        val width = fr.getStringWidth(renderedStackSize)
        matrixStack.push()
        
        matrixStack.translate(0.0, spacing.toDouble(), 0.02)
        
        matrixStack.scale(1.0f / 62.0f, -1.0f / 62.0f, 1.0f / 62.0f)
        matrixStack.scale(0.5f, 0.5f, 0f)

        matrixStack.push()
        
        matrixStack.translate(0.0, -10.0, 0.0)
        matrixStack.scale(.8f, .8f, .8f)
        matrixStack.translate((-0.5f * fr.getStringWidth(itemName)).toDouble(), 0.0, 0.5)
        fr.renderString(itemName, 0f, 0f, -1, false, matrixStack.last.matrix, buffers, false, 0, 15728880)
        matrixStack.pop()
        
        matrixStack.translate((-0.5f * width).toDouble(), 0.0, 0.5)
        fr.renderString(
            renderedStackSize, 0f, 0f, -1, false, matrixStack.last.matrix, buffers, false, 0,
            15728880
        )
        matrixStack.pop()
    }

    @OnlyIn(Dist.CLIENT)
    fun renderItem2d(
        matrixStack: MatrixStack, buffers: IRenderTypeBuffer?, itemStack: ItemStack,
        scale: Float, combinedLightIn: Int, combinedOverlayIn: Int
    ) {
        if (!itemStack.isEmpty) {
            matrixStack.push()
            // Push it out of the block face a bit to avoid z-fighting
            matrixStack.translate(0.0, 0.0, 0.01)
            // The Z-scaling by 0.0002 causes the model to be visually "flattened"
            // This cannot replace a proper projection, but it's cheap and gives the desired
            // effect at least from head-on
            matrixStack.scale(scale, scale, 0.0002f)
            Minecraft.getInstance().itemRenderer.renderItem(
                itemStack, ItemCameraTransforms.TransformType.GUI,
                combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStack, buffers
            )
            matrixStack.pop()
        }
    }
}
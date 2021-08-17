package com.the9grounds.aeadditions.client.helpers

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.the9grounds.aeadditions.util.Colors
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Rectangle2d
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class Blit(val texture: ResourceLocation, val referenceWidth: Int, val referenceHeight: Int) {

    companion object {
        const val DEFAULT_WIDTH = 255
        const val DEFAULT_HEIGHT = 255
    }

    var r = 255
    var g = 255
    var b = 255
    var a = 255

    /**
     * The section of the texture we are drawing from
     */
    var srcRect: Rectangle2d? = null

    /**
     * The destination on the screen that we are drawing to
     */
    var destRect = Rectangle2d(0, 0, 0, 0)

    /**
     * Apply blending function?
     */
    var blending = true

    constructor(texture: ResourceLocation) : this(texture, DEFAULT_WIDTH, DEFAULT_HEIGHT)

    constructor(sprite: TextureAtlasSprite) : this(sprite.atlasTexture.textureLocation, Int.MAX_VALUE, Int.MAX_VALUE) {
        src(
            (sprite.minU * Int.MAX_VALUE).toInt(),
            (sprite.minV * Int.MAX_VALUE).toInt(),
            ((sprite.maxU - sprite.minU) * Int.MAX_VALUE).toInt(),
            ((sprite.maxV - sprite.minV) * Int.MAX_VALUE).toInt()
        )
    }

    fun src(x: Int, y: Int, width: Int, height: Int): Blit {
        srcRect = Rectangle2d(x, y, width, height)

        return this
    }

    fun dest(x: Int, y: Int, width: Int, height: Int): Blit {
        destRect = Rectangle2d(x, y, width, height)

        return this
    }

    fun dest(x: Int, y: Int): Blit {
        return dest(x, y, 0, 0)
    }

    fun color(r: Float, g: Float, b: Float): Blit {
        return this.apply {
            this.r = (r.coerceIn(0f, 1f) * 255).toInt()
            this.g = (g.coerceIn(0f, 1f) * 255).toInt()
            this.b = (b.coerceIn(0f, 1f) * 255).toInt()
        }
    }

    fun opacity(a: Float): Blit {
        return this.apply {
            this.a = (a.coerceIn(0f, 1f) * 255).toInt()
        }
    }

    fun color(r: Float, g: Float, b: Float, a: Float): Blit {
        return this.color(r, g, b).opacity(a)
    }

    fun colorRgb(color: Int, withAlpha: Boolean = false): Blit {
        val r = Colors.getRed(color)
        val g = Colors.getGreen(color)
        val b = Colors.getBlue(color)

        color(r, g, b)

        if (withAlpha) {
            val a = Colors.getAlpha(color)

            opacity(a)
        }

        return this
    }

    fun copy(): Blit {
        val copy = Blit(texture, referenceWidth, referenceHeight)

        val self = this

        return copy.apply {
            srcRect = self.srcRect
            destRect = self.destRect
            blending = self.blending
            r = self.r
            g = self.g
            b = self.b
            a = self.a
        }
    }

    fun draw(matrixStack: MatrixStack, zIndex: Float) {
        Minecraft.getInstance().textureManager.bindTexture(texture)

        val minU: Float
        val minV: Float
        val maxU: Float
        val maxV: Float

        if (srcRect == null) {
            minU = 0f
            minV = 0f
            maxU = 1f
            maxV = 1f
        } else {
            minU = srcRect!!.x / referenceWidth.toFloat()
            minV = srcRect!!.y / referenceHeight.toFloat()
            maxU = (srcRect!!.x + srcRect!!.width) / referenceWidth.toFloat()
            maxV = (srcRect!!.y + srcRect!!.height) / referenceHeight.toFloat()
        }

        val x1: Float = destRect.x.toFloat()
        val y1: Float = destRect.y.toFloat()

        var x2 = x1
        var y2 = y1

        if (destRect.width != 0 && destRect.height != 0) {
            x2 += destRect.width.toFloat()
            y2 += destRect.height.toFloat()
        } else if (srcRect != null) {
            x2 += srcRect!!.width.toFloat()
            y2 += srcRect!!.height.toFloat()
        }

        val bufferBuilder = Tessellator.getInstance().buffer
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX)
        bufferBuilder.pos(matrixStack.last.matrix, x1, y2, zIndex)
            .color(r, g, b, a)
            .tex(minU, maxV)
            .endVertex()
        bufferBuilder.pos(matrixStack.last.matrix, x2, y2, zIndex)
            .color(r, g, b, a)
            .tex(maxU, maxV)
            .endVertex()
        bufferBuilder.pos(matrixStack.last.matrix, x2, y1, zIndex)
            .color(r, g, b, a)
            .tex(maxU, minV)
            .endVertex()
        bufferBuilder.pos(matrixStack.last.matrix, x1, y1, zIndex)
            .color(r, g, b, a)
            .tex(minU, minV)
            .endVertex()
        bufferBuilder.finishDrawing()

        if (blending) {
            RenderSystem.enableBlend()
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        } else {
            RenderSystem.disableBlend()
        }

        RenderSystem.enableTexture()

        WorldVertexBufferUploader.draw(bufferBuilder)
    }
}
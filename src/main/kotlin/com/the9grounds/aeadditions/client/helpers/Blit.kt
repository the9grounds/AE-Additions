package com.the9grounds.aeadditions.client.helpers

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.util.Colors
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.Rect2i
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

class Blit(val texture: ResourceLocation, val referenceWidth: Int, val referenceHeight: Int) {

    companion object {
        const val DEFAULT_WIDTH = 256
        const val DEFAULT_HEIGHT = 256
    }

    var r = 255
    var g = 255
    var b = 255
    var a = 255

    /**
     * The section of the texture we are drawing from
     */
    var srcRect: Rect2i? = null

    /**
     * The destination on the screen that we are drawing to
     */
    var destRect = Rect2i(0, 0, 0, 0)

    /**
     * Apply blending function?
     */
    var blending = true

    constructor(texture: ResourceLocation) : this(texture, DEFAULT_WIDTH, DEFAULT_HEIGHT)

    constructor(sprite: TextureAtlasSprite) : this(sprite.atlas().location(), DEFAULT_WIDTH, DEFAULT_HEIGHT) {
        src(
            (sprite.u0 * DEFAULT_WIDTH).toInt(),
            (sprite.v0 * DEFAULT_HEIGHT).toInt(),
            ((sprite.u1 - sprite.u0) * DEFAULT_WIDTH).toInt(),
            ((sprite.v1 - sprite.v0) * DEFAULT_HEIGHT).toInt()
        )
    }
    
    constructor(resource: String): this(ResourceLocation(AEAdditions.MOD_ID, "textures/${resource}"), DEFAULT_WIDTH, DEFAULT_HEIGHT)
    constructor(resource: String, width: Int, height: Int): this(ResourceLocation(AEAdditions.MOD_ID, "textures/${resource}"), width, height)

    fun src(x: Int, y: Int, width: Int, height: Int): Blit {
        srcRect = Rect2i(x, y, width, height)

        return this
    }

    fun dest(x: Int, y: Int, width: Int, height: Int): Blit {
        destRect = Rect2i(x, y, width, height)

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

    fun draw(poseStack: PoseStack, zIndex: Float) {
        RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
        RenderSystem.setShaderTexture(0, texture)

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
        
        val matrix = poseStack.last().pose()

        val bufferBuilder = Tesselator.getInstance().builder
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX)
        bufferBuilder.vertex(matrix, x1, y2, zIndex)
            .color(r, g, b, a)
            .uv(minU, maxV)
            .endVertex()
        bufferBuilder.vertex(matrix, x2, y2, zIndex)
            .color(r, g, b, a)
            .uv(maxU, maxV)
            .endVertex()
        bufferBuilder.vertex(matrix, x2, y1, zIndex)
            .color(r, g, b, a)
            .uv(maxU, minV)
            .endVertex()
        bufferBuilder.vertex(matrix, x1, y1, zIndex)
            .color(r, g, b, a)
            .uv(minU, minV)
            .endVertex()
        bufferBuilder.end()

        if (blending) {
            RenderSystem.enableBlend()
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        } else {
            RenderSystem.disableBlend()
        }

        RenderSystem.enableTexture()
        BufferUploader.end(bufferBuilder)
    }
}
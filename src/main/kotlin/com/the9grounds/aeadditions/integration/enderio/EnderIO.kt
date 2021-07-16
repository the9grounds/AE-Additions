package com.the9grounds.aeadditions.integration.enderio

import com.the9grounds.aeadditions.api.AEAApi
import com.the9grounds.aeadditions.block.BlockAE
import crazypants.enderio.api.tool.ITool
import net.minecraft.util.EnumHand
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.Event

object EnderIO {

    @JvmStatic fun init() {
        AEAApi.instance().registerWrenchHandler(WrenchHandler)
    }

    @JvmStatic fun handlePlayerInteract(event: PlayerInteractEvent.RightClickBlock) {
        if (event.hand != EnumHand.MAIN_HAND) {
            return
        }

        val blockState = event.world.getBlockState(event.pos)
        val block = blockState.block
        val itemStack = event.itemStack

        val item = itemStack.item

        if (item !is ITool || block !is BlockAE || event.face == null) {
            return
        }

        if (!event.entityPlayer.isSneaking) {
            return
        }

        val vec3d = event.hitVec

        try {
            val retVal = block.onBlockActivated(
                event.world, event.pos, blockState, event.entityPlayer, event.hand, event.face,
                vec3d.x.toFloat(),
                vec3d.y.toFloat(),
                vec3d.z.toFloat()
            )
            if (retVal) {
                event.result = Event.Result.DENY
                event.entityPlayer.swingArm(event.hand)
            }
        } catch (e: Exception) {
            // Catch all exceptions
        }
    }
}
package com.the9grounds.aeadditions.util;

import appeng.api.config.SecurityPermissions;
import appeng.api.util.AEPartLocation;
import appeng.core.Api;
import crazypants.enderio.api.tool.ITool;
import com.the9grounds.aeadditions.api.IECTileEntity;
import com.the9grounds.aeadditions.block.BlockEC;
import com.the9grounds.aeadditions.container.ITickContainer;
import com.the9grounds.aeadditions.container.gas.ContainerGasStorage;
import com.the9grounds.aeadditions.registries.ItemEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class AEAdditionsEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        IECTileEntity tileEntity = TileUtil.getTile(event.getWorld(), event.getPos(), IECTileEntity.class);
        if (tileEntity != null) {
            if (!PermissionUtil.hasPermission(event.getPlayer(), SecurityPermissions.BUILD, tileEntity.getGridNode(AEPartLocation.INTERNAL))) {
                event.setCanceled(true);
            }
        }
    }

    // Work around for ender io wrench
    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent.RightClickBlock event) {

        if( event.getHand() != EnumHand.MAIN_HAND )
        {
            return;
        }

        IBlockState blockState = event.getWorld().getBlockState(event.getPos());
        Block block = blockState.getBlock();
        ItemStack itemStack = event.getItemStack();

        Item item = itemStack.getItem();

        if (!(item instanceof ITool) || !(block instanceof BlockEC) || event.getFace() == null) {
            return;
        }

        if (!event.getEntityPlayer().isSneaking()) {
            return;
        }

        Vec3d vec3d = event.getHitVec();

        try {
            Boolean retVal = block.onBlockActivated(event.getWorld(), event.getPos(), blockState, event.getEntityPlayer(), event.getHand(), event.getFace(), (float) vec3d.x, (float) vec3d.y, (float) vec3d.z);

            if (retVal) {
                event.setResult(Event.Result.DENY);
                event.getEntityPlayer().swingArm(event.getHand());
            }
        } catch (Exception e) {
            // Catch all exceptions
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == Side.SERVER && event.player != null) {
            if (event.player.openContainer != null) {
                Container con = event.player.openContainer;
                if (con instanceof ContainerGasStorage)
                    ((ContainerGasStorage) con).removeEnergyTick();

                if (con instanceof ITickContainer)
                    ((ITickContainer) con).onTick();
            }
        }
    }
}

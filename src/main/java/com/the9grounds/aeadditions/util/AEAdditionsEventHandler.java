package com.the9grounds.aeadditions.util;

import appeng.api.config.SecurityPermissions;
import appeng.api.util.AEPartLocation;
import com.the9grounds.aeadditions.api.IECTileEntity;
import com.the9grounds.aeadditions.container.ITickContainer;
import com.the9grounds.aeadditions.container.gas.ContainerGasStorage;
import com.the9grounds.aeadditions.integration.Integration;
import com.the9grounds.aeadditions.integration.enderio.EnderIO;
import net.minecraft.inventory.Container;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

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
        if (Integration.Mods.ENDERIO.isEnabled()) {
            EnderIO.handlePlayerInteract(event);
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

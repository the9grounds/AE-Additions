package extracells.util;

import crazypants.enderio.api.tool.ITool;
import extracells.api.IECTileEntity;
import extracells.block.BlockEC;
import extracells.container.ITickContainer;
import extracells.container.fluid.ContainerFluidStorage;
import extracells.container.gas.ContainerGasStorage;
import extracells.item.ItemFluid;
import extracells.registries.BlockEnum;
import extracells.registries.ItemEnum;
import extracells.registries.PartEnum;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import appeng.api.config.SecurityPermissions;
import appeng.api.util.AEPartLocation;
import appeng.core.Api;

public class ExtraCellsEventHandler {

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
                if (con instanceof ContainerFluidStorage)
                    ((ContainerFluidStorage) con).removeEnergyTick();
                else if (con instanceof ContainerGasStorage)
                    ((ContainerGasStorage) con).removeEnergyTick();

                if (con instanceof ITickContainer)
                    ((ITickContainer) con).onTick();
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        List<String> tooltip = event.getToolTip();
        String name = stack.getTranslationKey();

        TextComponentTranslation text1 = new TextComponentTranslation("extracells.tooltip.deprecated1");
        TextComponentTranslation text2;
        TextComponentTranslation text3 = new TextComponentTranslation("extracells.tooltip.deprecated.part");
        Style style = new Style();
        style.setColor(TextFormatting.RED);

        String ae2Name = "";
        boolean isDeprecated = false;
        if (BlockEnum.ECBASEBLOCK.getItem().getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().blocks().fluidIface().maybeBlock().get().getLocalizedName();
            isDeprecated = true;
        } else if (PartEnum.INTERFACE.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidIface().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (PartEnum.FLUIDIMPORT.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidImportBus().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (PartEnum.FLUIDEXPORT.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidExportBus().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (PartEnum.FLUIDTERMINAL.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidTerminal().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (PartEnum.FLUIDSTORAGE.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidStorageBus().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (ItemEnum.FLUIDSTORAGE.getItem().getTranslationKey().equalsIgnoreCase(item.getTranslationKey())) {
            //ae2Name = Api.INSTANCE.definitions().parts().fluidStorageBus().maybeStack(1).get().getDisplayName();
            if (stack.getItemDamage() >= 0 && stack.getItemDamage() < 4) {
                isDeprecated = true;
                text3 = new TextComponentTranslation("extracells.tooltip.deprecated.cell");
                switch (stack.getItemDamage()) {
                    case 0:
                        ae2Name = Api.INSTANCE.definitions().items().fluidCell1k().maybeStack(1).get().getDisplayName();
                        break;
                    case 1:
                        ae2Name = Api.INSTANCE.definitions().items().fluidCell4k().maybeStack(1).get().getDisplayName();
                        break;
                    case 2:
                        ae2Name = Api.INSTANCE.definitions().items().fluidCell16k().maybeStack(1).get().getDisplayName();
                        break;
                    case 3:
                        ae2Name = Api.INSTANCE.definitions().items().fluidCell64k().maybeStack(1).get().getDisplayName();
                        break;
                }
            }
        } else if (ItemEnum.STORAGECOMPONET.getItem().getTranslationKey().equalsIgnoreCase(item.getTranslationKey())) {
            if (stack.getItemDamage() >= 4 && stack.getItemDamage() < 8) {
                isDeprecated = true;
                switch (stack.getItemDamage()) {
                    case 4:
                        ae2Name = Api.INSTANCE.definitions().materials().fluidCell1kPart().maybeStack(1).get().getDisplayName();
                        break;
                    case 5:
                        ae2Name = Api.INSTANCE.definitions().materials().fluidCell4kPart().maybeStack(1).get().getDisplayName();
                        break;
                    case 6:
                        ae2Name = Api.INSTANCE.definitions().materials().fluidCell16kPart().maybeStack(1).get().getDisplayName();
                        break;
                    case 7:
                        ae2Name = Api.INSTANCE.definitions().materials().fluidCell64kPart().maybeStack(1).get().getDisplayName();
                        break;
                }
            }
        } else if (PartEnum.FLUIDLEVELEMITTER.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidLevelEmitter().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (PartEnum.FLUIDPANEANNIHILATION.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidAnnihilationPlane().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        } else if (PartEnum.FLUIDPANEFORMATION.getTranslationKey().equalsIgnoreCase(name)) {
            ae2Name = Api.INSTANCE.definitions().parts().fluidFormationnPlane().maybeStack(1).get().getDisplayName();
            isDeprecated = true;
        }

        if (isDeprecated) {
            text3.setStyle(style);
            tooltip.add(1, text3.getFormattedText());
            if (!ae2Name.isEmpty()) { // Might not always have a conversion
                text2 = new TextComponentTranslation("extracells.tooltip.deprecated2", ae2Name);
                text2.setStyle(style);
                tooltip.add(1, text2.getFormattedText());
            }
            text1.setStyle(style);
            tooltip.add(1, text1.getFormattedText());
        }
    }
}

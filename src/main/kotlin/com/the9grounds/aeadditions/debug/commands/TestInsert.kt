package com.the9grounds.aeadditions.debug.commands

import appeng.api.config.Actionable
import appeng.api.networking.storage.IStorageGrid
import appeng.api.util.AEPartLocation
import appeng.me.helpers.PlayerSource
import appeng.tile.networking.ControllerTileEntity
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.MekanismAPI
import mekanism.api.chemical.Chemical
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld

class TestInsert : ICommand {
    override fun call(server: MinecraftServer, data: CommandContext<CommandSource?>, sender: CommandSource?) {
        //
    }

    override fun addArguments(builder: LiteralArgumentBuilder<CommandSource>) {
        builder.then(Commands.argument("chemicalName", StringArgumentType.string()).executes() {
            val chemicalName = StringArgumentType.getString(it, "chemicalName")
            insertItems(it, chemicalName)
            
            1
        }.then(Commands.argument("amount", LongArgumentType.longArg()).executes() {
            val chemicalName = StringArgumentType.getString(it, "chemicalName")
            val amount = LongArgumentType.getLong(it, "amount")
            
            insertItems(it, chemicalName, amount)
            
            1
        }))
    }

    private fun insertItems(
        it: CommandContext<CommandSource>,
        chemicalName: String?,
        amount: Long? = null
    ): Int {
        try {
            val world: ServerWorld = it.source.world ?: return 1

            val entity = world.getTileEntity(BlockPos(183, 74, -52)) as? ControllerTileEntity

            if (entity == null) {
                Logger.info("Could not find tile entity")

                return 1
            }

            val inventory =
                entity.getGridNode(AEPartLocation.INTERNAL)?.grid?.getCache<IStorageGrid>(IStorageGrid::class.java)
                    ?.getInventory(StorageChannels.CHEMICAL)

            if (inventory == null) {
                Logger.info("Could not get inventory")

                return 1
            }

            val resourceLocation = ResourceLocation("mekanism", chemicalName)

            val chemical: Chemical<*>? = when (true) {
                MekanismAPI.gasRegistry().containsKey(resourceLocation) -> MekanismAPI.gasRegistry()
                    .getValue(resourceLocation)
                MekanismAPI.infuseTypeRegistry().containsKey(resourceLocation) -> MekanismAPI.infuseTypeRegistry()
                    .getValue(resourceLocation)
                MekanismAPI.slurryRegistry().containsKey(resourceLocation) -> MekanismAPI.slurryRegistry()
                    .getValue(resourceLocation)
                MekanismAPI.pigmentRegistry().containsKey(resourceLocation) -> MekanismAPI.pigmentRegistry()
                    .getValue(resourceLocation)
                else -> null
            }

            if (chemical == null) {
                Logger.info("Could not find chemical")

                return 1
            }

            val aeStack = AEChemicalStack(chemical, amount ?: 1000)

            inventory.injectItems(aeStack, Actionable.MODULATE, PlayerSource(it.source.asPlayer(), null))
        } catch (e: Throwable) {
            Logger.info(e.message)
        }
        
        return 1
    }
}
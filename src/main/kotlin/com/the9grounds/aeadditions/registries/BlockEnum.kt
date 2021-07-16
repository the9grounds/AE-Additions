package com.the9grounds.aeadditions.registries

import appeng.api.config.Upgrades
import net.minecraft.item.ItemBlock
import com.the9grounds.aeadditions.integration.Integration
import appeng.block.crafting.BlockCraftingUnit
import com.the9grounds.aeadditions.block.*
import com.the9grounds.aeadditions.item.block.ItemBlockCertusTank
import kotlin.jvm.JvmOverloads
import com.the9grounds.aeadditions.util.CreativeTabEC
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.text.translation.I18n
import java.util.function.Function

enum class BlockEnum(
    val internalName: String,
    val block: Block,
    factory: Function<Block, ItemBlock>,
    mod: Integration.Mods?
) {
    CERTUSTANK(
        "certustank",
        BlockCertusTank(),
        Function<Block, ItemBlock> { block: Block? -> ItemBlockCertusTank(block) }),
    FLUIDCRAFTER("fluidcrafter", BlockFluidCrafter(), Pair(Upgrades.SPEED, 5), Pair(Upgrades.CAPACITY, 2)),
    FILLER(
        "fluidfiller",
        BlockFluidFiller(),
        Function<Block, ItemBlock> { block: Block? -> com.the9grounds.aeadditions.item.block.ItemBlockFluidFiller(block) }),
    BLASTRESISTANTMEDRIVE("hardmedrive", BlockHardMEDrive()),
    VIBRANTCHAMBERFLUID(
        "vibrantchamberfluid",
        BlockVibrationChamberFluid()
    ),
    GASINTERFACE("gas_interface", BlockGasInterface(), Function<Block, ItemBlock> { block: Block -> com.the9grounds.aeadditions.item.block.ItemBlockGasInterface(block) }, Integration.Mods.MEKANISMGAS),
    UPGRADEDCRAFTINGSTORAGE256(
        "crafting_storage_256",
        BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_1K),
        false
    ),
    UPGRADEDCRAFTINGSTORAGE1024(
        "crafting_storage_1024",
        BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_4K),
        false
    ),
    UPGRADEDCRAFTINGSTORAGE4096(
        "crafting_storage_4096",
        BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_16K),
        false
    ),
    UPGRADEDCRAFTINGSTORAGE16384(
        "crafting_storage_16384",
        BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_64K),
        false
    );

    val item: ItemBlock
    val mod: Integration.Mods?
    val upgrades = mutableMapOf<Upgrades, Int>()
    var enabled = true
        private set

    constructor(internalName: String, block: Block, mod: Integration.Mods) : this(
        internalName,
        block,
        Function<Block, ItemBlock> { block: Block? -> ItemBlock(block) },
        mod
    ) {
    }

    constructor(internalName: String, block: Block, vararg _upgrades: Pair<Upgrades, Int>) : this(internalName,
        block,
        Function<Block, ItemBlock> { block: Block? -> ItemBlock(block) }) {
        _upgrades.forEach { v ->
            upgrades[v.first] = v.second
        }
    }

    constructor(internalName: String, block: Block, factory: Function<Block, ItemBlock>, vararg _upgrades: Pair<Upgrades, Int>) : this(internalName,
        block,
        factory) {
        _upgrades.forEach { v ->
            upgrades[v.first] = v.second
        }
    }

    constructor(internalName: String, block: Block, enabled: Boolean) : this(
        internalName,
        block,
        Function<Block, ItemBlock> { block: Block? -> ItemBlock(block) }) {
        this.enabled = enabled
    }

    constructor(
        internalName: String,
        block: Block,
        itemFactory: Function<Block, ItemBlock> = Function { block: Block? -> ItemBlock(block) }
    ) : this(internalName, block, itemFactory, null) {
    }

    val statName: String
        get() = I18n.translateToLocal(block.translationKey + ".name")

    init {
        block.translationKey = "com.the9grounds.aeadditions.block." + internalName
        block.setRegistryName(internalName)
        item = factory.apply(block)
        item.registryName = block.registryName
        this.mod = mod
        if (mod == null || mod.isEnabled) {
            block.creativeTab = CreativeTabEC.INSTANCE
        }
    }

    fun registerUpgrades() {
        upgrades.forEach { k, v ->
            k.registerItem(ItemStack(block, 1), v)
        }
    }

    companion object {
        fun getValueByBlockInternalName(internalName: String): BlockEnum? {
            values().forEach {
                if (it.internalName === internalName ) {
                    return it
                }
            }

            return null
        }
    }
}
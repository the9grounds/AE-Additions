package extracells.tileentity

import appeng.tile.crafting.TileCraftingStorageTile
import appeng.tile.crafting.TileCraftingTile
import extracells.api.ECApi
import extracells.registries.BlockEnum
import net.minecraft.item.ItemStack
import java.util.*
import java.util.function.Supplier

class TileEntityCraftingStorage : TileCraftingStorageTile() {

    private val KILO_SCALAR = 1024

    override fun getItemFromTile(obj: Any?): ItemStack? {
        val blocks = ECApi.instance().blocks()

        val maybeItem = when((obj as TileCraftingTile).storageBytes / KILO_SCALAR) {
            256 -> blocks.craftingStorage256().maybeStack(1)
            1024 -> blocks.craftingStorage1024().maybeStack(1)
            4096 -> blocks.craftingStorage4096().maybeStack(1)
            16384 -> blocks.craftingStorage16384().maybeStack(1)
            else -> Optional.empty()
        }

        return maybeItem.orElseGet { super.getItemFromTile(obj) }
    }

    override fun getStorageBytes(): Int {
        if (world == null || notLoaded() || isInvalid) {
            return 0
        }

        val blockState = world.getBlockState(pos)

        return when(blockState.block) {
            BlockEnum.UPGRADEDCRAFTINGSTORAGE256.block -> 256 * KILO_SCALAR
            BlockEnum.UPGRADEDCRAFTINGSTORAGE1024.block -> 1024 * KILO_SCALAR
            BlockEnum.UPGRADEDCRAFTINGSTORAGE4096.block -> 4096 * KILO_SCALAR
            BlockEnum.UPGRADEDCRAFTINGSTORAGE16384.block -> 16384 * KILO_SCALAR
            else -> 0
        }
    }
}
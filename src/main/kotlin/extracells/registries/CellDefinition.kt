package extracells.registries

import extracells.item.storage.StorageRegistry
import net.minecraft.item.EnumRarity
import java.util.*

enum class CellDefinition(val sizes: List<Int>, val configCategory: String, val defaultNumberOfTypes: Int, val rarity: EnumRarity) {
    PHYSICAL(listOf(256, 1024, 4096, 16384), "Cells.Item", 63, EnumRarity.EPIC),
    FLUID(listOf(256, 1024, 4096), "Cells.Fluid", 5, EnumRarity.RARE),
    GAS(listOf(1, 4, 16, 64, 256, 1024, 4096), "Cells.Gas", 5, EnumRarity.UNCOMMON);

    @JvmField var cells: StorageRegistry? = null
    @JvmField var componentMetaStart: Int = 0

    override fun toString(): String {
        return name.toLowerCase(Locale.ENGLISH)
    }

    companion object {
        @JvmStatic fun get(index: Int): CellDefinition {
            return if (values().size <= index) {
                values()[0]
            } else values()[index]
        }
    }


}
package extracells.integration.opencomputers

import extracells.registries.ItemEnum
import li.cil.oc.api.Manual
import li.cil.oc.api.manual.PathProvider
import li.cil.oc.api.prefab.ItemStackTabIconRenderer
import li.cil.oc.api.prefab.ResourceContentProvider
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ExtraCellsPathProvider : PathProvider {

    init {
        Manual.addProvider(this)
        Manual.addProvider(ResourceContentProvider("extracells", "docs/"))
        Manual.addTab(ItemStackTabIconRenderer(ItemStack(ItemEnum.FLUIDSTORAGE.item)), "itemGroup.Extra_Cells", "extracells/%LANGUAGE%/index.md")
    }

    override fun pathFor(itemStack: ItemStack?): String? {
        if (itemStack != null && itemStack.item == ItemEnum.OCUPGRADE.item) {
            return "extracells/%LANGUAGE%/me_upgrade.md"
        }

        return null
    }

    override fun pathFor(world: World?, pos: BlockPos?): String? = null
}
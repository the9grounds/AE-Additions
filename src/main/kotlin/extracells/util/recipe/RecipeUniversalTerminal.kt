package extracells.util.recipe

import appeng.api.config.Actionable
import appeng.api.features.INetworkEncodable
import appeng.api.implementations.items.IAEItemPowerStorage
import extracells.item.ItemWirelessTerminalUniversal
import extracells.item.WirelessTerminalType
import extracells.registries.ItemEnum
import extracells.util.UniversalTerminal
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.registries.IForgeRegistryEntry

//From old file: TODO: Fix this. https://github.com/Shadows-of-Fire/XCPatch/blob/master/src/main/java/shadows/xcp/TerminalRecipe.java
object RecipeUniversalTerminal : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {

    val itemUniversal = ItemEnum.UNIVERSALTERMINAL.item as ItemWirelessTerminalUniversal

    override fun matches(inv: InventoryCrafting, worldIn: World): Boolean {
        var hasWireless = false
        var isUniversal = false
        var hasTerminal = false
        val terminals = mutableListOf<WirelessTerminalType>()
        var terminal: ItemStack? = null

        val size = inv.sizeInventory

        for (i in 0 until size) {
            val stack = inv.getStackInSlot(i)
            if (stack != null) {
                val item = stack.item

                if (item == itemUniversal) {
                    if (hasWireless) {
                        return false
                    }

                    hasWireless = true
                    isUniversal = true
                    terminal = stack
                } else if (UniversalTerminal.isWirelessTerminal(stack)) {
                    if (hasWireless) {
                        return false
                    }
                    hasWireless = true
                    terminal = stack
                } else if (UniversalTerminal.isTerminal(stack)) {
                    hasTerminal = true

                    val typeTerminal = UniversalTerminal.getTerminalType(stack)

                    if (terminals.contains(typeTerminal)) {
                        return false
                    }

                    if (typeTerminal != null) {
                        terminals.add(typeTerminal)
                    }
                }
            }
        }

        if (!(hasTerminal && hasWireless)) {
            return false
        }

        if (isUniversal) {
            for (x in terminals) {
                if (itemUniversal.isInstalled(terminal, x)) {
                    return false
                }
            }

            return true
        }

        val terminalType = UniversalTerminal.getTerminalType(terminal)

        for (x in terminals) {
            if (x == terminalType) {
                return false
            }
        }

        return true

    }

    override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
        var isUniversal = false
        val terminals = mutableListOf<WirelessTerminalType>()
        var terminal: ItemStack? = null
        val size = inv.sizeInventory
        for (i in 0 until size) {
            val stack = inv.getStackInSlot(i)
            if (stack != null) {
                val item = stack.item
                if (item == itemUniversal) {
                    isUniversal = true
                    terminal = stack.copy()
                } else if (UniversalTerminal.isWirelessTerminal(stack)) {
                    terminal = stack.copy()
                } else if (UniversalTerminal.isTerminal(stack)) {
                    val typeTerminal = UniversalTerminal.getTerminalType(stack)

                    if (typeTerminal != null) {
                        terminals.add(typeTerminal)
                    }

                }
            }
        }
        if (isUniversal) {
            for (x in terminals) {
                itemUniversal.installModule(terminal, x)
            }
        } else {
            val terminalType = UniversalTerminal.getTerminalType(terminal)
            val itemTerminal = terminal!!.item
            val t = ItemStack(itemUniversal)
            if (itemTerminal is INetworkEncodable) {
                val key = itemTerminal.getEncryptionKey(terminal)
                if (key != null)
                    itemUniversal.setEncryptionKey(t, key, null)
            }
            if (itemTerminal is IAEItemPowerStorage) {
                val power = itemTerminal.getAECurrentPower(terminal)
                itemUniversal.injectAEPower(t, power, Actionable.MODULATE)
            }
            if (terminal.hasTagCompound()) {
                val nbt = terminal.tagCompound
                if (!t.hasTagCompound())
                    t.setTagCompound(NBTTagCompound())
                if (nbt!!.hasKey("BoosterSlot")) {
                    t.tagCompound!!.setTag("BoosterSlot", nbt.getTag("BoosterSlot"))
                }
                if (nbt.hasKey("MagnetSlot"))
                    t.tagCompound!!.setTag("MagnetSlot", nbt.getTag("MagnetSlot"));
            }
            itemUniversal.installModule(t, terminalType!!)
            t.tagCompound!!.setByte("type", terminalType.ordinal.toByte())
            terminal = t
            for (x in terminals) {
                itemUniversal.installModule(terminal, x)
            }
        }
        return terminal
    }

    override fun canFit(width: Int, height: Int): Boolean = (width >= 1 && height >= 2) || (width >= 2 && height >= 1)

    override fun getRecipeOutput(): ItemStack = ItemEnum.UNIVERSALTERMINAL.getDamagedStack(1)

    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> = ForgeHooks.defaultRecipeGetRemainingItems(inv)
}
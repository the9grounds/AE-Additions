package extracells.util

import appeng.api.AEApi
import com.sun.org.apache.xpath.internal.operations.Bool
import extracells.integration.Integration
import extracells.integration.wct.WirelessCrafting
import extracells.item.WirelessTerminalType
import extracells.registries.ItemEnum
import extracells.registries.PartEnum
import net.minecraft.item.ItemStack

object UniversalTerminal {
    val isMekLoaded = Integration.Mods.MEKANISMGAS.isEnabled
    val isThaLoaded = Integration.Mods.THAUMATICENERGISTICS.isEnabled
    val isWcLLoaded = Integration.Mods.WIRELESSCRAFTING.isEnabled

    @JvmStatic val wirelessTerminals: List<ItemStack>
    get() {
        val terminals = mutableListOf<ItemStack>()
        val terminalDefinition = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).orElse(null)
        terminals.add(terminalDefinition)
        terminals.add(ItemEnum.FLUIDWIRELESSTERMINAL.getSizedStack(1))

        if (isMekLoaded) {
            terminals.add(ItemEnum.GASWIRELESSTERMINAL.getSizedStack(1))
        }

        if (isWcLLoaded) {
            terminals.add(WirelessCrafting.getCraftingTerminal())
        }

        return terminals.toList()
    }

    @JvmStatic val terminals: List<ItemStack>
    get() {
        val terminals = mutableListOf<ItemStack>()

        val terminalDefinition = AEApi.instance().definitions().parts().terminal().maybeStack(1).orElse(null)
        terminals.add(terminalDefinition)
        terminals.add(ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal))

        if (isMekLoaded) {
            terminals.add(ItemEnum.PARTITEM.getDamagedStack(PartEnum.GASTERMINAL.ordinal))
        }

        return terminals.toList()
    }

    @JvmStatic fun isTerminal(itemStack: ItemStack?): Boolean {
        if (itemStack == null) {
            return false
        }

        val item = itemStack.item
        val meta = itemStack.itemDamage

        if (item == null) {
            return false
        }

        val aeTerm = AEApi.instance().definitions().parts().terminal().maybeStack(1).orElse(null)

        if (aeTerm != null && item == aeTerm.item && meta == aeTerm.itemDamage) {
            return true
        }
        val aeTermFluid = AEApi.instance().definitions().parts().fluidTerminal().maybeStack(1).orElse(null)
        if (aeTermFluid != null && item == aeTermFluid.item && meta == aeTermFluid.itemDamage) {
            return true
        }
        val fluidTerm = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal)
        if (item == fluidTerm.item && meta == fluidTerm.itemDamage) {
            return true
        }
        val aeTermGas = ItemEnum.PARTITEM.getDamagedStack(PartEnum.GASTERMINAL.ordinal)
        if (item == aeTermGas.item && meta == aeTermGas.itemDamage) {
            return true
        }
        val aeTermCrafting = AEApi.instance().definitions().parts().craftingTerminal().maybeStack(1).orElse(null)
        if (aeTermCrafting != null && item == aeTermCrafting.item && meta == aeTermCrafting.itemDamage) {
            return true
        }

        return false
    }

    @JvmStatic fun isWirelessTerminal(itemStack: ItemStack?): Boolean {
        if (itemStack == null) {
            return false
        }

        val item = itemStack.item
        val meta = itemStack.itemDamage

        if (item == null) {
            return false
        }
        val wirelessTerminal = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).orElse(null)
        if (wirelessTerminal != null && item == wirelessTerminal.item && meta == wirelessTerminal.itemDamage) {
            return true
        }
        val fluidWirelessTerminal = ItemEnum.FLUIDWIRELESSTERMINAL.getDamagedStack(0)
        if (item == fluidWirelessTerminal.item && meta == fluidWirelessTerminal.itemDamage) {
            return true
        }
        val wirelessGasTerminal = ItemEnum.GASWIRELESSTERMINAL.getDamagedStack(0)
        if (item == wirelessGasTerminal.item && meta == wirelessGasTerminal.itemDamage) {
            return true
        }

        if(isWcLLoaded) {
            val wcTerm = WirelessCrafting.getCraftingTerminal()
            if(item == wcTerm.item && meta == wcTerm.itemDamage) {
                return true
            }
        }
        return false
    }

    @JvmStatic fun getTerminalType(itemStack: ItemStack?): WirelessTerminalType? {
        if (itemStack == null) {
            return null
        }

        val item = itemStack.item
        val meta = itemStack.itemDamage

        if (item == null) {
            return null
        }

        val aeTerminal = AEApi.instance().definitions().parts().terminal().maybeStack(1).orElse(null)
        if (aeTerminal != null && item == aeTerminal.item && meta == aeTerminal.itemDamage)  {
            return WirelessTerminalType.ITEM
        }
        val aeTerminalFluid = AEApi.instance().definitions().parts().fluidTerminal().maybeStack(1).orElse(null)
        if (aeTerminalFluid != null && item == aeTerminalFluid.item && meta == aeTerminalFluid.itemDamage) {
            return WirelessTerminalType.FLUID
        }
        val fluidTerminal = ItemEnum.PARTITEM.getDamagedStack(PartEnum.FLUIDTERMINAL.ordinal)
        if (item == fluidTerminal.item && meta == fluidTerminal.itemDamage) {
            return WirelessTerminalType.FLUID
        }
        val gasTerminal = ItemEnum.PARTITEM.getDamagedStack(PartEnum.GASTERMINAL.ordinal)
        if (item == gasTerminal.item && meta == gasTerminal.itemDamage) {
            return WirelessTerminalType.GAS
        }

        // Wireless Terminals
        val aeWirelessTerminal = AEApi.instance().definitions().items().wirelessTerminal().maybeStack(1).orElse(null)
        if (aeWirelessTerminal != null && item == aeWirelessTerminal.item && meta == aeWirelessTerminal.itemDamage) {
            return WirelessTerminalType.ITEM
        }
        val wirelessFluidTerminal = ItemEnum.FLUIDWIRELESSTERMINAL.getDamagedStack(0)
        if (item == wirelessFluidTerminal.item && meta == wirelessFluidTerminal.itemDamage) {
            return WirelessTerminalType.FLUID
        }
        val wirelessGasTerminal = ItemEnum.GASWIRELESSTERMINAL.getDamagedStack(0)
        if (item == wirelessGasTerminal.item && meta == wirelessGasTerminal.itemDamage) {
            return WirelessTerminalType.GAS
        }

        if(isWcLLoaded){
            val aeCraftingTerminal = AEApi.instance().definitions().parts().craftingTerminal().maybeStack(1).orElse(null)
            if (aeCraftingTerminal != null && item == aeCraftingTerminal.item && meta == aeCraftingTerminal.itemDamage) {
                return WirelessTerminalType.CRAFTING
            }

            val wirelessCraftingTerminal = WirelessCrafting.getCraftingTerminal()
            if(item == wirelessCraftingTerminal.item && meta == wirelessCraftingTerminal.itemDamage) {
                return WirelessTerminalType.CRAFTING
            }
        }
        return null
    }
}
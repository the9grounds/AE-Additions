package com.the9grounds.aeadditions.client.gui

import appeng.api.config.Upgrades
import appeng.api.parts.IPart
import appeng.api.parts.PartItemStack
import appeng.core.localization.GuiText
import com.the9grounds.aeadditions.api.IUpgradeableHost
import com.the9grounds.aeadditions.client.gui.widget.UpgradePanel
import com.the9grounds.aeadditions.container.AbstractUpgradableContainer
import com.the9grounds.aeadditions.container.SlotType
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting

abstract class AbstractUpgradableScreen <T: AbstractUpgradableContainer<T>>(
    screenContainer: T,
    inv: PlayerInventory,
    titleIn: ITextComponent,
    upgradeX: Int,
    upgradeY: Int
) : AEABaseScreen<T>(screenContainer, inv, titleIn) {
    
    init {
        this.widgetContainer.add(UpgradePanel(
            container.getSlotsForType(SlotType.Upgrade),
            upgradeX,
            upgradeY,
            ::getCompatibleUpgrades
        ))
    }

    protected open fun getCompatibleUpgrades(): List<ITextComponent> {
        val host: IUpgradeableHost = container.upgradable
        val item: Item
        item = if (host is IPart) {
            (host as IPart).getItemStack(PartItemStack.NETWORK).item
        } else if (host is TileEntity) {
            val te = host as TileEntity
            te.blockState.block.asItem()
        } else {
            return emptyList()
        }
        return getCompatibleUpgrades(item)
    }

    protected open fun getCompatibleUpgrades(machineItem: Item?): List<ITextComponent> {
        val list: MutableList<ITextComponent> = ArrayList()
        list.add(GuiText.CompatibleUpgrades.text())
        for (upgrade in Upgrades.values()) {
            for (supported in upgrade.supported) {
                if (supported.isSupported(machineItem)) {
                    list.add(
                        GuiText.CompatibleUpgrade.text(upgrade.displayName, supported.maxCount)
                            .mergeStyle(TextFormatting.GRAY)
                    )
                    break
                }
            }
        }
        return list
    }
}
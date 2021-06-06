package extracells.item

import appeng.api.AEApi
import appeng.api.config.Actionable
import appeng.api.features.IWirelessTermHandler
import appeng.api.util.IConfigManager
import baubles.api.BaubleType
import extracells.Constants
import extracells.api.ECApi
import extracells.api.IWirelessFluidTermHandler
import extracells.api.IWirelessGasTermHandler
import extracells.integration.Integration
import extracells.integration.wct.WirelessCrafting
import extracells.models.ModelManager
import extracells.util.HandlerUniversalWirelessTerminal
import extracells.wireless.ConfigManager
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import p455w0rd.ae2wtlib.api.client.IBaubleItem
import p455w0rd.ae2wtlib.api.client.IBaubleRender
import p455w0rd.wct.api.IWirelessCraftingTerminalItem
import java.util.*

@Optional.Interface(iface = "p455w0rd.wct.api.IWirelessCraftingTerminalItem", modid = "wct", striprefs = true)
class ItemWirelessTerminalUniversal : WirelessTermBase(), IWirelessFluidTermHandler, IWirelessGasTermHandler, IWirelessTermHandler, IWirelessCraftingTerminalItem {

    val isTeEnabled = Integration.Mods.THAUMATICENERGISTICS.isEnabled
    val isMekEnabled = Integration.Mods.MEKANISMGAS.isEnabled
    val isWcEnabled = Integration.Mods.WIRELESSCRAFTING.isEnabled

    private var holder: EntityPlayer? = null

    init {
        if (isWcEnabled) {
            ECApi.instance().registerWirelessTermHandler(this)
            AEApi.instance().registries().wireless().registerWirelessHandler(this)
        } else {
            ECApi.instance().registerWirelessTermHandler(HandlerUniversalWirelessTerminal)
            AEApi.instance().registries().wireless().registerWirelessHandler(HandlerUniversalWirelessTerminal)
        }
    }

    override fun isItemNormalWirelessTermToo(`is`: ItemStack?): Boolean = true

    override fun getConfigManager(itemStack: ItemStack?): IConfigManager {
        val nbt = ensureTagCompound(itemStack!!)
        if (!nbt.hasKey("settings")) {
            nbt.setTag("settings", NBTTagCompound())
        }

        return ConfigManager(nbt.getCompoundTag("settings"))
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        return super.getUnlocalizedName(stack).replace("item.extracells", "extracells.item")
    }

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val tag = ensureTagCompound(stack)
        if (!tag.hasKey("type")) {
            tag.setByte("type", 0)
        }
        return super.getItemStackDisplayName(stack) + " - " + I18n.translateToLocal("extracells.tooltip." + WirelessTerminalType.values().apply { tag.getByte("type") }.toString().toLowerCase())
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand)
        val tag = ensureTagCompound(itemStack)
        if (world.isRemote) {
            if (player.isSneaking) {
                return ActionResult(EnumActionResult.SUCCESS, itemStack)
            }
            if (!tag.hasKey("type")) {
                tag.setByte("type", 0)
            }
            if (tag.getByte("type") == 4.toByte() && isWcEnabled) {
                WirelessCrafting.openCraftingTerminal(player, player.inventory.currentItem)
            }
            return ActionResult(EnumActionResult.SUCCESS, itemStack)
        }

        if (!tag.hasKey("type")) {
            tag.setByte("type", 0)
        }
        if (player.isSneaking) {
            if (itemStack == null) {
                return ActionResult(EnumActionResult.FAIL, itemStack)
            }
            return ActionResult(EnumActionResult.SUCCESS, changeMode(itemStack, player, tag)!!)
        }

        when(tag.getByte("type").toInt()) {
            0 -> AEApi.instance().registries().wireless().openWirelessTerminalGui(itemStack, world, player)
            1 -> ECApi.instance().openWirelessFluidTerminal(player, hand, world)
            2 -> ECApi.instance().openWirelessGasTerminal(player, hand, world)
        }

        return ActionResult(EnumActionResult.SUCCESS, itemStack)
    }

    fun changeMode(itemStack: ItemStack?, player: EntityPlayer, tag: NBTTagCompound): ItemStack? {
        val installed = getInstalledModules(itemStack)

        when(tag.getByte("type").toInt()) {
            0 -> {
                if (installed.contains(WirelessTerminalType.FLUID)) {
                    tag.setByte("type", 1)
                } else if (isMekEnabled && installed.contains(WirelessTerminalType.GAS)) {
                    tag.setByte("type", 2)
                } else if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA)) {
                    tag.setByte("type", 3)
                } else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING)) {
                    tag.setByte("type", 4)
                }
            }
            1 -> {
                if (isMekEnabled && installed.contains(WirelessTerminalType.GAS)) {
                    tag.setByte("type", 2)
                } else if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA)) {
                    tag.setByte("type", 3)
                } else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING)) {
                    tag.setByte("type", 4)
                } else if (installed.contains(WirelessTerminalType.ITEM)) {
                    tag.setByte("type", 0)
                }
            }
            2 -> {
                if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA)) {
                    tag.setByte("type", 3)
                } else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING)) {
                    tag.setByte("type", 4)
                } else if (installed.contains(WirelessTerminalType.ITEM)) {
                    tag.setByte("type", 0)
                } else if (installed.contains(WirelessTerminalType.FLUID)) {
                    tag.setByte("type", 1)
                }
            }
            3 -> {
                if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING)) {
                    tag.setByte("type", 4)
                } else if (installed.contains(WirelessTerminalType.ITEM)) {
                    tag.setByte("type", 0)
                } else if (installed.contains(WirelessTerminalType.FLUID)) {
                    tag.setByte("type", 1)
                } else if (isMekEnabled && installed.contains(WirelessTerminalType.GAS)) {
                    tag.setByte("type", 2)
                }
            }
            else -> {
                if (installed.contains(WirelessTerminalType.ITEM)) {
                    tag.setByte("type", 0)
                } else if (installed.contains(WirelessTerminalType.FLUID)) {
                    tag.setByte("type", 1)
                } else if (isMekEnabled && installed.contains(WirelessTerminalType.GAS)) {
                    tag.setByte("type", 2)
                } else if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA)) {
                    tag.setByte("type", 3)
                } else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING)) {
                    tag.setByte("type", 4)
                }
            }
        }
        return itemStack
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item?, manager: ModelManager?) {
        manager!!.registerItemModel(item, 0, "terminals/universal_wireless")
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        val tag = ensureTagCompound(stack)
        if (!tag.hasKey("type")) {
            tag.setByte("type", 0)
        }
        tooltip.add(I18n.translateToLocal("extracells.tooltip.mode") + ": " + I18n.translateToLocal("extracells.tooltip." + WirelessTerminalType.values().apply { tag.getByte("type") }.toString().toLowerCase()))
        tooltip.add(I18n.translateToLocal("extracells.tooltip.installed"))
        val it = getInstalledModules(stack).iterator()
        while (it.hasNext()) {
            tooltip.add("- " + I18n.translateToLocal("extracells.tooltip." + it.next().name.toLowerCase()))
        }
        super.addInformation(stack, worldIn, tooltip, flagIn)
    }

    fun installModule(itemStack: ItemStack?, module: WirelessTerminalType) {
        if (isInstalled(itemStack, module) || itemStack == null) {
            return
        }

        val install = (1 shl module.ordinal)

        val tag = ensureTagCompound(itemStack)

        val installed = if (tag.hasKey("modules")) tag.getByte("modules").toInt() + install else install

        tag.setByte("modules", installed.toByte())
    }

    fun getInstalledModules(itemStack: ItemStack?): EnumSet<WirelessTerminalType> {
        if (itemStack == null || itemStack.item == null) {
            return EnumSet.noneOf(WirelessTerminalType::class.java)
        }

        val tag = ensureTagCompound(itemStack)
        val installed = if (tag.hasKey("modules")) tag.getByte("modules") else 0.toByte()

        val set = EnumSet.noneOf(WirelessTerminalType::class.java)

        for (x in WirelessTerminalType.values()) {
            if (1 == (installed.toInt() shr x.ordinal) % 2) {
                set.add(x)
            }
        }

        return set
    }

    fun isInstalled(itemStack: ItemStack?, module: WirelessTerminalType): Boolean {
        if (itemStack == null || itemStack.item == null) {
            return false
        }

        val tag = ensureTagCompound(itemStack)

        val installed = if (tag.hasKey("modules")) tag.getByte("modules").toInt() else 0

        return 1 == (installed shr module.ordinal) % 2
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) {
            return
        }

        val tag = NBTTagCompound()
        tag.setByte("modules", 23)
        val itemStack = ItemStack(this)
        itemStack.tagCompound = tag
        items.add(itemStack.copy())
        injectAEPower(itemStack, this.MAX_POWER, Actionable.MODULATE)
        items.add(itemStack)
    }

    @Optional.Method(modid = "wct")
    override fun getRender(): IBaubleRender? = null

    @Optional.Method(modid = "wct")
    override fun getBaubleType(itemStack: ItemStack?): BaubleType = BaubleType.TRINKET

    @Optional.Method(modid = "wct")
    override fun initModel() {
        // Do Nothing
    }

    @Optional.Method(modid = "wct")
    override fun getModelResource(item: Item?): ModelResourceLocation? = null

    @Optional.Method(modid = "wct")
    override fun openGui(player: EntityPlayer?, isBauble: Boolean, playerSlot: Int) {
        if (player != null) {
            WirelessCrafting.openCraftingTerminal(player, isBauble, playerSlot)
        }
    }

    @Optional.Method(modid = "wct")
    override fun getPlayer(): EntityPlayer? = this.holder

    @Optional.Method(modid = "wct")
    override fun setPlayer(player: EntityPlayer?) {
        this.holder = player
    }

    @Optional.Method(modid = "wct")
    override fun getColor(): Int = (0xFF8F15D4).toInt()

    @Optional.Method(modid = "wct")
    override fun getMenuIcon(): ResourceLocation = ResourceLocation(Constants.MOD_ID, "textures/items/terminal.universal.wireless.png")
}
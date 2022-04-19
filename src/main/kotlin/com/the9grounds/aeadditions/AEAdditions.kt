package com.the9grounds.aeadditions

import appeng.api.AEApi
import com.google.common.base.Preconditions
import com.the9grounds.aeadditions.config.AEAConfiguration
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler
import com.the9grounds.aeadditions.network.GuiHandler
import com.the9grounds.aeadditions.network.PacketHandler
import com.the9grounds.aeadditions.proxy.CommonProxy
import com.the9grounds.aeadditions.util.AEAConfigHandler
import com.the9grounds.aeadditions.util.AEAdditionsEventHandler
import com.the9grounds.aeadditions.util.EventHandler
import com.the9grounds.aeadditions.util.NameHandler
import com.the9grounds.aeadditions.util.datafix.BasicCellDataFixer
import com.the9grounds.aeadditions.util.datafix.PortableCellDataFixer
import com.the9grounds.aeadditions.wireless.AEWirelessTermHandler
import net.minecraft.util.datafix.FixTypes
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import java.io.File

@Mod(
    modid = Constants.MOD_ID,
    version = Constants.VERSION,
    name = "AE Additions",
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    dependencies = "after:waila;required-after:appliedenergistics2;after:mekanism;required-after:forgelin"
)
object AEAdditions {
    @SidedProxy(clientSide = "com.the9grounds.aeadditions.proxy.ClientProxy", serverSide = "com.the9grounds.aeadditions.proxy.CommonProxy")
    @JvmField var proxy: CommonProxy? = null

    @JvmField val integration = Integration()
    private var configFolder: File? = null
    @JvmStatic var packetHandler: PacketHandler? = null
    get() {
        Preconditions.checkNotNull(field)

        return field
    }
    lateinit var configuration: AEAConfiguration

    init {
        FluidRegistry.enableUniversalBucket()
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        packetHandler = PacketHandler()
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler)

        configFolder = event.modConfigurationDirectory

        val config = Configuration(File(configFolder, "aeadditions.cfg"))
        val javaConfigHandler = AEAConfigHandler(config)
        javaConfigHandler.reload()
        configuration = AEAConfiguration(config)
        configuration.reload()
        MinecraftForge.EVENT_BUS.register(javaConfigHandler)
        MinecraftForge.EVENT_BUS.register(configuration)

        integration.preInit()
        proxy!!.registerItems()
        proxy!!.registerBlocks()
//		CellDefinition.create();
        configuration.createCellsAndComponentEntries()
        proxy!!.registerModels()

        val registries = AEApi.instance().registries()
        registries.recipes().addNewSubItemResolver(NameHandler())
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        val registries = AEApi.instance().registries()
        registries.wireless().registerWirelessHandler(AEWirelessTermHandler())
        registries.cell().addCellHandler(AEAdditionsCellHandler())
        val handler = AEAdditionsEventHandler()
        MinecraftForge.EVENT_BUS.register(handler)
        MinecraftForge.EVENT_BUS.register(EventHandler)
        proxy!!.registerMovables()
        proxy!!.registerRenderers()
        proxy!!.registerTileEntities()
        proxy!!.registerFluidBurnTimes()
        proxy!!.addRecipes(configFolder)
        proxy!!.registerPackets()
        //RenderingRegistry.registerBlockHandler(new RenderHandler(RenderingRegistry.getNextAvailableRenderId))
        //RenderingRegistry.registerBlockHandler(new RenderHandler(RenderingRegistry.getNextAvailableRenderId))
        integration.init()

        val fixes = FMLCommonHandler.instance().dataFixer.init(Constants.MOD_ID, 4)
        fixes.registerFix(FixTypes.ITEM_INSTANCE, BasicCellDataFixer())
        fixes.registerFix(FixTypes.ITEM_INSTANCE, PortableCellDataFixer())
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        integration.postInit()
    }
}
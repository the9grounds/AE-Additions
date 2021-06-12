package extracells

import appeng.api.AEApi
import com.google.common.base.Preconditions
import extracells.config.AEAConfiguration
import extracells.integration.Integration
import extracells.me.storage.ExtraCellsCellHandler
import extracells.network.GuiHandler
import extracells.network.PacketHandler
import extracells.proxy.CommonProxy
import extracells.util.ECConfigHandler
import extracells.util.ExtraCellsEventHandler
import extracells.util.NameHandler
import extracells.util.datafix.BasicCellDataFixer
import extracells.util.datafix.PortableCellDataFixer
import extracells.wireless.AEWirelessTermHandler
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
    name = "Extra Cells",
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
    dependencies = "after:waila;required-after:appliedenergistics2;after:mekanism"
)
object AEAdditions {
    @SidedProxy(clientSide = "extracells.proxy.ClientProxy", serverSide = "extracells.proxy.CommonProxy")
    @JvmField var proxy: CommonProxy? = null

    @JvmField val integration = Integration()
    private var configFolder: File? = null
    @JvmStatic var packetHandler: PacketHandler? = null
    get() {
        Preconditions.checkNotNull(field)

        return field
    }

    init {
        FluidRegistry.enableUniversalBucket()
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        packetHandler = PacketHandler()
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler)

        configFolder = event.modConfigurationDirectory

        val config = Configuration(File(configFolder, "aeadditions.cfg"))
        val javaConfigHandler = ECConfigHandler(config)
        javaConfigHandler.reload()
        val configuration = AEAConfiguration(config)
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
        registries.cell().addCellHandler(ExtraCellsCellHandler())
        val handler = ExtraCellsEventHandler()
        MinecraftForge.EVENT_BUS.register(handler)
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
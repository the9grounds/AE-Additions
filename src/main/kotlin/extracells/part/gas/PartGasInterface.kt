package extracells.part.gas

import appeng.api.parts.IPartCollisionHelper
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import extracells.integration.Integration
import extracells.integration.mekanism.gas.GasInterfaceBase
import extracells.part.PartECBase
import mekanism.api.gas.GasTank
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.Optional

class PartGasInterface : GasInterfaceBase() {
    var fluidFilter: String = ""

    override fun getBoxes(bch: IPartCollisionHelper?) {
        TODO("Not yet implemented")
    }

    override fun getCableConnectionLength(p0: AECableType?): Float {
        TODO("Not yet implemented")
    }

    override fun getFilter(side: AEPartLocation): String = fluidFilter

    override fun setFilter(side: AEPartLocation, fluid: String) {
        fluidFilter = fluid
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun getGasTank(side: EnumFacing?): GasTank {
        TODO("Not yet implemented")
    }
}
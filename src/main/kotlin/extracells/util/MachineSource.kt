package extracells.util

import appeng.api.networking.security.IActionHost
import appeng.api.networking.security.IActionSource
import net.minecraft.entity.player.EntityPlayer
import java.util.*

class MachineSource(val via: IActionHost) : IActionSource {
    override fun player(): Optional<EntityPlayer> = Optional.empty()

    override fun machine(): Optional<IActionHost> = Optional.of(this.via)

    override fun <T : Any?> context(p0: Class<T>): Optional<T> = Optional.empty()
}
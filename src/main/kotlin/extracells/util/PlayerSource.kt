package extracells.util

import appeng.api.networking.security.IActionHost
import appeng.api.networking.security.IActionSource
import com.google.common.base.Preconditions
import net.minecraft.entity.player.EntityPlayer
import java.util.*

class PlayerSource(private val playerObj: EntityPlayer, val via: IActionHost?) : IActionSource {

    init {
        Preconditions.checkNotNull(playerObj)
    }

    override fun player(): Optional<EntityPlayer> = Optional.of(this.playerObj)

    override fun machine(): Optional<IActionHost> = Optional.ofNullable(this.via)

    override fun <T : Any?> context(p0: Class<T>): Optional<T> = Optional.empty()
}
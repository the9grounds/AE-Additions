package com.the9grounds.aeadditions.util

import io.github.projectet.ae2things.util.Constants
import io.github.projectet.ae2things.util.DataStorage
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class StorageManager : SavedData {
    private val disks: MutableMap<UUID, DataStorage?>

    constructor() {
        disks = HashMap()
        this.setDirty()
    }

    private constructor(disks: MutableMap<UUID, DataStorage?>) {
        this.disks = disks
        this.setDirty()
    }

    override fun save(nbt: CompoundTag): CompoundTag {
        val diskList = ListTag()
        disks.forEach { (key, value) ->
            val disk = CompoundTag()
            disk.putUUID(Constants.DISKUUID, key)
            disk.put(Constants.DISKDATA, value!!.toNbt())
            diskList.add(disk)
        }
        nbt.put(Constants.DISKLIST, diskList)
        return nbt
    }

    fun updateDisk(uuid: UUID, dataStorage: DataStorage?) {
        disks[uuid] = dataStorage
        setDirty()
    }

    fun removeDisk(uuid: UUID) {
        disks.remove(uuid)
        setDirty()
    }

    fun hasUUID(uuid: UUID): Boolean {
        return disks.containsKey(uuid)
    }

    fun getOrCreateDisk(uuid: UUID): DataStorage? {
        if (!disks.containsKey(uuid)) {
            updateDisk(uuid, DataStorage())
        }
        return disks[uuid]
    }

    fun modifyDisk(diskID: UUID, stackKeys: ListTag?, stackAmounts: LongArray?, itemCount: Long) {
        val diskToModify = getOrCreateDisk(diskID)
        if (stackKeys != null && stackAmounts != null) {
            diskToModify!!.stackKeys = stackKeys
            diskToModify.stackAmounts = stackAmounts
        }
        diskToModify!!.itemCount = itemCount
        updateDisk(diskID, diskToModify)
    }

    companion object {
        fun readNbt(nbt: CompoundTag): StorageManager {
            val disks: MutableMap<UUID, DataStorage?> = HashMap()
            val diskList = nbt.getList(Constants.DISKLIST, CompoundTag.TAG_COMPOUND.toInt())
            for (i in diskList.indices) {
                val disk = diskList.getCompound(i)
                disks[disk.getUUID(Constants.DISKUUID)] = DataStorage.fromNbt(disk.getCompound(Constants.DISKDATA))
            }
            return StorageManager(disks)
        }

        fun getInstance(server: MinecraftServer): StorageManager {
            val world = server.getLevel(ServerLevel.OVERWORLD)
            return world!!.dataStorage.computeIfAbsent(
                { nbt: CompoundTag ->
                    readNbt(
                        nbt
                    )
                },
                { StorageManager() }, Constants.MANAGER_NAME
            )
        }
    }
}
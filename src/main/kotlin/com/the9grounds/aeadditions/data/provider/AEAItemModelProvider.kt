package com.the9grounds.aeadditions.data.provider

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Ids
import net.minecraft.data.DataGenerator
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper

class AEAItemModelProvider(generator: DataGenerator?, modid: String?, existingFileHelper: ExistingFileHelper?) : ItemModelProvider(generator, modid,
    existingFileHelper
) {
    override fun registerModels() {
        val fluidDisks = mapOf(
            1 to Ids.DISK_FLUID_1k,
            4 to Ids.DISK_FLUID_4k,
            16 to Ids.DISK_FLUID_16k,
            64 to Ids.DISK_FLUID_64k,
            256 to Ids.DISK_FLUID_256k,
            1024 to Ids.DISK_FLUID_1024k,
            4096 to Ids.DISK_FLUID_4096k,
            16384 to Ids.DISK_FLUID_16384k,
            65536 to Ids.DISK_FLUID_65536k
        )

        val chemicalDisks = mapOf(
            1 to Ids.DISK_CHEMICAL_1k,
            4 to Ids.DISK_CHEMICAL_4k,
            16 to Ids.DISK_CHEMICAL_16k,
            64 to Ids.DISK_CHEMICAL_64k,
            256 to Ids.DISK_CHEMICAL_256k,
            1024 to Ids.DISK_CHEMICAL_1024k,
            4096 to Ids.DISK_CHEMICAL_4096k,
            16384 to Ids.DISK_CHEMICAL_16384k,
            65536 to Ids.DISK_CHEMICAL_65536k
        )
        
        for (fluidDisk in fluidDisks) {
            this.singleTexture(
                fluidDisk.value.path,
                mcLoc("item/generated"),
                "layer0",
                ResourceLocation(AEAdditions.ID, "item/disk_fluid_${fluidDisk.key}k")
            ).texture("layer1", ResourceLocation("ae2:item/storage_cell_led"))
        }
        for (chemicalDisk in chemicalDisks) {
            this.singleTexture(
                chemicalDisk.value.path,
                mcLoc("item/generated"),
                "layer0",
                ResourceLocation(AEAdditions.ID, "item/disk_chemical_${chemicalDisk.key}k")
            ).texture("layer1", ResourceLocation("ae2:item/storage_cell_led"))
        }
        
        
        
    }
}
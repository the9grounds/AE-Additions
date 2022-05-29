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
        val superCellComponents = mapOf(
            "1k" to Ids.SUPER_CELL_COMPONENT_1k,
            "4k" to Ids.SUPER_CELL_COMPONENT_4k,
            "16k" to Ids.SUPER_CELL_COMPONENT_16k,
            "64k" to Ids.SUPER_CELL_COMPONENT_64k,
            "256k" to Ids.SUPER_CELL_COMPONENT_256k,
            "1024k" to Ids.SUPER_CELL_COMPONENT_1024k,
            "4096k" to Ids.SUPER_CELL_COMPONENT_4096k,
            "16m" to Ids.SUPER_CELL_COMPONENT_16M,
            "65m" to Ids.SUPER_CELL_COMPONENT_65M,
        )
        val superStorageCells = mapOf(
            "1k" to Ids.SUPER_CELL_1k,
            "4k" to Ids.SUPER_CELL_4k,
            "16k" to Ids.SUPER_CELL_16k,
            "64k" to Ids.SUPER_CELL_64k,
            "256k" to Ids.SUPER_CELL_256k,
            "1024k" to Ids.SUPER_CELL_1024k,
            "4096k" to Ids.SUPER_CELL_4096k,
            "16m" to Ids.SUPER_CELL_16M,
            "65m" to Ids.SUPER_CELL_65M,
        )
        
        for (cellComponent in superCellComponents) {
            this.singleTexture(
                cellComponent.value.path,
                mcLoc("item/generated"),
                "layer0",
                ResourceLocation(AEAdditions.ID, "item/super_cell_component_${cellComponent.key}")
            )
        }

        for (storageCell in superStorageCells) {
            this.singleTexture(
                storageCell.value.path,
                mcLoc("item/generated"),
                "layer0",
                ResourceLocation(AEAdditions.ID, "item/super_storage_cell_${storageCell.key}")
            )
        }
        
        this.singleTexture(
            Ids.SUPER_CELL_HOUSING.path,
            mcLoc("item/generated"),
            "layer0",
            ResourceLocation(AEAdditions.ID, "item/super_cell_housing")
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
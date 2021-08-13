package com.the9grounds.aeadditions.parts

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.parts.chemical.ChemicalTerminalPart
import net.minecraft.util.ResourceLocation
import kotlin.reflect.*
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object PartModelsHelper {
    
    fun <T : AEABasePart> createModels(clazz: KClass<T>): List<ResourceLocation> {
        val list = mutableListOf<ResourceLocation>()
        var companion: KClass<*>? = clazz.companionObject ?: return list

        companion = companion as KClass<out T>
        
        companion.memberProperties.filter { it is KProperty1<*, *> && it.hasAnnotation<PartModels>() }.forEach {
            val property = it as? KProperty1<out T, ResourceLocation> ?: return@forEach
            property.isAccessible = true
//            list.add(property.get(companion.objectInstance))
        }
        
        return list
    }
}
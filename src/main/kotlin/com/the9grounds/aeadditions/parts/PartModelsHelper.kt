package com.the9grounds.aeadditions.parts

import net.minecraft.util.ResourceLocation
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.companionObject
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
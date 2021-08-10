package com.the9grounds.aeadditions.sync.gui

import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.javaType
import kotlin.reflect.jvm.isAccessible

abstract class Field<T>(val source: Any, val property: KMutableProperty<*>) {
    
    private var clientVersion: T? = null
    
    init {
        property.isAccessible = true
    }
    
    private fun getCurrentValue(): T {
        return property.getter.call() as T
    }
    
    fun hasChanges(): Boolean {
        return getCurrentValue() != clientVersion
    }
    
    fun write(data: PacketBuffer) {
        val currentValue = getCurrentValue()
        
        clientVersion = currentValue
        writeValue(data, currentValue)
    }
    
    fun read(data: PacketBuffer) {
        val value = readValue(data)
        
        property.setter.call(value)
    }
    
    protected abstract fun writeValue(data: PacketBuffer, value: T?)
    
    protected abstract fun readValue(data: PacketBuffer): T?
    
    companion object {
        
        fun create(source: Any, property: KMutableProperty<*>): Field<*> {
            val kClass = property.returnType
            
            return when(true) {
                kClass.javaClass.isAssignableFrom(ITextComponent::class.java) -> TextComponentField(source, property)
                kClass.javaClass == String::class.java -> StringField(source, property)
                kClass.javaClass == Int::class.java -> IntegerField(source, property)
                kClass.javaClass == Long::class.java -> LongField(source, property)
                kClass.javaClass == Boolean::class.java -> BooleanField(source, property)
                kClass.javaClass.isEnum -> createEnumField(source, property, kClass.javaClass.asSubclass(Enum::class.java))
                else -> throw IllegalArgumentException("Cannot sync field ${property.name}")
            }
        }
        
        fun <T: Enum<T>> createEnumField(source: Any, property: KMutableProperty<*>, fieldType: Class<T>): EnumField<T> {
            return EnumField(source, property, fieldType.enumConstants)
        }
        
        class StringField(source: Any, property: KMutableProperty<*>) : Field<String>(source, property) {
            override fun writeValue(data: PacketBuffer, value: String?) {
                data.writeString(value)
            }

            override fun readValue(data: PacketBuffer): String = data.readString()
        }
        
        class IntegerField(source: Any, property: KMutableProperty<*>): Field<Int>(source, property) {
            override fun writeValue(data: PacketBuffer, value: Int?) {
                data.writeInt(value!!)
            }

            override fun readValue(data: PacketBuffer): Int = data.readInt()
        }
        
        class LongField(source: Any, property: KMutableProperty<*>): Field<Long>(source, property) {
            override fun writeValue(data: PacketBuffer, value: Long?) {
                data.writeLong(value!!)
            }

            override fun readValue(data: PacketBuffer): Long = data.readLong()
        }
        
        class BooleanField(source: Any, property: KMutableProperty<*>): Field<Boolean>(source, property) {
            override fun writeValue(data: PacketBuffer, value: Boolean?) {
                data.writeBoolean(value!!)
            }

            override fun readValue(data: PacketBuffer): Boolean = data.readBoolean()
        }
        
        class EnumField<T: Enum<T>>(source: Any, property: KMutableProperty<*>, private val values: Array<T>): Field<T>(source, property) {
            override fun writeValue(data: PacketBuffer, value: T?) {
                data.writeVarInt(value!!.ordinal)
            }

            override fun readValue(data: PacketBuffer): T {
                return values[data.readVarInt()]
            }
        }
        
        class TextComponentField(source: Any, property: KMutableProperty<*>): Field<ITextComponent>(source, property) {
            override fun writeValue(data: PacketBuffer, value: ITextComponent?) {
                if (value == null) {
                    data.writeBoolean(false)
                } else {
                    data.writeBoolean(true)
                    data.writeTextComponent(value)
                }
            }

            override fun readValue(data: PacketBuffer): ITextComponent? {
                return if (data.readBoolean()) {
                    data.readTextComponent()
                } else {
                    null
                }
            }
        }
    }
}
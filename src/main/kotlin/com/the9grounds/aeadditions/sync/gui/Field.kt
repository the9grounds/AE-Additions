package com.the9grounds.aeadditions.sync.gui

import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

abstract class Field<T, HOST_VAL>(val source: Any, val property: KMutableProperty1<HOST_VAL, T?>) {
    
    private var clientVersion: T? = null
    
    init {
        property.isAccessible = true
    }
    
    private fun getCurrentValue(): T {
        return property.get(source as HOST_VAL) as T
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
        
        property.set(source as HOST_VAL, value)
    }
    
    protected abstract fun writeValue(data: PacketBuffer, value: T?)
    
    protected abstract fun readValue(data: PacketBuffer): T?
    
    companion object {
        
        fun <T> create(source: Any, property: KMutableProperty1<T, *>): Field<*, T> {
            val kClass = property.returnType
            
            return when(true) {
                kClass.javaClass.isAssignableFrom(ITextComponent::class.java) -> TextComponentField(source, property)
                kClass == String::class.starProjectedType -> StringField(source, property)
                kClass == Int::class.starProjectedType -> IntegerField(source, property)
                kClass == Long::class.starProjectedType -> LongField(source, property)
                kClass == Boolean::class.starProjectedType -> BooleanField(source, property)
                kClass.javaClass.isEnum -> createEnumField(source, property, kClass.javaClass.asSubclass(Enum::class.java))
                else -> throw IllegalArgumentException("Cannot sync field ${property.name}")
            }
        }
        
        fun <T: Enum<T>, HOST_VAL> createEnumField(source: Any, property: KMutableProperty1<HOST_VAL, *>, fieldType: Class<T>): EnumField<T, HOST_VAL> {
            return EnumField(source, property, fieldType.enumConstants)
        }
        
        class StringField<HOST_VAL>(source: Any, property: KMutableProperty1<HOST_VAL, *>) : Field<String, HOST_VAL>(source, property as KMutableProperty1<HOST_VAL, String?>) {
            override fun writeValue(data: PacketBuffer, value: String?) {
                data.writeString(value)
            }

            override fun readValue(data: PacketBuffer): String = data.readString()
        }
        
        class IntegerField<HOST_VAL>(source: Any, property: KMutableProperty1<HOST_VAL, *>): Field<Int, HOST_VAL>(source, property as KMutableProperty1<HOST_VAL, Int?>) {
            override fun writeValue(data: PacketBuffer, value: Int?) {
                data.writeInt(value!!)
            }

            override fun readValue(data: PacketBuffer): Int = data.readInt()
        }
        
        class LongField<HOST_VAL>(source: Any, property: KMutableProperty1<HOST_VAL, *>): Field<Long, HOST_VAL>(source, property as KMutableProperty1<HOST_VAL, Long?>) {
            override fun writeValue(data: PacketBuffer, value: Long?) {
                data.writeLong(value!!)
            }

            override fun readValue(data: PacketBuffer): Long = data.readLong()
        }
        
        class BooleanField<HOST_VAL>(source: Any, property: KMutableProperty1<HOST_VAL, *>): Field<Boolean, HOST_VAL>(source, property as KMutableProperty1<HOST_VAL, Boolean?>) {
            override fun writeValue(data: PacketBuffer, value: Boolean?) {
                data.writeBoolean(value!!)
            }

            override fun readValue(data: PacketBuffer): Boolean = data.readBoolean()
        }
        
        class EnumField<T: Enum<T>, HOST_VAL>(source: Any, property: KMutableProperty1<HOST_VAL, *>, private val values: Array<T>): Field<T, HOST_VAL>(source, property as KMutableProperty1<HOST_VAL, T?>) {
            override fun writeValue(data: PacketBuffer, value: T?) {
                data.writeVarInt(value!!.ordinal)
            }

            override fun readValue(data: PacketBuffer): T {
                return values[data.readVarInt()]
            }
        }
        
        class TextComponentField<HOST_VAL>(source: Any, property: KMutableProperty1<HOST_VAL, *>): Field<ITextComponent, HOST_VAL>(source, property as KMutableProperty1<HOST_VAL, ITextComponent?>) {
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
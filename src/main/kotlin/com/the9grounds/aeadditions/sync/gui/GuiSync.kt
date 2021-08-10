package com.the9grounds.aeadditions.sync.gui

/**
 * Taken from AE2 source for more control
 * https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/master/src/main/java/appeng/container/guisync/GuiSync.java
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GuiSync(val key: String)

package vip.cdms.allay_exp.test

import org.allaymc.api.plugin.Plugin

@Suppress("unused")
class TestPlugin : Plugin() {
    override fun onLoad() = pluginLogger.info("TestPlugin loaded!")
    override fun onEnable() = pluginLogger.info("TestPlugin enabled!")
    override fun onDisable() = pluginLogger.info("TestPlugin disabled!")
}

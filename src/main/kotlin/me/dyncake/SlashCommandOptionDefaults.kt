package me.dyncake

import me.dyncake.SlashCommandOptionDefaults.getString
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import java.lang.IllegalArgumentException

object SlashCommandOptionDefaults {
    fun SlashCommandEvent.getBoolean(option: String, default: Boolean?): Boolean {
        val op = this.getOption(option)
        return op?.asBoolean ?: (default ?: throw IllegalArgumentException("Default value cannot be null"))
    }

    @JvmStatic
    fun SlashCommandEvent.getString(option: String, default: String?): String {
        val op = this.getOption(option)
        return op?.asString ?: (default ?: throw IllegalArgumentException("Default value cannot be null"))
    }

    fun SlashCommandEvent.getLong(option: String, default: Long?): Long {
        val op = this.getOption(option)
        return op?.asLong ?: (default ?: throw IllegalArgumentException("Default value cannot be null"))
    }
}
package me.dyncake.listeners

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CmdHelp : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name == "fart") {
            event.reply("U FORTED 💀").queue()
        } else if (event.name == "add") {
            val addend1 = event.getOption("num1")
            if (addend1 == null) {
                println("Somehow, option 1 was not provided")
                event.reply("An internal error has occurred").queue()
                return
            }
            val addend2 = event.getOption("num2")
            if (addend2 == null) {
                println("Somehow, option 2 was not provided")
                event.reply("An internal error has occurred").queue()
                return
            }
            event.reply("The sum is: " + (addend1.asDouble + addend2.asDouble)).queue()
        }
    } //https://youtu.be/LvHELa9ji0A?t=118
}
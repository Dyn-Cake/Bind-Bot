package me.dyncake.listeners

import me.dyncake.SlashCommandOptionDefaults.getBoolean
import me.dyncake.SlashCommandOptionDefaults.getLong
import me.dyncake.SlashCommandOptionDefaults.getString
import me.dyncake.TF2Files
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.nio.charset.StandardCharsets
import java.util.*

class CmdIpBind : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name != "gen-ip") return
        println("[${System.currentTimeMillis()}] User ${event.user.name} with id ${event.user.id} has executed command ${event.commandString}")
        val key = event.getString("key", null)
        val ips = event.getLong("ips", 100)
        val displayNumber = event.getBoolean("display_number", false)
        val validateKey = event.getBoolean("validate_key", true)
        val fileName = event.getString("file_name", "ip.cfg")

        if (validateKey) {
            if (!TF2Files.tf2KeyValidator.isValidKey(key)) {
                event.reply("Your key is not a valid one, please double check the key if it is valid\n" +
                        "To disable this check, make validate key false").queue()
                return
            }
        }
        if (ips > 100_000) {
            event.reply("Please do not try to break me, I know what you are doing").queue()
            return
        }

        val script =
            StringBuilder(
            """
            //Made by the script generator
            //Script name: ${event.commandString.lowercase()}
            //Invite the bot: https://discord.com/api/oauth2/authorize?client_id=919819362595127356&permissions=414464723008&scope=bot%20applications.commands
            
            """.trimIndent()
        )
        script.append(
            """
                alias ip.say ip.0;
                bind $key ip.say;
                
            """.trimIndent()
        )
        val rand = Random()
        for (i in 0..ips) {
            script.append(
                "alias ip.$i \"say ${rand.nextInt(256)}.${rand.nextInt(256)}.${rand.nextInt(256)}." +
                        "${rand.nextInt(256)};${if (displayNumber) "echo $i;" else ""}alias ip.say ip.${i + 1}\";\n"
            )
        }

        event.reply("Finished!").addFile(script.toString().toByteArray(StandardCharsets.UTF_8), fileName).queue()

    }


}
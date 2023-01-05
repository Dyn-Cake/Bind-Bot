package me.dyncake.listeners

import me.dyncake.TF2Files
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.nio.charset.StandardCharsets

class CmdScunt : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name != "gen-scunt") return
        println("[${System.currentTimeMillis()}] User ${event.user.name} with id ${event.user.id} has executed command ${event.commandString}")
        val boundKeyOption = event.getOption("key")
        if (boundKeyOption == null) {
            println("Somehow, key was not provided")
            event.reply("An internal error has occurred").queue()
            return
        }
        val boundKey = boundKeyOption.asString
        val randomizeOrder: Boolean
        val randomizeOrderOption = event.getOption("randomize_order")
        randomizeOrder = randomizeOrderOption?.asBoolean ?: false
        val validateKey: Boolean
        val validateKeyOption = event.getOption("validate_key")
        validateKey = validateKeyOption?.asBoolean ?: true
        val fileName: String
        val fileNameOption = event.getOption("file_name")
        fileName = fileNameOption?.asString ?: "scunt_insults.cfg"
        if (validateKey) {
            if (!TF2Files.tf2KeyValidator.isValidKey(boundKey)) {
                event.reply("Your key is not a valid one, please double check the key if it is valid\nTo disable this check, make validate key false")
                    .queue()
                return
            }
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
            bind ${boundKey.lowercase()} "scunt.sayline";
            alias scunt.rotate scunt.rotation_0;
            alias scunt.sayline scunt.line_0;
            
            """
        )

        val scoutVoicelines: ArrayList<String> = ArrayList(TF2Files.scoutVoicelines)
        if (randomizeOrder) scoutVoicelines.shuffled()
        script.append(
            "alias scunt.line_0 \"say ${scoutVoicelines[0]}; alias scunt.sayline scunt.line_${scoutVoicelines.size - 1}; alias scunt.rotate scunt.rotation_${scoutVoicelines.size - 1}\";\n",
        )
        script.append(
            "alias scunt.rotation_0 \"alias scunt.sayline scunt.line_${scoutVoicelines.size - 1}; alias scunt.rotate scunt.rotation_${scoutVoicelines.size - 1}\";\n",
        )
        run {
            var i = 1
            for (voiceline in scoutVoicelines.subList(1, scoutVoicelines.size)) {
                script.append(
                    "alias scunt.line_${i} \"say ${voiceline}; alias scunt.sayline scunt.line_${i - 1}; alias scunt.rotate scunt.rotation_${i - 1}\";\n",
                )
                script.append(
                    "alias scunt.rotation_${i} \"alias scunt.sayline scunt.line_${i - 1}; alias scunt.rotate scunt.rotation_${i - 1}\";\n",
                )
                i++
            }
        }
        event.reply("Finished!").addFile(script.toString().toByteArray(StandardCharsets.UTF_8), fileName).queue()
    }
}
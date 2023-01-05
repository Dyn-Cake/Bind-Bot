package me.dyncake

import me.dyncake.listeners.CmdHelp
import me.dyncake.listeners.CmdIpBind
import me.dyncake.listeners.CmdJukebox
import me.dyncake.listeners.CmdScunt
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.OptionType
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.security.auth.login.LoginException

object BotMain {


    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting bot")
        val jda = getJda(args[0]) ?: return
        jda.addEventListener(
            CmdHelp(),
            CmdScunt(),
            CmdJukebox(),
            CmdIpBind()
        )

        println("Bot started!")
        val testGuild = jda.getGuildById(847073885149069312L)
        testGuild!!.upsertCommand("fart", "Fard 💀").queue()
        testGuild.upsertCommand("add", "How math doe")
            .addOption(OptionType.NUMBER, "num1", "Provide the first addend", true)
            .addOption(OptionType.NUMBER, "num2", "Provide the second addend", true)
            .queue()
        testGuild.upsertCommand("gen-scunt", "Generate a script to become a true scunt (Non global)")
            .addOption(OptionType.STRING, "key", "The key to bind", true)
            .addOption(OptionType.BOOLEAN, "randomize_order", "Randomize the order the lines are in", false)
            .addOption(OptionType.STRING, "file_name", "Set the file name of the bind", false)
            .addOption(OptionType.BOOLEAN, "validate_keys", "Attempt to validate the keybind you made", false)
            .queue()
        jda.upsertCommand("gen-scunt", "Generate a script to become a true scunt")
            .addOption(OptionType.STRING, "key", "The key to bind", true)
            .addOption(OptionType.BOOLEAN, "randomize_order", "Randomize the order the lines are in", false)
            .addOption(OptionType.STRING, "file_name", "Set the file name of the bind", false)
            .addOption(OptionType.BOOLEAN, "validate_keys", "Attempt to validate the keybind you made", false)
            .queue()
        jda.upsertCommand("gen-jukebox", "Generate a script to play some music when you play")
            .addOption(OptionType.STRING, "play_song_key", "Set the key to play the selected track", true)
            .addOption(OptionType.STRING, "next_song_key", "Set the key to switch to the next song", true)
            .addOption(
                OptionType.STRING,
                "prev_song_key",
                "Set the key to switch to the previous song",
                true
            ) //.addOption(OptionType.STRING, "song_name_url", "Unimplemented")
            .addOption(OptionType.BOOLEAN, "randomize_order", "Randomize the order the songs are in", false)
            .addOption(OptionType.STRING, "file_name", "Set the file name of the bind")
            .addOption(OptionType.BOOLEAN, "validate_keys", "Attempt to validate the keybind you made", false)
            .queue()
        testGuild.upsertCommand("gen-ip", "Generates a bunch of ips to fake doxx people")
            .addOption(OptionType.STRING, "key", "The key to bind", true)
            .addOption(OptionType.INTEGER, "ips", "The amount of ips to generate", true)
            .addOption(OptionType.BOOLEAN, "display_number", "If the ip number should be displayed", false)
            .addOption(OptionType.STRING, "file_name", "Set the file name of the bind", false)
            .addOption(OptionType.BOOLEAN, "validate_key", "Attempt to validate the keybind you made", false)
            .queue()


    }

    @Throws(IOException::class)
    fun readRecourseFile(filePath: String): List<String> {
        val ioStream = BotMain::class.java
            .classLoader
            .getResourceAsStream(filePath)
        if (ioStream == null) {
            println("$filePath does not exist!")
            return listOf()
        }
        val inputStreamReader = InputStreamReader(ioStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val list: MutableList<String> = ArrayList()
        var line: String
        while (bufferedReader.readLine().also { line = it } != null) {
            list.add(line)
        }
        ioStream.close()
        return list
    }

    private fun getJda(token: String): JDA? {
        val jda: JDA = try {
            JDABuilder.createDefault(token)
                .setActivity(Activity.watching("How 2 Script"))
                .build().awaitReady()
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
            return null
        } catch (ex: LoginException) {
            ex.printStackTrace()
            return null
        }
        return jda
    }

    @Throws(IOException::class)
    private fun getFileJda(): JDA? {

        val tokenFile = File("botToken.txt")
        if (!tokenFile.exists()) {
            println("Error: File botToken.txt does not exist")
            return null
        }
        val content = Files.readString(Paths.get("botToken.txt"))

        return getJda(content)
    }
}
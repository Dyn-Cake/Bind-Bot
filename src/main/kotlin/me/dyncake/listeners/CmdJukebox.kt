package me.dyncake.listeners

import me.dyncake.TF2Files
import me.dyncake.keyValidators.TF2KeyValidator
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.nio.charset.StandardCharsets
import me.dyncake.SlashCommandOptionDefaults
import me.dyncake.SlashCommandOptionDefaults.getBoolean
import me.dyncake.SlashCommandOptionDefaults.getString

class CmdJukebox : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name != "gen-jukebox") return
        println("[${System.currentTimeMillis()}] User ${event.user.name} with id ${event.user.id} has executed command ${event.commandString}")
        val nextSongKey = event.getString("next_song_key", "KP_HOME")
        val prevSongKey = event.getString("prev_song_key", "KP_UPARROW")
        val playSongKey = event.getString("play_song_key", "KP_PGUP")
        val additionalSongs = event.getString("songs", "null") //TODO Future me problem
        val fileName = event.getString("file_name", "jukebox.cfg")
        val randomizeOrder = event.getBoolean("randomize_order", false)
        val validateKeys = event.getBoolean("validate_key", true)
        val overrideDefaultSongs = event.getBoolean( "override_default_songs", false)
        if (validateKeys) {
            val validator = TF2Files.tf2KeyValidator
            if (validate(event, playSongKey, validator)) {
                return
            }
            if (validate(event, nextSongKey, validator)) {
                return
            }
            if (validate(event, prevSongKey, validator)) {
                return
            }
        }
        val songs = TF2Files.songs
        val script = StringBuilder(
            """
           //Made by the script generator
           //Script name: ${event.commandString.lowercase()}
           //Invite the bot: https://discord.com/api/oauth2/authorize?client_id=919819362595127356&permissions=414464723008&scope=bot%20applications.commands
           
            """.trimIndent()
        )
        script.append(
            """
                bind $playSongKey "jb.play_song";
                bind $nextSongKey "jb.next_song";
                bind $prevSongKey "jb.prev_song";
                
                alias jb.select_song jb.play_song_0;
                alias jb.next_song jb.select_song_0;
                alias jb.prev_song jb.select_song_${songs.size - 1};
            
            """.trimIndent()
        )
        if (randomizeOrder) songs.shuffled()
        //alias jb.song1 "say_party playing song1; play /ui/gamestartup1.mp3; alias jb.next_song jb.song2; alias jb.prev_song jb.song9;"
        script.append(
            """
                alias jp.pointer_${0} "play /ui/buttonrollover; alias jp.play_song jp.song_${0}; alias jp.next_song jp.next_${1}; alias jp.prev_song jp.prev_${songs.size - 1};"
                                
                alias jp.song_${0} "/ui/buttonclick; play /ui/${songs[0].name}; say_party Now playing: ${songs[0].fileName};";
                alias jp.next_${0} "jb.pointer_${1}";
                alias jp.prev_${0} "jb.pointer_${songs.size - 1}";

            """

        )

        //https://gamebanana.com/scripts/8268
        run {
            var i = 1
            for (song in songs.subList(1, songs.size - 1)) {
                script.append(
                    """
                        alias jp.pointer_${i} "play /ui/buttonrollover; alias jp.play_song jp.song_${i}; alias jp.next_song jp.next_${i + 1}; alias jp.prev_song jp.prev_${song.name};"
                        
                        alias jp.song_${i} "/ui/buttonclick; play /ui/${i - 1}; say_party Now playing: ${song.fileName};";
                        alias jp.next_${i} "jb.pointer_${i + 1}";
                        alias jp.prev_${i} "jb.pointer_${i - 1}";
                    
                    """
                )
                i++
            }
        }
        script.append(
            """
                alias jp.pointer_${songs.size - 1} "play /ui/buttonrollover; alias jp.play_song jp.song_${songs.size - 1}; alias jp.next_song jp.next_${0}; alias jp.prev_song jp.prev_${songs.size - 2};"
                                
                alias jp.song_${songs.size - 1} "/ui/buttonclick; play /ui/${songs[songs.size - 1].name}; say_party Now playing: ${songs[songs.size - 1].fileName};";
                alias jp.next_${songs.size - 1} "jb.pointer_${0}";
                alias jp.prev_${songs.size - 1} "jb.pointer_${songs[songs.size - 1].name}";
            """.trimIndent()
        )
        event.reply("Finished!").addFile(script.toString().toByteArray(StandardCharsets.UTF_8), fileName).queue()
        println("Done!")
    }


    private fun validate(event: SlashCommandEvent, key: String, validator: TF2KeyValidator): Boolean {
        if (!validator.isValidKey(key)) {
            event.reply(
                "Key $key is not a valid key, please double check the key if it is valid\n" +
                        "To disable this check, make the option validate key false"
            ).queue()
            return true
        }
        return false
    }
}
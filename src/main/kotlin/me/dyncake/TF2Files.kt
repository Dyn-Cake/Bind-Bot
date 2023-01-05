package me.dyncake

import me.dyncake.keyValidators.TF2KeyValidator
import java.util.*
import java.util.stream.Collectors

object TF2Files {
    var scoutVoicelines: List<String> = BotMain.readRecourseFile("scout_voicelines.txt")
    var tf2KeyValidator: TF2KeyValidator = TF2KeyValidator(BotMain.readRecourseFile("valid_keys.txt"))
    var songs: List<Song>
    init {

        val content = java.lang.String.join("|", BotMain.readRecourseFile("songs.txt"))
        val map = Arrays.stream(content.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .map { s: String ->
                s.split(";".toRegex()).dropLastWhile {
                    it.isEmpty()
                }
                    .toTypedArray()
            }.collect(
                Collectors.toMap(
                { s: Array<String> -> s[0] },
                { s: Array<String> -> s[1] }
            )
            )
        val songs: MutableList<Song> = ArrayList()
        for ((key, value) in map) songs.add(Song(key, value))
        TF2Files.songs = songs
    }
}
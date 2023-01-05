package me.dyncake.keyValidators

import java.util.*

class TF2KeyValidator(private var validKeys: List<String>) {

     fun isValidKey(key: String): Boolean {
        return validKeys.contains(key.uppercase(Locale.getDefault()))
    }
}
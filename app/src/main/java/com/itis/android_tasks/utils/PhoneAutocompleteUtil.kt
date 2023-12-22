package com.itis.android_tasks.utils

import com.itis.android_tasks.R

object PhoneAutocompleteUtil {

    const val PHONE_FIRST_PART = "+7 (9"
    private const val PHONE_SECOND_PART = ")-"
    private const val PHONE_END_PARTS = "-"

    fun addNumbersAutocomplete(
        input: CharSequence,
        selection: Int,
        count: Int
    ): CharSequence {
        var inputModified = input
        if (selection != input.length && count <= 1) {
            input.subSequence(selection, selection + count).let {
                inputModified = input.removeRange(selection, selection + count)
                    .toString() + it
            }
        }
        if (input.length == 7) {
            return "$inputModified$PHONE_SECOND_PART"
        }
        if (input.length == 12 || input.length == 15) {
            return "$inputModified$PHONE_END_PARTS"
        }
        return inputModified
    }

    fun removeNumbersAutocomplete(
        input: CharSequence
    ): CharSequence {
        if (input.length <= 5) {
            return PHONE_FIRST_PART
        }
        if (input.length == 9) {
            return input.subSequence(0, input.length - 3)
        }
        if (input.length == 13 || input.length == 16) {
            return input.subSequence(0, input.length - 2)
        }
        return input.subSequence(0, input.length - 1)
    }
}

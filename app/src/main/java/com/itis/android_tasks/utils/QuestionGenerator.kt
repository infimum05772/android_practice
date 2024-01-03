package com.itis.android_tasks.utils

import android.content.Context
import android.os.Bundle
import com.itis.android_tasks.R
import com.itis.android_tasks.model.Answer
import com.itis.android_tasks.model.Question
import kotlin.random.Random

object QuestionGenerator {
    private fun initAnswers(): List<Answer> {
        return listOf(
            Answer("Absolutely me", false),
            Answer("That applies to me in a lot of ways", false),
            Answer("That could apply to me", false),
            Answer("Not sure", false),
            Answer("More no than yes", false),
            Answer("It's hard to relate to me", false),
            Answer("No, never", false)
        )
    }

    fun getQuestions(ctx: Context, questionCount: Int): List<Question> {
        val array = ctx.resources.getStringArray(R.array.question_texts).toMutableList()

        val resultList = mutableListOf<Question>()
        var index = 0
        repeat(questionCount) {
            index++
            val newIndex = Random.nextInt(0, array.size)
            resultList.add(Question("Question $index", array[newIndex], initAnswers()))
            array.removeAt(newIndex)
        }
        return resultList
    }
}

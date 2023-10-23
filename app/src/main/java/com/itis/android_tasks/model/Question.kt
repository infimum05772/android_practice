package com.itis.android_tasks.model

data class Question(
    val title: String,
    val text: String,
    val answers: List<Answer>
)

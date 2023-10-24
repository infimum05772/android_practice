package com.itis.android_tasks.model

import java.io.Serializable

data class Question (
    val title: String,
    val text: String,
    val answers: List<Answer>
) : Serializable

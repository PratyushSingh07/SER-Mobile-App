package com.project.ser.model

data class EmotionResponse(
    val emo: String,
    val prob: List<Int>
)
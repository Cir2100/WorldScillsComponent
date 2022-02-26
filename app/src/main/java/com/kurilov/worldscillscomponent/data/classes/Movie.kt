package com.kurilov.worldscillscomponent.data.classes

data class Flags(
    val png : String,
    val svg : String
)

data class Movie(
    val flags : Flags
)

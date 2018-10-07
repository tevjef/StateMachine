package com.tinder

data class EventModel(
    val name: String,
    val transition: TransitionModel?,
    val dontTransition: DontTransition?)
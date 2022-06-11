package com.example.board_game_collector

import kotlinx.serialization.Serializable

@Serializable
class Game(var id: Long, var title: String, var year: Int, var ranking: Int)
package com.bignerdranch.android.geoquiz

data class Answers(var correct : Int, var incorrect : Int) {
    constructor() : this(0, 0)
}

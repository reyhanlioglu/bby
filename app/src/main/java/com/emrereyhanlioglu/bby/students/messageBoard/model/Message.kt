package com.emrereyhanlioglu.bby.students.messageBoard.model

data class Message(
    var writer: String = "",
    var message: String = "",
    var date: String = ""

) {
    constructor(): this(
        "",
        "",
        ""
    )
}
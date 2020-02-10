package com.emrereyhanlioglu.bby.students.announcements.model

data class Announcement(
    var header: String = "",
    var message: String = "",
    var date: String = "",
    var info: String = ""

) {
    constructor(): this(
        "",
        "",
        "",
        ""
    )
}
package com.emrereyhanlioglu.bby.students.attendance.model

data class AttendanceDetail (
    var type: String?,
    var date: String?,
    var hours: Int?,
    var writer: String?,
    var month: String?
) {
    constructor(): this(
        "-",
        "-",
        0,
        "",
        ""
    )
}


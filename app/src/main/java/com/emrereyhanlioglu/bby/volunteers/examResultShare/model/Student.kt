package com.emrereyhanlioglu.bby.volunteers.examResultShare.model

data class Student(
    var fullname: String = "",
    var uid: String = "",
    var className: String = ""

) {
    constructor(): this(
        "",
        "",
        ""
    )
}
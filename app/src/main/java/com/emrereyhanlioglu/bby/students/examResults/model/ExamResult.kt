package com.emrereyhanlioglu.bby.students.examResults.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
@Entity
data class ExamResult(
    var examType: String = "",
    @PrimaryKey
    var examName: String = "",
    var turkceD: Int = 0,
    var turkceY: Int = 0,
    var turkceT: Int = 0,
    var matD: Int = 0,
    var matY: Int = 0,
    var matT: Int = 0,
    var fenD: Int = 0,
    var fenY: Int = 0,
    var fenT: Int = 0,
    var sosyalD: Int = 0,
    var sosyalY: Int = 0,
    var sosyalT: Int = 0,
    var totalPoint: Double = 0.0
) {


    constructor(): this(
        "",
        "",
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0
    )



 /*   @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "matD" to matD,
            "matY" to matY,
            "matT" to matT,
            "turkceD" to turkceD,
            "turkceY" to turkceY

        )
    }
*/

}

package com.arrowwould.womenhomeworkout.reminderNew

// Reminder class
class Reminder {
    var iD = 0
    var title: String? = null
    var date: String? = null
    var time: String? = null
    var repeat: String? = null
    var repeatNo: String? = null
    var repeatType: String? = null
    var active: String? = null
    var days :String?=null

    constructor(ID: Int, Title: String?, Date: String?, Time: String?, Repeat: String?,
                RepeatNo: String?, RepeatType: String?, Active: String?, Days: String?) {
        iD = ID
        title = Title
        date = Date
        time = Time
        repeat = Repeat
        repeatNo = RepeatNo
        repeatType = RepeatType
        active = Active
        days = Days
    }

    constructor(Title: String?, Date: String?, Time: String?, Repeat: String?,
                RepeatNo: String?, RepeatType: String?, Active: String?, Days: String?) {
        title = Title
        date = Date
        time = Time
        repeat = Repeat
        repeatNo = RepeatNo
        repeatType = RepeatType
        active = Active
        days = Days
    }

    constructor() {}

}
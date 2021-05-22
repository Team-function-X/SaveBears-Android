package com.junction.savebears.component.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.toSimpleString(form : String ="yyyy.MM.dd") : String {
    return SimpleDateFormat(form).format(this)
}
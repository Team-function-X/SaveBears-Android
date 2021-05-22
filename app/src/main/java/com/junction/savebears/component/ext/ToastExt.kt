package com.junction.savebears.component.ext

import android.content.Context
import android.widget.Toast.*
import androidx.annotation.StringRes

fun Context.toastShort(@StringRes messageRes: Int) = makeText(this, getString(messageRes), LENGTH_SHORT).show()
fun Context.toastLong(@StringRes messageRes: Int) = makeText(this, getString(messageRes), LENGTH_LONG).show()
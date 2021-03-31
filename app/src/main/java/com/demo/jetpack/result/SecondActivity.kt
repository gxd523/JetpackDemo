package com.demo.jetpack.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class SecondActivity : Activity() {
    companion object {
        const val INPUT_PARAM = "inputParam"
        const val OUTPUT_PARAM = "outputParam"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TextView(this).apply {
            text = "SecondActivity"
            textSize = 30f
            gravity = Gravity.CENTER
            setOnClickListener {
                Intent().apply {
                    val inputParam = this@SecondActivity.intent.getIntExtra(INPUT_PARAM, -1)
                    putExtra(OUTPUT_PARAM, "$inputParam...SencondActivity")
                }.let { setResult(RESULT_OK, it) }
                finish()
            }
        }.let(::setContentView)
    }
}
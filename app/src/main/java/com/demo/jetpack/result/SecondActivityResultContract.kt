package com.demo.jetpack.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class SecondActivityResultContract : ActivityResultContract<Int, String>() {
    override fun createIntent(context: Context, input: Int?): Intent = Intent(context, SecondActivity::class.java).apply {
        putExtra(SecondActivity.INPUT_PARAM, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        val result = intent?.getStringExtra(SecondActivity.OUTPUT_PARAM)
        return if (resultCode == Activity.RESULT_OK && result != null) result else null
    }
}
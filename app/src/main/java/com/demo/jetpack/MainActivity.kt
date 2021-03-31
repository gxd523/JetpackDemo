package com.demo.jetpack

import android.content.Intent
import android.os.Bundle
import com.demo.jetpack.databinding.ActivityMainBinding
import com.demo.jetpack.result.StartForResultActivity
import com.gxd.viewbindingwrapper.ViewBindingActivity

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.startForResultActivityBtn.setOnClickListener {
            startActivity(Intent(this, StartForResultActivity::class.java))
        }
    }
}
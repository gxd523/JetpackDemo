package com.demo.jetpack.result

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.demo.jetpack.databinding.ActivityStartForResultBinding
import com.gxd.viewbindingwrapper.ViewBindingActivity

class StartForResultActivity : ViewBindingActivity<ActivityStartForResultBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityLauncher = registerForActivityResult(SecondActivityResultContract()) { result ->
            binding.startSecondActivityBtn.text = result
        }

        binding.startSecondActivityBtn.apply {
            setOnClickListener {
                activityLauncher.launch(9527)
            }
        }

        val defaultActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                if (activityResult.resultCode == Activity.RESULT_OK) {
                    val result = activityResult.data?.getStringExtra("result")
                    binding.startSecondActivityDefaultBtn.text = result
                }
            }

        binding.startSecondActivityDefaultBtn.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(SecondActivity.INPUT_PARAM, 9528)
            }
            defaultActivityLauncher.launch(intent)
        }

        requestPermission()
    }

    private var permission: String? = null
    private var permissionDialog: DialogInterface? = null

    private fun requestPermission() {
        val requestSinglePermissionActivityLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    Toast.makeText(this, "用户没同意权限：$permission", Toast.LENGTH_SHORT).show()
                }
                permissionDialog?.dismiss()
            }

        val requestPermissionsActivityLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                var allGranted = true
                permissions.entries.forEach { entry ->
                    val (permission, granted) = entry
                    if (!granted &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        shouldShowRequestPermissionRationale(permission)
                    ) { // 用户禁止了该权限，但仍允许下次提示，此时应该向用户解释为啥需要该权限
                        this@StartForResultActivity.permission = permission
                        if (allGranted) {
                            allGranted = false
                        }
                        showPermissionDialog(permission) {
                            requestSinglePermissionActivityLauncher.launch(permission)
                        }
                    }
                }
                if (allGranted) {
                    Toast.makeText(this@StartForResultActivity, "以获取所有权限", Toast.LENGTH_SHORT).show()
                }
            }

        binding.requestMultiplePermissionsBtn.setOnClickListener {
            requestPermissionsActivityLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CONTACTS
                )
            )
        }
    }

    private fun showPermissionDialog(permission: String, onReselectClick: () -> Unit) {
        permissionDialog = AlertDialog.Builder(this)
            .setTitle("请求权限")
            .setMessage(permission)
            .setNegativeButton("不需要") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setPositiveButton("重新选择") { _: DialogInterface, _: Int -> onReselectClick() }
            .setCancelable(false)
            .create()
            .apply {
                setOnDismissListener {
                    this@StartForResultActivity.permission = null
                    permissionDialog = null
                }
                show()
            }
    }
}
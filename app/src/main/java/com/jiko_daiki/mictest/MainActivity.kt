package com.jiko_daiki.mictest

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun showMike(){
        createSnack(SnackType.ENABLE)
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    fun showRationaleMike(request: PermissionRequest){
        AlertDialog.Builder(this)
                .setPositiveButton("OK"){_,_ -> request.proceed()}
                .setNegativeButton("Cancel"){_,_ -> request.cancel()}
                .setCancelable(false)
                .setTitle("MicTest")
                .setMessage("MicTest needs permmision for Microphone. Would you agree?")
                .show()
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    fun onCameraDenied() {
        createSnack(SnackType.DENIED)
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    fun onCameraNeverAskAgain() {
        createSnack(SnackType.NEVER)
    }

    fun createSnack(type: SnackType){
        val snackbar:Snackbar
        snackbar = when(type){
            SnackType.ENABLE ->{
                Snackbar.make(rootLayout, "Mic enabled", Snackbar.LENGTH_SHORT)
            }
            SnackType.DENIED -> {
                Snackbar.make(rootLayout, "Permission Denied", Snackbar.LENGTH_LONG)
            }
            SnackType.NEVER -> {
                Snackbar.make(rootLayout, "go setting and give permission", Snackbar.LENGTH_LONG)
            }
        }
        when(type){
            SnackType.ENABLE, SnackType.DENIED -> {
                snackbar.setAction("Close", View.OnClickListener {
                    snackbar.dismiss()
                }).show()
            }
            SnackType.NEVER->{
                snackbar.setAction("Go To App Setting", View.OnClickListener {
                    val intent = Intent()
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.setData(Uri.fromParts("package", packageName, null))
                    startActivity(intent)
                })
                .show()
            }
        }
    }

    enum class SnackType{ENABLE, DENIED, NEVER}
}

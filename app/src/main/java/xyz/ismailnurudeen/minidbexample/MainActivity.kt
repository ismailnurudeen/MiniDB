package xyz.ismailnurudeen.minidbexample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import xyz.ismailnurudeen.minidb.MiniDB

class MainActivity : AppCompatActivity() {
    var image: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        signup_btn.setOnClickListener {
            signUp()
            startActivity(Intent(this, DetailsActivity::class.java))
        }
        upload_img_btn.setOnClickListener {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101)) {
                val imgReadIntent = Intent(Intent.ACTION_PICK)
                imgReadIntent.type = "image/*"
                startActivityForResult(imgReadIntent, 101)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val selectedPicUri = data?.data
            image = MediaStore.Images.Media.getBitmap(contentResolver, selectedPicUri)
            imagview.setImageBitmap(image)
            upload_img_btn.alpha = 0.0F
        }
    }

    fun signUp() {
        val miniDb = MiniDB.open(this, "MY_LOGIN_DB")
        val name = name_editText.text.toString()
        val password = password_editText.text.toString()
        val email = email_editText.text.toString()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return
        }
        if (miniDb.insertObject("user", User(name, email, password))) {
            Log.d("SIGN_UP", "user object saved successfully")
        } else {
            Log.d("SIGN_UP", "user object saved failed")
        }

        if (image != null) {
            if (miniDb.insertBitmap("profile_image", image)) {
                Log.d("SIGN_UP", "image saved successfully")
            } else {
                Log.d("SIGN_UP", "image save failed")
            }
        }
        val likes = ArrayList<String>()
        if (like_fashion.isChecked) likes.add("Fashion")
        if (like_football.isChecked) likes.add("Football")
        if (like_comp.isChecked) likes.add("Computers")
        if (like_music.isChecked) likes.add("Music")
        if (likes.isNotEmpty()) {
            if (miniDb.insertList("likes_list", likes as List<Any>?)) {
                Log.d("SIGN_UP", "likes saved successfully")
            } else {
                Log.d("SIGN_UP", "likes save failed")
            }
        }
    }

    data class User(val name: String, val email: String, val password: String)

    private fun checkPermission(permission: String, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                            permission) == PackageManager.PERMISSION_GRANTED) {
                Log.v(packageName, "Permission is granted")
                return true
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Toast.makeText(this, "We need this permission for the app to work properly", Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                }
                Log.v(packageName, "Permission is revoked")
                return false
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(packageName, "Permission is granted")
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(packageName, "Permission: " + permissions[0] + "was " + grantResults[0])
            if (requestCode == 101) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 101)
            }
        }
    }
}

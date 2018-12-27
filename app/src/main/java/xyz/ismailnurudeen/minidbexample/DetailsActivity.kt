package xyz.ismailnurudeen.minidbexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import xyz.ismailnurudeen.minidb.MiniDB

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val miniDb = MiniDB.open(this, "MY_LOGIN_DB")
        val user = miniDb.readObject("user", MainActivity.User("null", "null", "null")) as MainActivity.User
        name_tv.setText(user.name)
        email_tv.setText(user.email)
        img_view.setImageDrawable(miniDb.readDrawable("profile_image", null))
        var likesStr = ""
        for (like in miniDb.readList("likes_list", arrayListOf())) {
            if (like.toString().isNotEmpty()) {
                likesStr += "${like},"
            }
        }
        if (!likesStr.isNotEmpty().equals(",")) {
            likes_tv.setText(likesStr)
        } else {
            likes_tv.setText("No likes...")
        }
    }
}

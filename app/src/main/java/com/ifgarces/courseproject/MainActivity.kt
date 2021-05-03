package com.ifgarces.courseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ifgarces.courseproject.models.DataMaster


class MainActivity : AppCompatActivity() {

    private class ActivityUI(owner :AppCompatActivity) {
        val loginButton :Button = owner.findViewById(R.id.login_loginButton)
    }; private lateinit var UI :ActivityUI
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_login)
        this.UI = ActivityUI(owner=this)

        UI.loginButton.setOnClickListener {
            this.startActivity(
                Intent(this, PlanningActivity::class.java)
            )
        }

        DataMaster.init()
    }
}

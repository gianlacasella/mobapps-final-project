package com.ifgarces.courseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_login)
        var login_button = findViewById(R.id.log_in_button) as Button
        login_button.setOnClickListener(){
            val intent = Intent(this, PlanningActivity::class.java)
            startActivity(intent)
        }
    }
}

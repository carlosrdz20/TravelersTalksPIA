package com.example.travelers_talks

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.travelers_talks.data.UserSingleton
import com.google.android.material.navigation.NavigationView
import android.util.Base64

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, popUpSeguro.OnUserUpdateListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle : ActionBarDrawerToggle



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        val headerView: View = navigationView.getHeaderView(0)
        val textView: TextView = headerView.findViewById(R.id.tvNickName)
        textView.text = UserSingleton.currentUserNickname

        val imageView: ImageView = headerView.findViewById(R.id.ivProfilePicture)

        val imageDataBytes = java.util.Base64.getDecoder().decode(UserSingleton.currentUserImg!!.substringAfter(','))
        val decodedImageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size)
        imageView.setImageBitmap(decodedImageBitmap)

        if(savedInstanceState == null){
            val extras = intent.extras
            if (extras != null) {
                val datoRecibido = extras.get("opcionMain")

                when (datoRecibido) {
                    0->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,HomeFragment()).commit()
                        navigationView.setCheckedItem(R.id.nav_home)
                    }
                    1->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,MyPostsFragment(0)).commit()
                        navigationView.setCheckedItem(R.id.nav_myposts)
                    }
                    2->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,MyPostsFragment(1)).commit()
                        navigationView.setCheckedItem(R.id.nav_myposts)
                    }
                    3->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,MyPostsFragment(2)).commit()
                        navigationView.setCheckedItem(R.id.nav_myposts)
                    }
                }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,HomeFragment()).commit()
            R.id.nav_profile -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,ProfileFragment()).commit()
            R.id.nav_myposts -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,MyPostsFragment(0)).commit()
            R.id.nav_logout ->{
                val intent = Intent(this, loginActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onUserUpdated() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView: View = navigationView.getHeaderView(0)
        val textView: TextView = headerView.findViewById(R.id.tvNickName)
        textView.text = UserSingleton.currentUserNickname

        val imageView: ImageView = headerView.findViewById(R.id.ivProfilePicture)

        val imageDataBytes = java.util.Base64.getDecoder().decode(UserSingleton.currentUserImg!!.substringAfter(','))
        val decodedImageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size)
        imageView.setImageBitmap(decodedImageBitmap)
    }



}
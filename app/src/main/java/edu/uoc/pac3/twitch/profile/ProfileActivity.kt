package edu.uoc.pac3.twitch.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.bumptech.glide.Glide
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupButtons()

        lifecycleScope.launch {
            whenStarted {
                var user = getUserData()
                user?.let {
                    currentUser = user
                    userNameTextView.text = user.userName
                    viewsText.text = user.viewCount.toString()
                    userDescriptionEditText.setText(user.description)
                    Glide.with(applicationContext).load(user.profileImageUrl).into(imageView);
                }

            }
        }
    }

    private fun setupButtons() {

        updateDescriptionButton.setOnClickListener {
           //do request
            lifecycleScope.launch {
                whenStarted {
                    val description = userDescriptionEditText.text.toString()
                    val resultUserData = updateDescription(description)
                    if(resultUserData != null && resultUserData.description == description)
                    {
                        currentUser = resultUserData
                        Toast.makeText(applicationContext, getString(R.string.update_description_ok), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.update_description_fail), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        logoutButton.setOnClickListener {
            val sm = SessionManager(applicationContext)
            sm.clearUserData()

            Toast.makeText(applicationContext, getString(R.string.logout_description), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LaunchActivity::class.java))
            finish()
        }


    }


    suspend fun getUserData(): User? {
        val ts = TwitchApiService(Network.createHttpClient(this))
        //val sm = SessionManager(this)

        var user = ts.getUser()

        return user
    }

    suspend fun updateDescription(description: String) : User?
    {
        return TwitchApiService(Network.createHttpClient(this)).updateUserDescription(description)
    }

}
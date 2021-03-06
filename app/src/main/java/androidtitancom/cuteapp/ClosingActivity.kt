package androidtitancom.cuteapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidtitancom.cuteapp.model.CuteUser

import kotlinx.android.synthetic.main.activity_closing.*
import kotlinx.android.synthetic.main.content_closing.*
import android.os.Build
import android.annotation.TargetApi



class ClosingActivity : AppCompatActivity() {

    private lateinit var cutie: CuteUser
    private var messageSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closing)

        //if the user hasn't given permission yet
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE),1)

        //view
//        toolbar.title = resources.getString(R.string.welcome_back)
//        toolbar.setTitleTextColor(getColor(R.color.colorAccent))

        val extras = intent.extras
        val optionSelected = extras?.getString(CuteActivity.OPTION_EXTRA)

        if (optionSelected != null && optionSelected.isNotEmpty()) {
            cutie = CuteUser(optionSelected)
            setHintImage(optionSelected)
        }

        if (savedInstanceState == null) {
            //closingRootRelativeLayout.visibility = View.INVISIBLE

            val viewTreeObserver = closingRootRelativeLayout.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        //circularRevealActivity(closingRootRelativeLayout)

                        Log.e("ClosingActivity", "onGlobalLayout")
                        removeOnGlobalLayoutListener(closingRootRelativeLayout, this)
                        //closingRootRelativeLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                })
            }
        }

        numberEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (numberEditText.text.length >= 10) {

                    sendLoadingDots.visibility = View.GONE
                    sendTextView.visibility = View.VISIBLE

                } else if(numberEditText.text.length < 10
                        && sendLoadingDots.visibility == View.VISIBLE
                        && sendTextView.visibility == View.GONE) {

                    sendLoadingDots.visibility = View.VISIBLE
                    sendTextView.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        sendTextView.setOnClickListener {

                val sendName = nameEditText.text.toString()
                val sendNumber = numberEditText.text.toString()
                val properOption = getProperOption(optionSelected)

                nameTextInputLayout.visibility = View.GONE
                numberTextInputLayout.visibility = View.GONE
                sendTextView.text = "Sweet"
                sendTextView.setOnClickListener(null)

                congrats.text = "Congrats ${ if (!sendName.isBlank()) sendName  else "Beautiful Stranger" }!  You\'re going to ${getProperOption(optionSelected)} with this guy! Get Excited!"
                congrats.visibility = View.VISIBLE

                //sendTextMessage(optionSelected)

        }

        returnImageView.setOnClickListener {
            restartApp()
        }
    }

    private fun sendTextMessage(optionSelected: String?) {
        val sendName = nameEditText.text.toString()
        val sendNumber = numberEditText.text.toString()
        val properOption = getProperOption(optionSelected)

        try {
            val smsManager = SmsManager.getDefault()

            smsManager.sendTextMessage(sendNumber, null,
                    "",
                    null, null)

            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("ClosingActivity", e.message)
            Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getProperOption(optionSelected: String?): String {

        when(optionSelected) {
            getString(R.string.crazy) -> return getString(R.string.action_crazy)
            getString(R.string.classy) -> return getString(R.string.action_classy)
            getString(R.string.basic) -> return getString(R.string.action_basic)
            getString(R.string.adventurous) -> return getString(R.string.action_adventurous)
            getString(R.string.chill) -> return getString(R.string.action_chill)
        }
        return ""
    }

    private fun setHintImage(optionSelected: String) {

        when(optionSelected) {
            getString(R.string.crazy) -> selectionImageView.setImageResource(R.drawable.ic_alert_octagram)
            getString(R.string.classy) -> selectionImageView.setImageResource(R.drawable.ic_glass_flute)
            getString(R.string.basic) -> selectionImageView.setImageResource(R.drawable.ic_airplane_off)
            getString(R.string.adventurous) -> selectionImageView.setImageResource(R.drawable.ic_tree)
            getString(R.string.chill) -> selectionImageView.setImageResource(R.drawable.ic_movie)
        }
    }

    private fun restartApp() {
        val intent = Intent(this@ClosingActivity, PressButtonActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        restartApp()
    }

    @SuppressLint("ObsoleteSdkInt")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.viewTreeObserver.removeGlobalOnLayoutListener(listener)
        } else {
            v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
}

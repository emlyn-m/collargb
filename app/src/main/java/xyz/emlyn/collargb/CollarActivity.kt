package xyz.emlyn.collargb

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat


class CollarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collar)

        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val window: Window = this@CollarActivity.window
        window.navigationBarColor = ContextCompat.getColor(this@CollarActivity, R.color.white)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mToolbar.setNavigationOnClickListener { onBackPressed() }

        val deviceName = intent.extras?.get("device_name")
        findViewById<TextView>(R.id.collar_title).text = deviceName.toString()

        val oneColorSwitch = findViewById<SwitchCompat>(R.id.collarOneColor_switch)
        oneColorSwitch.setOnClickListener {

            val singleTV = findViewById<TextView>(R.id.collarOneColor_single_tv)
            val perLedTV = findViewById<TextView>(R.id.collarOneColor_perLed_tv)

            var singleColorFrom = ContextCompat.getColor(this, R.color.white)
            var singleColorTo = ContextCompat.getColor(this, R.color.black)
            var perLedColorFrom = ContextCompat.getColor(this, R.color.black)
            var perLedColorTo = ContextCompat.getColor(this, R.color.white)

            if (oneColorSwitch.isChecked) {
                singleColorFrom = ContextCompat.getColor(this, R.color.black)
                singleColorTo = ContextCompat.getColor(this, R.color.white)
                perLedColorFrom = ContextCompat.getColor(this, R.color.white)
                perLedColorTo = ContextCompat.getColor(this, R.color.black)
            }


            val singleTextColorVA = ValueAnimator.ofObject(ArgbEvaluator(), singleColorFrom, singleColorTo)
            val perLedTextColorVA = ValueAnimator.ofObject(ArgbEvaluator(), perLedColorFrom, perLedColorTo)
            singleTextColorVA.addUpdateListener { animator -> singleTV.setTextColor(animator.animatedValue as Int) }
            perLedTextColorVA.addUpdateListener { animator -> perLedTV.setTextColor(animator.animatedValue as Int) }

            singleTextColorVA.start()
            perLedTextColorVA.start()


        }
    }
}
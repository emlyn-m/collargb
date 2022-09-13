package xyz.emlyn.collargb

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import kotlin.math.atan2
import kotlin.math.pow


class CollarActivity : AppCompatActivity() {

    var mColor : Int = Color.RED

    @SuppressLint("ClickableViewAccessibility")
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

        val csHSIV = findViewById<ImageView>(R.id.colorSelectorHS_iv)
        val csHSAlphaIV = findViewById<ImageView>(R.id.colorSelectorHS_alpha_iv)
        val csVSB = findViewById<SeekBar>(R.id.colorSelectorV_sb)

        csHSIV.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                mColor = getColor(csHSIV, event.x.toInt(), event.y.toInt(), csVSB.progress.toFloat()/100)
                setSliderBackground(csVSB)

                true
            } else false
        }


        csVSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                if (progress < 5) { seekBar.progress = 0 }
                if (progress > 95) { seekBar.progress = 100 }
                csHSAlphaIV.alpha = (1 - progress.toFloat()/100)

                setSliderBackground(csVSB)
            }
        })

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val sbLP = csVSB.layoutParams

            sbLP.width = csHSIV.measuredHeight + (30 * this.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
            sbLP.height = csHSIV.x.toInt()

            csVSB.layoutParams = sbLP

            setSliderBackground(csVSB)

        }, 50)
    }

    private fun setSliderBackground(seekbar : SeekBar) {

        var hsv = floatArrayOf(0f,0f,0f)
        Color.colorToHSV(mColor, hsv)
        hsv[2] = 1f

        val sliderLG = LinearGradient(0f, 0f, seekbar.measuredWidth.toFloat(), 0f, Color.BLACK, Color.HSVToColor(hsv), Shader.TileMode.CLAMP)
        val sliderSD = ShapeDrawable(RectShape())
        sliderSD.paint.shader = sliderLG
        val bounds = seekbar.progressDrawable.bounds
        seekbar.progressDrawable = sliderSD
        seekbar.progressDrawable.bounds = bounds

        hsv = floatArrayOf(0f,0f,0f)
        Color.colorToHSV(mColor, hsv)

        hsv[2] = seekbar.progress.toFloat() / 100

        seekbar.thumb.setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.SRC_IN)
    }

    private fun getColor(view : ImageView, x : Int, y : Int, v : Float) : Int {
        val hue = toPolar((x - view.measuredWidth/2).toDouble(), (y - view.measuredHeight/2).toDouble()).toFloat()
        var sat = ((y-view.measuredHeight/2).toDouble().pow(2) + (x-view.measuredWidth/2).toDouble().pow(2)).pow(0.5).toFloat()
        sat /= (view.measuredWidth / 2)

        if (sat > 1) { return mColor }

        return Color.HSVToColor(floatArrayOf(hue, sat, v))
    }

    private fun toPolar(x : Double, y : Double): Double {
        return flippedAtan2(y, x) * (180 / Math.PI)
    }


    private fun flippedAtan2(y: Double, x: Double): Double {
        val angle = atan2(y, x)
        val flippedAngle: Double = (Math.PI / 2) - angle
        return if (flippedAngle >= 0) flippedAngle else flippedAngle + 2 * Math.PI
    }

}
package xyz.emlyn.collargb

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager


class HomeActivity : AppCompatActivity() {

    private var mCardAdapter: CardPagerAdapter? = null
    private var mFragmentCardAdapter: CardFragmentPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val window: Window = this@HomeActivity.window
        window.navigationBarColor = ContextCompat.getColor(this@HomeActivity, R.color.white)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        val mViewPager : ViewPager = findViewById(R.id.viewPager)

        mCardAdapter = CardPagerAdapter()

        mCardAdapter!!.addCardItem(CardItem(applicationContext, "title.1", R.drawable.ic_android_black_24dp))
        mCardAdapter!!.addCardItem(CardItem(applicationContext, "title.2", R.drawable.ic_android_black_24dp))
        mCardAdapter!!.addCardItem(CardItem(applicationContext, "title.3", R.drawable.ic_android_black_24dp))
        mCardAdapter!!.addCardItem(CardItem(applicationContext, "title.4", R.drawable.ic_android_black_24dp))

        mFragmentCardAdapter = CardFragmentPagerAdapter(
            supportFragmentManager,
            dpToPixels(2, this)
        )

        val mCardShadowTransformer = ShadowTransformer(mViewPager, mCardAdapter)

        mViewPager.adapter = mCardAdapter
        mViewPager.setPageTransformer(false, mCardShadowTransformer)
        mViewPager.offscreenPageLimit = 3

    }


    private fun dpToPixels(dp: Int, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }

}

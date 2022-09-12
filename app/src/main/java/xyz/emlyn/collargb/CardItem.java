package xyz.emlyn.collargb;

import android.content.Context;

public class CardItem {

    private int mImageResource;
    private String mTitleResource;
    private Context mContext;

    public CardItem(Context ctxt, String title, int img) {
        mTitleResource = title;
        mImageResource = img;
        mContext = ctxt;

    }

    public int getImage() {
        return mImageResource;
    }

    public String getTitle() {
        return mTitleResource;
    }

    public Context getContext() {
        return mContext;
    }
}
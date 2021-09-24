package com.jobsity.tvshows.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.jobsity.tvshows.R;
import com.squareup.picasso.Picasso;

public class InternetImageView extends androidx.appcompat.widget.AppCompatImageView {

    public InternetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadImageFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.get()
                    .load(url)
                    .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.poster_placeholder))
                    .error(ContextCompat.getDrawable(getContext(), R.drawable.poster_placeholder))
                    .into(this);
        }
    }
}

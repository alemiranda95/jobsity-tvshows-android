package com.jobsity.tvshows.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.interfaces.SHowAdapterInterface;
import com.jobsity.tvshows.ui.presentation.search.SearchFragment;
import com.jobsity.tvshows.view.InternetImageView;

import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

    private final List<Show> mData;
    private final Context mContext;
    private SHowAdapterInterface mListener;

    public ShowAdapter(Context context,
                       List<Show> data,
                       SHowAdapterInterface listener) {
        this.mContext = context;
        this.mData = data;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_show, parent, false);
        if (mListener instanceof SearchFragment) {
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    mContext.getResources().getDimensionPixelOffset(R.dimen.search_items_width),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Show show = mData.get(position);
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_anim));
        holder.bindData(show);
        if (mData.size() - 1 == position) {
            mListener.onLastItemReached();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mShowNameTextView;
        private final InternetImageView mShowPosterImageView;
        private final TextView mShowRatingTextView;
        private final ImageView mShowRatingIconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mShowNameTextView = itemView.findViewById(R.id.textview_show_name);
            mShowPosterImageView = itemView.findViewById(R.id.imageview_show_poster);
            mShowRatingTextView = itemView.findViewById(R.id.textview_show_rating);
            mShowRatingIconImageView = itemView.findViewById(R.id.imageview_show_rating_icon);
        }

        public void bindData(Show show) {
            mShowNameTextView.setText(show.getName());
            mShowPosterImageView.loadImageFromUrl(show.getPosterMedium());
            mShowRatingTextView.setVisibility(!TextUtils.isEmpty(show.getRating()) ?
                    View.VISIBLE :
                    View.INVISIBLE);
            mShowRatingTextView.setText(show.getRating());
            mShowRatingIconImageView.setVisibility(!TextUtils.isEmpty(show.getRating()) ?
                    View.VISIBLE :
                    View.INVISIBLE);
            itemView.setOnClickListener(v ->  {
                mListener.onShowClick(show);
            });
        }
    }

}
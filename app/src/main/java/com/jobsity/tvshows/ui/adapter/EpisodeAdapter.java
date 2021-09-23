package com.jobsity.tvshows.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.domain.model.episode.Episode;
import com.jobsity.tvshows.util.FormatUtils;
import com.jobsity.tvshows.view.InternetImageView;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private final List<Episode> mData;
    private final Context mContext;
    private int mExpandedPosition = -1;
    private int mPreviousExpandedPosition = -1;

    public EpisodeAdapter(Context context,
                          List<Episode> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Episode episode = mData.get(position);
        holder.bindDate(episode);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView mEpisodeCardView;
        private InternetImageView mEpisodePosterImageView;
        private TextView mEpisodeNameTextView;
        private ConstraintLayout mColapsedConstraintLayout;
        private ConstraintLayout mExpandedConstraintLayout;
        private InternetImageView mEpisodePosterExpandedImageView;
        private TextView mEpisodeNameExpandedTextView;
        private TextView mEpisodeSummaryExpandedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mEpisodeCardView = itemView.findViewById(R.id.cardview_episode);
            mEpisodeNameTextView = itemView.findViewById(R.id.textview_episode_name);
            mEpisodePosterImageView = itemView.findViewById(R.id.imageview_episode_poster);
            mColapsedConstraintLayout = itemView.findViewById(R.id.constraint_layout_episode_colaprsed);
            mExpandedConstraintLayout = itemView.findViewById(R.id.constraint_layout_episode_expanded);
            mEpisodePosterExpandedImageView = itemView.findViewById(R.id.imageview_episode_poster_expanded);
            mEpisodeNameExpandedTextView = itemView.findViewById(R.id.textview_episode_name_expanded);
            mEpisodeSummaryExpandedTextView = itemView.findViewById(R.id.textview_episode_summary_expanded);
        }

        public void bindDate(Episode episode) {
            String episodeName = String.format(
                    TvShowApp.getRes().getString(R.string.episode_info_number_name),
                    episode.getNumber(),
                    episode.getName()
            );
            mEpisodeNameTextView.setText(episodeName);
            mEpisodePosterImageView.loadImageFromUrl(episode.getPosterMedium());
            expandCollapseLayouts(episode);
        }

        private void expandCollapseLayouts(Episode episode) {
            int position = mData.indexOf(episode);
            final boolean isExpanded = position==mExpandedPosition;
            itemView.setActivated(isExpanded);
            ViewGroup.LayoutParams cardParams = mEpisodeCardView.getLayoutParams();
            cardParams.height = isExpanded ? ViewGroup.LayoutParams.WRAP_CONTENT: FormatUtils.dpToPx(100);
            mEpisodeCardView.setLayoutParams(cardParams);
            mPreviousExpandedPosition = isExpanded ? position : mPreviousExpandedPosition;
            mExpandedConstraintLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            mColapsedConstraintLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);

            if (isExpanded) {
                String episodeExpandedName = String.format(
                        TvShowApp.getRes().getString(R.string.episode_info_seasson_number_name),
                        episode.getSeason(),
                        episode.getNumber(),
                        episode.getName()
                );
                mEpisodeNameExpandedTextView.setText(episodeExpandedName);
                mEpisodeSummaryExpandedTextView.setText(FormatUtils.formatHtml(episode.getSummary()));
                mEpisodePosterExpandedImageView.loadImageFromUrl(episode.getPosterOriginal());
            }

            itemView.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(mPreviousExpandedPosition);
                notifyItemChanged(position);
            });
        }
    }
}

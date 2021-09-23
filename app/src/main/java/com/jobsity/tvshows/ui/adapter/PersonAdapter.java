package com.jobsity.tvshows.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.ui.interfaces.PersonAdapterInterface;
import com.jobsity.tvshows.view.InternetImageView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{

    private final List<Person> mData;
    private final Context mContext;
    private final PersonAdapterInterface mListener;

    public PersonAdapter(Context context,
                       List<Person> data,
                       PersonAdapterInterface listener) {
        this.mContext = context;
        this.mData = data;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.ViewHolder holder, int position) {
        Person person = mData.get(position);
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_anim));
        holder.bindData(person);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mPersonNameTextView;
        private final InternetImageView mPersonPhotoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPersonNameTextView = itemView.findViewById(R.id.textview_person_name);
            mPersonPhotoImageView = itemView.findViewById(R.id.imageview_person_photo);
        }

        public void bindData(Person person) {
            mPersonNameTextView.setText(person.getName());
            mPersonPhotoImageView.loadImageFromUrl(person.getPhotoMedium());
            itemView.setOnClickListener(v -> mListener.onPersonClick(person));
        }
    }

}

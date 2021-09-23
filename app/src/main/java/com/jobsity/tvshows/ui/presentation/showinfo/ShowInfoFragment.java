package com.jobsity.tvshows.ui.presentation.showinfo;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.ui.adapter.EpisodeAdapter;
import com.jobsity.tvshows.databinding.FragmentShowInfoBinding;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.interfaces.FavoriteChangedInterface;
import com.jobsity.tvshows.ui.presentation.BottomSheetBaseFragment;

public class ShowInfoFragment extends BottomSheetBaseFragment implements View.OnClickListener {

    //Constants
    private static final String BUNDLE_SHOW_SELECTED = "BUNDLE_SHOW_SELECTED";

    //Binding
    private FragmentShowInfoBinding mBinding;

    //ViewModel
    private ShowInfoViewModel mShowInfoViewModel;

    //Adapters
    private EpisodeAdapter mEpisodeAdapter;

    //Variables
    private Show mShowSelected;
    private FavoriteChangedInterface mFavoriteListener;

    public static ShowInfoFragment newInstance(Show show) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SHOW_SELECTED, show);
        ShowInfoFragment f = new ShowInfoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mShowSelected = (Show) args.get(BUNDLE_SHOW_SELECTED);
        }
    }

    @Override
    protected View setBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentShowInfoBinding.inflate(inflater, container, false);
        mBinding.imageviewShowInfoPoster.loadImageFromUrl(mShowSelected.getPosterOriginal());
        mBinding.collapsingToolbarShowInfo.setTitle(mShowSelected.getName());

        mBinding.recyclerviewEpisodes.setLayoutManager(new LinearLayoutManager(requireContext()));
        mEpisodeAdapter = new EpisodeAdapter(getContext(),
                mShowInfoViewModel.getShowInfoViewState().getValue().getEpisodeList());
        mBinding.recyclerviewEpisodes.setAdapter(mEpisodeAdapter);

        mBinding.setShowInfoVM(mShowInfoViewModel);
        return mBinding.getRoot();
    }

    @Override
    protected void setListeners() {
        mBinding.spinnerShowInfoSeassons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mShowInfoViewModel.loadEpisodeList(i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nothing to do
            }
        });

        mBinding.fabShowFavorite.setOnClickListener(view -> {
            mShowInfoViewModel.setFavorite();
        });
    }

    @Override
    protected void setViewModel() {
        ViewModelProvider.Factory showInfoFactory = new ViewModelProvider.NewInstanceFactory() {
            @SuppressWarnings("unchecked cast")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(ShowInfoViewModel.class)) {
                    return (T) new ShowInfoViewModel(mShowSelected);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        };

        mShowInfoViewModel = new ViewModelProvider(this, showInfoFactory).get(ShowInfoViewModel.class);
    }

    @Override
    protected void setObservers() {
        mShowInfoViewModel.getShowInfoViewState().observe(getViewLifecycleOwner(), showInfoViewState -> {
            if (showInfoViewState.isEpisodeListChanged()) {
                mEpisodeAdapter.notifyDataSetChanged();
            }

            if (showInfoViewState.isSeassonsSetted()) {
                loadSeassonSpinner(showInfoViewState.getLastSeasson());
            }

            mBinding.fabShowFavorite.setImageDrawable(showInfoViewState.isFavorite() ?
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_black_40dp) :
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_40dp)
            );

            if (mFavoriteListener != null) {
                mFavoriteListener.onFavoriteShowListChanged();
            }

            mBinding.layoutLoadingLists.progressbarLoading.setVisibility(showInfoViewState.isShowProgress() ?
                    View.VISIBLE :
                    View.GONE);

            if (showInfoViewState.isShowNoInternetError()) {
                showNoInternetSnackbar(this);
            }

            if (showInfoViewState.getErrorMessage() != null) {
                showErrorSnackbar(showInfoViewState.getErrorMessage());
            }
        });
    }

    private void loadSeassonSpinner(int lastSeasson) {
        String[] seassons = new String[lastSeasson];
        for (int i = 0; i < lastSeasson; i++) {
            String seasson = String.format(getString(R.string.seasson_number), i+1);
            seassons[i] = seasson;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                seassons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerShowInfoSeassons.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == com.google.android.material.R.id.snackbar_action) {
            mBinding.imageviewShowInfoPoster.loadImageFromUrl(mShowSelected.getPosterOriginal());
            mShowInfoViewModel.obtainEpisodes();
        }
    }

    @Override
    public void onConnect() {
        mBinding.imageviewShowInfoPoster.loadImageFromUrl(mShowSelected.getPosterOriginal());
        mShowInfoViewModel.obtainEpisodes();
    }

    //Setters
    public void setFavoriteListener(FavoriteChangedInterface favoriteListener) {
        this.mFavoriteListener = favoriteListener;
    }

}
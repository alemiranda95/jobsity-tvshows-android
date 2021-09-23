package com.jobsity.tvshows.ui.presentation.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.jobsity.tvshows.databinding.FragmentFavoriteBinding;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.adapter.ShowAdapter;
import com.jobsity.tvshows.ui.interfaces.FavoriteChangedInterface;
import com.jobsity.tvshows.ui.interfaces.SHowAdapterInterface;
import com.jobsity.tvshows.ui.presentation.BaseFragment;
import com.jobsity.tvshows.ui.presentation.showinfo.ShowInfoFragment;
import com.jobsity.tvshows.util.constant.AppConstants;

public class FavoriteFragment extends BaseFragment
        implements SHowAdapterInterface, FavoriteChangedInterface {

    //Binding
    private FavoriteViewModel mFavoriteViewModel;
    private FragmentFavoriteBinding mBinding;

    //Variables
    private ShowAdapter mShowAdapter;

    @Override
    protected View setBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentFavoriteBinding.inflate(inflater, container, false);

        mBinding.recyclerviewFavoriteShows.setLayoutManager(new GridLayoutManager(getContext(),
                AppConstants.SHOWS_GRID_LAYOUT_COLUMN_COUNT));
        mShowAdapter = new ShowAdapter(getContext(),
                mFavoriteViewModel.getFavoriteViewState().getValue().getShowList(),
                this);
        mBinding.recyclerviewFavoriteShows.setAdapter(mShowAdapter);

        return mBinding.getRoot();
    }

    @Override
    protected void setListeners() {
        //Nothing to do
    }

    @Override
    protected void setViewModel() {
        mFavoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
    }

    @Override
    protected void setObservers() {
        mFavoriteViewModel.getFavoriteViewState().observe(getViewLifecycleOwner(), favoriteViewState -> {
            if (favoriteViewState.isShowListChanged()) {
                mShowAdapter.notifyDataSetChanged();
            }

            mBinding.layoutLoadingLists.textviewNothingToShow.setVisibility(favoriteViewState.isNothingToShow() ?
                    View.VISIBLE :
                    View.GONE);

            mBinding.layoutLoadingLists.progressbarLoading.setVisibility(favoriteViewState.isShowProgress() ?
                    View.VISIBLE :
                    View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onShowClick(Show show) {
        ShowInfoFragment showInfoFragment = ShowInfoFragment.newInstance(show);
        showInfoFragment.setFavoriteListener(this);
        showInfoFragment.show(getParentFragmentManager(), null);
    }

    @Override
    public void onLastItemReached() {
        //Nothing to do
    }

    @Override
    public void onFavoriteShowListChanged() {
        mFavoriteViewModel.obtainShows();
    }

    @Override
    public void onConnect() {
        //Nothing to do
    }
}
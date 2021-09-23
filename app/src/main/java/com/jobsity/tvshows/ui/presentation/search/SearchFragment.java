package com.jobsity.tvshows.ui.presentation.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jobsity.tvshows.ui.adapter.PersonAdapter;
import com.jobsity.tvshows.ui.adapter.ShowAdapter;
import com.jobsity.tvshows.databinding.FragmentSearchBinding;
import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.interfaces.PersonAdapterInterface;
import com.jobsity.tvshows.ui.interfaces.SHowAdapterInterface;
import com.jobsity.tvshows.ui.presentation.BaseFragment;
import com.jobsity.tvshows.ui.presentation.personinfo.PersonInfoFragment;
import com.jobsity.tvshows.ui.presentation.showinfo.ShowInfoFragment;

public class SearchFragment extends BaseFragment
        implements SHowAdapterInterface, PersonAdapterInterface, View.OnClickListener {

    //Binding
    private FragmentSearchBinding mBinding;

    //ViewModel
    private SearchViewModel mSearchViewModel;

    //Adapters
    private ShowAdapter mShowAdapter;
    private PersonAdapter mPersonAdapter;

    @Override
    protected View setBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false);

        mShowAdapter = new ShowAdapter(getContext(),
                mSearchViewModel.getSearchViewState().getValue().getShowList(),
                this);
        mBinding.recyclerviewSearchShows.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mBinding.recyclerviewSearchShows.setAdapter(mShowAdapter);

        mPersonAdapter = new PersonAdapter(getContext(),
                mSearchViewModel.getSearchViewState().getValue().getPersonList(),
                this);
        mBinding.recyclerviewSearchPeople.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mBinding.recyclerviewSearchPeople.setAdapter(mPersonAdapter);

        mBinding.searchviewQuery.requestFocus();

        return mBinding.getRoot();
    }

    @Override
    protected void setListeners() {
        mBinding.searchviewQuery.setOnQueryTextFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                mActivity.showSoftKeyboard();
            }
        });

        mBinding.searchviewQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showSearchAndPeople(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void showSearchAndPeople(String query) {
        if (!query.isEmpty()) {
            mActivity.hideSoftKeyboard();
            mSearchViewModel.obtainShowsAndPeople(query);
            mBinding.searchviewQuery.clearFocus();
        }
    }

    @Override
    protected void setViewModel() {
        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Override
    protected void setObservers() {
        mSearchViewModel.getSearchViewState().observe(getViewLifecycleOwner(), searchViewState -> {
            if (searchViewState.isShowListChanged()) {
                mShowAdapter.notifyDataSetChanged();
            }

            if (searchViewState.isPersonListChanged()) {
                mPersonAdapter.notifyDataSetChanged();
            }

            mBinding.layoutLoadingLists.textviewNothingToShow.setVisibility(searchViewState.isNothingToShow() ?
                    View.VISIBLE :
                    View.GONE);

            mBinding.layoutLoadingLists.progressbarLoading.setVisibility(searchViewState.isShowProgress() ?
                    View.VISIBLE :
                    View.GONE);

            if (searchViewState.isShowNoInternetError()) {
                mActivity.showNoInternetSnackbar(this);
            }

            if (searchViewState.getErrorMessage() != null) {
                mActivity.showErrorSnackbar(searchViewState.getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onShowClick(Show show) {
        ShowInfoFragment.newInstance(show).show(getParentFragmentManager(), null);
    }

    @Override
    public void onLastItemReached() {
        //Nothing to do
    }

    @Override
    public void onPersonClick(Person person) {
        PersonInfoFragment.newInstance(person).show(getParentFragmentManager(), null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == com.google.android.material.R.id.snackbar_action) {
            showSearchAndPeople(mBinding.searchviewQuery.getQuery().toString());
        }
    }

    @Override
    public void onConnect() {
        showSearchAndPeople(mBinding.searchviewQuery.getQuery().toString());
    }
}
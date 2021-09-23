package com.jobsity.tvshows.ui.presentation.personinfo;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobsity.tvshows.domain.model.show.Show;
import com.jobsity.tvshows.ui.adapter.ShowAdapter;
import com.jobsity.tvshows.databinding.FragmentPersonInfoBinding;
import com.jobsity.tvshows.domain.model.person.Person;
import com.jobsity.tvshows.ui.interfaces.SHowAdapterInterface;
import com.jobsity.tvshows.ui.presentation.BottomSheetBaseFragment;
import com.jobsity.tvshows.ui.presentation.showinfo.ShowInfoFragment;
import com.jobsity.tvshows.util.constant.AppConstants;

public class PersonInfoFragment extends BottomSheetBaseFragment implements View.OnClickListener, SHowAdapterInterface {

    //Constants
    private static final String BUNDLE_PERSON_SELECTED = "BUNDLE_PERSON_SELECTED";

    //Binding
    private FragmentPersonInfoBinding mBinding;

    //ViewModel
    private PersonInfoViewModel mPersonViewModel;

    //Adapters
    private ShowAdapter mShowAdapter;

    //Variables
    private Person mPersonSelected;

    public static PersonInfoFragment newInstance(Person person) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_PERSON_SELECTED, person);
        PersonInfoFragment f = new PersonInfoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mPersonSelected = (Person) args.get(BUNDLE_PERSON_SELECTED);
        }
    }

    @Override
    protected View setBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = FragmentPersonInfoBinding.inflate(inflater, container, false);
        mBinding.imageviewPersonInfoPhoto.loadImageFromUrl(mPersonSelected.getPhotoOriginal());
        mBinding.collapsingToolbarPersonInfo.setTitle(mPersonSelected.getName());

        mBinding.recyclerviewPersonShows.setLayoutManager(new GridLayoutManager(getContext(),
                AppConstants.SHOWS_GRID_LAYOUT_COLUMN_COUNT));
        mShowAdapter = new ShowAdapter(getContext(),
                mPersonViewModel.getPersonInfoViewState().getValue().getPersonShowList(),
                this);
        mBinding.recyclerviewPersonShows.setAdapter(mShowAdapter);

        return mBinding.getRoot();
    }

    @Override
    protected void setListeners() {
        //Nothing to do
    }

    @Override
    protected void setViewModel() {
        ViewModelProvider.Factory showInfoFactory = new ViewModelProvider.NewInstanceFactory() {
            @SuppressWarnings("unchecked cast")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(PersonInfoViewModel.class)) {
                    return (T) new PersonInfoViewModel(mPersonSelected);
                }
                throw new IllegalArgumentException("Unknown ViewModel class");
            }
        };

        mPersonViewModel = new ViewModelProvider(this, showInfoFactory).get(PersonInfoViewModel.class);
    }

    @Override
    protected void setObservers() {
        mPersonViewModel.getPersonInfoViewState().observe(getViewLifecycleOwner(), personInfoViewState -> {
            if (personInfoViewState.isPersonShowListChanged()) {
                mShowAdapter.notifyDataSetChanged();
            }

            mBinding.layoutLoadingLists.progressbarLoading.setVisibility(personInfoViewState.isShowProgress() ?
                    View.VISIBLE :
                    View.GONE);

            if (personInfoViewState.isShowNoInternetError()) {
                showNoInternetSnackbar(this);
            }

            if (personInfoViewState.getErrorMessage() != null) {
                showErrorSnackbar(personInfoViewState.getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == com.google.android.material.R.id.snackbar_action) {
            mBinding.imageviewPersonInfoPhoto.loadImageFromUrl(mPersonSelected.getPhotoOriginal());
            mPersonViewModel.obtainPersonShows();
        }
    }

    @Override
    public void onConnect() {
        mBinding.imageviewPersonInfoPhoto.loadImageFromUrl(mPersonSelected.getPhotoOriginal());
        mPersonViewModel.obtainPersonShows();
    }

    @Override
    public void onShowClick(Show show) {
        ShowInfoFragment.newInstance(show).show(getParentFragmentManager(), null);
    }

    @Override
    public void onLastItemReached() {
        //Nothing to do
    }
}
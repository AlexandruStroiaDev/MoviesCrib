package co.alexdev.moviescrib_phase2.fragments;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.activities.DetailActivity;
import co.alexdev.moviescrib_phase2.adapter.TopRatedMoviesAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;

public class TopRatedFragment extends BaseFragment implements TopRatedMoviesAdapter.onTopRatedMovieClick {

    private static final int GRID_COLUMN_SPAN = 2;
    private RecyclerView rv_movies;
    private TopRatedMoviesAdapter mTopRatedMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<Movie> mMovieList = new ArrayList<>();
    private boolean hasBeenVisibleOnce = false;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null) {
            mGridLayoutManager.onRestoreInstanceState(listState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);

        rv_movies = rootView.findViewById(R.id.rv_top_rated);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        mTopRatedMoviesAdapter = new TopRatedMoviesAdapter(getActivity(), mMovieList, this);
        mGridLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMN_SPAN);
        rv_movies.setAdapter(mTopRatedMoviesAdapter);
        rv_movies.setLayoutManager(mGridLayoutManager);
    }

    private void showDetailActivity(final int movieId) {
        final Resources resources = getResources();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(resources.getString(R.string.selected_movie_key), movieId);
        startActivity(intent);
    }

    @Override
    public void onMovieClick(int position) {
        if (mMovieList != null) {
            final Movie movie = mMovieList.get(position);
            showDetailActivity(movie.getId());
        }
    }

    private void initView() {
        if (MovieUtils.isNetworkAvailable(getActivity())) {
            getData();
        } else if (canStoreOfflineData) {
            getData();
        } else {
            MovieUtils.showDialog(getActivity(), Enums.DialogType.NO_INTERNET, null);
        }
    }

    private void getData() {
        baseViewModel.getTopRatedMoviesFromDatabase().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                mMovieList = movieList;
                mTopRatedMoviesAdapter.setMovieList(movieList);
            }
        });
    }

    /*Load data in the fragment only after it gets visible to the user*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && !hasBeenVisibleOnce) {
            initView();
            hasBeenVisibleOnce = true;
        }
    }
}


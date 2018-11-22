package co.alexdev.moviescrib_phase2.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import co.alexdev.moviescrib_phase2.adapter.MoviesAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieRequest;

public class TopRatedFragment extends Fragment implements MoviesAdapter.onMovieClickListener, MovieRequest.MovieListListener {

    private static final String TAG = "TopRatedFragment";
    private static final int GRID_COLUMN_SPAN = 2;
    private RecyclerView rv_movies;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<Movie> mMovieList = new ArrayList<>();
    private boolean hasBeenVisibleOnce = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);

        rv_movies = rootView.findViewById(R.id.rv_movies2);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        mMoviesAdapter = new MoviesAdapter(getActivity(), mMovieList, this);
        mGridLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMN_SPAN);
        rv_movies.setAdapter(mMoviesAdapter);
        rv_movies.setLayoutManager(mGridLayoutManager);
    }

    /*Get top rated movies*/
    private void getTopRatedMovies() {
        MovieRequest.getTopRatedMovies(this);
    }

    private void showDetailActivity(final Movie movie) {
        final Resources resources = getResources();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(resources.getString(R.string.selected_movie_key), movie);
        startActivity(intent);
    }

    @Override
    public void onMovieClick(int position) {
        final Movie movie = mMovieList.get(position);
        showDetailActivity(movie);
        Log.d(TAG, "onMovieClick: " + movie.toString());
    }

    /*Set da adapter with the populated list*/
    @Override
    public void onMostPopularListReceivedListener(List<Movie> movieList) {
    }

    @Override
    public void onTopRatedListReceivedListener(List<Movie> movieList) {
        Log.d(TAG, "onTopRatedListReceivedListener: " + movieList.toString());
        mMovieList = movieList;
        mMoviesAdapter.setMovieList(movieList);
    }

    /*Load data in the fragment only after it gets visible to the user*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && !hasBeenVisibleOnce) {
            getTopRatedMovies();
            hasBeenVisibleOnce = true;
        }
    }
}

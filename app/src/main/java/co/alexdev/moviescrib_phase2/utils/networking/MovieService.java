package co.alexdev.moviescrib_phase2.utils.networking;

import co.alexdev.moviescrib_phase2.model.MovieResponse;
import co.alexdev.moviescrib_phase2.model.TrailerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*Endpoints used when we are querying*/
/*GET means that we want to get some data*/
public interface MovieService {
    @GET("movie/popular")
    Call<MovieResponse> popularMovieList();

    @GET("movie/top_rated")
    Call<MovieResponse> topRatedMovieList();

    /*Add id parameter to the request*/
    @GET("movie/{id}/videos ")
    Call<TrailerResponse> movieTrailers(@Path("id") String id);
}
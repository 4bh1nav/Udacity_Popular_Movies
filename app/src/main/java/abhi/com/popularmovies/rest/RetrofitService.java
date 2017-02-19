package abhi.com.popularmovies.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import abhi.com.popularmovies.data.model.Movie;
import abhi.com.popularmovies.data.model.Result;
import abhi.com.popularmovies.data.model.Review;
import abhi.com.popularmovies.data.model.Video;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ron on 17/10/16.
 */

public class RetrofitService {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static movieApiInterface movieApiInterface ;

    public static movieApiInterface getClient() {
        if (movieApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addInterceptor(
                            new Interceptor() {
                                @Override
                                public Response intercept(Interceptor.Chain chain) throws IOException {
                                    Request original = chain.request();

                                    // Request customization: add request headers
                                    Request.Builder requestBuilder = original.newBuilder()
                                            .method(original.method(), original.body())
                                            .addHeader("Accept", "application/json");

                                    Request request = requestBuilder.build();
                                    return chain.proceed(request);
                                }
                            })
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            movieApiInterface = client.create(movieApiInterface.class);
        }
        return movieApiInterface;
    }

    public interface movieApiInterface {

        @GET("movie/popular")
        Call<Result> getPopularMovies (@Query("api_key") String api_key);

        @GET("movie/top_rated")
        Call<Result> getTopRatedMovies (@Query("api_key") String api_key);

        @GET("movie/{id}/videos") Call<Video.Result> getMovieVideos(@Path("id") long movieId, @Query("api_key") String api_key);

        @GET("movie/{id}/reviews") Call<Review.Result> getMovieReviews(@Path("id") long movieId, @Query("api_key") String api_key);

        @GET("movie/{movie_id}") Call<Movie> getMovie(@Path("movie_id") long movieId, @Query("api_key") String api_key);
}


}

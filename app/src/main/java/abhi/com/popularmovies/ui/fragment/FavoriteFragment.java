package abhi.com.popularmovies.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.Favorite;
import abhi.com.popularmovies.data.model.Movie;
import abhi.com.popularmovies.data.model.Review;
import abhi.com.popularmovies.data.model.Video;
import abhi.com.popularmovies.rest.RetrofitService;
import abhi.com.popularmovies.ui.adapter.MovieTrailerAdapter;
import abhi.com.popularmovies.ui.adapter.ReviewsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    private static final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private static final String API_KEY = "" ;

    @BindView(R.id.main_backdrop)
    ImageView posterBackground;
    @BindView(R.id.movie_overview)
    TextView overview;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.user_rating) TextView userRating;
    @BindView(R.id.trailers_recyclerView) RecyclerView trailerRecylerview;
    @BindView(R.id.reviews_recyclerView) RecyclerView reviewRecyclerView;

    @BindView(R.id.favorite_movie_button) FloatingActionButton FavoriteFab;
    private RetrofitService.movieApiInterface mMovieApi;
    private Movie mMovieData;
    private Realm realm;
    private List<Video> trailers;
    private List<Review> reviews;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(Movie movie) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie",movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mMovieApi = RetrofitService.getClient();
        mMovieData = getArguments().getParcelable("movie");

        Realm.init(getContext());

        // Create the Realm instance
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this,view);

        String posterPath = mMovieData.getPosterPath();
        String posterUrl = MOVIE_POSTER_BASE_URL.concat(posterPath);
        Picasso.with(getContext()).load(posterUrl).into(posterBackground);

        overview.setText(mMovieData.getOverview());
        toolbar.setTitle(mMovieData.getTitle());
        userRating.setText(String.valueOf(mMovieData.getVoteAverage()));
        releaseDate.setText(mMovieData.getReleaseDate());

//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        initTrailerRecyclerView();
        loadTrailers(mMovieData.getId(),API_KEY);
        loadReviews(mMovieData.getId(),API_KEY);


        // add back arrow to toolbar
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FavoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build the query looking at all users:
                RealmQuery<Favorite> query = realm.where(Favorite.class);
                final RealmResults<Favorite> fav = query.equalTo("id", mMovieData.getId()).findAll();
                if (fav.size() == 0){
                    Toast toast = Toast.makeText(getContext(),"Saved as a favorite",Toast.LENGTH_SHORT);
                    toast.show();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Favorite favorite =realm.createObject(Favorite.class);
                            favorite.setId(mMovieData.getId());
                            favorite.setTitle(mMovieData.getOriginalTitle());
                            favorite.setReleaseDate(mMovieData.getReleaseDate());
                            favorite.setVoteAverage(mMovieData.getVoteAverage());
                            favorite.setPosterPath(mMovieData.getPosterPath());

                        }
                    });
                }else{
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Favorite> favorite = realm.where(Favorite.class).equalTo("id",mMovieData.getId()).findAll();
                            favorite.deleteAllFromRealm();
                            Toast toast = Toast.makeText(getContext(),"UnSaved",Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });
                }
            }
        });

        return view;
    }

    private void loadTrailers(Integer id, String apiKey) {
        mMovieApi.getMovieVideos(id,apiKey).enqueue(new Callback<Video.Result>() {
            @Override
            public void onResponse(Call<Video.Result> call, Response<Video.Result> response) {
                Video.Result result = response.body();
                trailers = result.getResults();
                MovieTrailerAdapter movieTrailerAdapter = new MovieTrailerAdapter(FavoriteFragment.this,trailers);
                trailerRecylerview.setAdapter(movieTrailerAdapter);
            }

            @Override
            public void onFailure(Call<Video.Result> call, Throwable t) {

            }
        });
    }

    private void loadReviews(Integer id, String apiKey){
        mMovieApi.getMovieReviews(id,apiKey).enqueue(new Callback<Review.Result>() {
            @Override
            public void onResponse(Call<Review.Result> call, Response<Review.Result> response) {
                Review.Result result = response.body();
                reviews = result.getResults();
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(FavoriteFragment.this,reviews);
                reviewRecyclerView.setAdapter(reviewsAdapter);
            }

            @Override
            public void onFailure(Call<Review.Result> call, Throwable t) {

            }
        });
    }

    private void initTrailerRecyclerView() {
        LinearLayoutManager linearLayoutManagerFortrailer = new LinearLayoutManager(getContext());
        linearLayoutManagerFortrailer.setOrientation(GridLayout.VERTICAL);
        trailerRecylerview.setLayoutManager(linearLayoutManagerFortrailer);
        trailerRecylerview.hasFixedSize();

        LinearLayoutManager linearLayoutManagerForreview = new LinearLayoutManager(getContext());
        linearLayoutManagerForreview.setOrientation(GridLayout.VERTICAL);
        reviewRecyclerView.setLayoutManager(linearLayoutManagerForreview);
    }

}

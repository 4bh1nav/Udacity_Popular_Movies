package abhi.com.popularmovies.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.MovieData;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
    private static final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w780";

    @BindView(R.id.main_backdrop) ImageView posterBackground;
    @BindView(R.id.movie_overview) TextView overview;
    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.release_date) TextView releaseDate;
    @BindView(R.id.user_rating) TextView userRating;

    MovieData MovieData;
    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(MovieData data) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putParcelable("MovieData",data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieData = getArguments().getParcelable("MovieData");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this,view);

        String posterPath = MovieData.getPosterPath();
        String posterUrl = MOVIE_POSTER_BASE_URL.concat(posterPath);
        Picasso.with(getContext()).load(posterUrl).into(posterBackground);

        overview.setText(MovieData.getOverview());
        toolbar.setTitle(MovieData.getTitle());
        userRating.setText(String.valueOf(MovieData.getVoteAverage()));
        releaseDate.setText(MovieData.getReleaseDate());

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        return view;
    }


}

package abhi.com.popularmovies.ui.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.MovieData;
import abhi.com.popularmovies.data.model.Result;
import abhi.com.popularmovies.rest.retrofitService;
import abhi.com.popularmovies.ui.adapter.MoviesGridAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment{

    private static final String MOVIESTATE = "Movie_state";
    private retrofitService.movieApiInterface movieApi;
    private String LOAD_MOST_POPULAR= "Most Popular";
    private String LOAD_TOP_RATED= "Top Rated";

    private static final String API_KEY = "" ;
    private MoviesGridAdapter moviesGridAdapter;

    @BindView(R.id.movies_recycler_view) RecyclerView moviesRecyclerView;
    public MovieListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MovieListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieApi = retrofitService.getClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this,view);


        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIESTATE)) {
            loadMovies(LOAD_MOST_POPULAR);

        }
        else {
            List<MovieData> movies = savedInstanceState.getParcelableArrayList(MOVIESTATE);
            moviesGridAdapter = new MoviesGridAdapter(MovieListFragment.this,movies);
        }

        initRecyclerView();
        moviesRecyclerView.setAdapter(moviesGridAdapter);

        return view;
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.hasFixedSize();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_grid_fragment, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.most_popular:
                loadMovies(LOAD_MOST_POPULAR);
                return true;
            case R.id.top_rated:
                loadMovies(LOAD_TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void loadMovies(String sort){

        if (isOnline()){
            if (LOAD_MOST_POPULAR.equals(sort)){
                movieApi.getPopularMovies(API_KEY).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        List<MovieData> movies = result.getResults();
                        moviesGridAdapter = new MoviesGridAdapter(MovieListFragment.this,movies);
                        moviesRecyclerView.setAdapter(moviesGridAdapter);

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                    }
                });
            }else{
                movieApi.getTopRatedMovies(API_KEY).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        List<MovieData> movies = result.getResults();
                        moviesGridAdapter = new MoviesGridAdapter(MovieListFragment.this,movies);
                        moviesRecyclerView.setAdapter(moviesGridAdapter);

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                    }
                });
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            }).setMessage("Check your network connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIESTATE,new ArrayList<Parcelable>(moviesGridAdapter.getItems()));
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

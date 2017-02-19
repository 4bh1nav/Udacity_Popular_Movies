package abhi.com.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.Movie;
import abhi.com.popularmovies.ui.fragment.FavoriteFragment;

public class FavDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favmovie_detail);

        if (savedInstanceState != null) {
            return;
        }

        Movie Movie = getIntent().getParcelableExtra("data");

        FavoriteFragment movieFragment = FavoriteFragment.newInstance(Movie);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.movieFrameLayout,movieFragment).commit();

    }
}

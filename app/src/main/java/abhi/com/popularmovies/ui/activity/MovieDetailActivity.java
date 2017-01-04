package abhi.com.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.MovieData;
import abhi.com.popularmovies.ui.fragment.MovieFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState != null) {
            return;
        }

        MovieData MovieData = getIntent().getParcelableExtra("data");

        MovieFragment movieFragment = MovieFragment.newInstance(MovieData);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.movieFrameLayout,movieFragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package abhi.com.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.ui.fragment.MovieListFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.movieGirdFrameLayout) FrameLayout movieGridFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            return;
        }

        MovieListFragment movieListFragment = MovieListFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.movieGirdFrameLayout,movieListFragment).commit();

    }
}

package abhi.com.popularmovies.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import abhi.com.popularmovies.R;
import abhi.com.popularmovies.data.model.Video;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ron on 08/02/17.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private final Fragment mFragment;
    private final List<Video> mItems;

    public MovieTrailerAdapter(Fragment fragment, List<Video> movies) {
        mFragment = fragment;
        mItems = movies;
    }

    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);

        return new MovieTrailerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.ViewHolder holder, int position) {

        final Video video = mItems.get(position);
        holder.VideoNo.setText(video.getName());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(video.getKey());
            }
        });

    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            mFragment.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mFragment.startActivity(webIntent);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.video_no) TextView VideoNo;
        private final View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);

        }
    }


}

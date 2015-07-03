package com.benjaminearley.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistArrayAdapter extends BaseAdapter {

    private Activity mContext;
    private List<ArtistShort> mArtistShortList;
    private LayoutInflater mLayoutInflater = null;

    public ArtistArrayAdapter(Activity context, List<ArtistShort> list) {
        mContext = context;
        mArtistShortList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mArtistShortList != null) return mArtistShortList.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return mArtistShortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.artist_card, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.artistNameView.setText(mArtistShortList.get(position).getName());
        if (mArtistShortList.get(position).getAlbumUrl() != null) {
            Picasso.with(parent.getContext()).load(mArtistShortList.get(position).getAlbumUrl()).into(viewHolder.artistImageView);
        } else {
            viewHolder.artistImageView.setImageResource(R.drawable.ic_launcher);
        }

        return v;
    }
}

class CompleteListViewHolder {
    public TextView artistNameView;
    public ImageView artistImageView;

    public CompleteListViewHolder(View base) {
        artistNameView = (TextView) base.findViewById(R.id.artist_name);
        artistImageView = (ImageView) base.findViewById(R.id.artist_image);
    }
}


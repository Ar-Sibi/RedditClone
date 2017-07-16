package arsibi_has_no_website.redditclone;

/**
 * Created by hp on 15-07-2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 15-07-2017.
 */

public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.RViewHolder> {
    List<RedditPostItem> items;
    LayoutInflater inflater;
    Context context;

    public RViewAdapter(Context c,List<RedditPostItem> posts) {
        items=posts;
        context=c;
        inflater=LayoutInflater.from(c);
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.listitem,parent,false);
        RViewHolder holder = new RViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        RedditPostItem item = items.get(position);
        holder.timestamp.setText(item.timeStamp);
        holder.body.setText(item.body);
        holder.comments.setText(item.comments);
        holder.layout.setBackgroundColor(Color.parseColor(position%2==0?"#91ffff":"#99eeff" ));
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d("MOO",exception.toString());
            }
        });
        builder.build().load(item.imageurl).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class RViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        TextView comments;
        TextView body;
        TextView timestamp;
        ImageView thumbnail;
        public RViewHolder(View itemView) {
            super(itemView);
            thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
            comments=(TextView)itemView.findViewById(R.id.commentno);
            body=(TextView)itemView.findViewById(R.id.body);
            timestamp=(TextView)itemView.findViewById(R.id.timestamp);
            layout=(RelativeLayout)itemView.findViewById(R.id.colorer);
        }
    }
}

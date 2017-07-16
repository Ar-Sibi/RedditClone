package arsibi_has_no_website.redditclone;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    RViewAdapter adapter;
    ProgressBar bar;
    RelativeLayout layout;
    static boolean isloading=true;
    static List<RedditPostItem> posts;
    Runnable progress= new Runnable() {
        @Override
        public void run() {
        while(isloading) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
            }
        }
            handler.sendEmptyMessage(0);
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            layout.setVisibility(LinearLayout.GONE);
        }
    };
    /*@Override
    protected void onStart() {
        try{
            File file = new File(getCacheDir(),"hi.txt");
            if(!file.exists())
                file.createNewFile();
            else{
                ObjectInputStream ops = new ObjectInputStream(new FileInputStream(file));
                posts=(List<RedditPostItem>)ops.readObject();
            }
        }catch (FileNotFoundException e){}
        catch (IOException e){}
        catch (ClassNotFoundException c){}
        super.onStart();
    }

    @Override
    protected void onStop() {
        try{
            File file = new File(getCacheDir(),"hi.txt");
                file.createNewFile();
            {
                ObjectOutputStream ops = new ObjectOutputStream(new FileOutputStream(file));
                ops.writeObject(posts);
            }
        }catch (FileNotFoundException e){}
        catch (IOException e){}
        super.onStop();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        posts=new ArrayList<>();
        layout = (RelativeLayout)findViewById(R.id.progress_dialog);
        bar =(ProgressBar) findViewById(R.id.progressBar) ;
        rv=(RecyclerView)findViewById(R.id.recycler);
        adapter=new RViewAdapter(getApplicationContext(),posts);
        rv.setLayoutManager(layoutManager);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(layoutManager.findLastCompletelyVisibleItemPosition()==posts.size()-1&&posts.size()!=0&&!isloading){
                    Loader loader= new Loader(posts,adapter);
                    isloading=true;
                    new Thread(progress).start();
                    layout.setVisibility(LinearLayout.VISIBLE);
                    loader.execute();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Loader loader = new Loader(posts,adapter);
        new Thread(progress).start();
        loader.execute();

    }
}

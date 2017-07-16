package arsibi_has_no_website.redditclone;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by hp on 15-07-2017.
 */

public class Loader extends AsyncTask<String,Void,List<RedditPostItem>> {
    List<RedditPostItem> posts;
    static String before="";
    RViewAdapter adapter;
    public Loader(List<RedditPostItem> items,RViewAdapter adapter) {
        posts=items;
        this.adapter=adapter;
    }
    @Override
    protected List<RedditPostItem> doInBackground(String... params) {
        try {
            URL url = new URL("https://www.reddit.com/r/all/top/.json?sort=top&t=all"+(before.equals("")?"":"&after="+before));
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            String processedinput= convertString(connection.getInputStream());
            JSONObject json = new JSONObject(processedinput);
            json=json.getJSONObject("data");
            before=json.getString("after");
            JSONArray jsonarr=json.getJSONArray("children");
            processJSONArray(jsonarr);
            Log.d("MOO",processedinput);
        }catch (MalformedURLException e){
            Log.d("MOO",e.toString());
        }
        catch (IOException e){
            Log.d("MOO",e.toString());
        }catch (JSONException e){
            Log.d("MOO",e.toString());
        }
        return null;
    }
    public void processJSONArray(JSONArray jsonArray) throws JSONException{
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject= jsonArray.getJSONObject(i);
            JSONObject data = jsonObject.getJSONObject("data");
            String title=data.getString("title");
            String image=data.getString("thumbnail");
            Date date = new Date((long)Math.floor(Double.parseDouble(data.getString("created_utc"))*1000L)); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date);
            RedditPostItem item = new RedditPostItem(title,formattedDate,data.getString("num_comments")+" comments",image);
            posts.add(item);
            publishProgress();
        }
    }
    String convertString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        adapter.notifyDataSetChanged();
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<RedditPostItem> redditPostItems) {
        MainActivity.isloading=false;
        super.onPostExecute(redditPostItems);
        adapter.notifyDataSetChanged();
    }
}

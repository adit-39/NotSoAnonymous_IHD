package smartgallery.notsoanonymous.com.smartgallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyGridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ImageItem> data = getData();

        if(data == null){
            setContentView(R.layout.layout_no_galleries);
        }else {
            setContentView(R.layout.activity_main);
            gridView = (MyGridView) findViewById(R.id.gridView);
            gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, data);
            gridView.setAdapter(gridAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("heart", true);
                    startActivity(intent);
                }
            });
        }

//        startService(new Intent(this, FileModificationService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String galleries = pref.getString("galleries", null);

        if(galleries == null){
            pref.edit().putString("galleries", "").apply();
            return null;
        }

        if(galleries.equals("")){
            return null;
        }

        for(String gallery : galleries.split(";")){



            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inScreenDensity = 1;


            Bitmap bitmap = BitmapFactory.decodeFile(gallery.split(":")[1].split(",")[0], options);

            if(!new File(gallery.split(":")[1].split(",")[0]).exists()){
                continue;
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

            imageItems.add(new ImageItem(bitmap, gallery.split(":")[0], gallery.split(":")[1].split(",")[0]));
        }

        return imageItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

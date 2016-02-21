package smartgallery.notsoanonymous.com.smartgallery;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class SearchActivity extends AppCompatActivity {

    protected static final int REQUEST_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FloatingActionButton fabnext = (FloatingActionButton) findViewById(R.id.fabnext);
        fabnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            TextView query = (TextView) findViewById(R.id.query);
            Search s = new Search(SearchActivity.this);
            String result = s.search(s.stopWordCreator(), query.getText().toString()).toString();

            Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
            intent.putExtra("query", query.getText().toString());
            intent.putExtra("galleries", FileModificationService.root + result.substring(1, result.length() - 1).replaceAll("\\, ", "\\," + FileModificationService.root));
            intent.putExtra("heart", false);
            startActivity(intent);
            }
        });

        FloatingActionButton fabvoice = (FloatingActionButton) findViewById(R.id.fabvoice);
        fabvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                try {
                    startActivityForResult(i, REQUEST_OK);
                } catch (Exception e) {}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

//            Toast.makeText(SearchActivity.this, thingsYouSaid.get(0), Toast.LENGTH_LONG).show();

            EditText query = (EditText) findViewById(R.id.query);

            query.setText(thingsYouSaid.get(0));
        }
    }
}

package co.alexdev.moviescrib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MoviesActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        toolbar = findViewById(R.id.toolbar);

        setup();
    }

    //TODO Setup all the content in this function
    private void setup() {
    }
}

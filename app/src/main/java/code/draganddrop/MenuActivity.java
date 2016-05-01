package code.draganddrop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import code.draganddrop.models.GameManagement;

public class MenuActivity extends AppCompatActivity {

    // Game Management instance. Control all the game
    public static GameManagement gameManagement = new GameManagement();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void newGame(View view) {

        // establish attributes for the new game
        gameManagement.newGame();

        // Call Activity
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}

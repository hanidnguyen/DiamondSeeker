package cmpt276.assignment3.diamondseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setMainMenuButton();
    }

    private void setMainMenuButton() {
        Button btn = findViewById(R.id.main_menu_btn);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this,MainMenu.class);
            startActivity(intent);
        });
    }
}
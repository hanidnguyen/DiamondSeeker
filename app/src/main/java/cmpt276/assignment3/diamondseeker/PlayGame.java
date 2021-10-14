package cmpt276.assignment3.diamondseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Random;
/*
    Game class for the Play game activity:
    -   Has six fields: the number of rows/cols/diamonds
                        grid of buttons/diamonds location/scanned buttons
    Game play:
    -   When button is clicked - shows number of diamonds in corresponding row/col
                - if found diamond: update all scanned buttons
 */

public class PlayGame extends AppCompatActivity {

    private static final int NUM_ROWS = 4;
    private static final int NUM_COLS = 5;
    private int diamonds = 5;
    Button[][] buttons = new Button[NUM_ROWS][NUM_COLS];
    Boolean[][] diamonds_location = new Boolean[NUM_ROWS][NUM_COLS];
    Boolean[][] buttons_scanned = new Boolean[NUM_ROWS][NUM_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange, getTheme())));

        if (diamonds > (NUM_ROWS * NUM_COLS)) throw new AssertionError();

        setupBooleanArrays();
        populateDiamonds();
        populateTableOfButtons();
    }

    //setting up the initialized boolean arrays
    private void setupBooleanArrays() {
        for(int row = 0; row < NUM_ROWS; row++){
            for(int col = 0; col < NUM_COLS; col++){
                diamonds_location[row][col] = false;
                buttons_scanned[row][col] = false;
            }
        }
    }

    //random diamond generator correspond to number of diamonds
    private void populateDiamonds() {
        Random random = new Random();

        while(diamonds != 0){
            int random_col = random.nextInt(NUM_COLS);
            int random_row = random.nextInt(NUM_ROWS);
            if(!diamonds_location[random_row][random_col]){
                diamonds_location[random_row][random_col] = true;
                diamonds--;
            }
        }

        for(int row = 0; row < NUM_ROWS;row++){
            for(int col = 0; col < NUM_COLS; col++){
                Log.e("PlayGame.java","row: " + row + ", col: " + col +
                        ", mines: " + diamonds_location[row][col] + "\n");
            }
        }

    }

    //Create table of buttons
    private void populateTableOfButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);

        //Create new rows
        for(int row = 0; row < NUM_ROWS;row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            //Create a button according to number of columns for every row
            for(int col = 0; col < NUM_COLS;col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                //Make text not clip on small buttons
                button.setPadding(0,0,0,0);

                /*OnclickListener:
                 -  record scanned button
                 -  show diamond if found
                 -  refresh grid for every click
                 */
                button.setOnClickListener(view -> {
                    buttons_scanned[FINAL_ROW][FINAL_COL] = true;
                    if(diamonds_location[FINAL_ROW][FINAL_COL]) {
                        showDiamond(FINAL_ROW, FINAL_COL);

                    }
                    refreshGrid();
                });
                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    //Re-setText to buttons to update number of diamonds at corresponding row/col
    private void refreshGrid() {
        for(int row = 0; row < NUM_ROWS; row++){
            for(int col = 0; col < NUM_COLS; col++){
                if(buttons_scanned[row][col]){
                    int diamonds_number = displayNumberOfDiamonds(row,col);
                    Button button = buttons[row][col];
                    button.setText(""+diamonds_number);
                }
            }
        }
    }

    //ROW and COL are given location of button to be scanned
    //Scan the corresponding row and column for diamonds, then
    //return number of diamonds.
    private int displayNumberOfDiamonds(int ROW, int COL) {
        Button button = buttons[ROW][COL];
        int diamonds_number = 0;
        for(int col = 0; col < NUM_COLS; col++){
            if(diamonds_location[ROW][col] && (col != COL)){
                diamonds_number++;
            }
        }

        for(int row = 0; row < NUM_ROWS; row++){
            if(diamonds_location[row][COL] && (row != ROW)){
                diamonds_number++;
            }
        }

        if(diamonds_location[ROW][COL]){
            diamonds_number++;
        }
        return diamonds_number;
    }

    //show diamonds by locking button sizes and scale image to button then setBackground as image
    private void showDiamond(int row, int col) {
        Toast.makeText(this,"button clicked: " + row + "," + col,Toast.LENGTH_SHORT).show();
        Button button = buttons[row][col];

        //Lock Button sizes:
        lockButtonSizes();

        //Scale image to buttons
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.diamond_icon);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap,newWidth,newHeight,true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource,scaledBitmap));

        //set diamond location to be false because diamond is found
        diamonds_location[row][col] = false;

    }

    //set Min and Max height for the button
    private void lockButtonSizes() {
        for(int row = 0; row < NUM_ROWS; row++){
            for(int col = 0; col < NUM_COLS; col++){
                Button button = buttons[row][col];
                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
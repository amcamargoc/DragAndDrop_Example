package code.draganddrop;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import code.draganddrop.models.Bill;
import code.draganddrop.models.CashRegister;
import code.draganddrop.models.ContinueDialogFragment;

public class GameActivity extends AppCompatActivity {


    // Object dragged
    Bill billDragged;

    // Bills manage
    public static CashRegister cashRegister;

    // Elements that can be dragged
    Button bill_1000, bill_2000;
    public static TextView money, toPay;

    // Value of element dragged
    int value_bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize Elements
        bill_1000 = (Button) findViewById(R.id.one_thousand);
        bill_2000 = (Button) findViewById(R.id.two_thousand);

        money = (TextView) findViewById(R.id.money);

        toPay = (TextView) findViewById(R.id.toPay);

        // Button's value
        bill_1000.setTag(1000);
        bill_2000.setTag(2000);


        //Show values
        setValues();

        // Button event for drag
        bill_1000.setOnLongClickListener(longListener);
        bill_2000.setOnLongClickListener(longListener);


        // Element where you can  action drop
        findViewById(R.id.target).setOnDragListener(dragListener);
    }

    public static void setValues() {
        GameActivity.cashRegister = new CashRegister();
        money.setText(String.valueOf(GameActivity.cashRegister.getTotal()));
        toPay.setText(String.valueOf(MenuActivity.gameManagement.getMoneyToPay()));
    }


    /* Drag and Drog LOGIC */

    View.OnLongClickListener longListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {

            ClipData data = ClipData.newPlainText("", "");
            DragShadow dragShadow = new DragShadow(v);
            v.startDrag(data, dragShadow, v, 0);

            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener()
    {
        @Override
        public boolean onDrag(View v, DragEvent event)
        {
            int dragEvent = event.getAction();
            TextView dropText = (TextView) v;

            switch(dragEvent)
            {
                case DragEvent.ACTION_DRAG_ENTERED:
                    dropText.setTextColor(Color.GREEN);
                    Log.d("DEBUG", "Entroo");
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    dropText.setTextColor(Color.BLACK);
                    Log.d("DEBUG", "SAlio");
                    break;

                case DragEvent.ACTION_DROP:
                    dropText.setTextColor(Color.BLACK);
                    //TextView draggedText = (TextView)event.getLocalState();
                    //dropText.setText(draggedText.getText());

                    // The bill is dropped in accepted area. instance bill and save in cash register
                    billDragged = new Bill(value_bill);
                    cashRegister.addBill(billDragged);

                    // Update total money
                    money.setText(cashRegister.getTotal() + "");

                    // Ask if the player win, lost or continue playing
                    if (MenuActivity.gameManagement.gameStatus(cashRegister) == 1) {
                        // WIN!!!!!!!

                        Toast.makeText(getApplicationContext(), "You WIN!", Toast.LENGTH_SHORT).show();

                        // Menu for reset game
                        ContinueDialogFragment continueDialogFragment = new ContinueDialogFragment();
                        continueDialogFragment.setvalues("Has Ganado!", "Seguir jugando", GameActivity.this);
                        continueDialogFragment.show(getSupportFragmentManager(), "");

                        // Reset Menu
                        //MenuActivity.gameManagement.newGame();
                        //setValues();


                    } else if (MenuActivity.gameManagement.gameStatus(cashRegister) == -1){
                        // LOST!!
                        Toast.makeText(getApplicationContext(), "You Lost!", Toast.LENGTH_SHORT).show();


                        // Menu for reset game
                        ContinueDialogFragment continueDialogFragment = new ContinueDialogFragment();
                        continueDialogFragment.setvalues("Has perdido :(", "Volver a intentar", GameActivity.this);
                        continueDialogFragment.show(getSupportFragmentManager(), "");

                        //MenuActivity.gameManagement.newGame();
                        //setValues();

                    }

                    // logs
                    Log.d("MONEY SAVE!", cashRegister.getTotal() + "");
                    Log.d("DEBUG", "Soltooooo");

                    break;
            }

            return true;
        }

    };




    private class DragShadow extends View.DragShadowBuilder {
        ColorDrawable greyBox;

        public DragShadow(View view) {
            super(view);
            greyBox =  new ColorDrawable(Color.RED);
            Log.d("BILL",  view.getTag() + "");
            value_bill = (Integer) view.getTag();
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            greyBox.draw(canvas);
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            View v = getView();

            int height = v.getHeight();
            int width = v.getWidth();


            greyBox.setBounds(0, 0, width, height);

            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

            shadowSize.set(width, height);
            shadowTouchPoint.set(width, height);
        }
    }

    // End Drag and Drog Logic


    // DIALOG




}

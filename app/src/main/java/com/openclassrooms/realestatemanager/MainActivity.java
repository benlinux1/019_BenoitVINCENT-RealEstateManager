package com.openclassrooms.realestatemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Update activity layout name for textView, from second to main
        // this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();
    }

    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        // int quantity = Utils.convertDollarToEuro(100);   Must be a string to be readable
        String quantity = String.valueOf(Utils.convertDollarToEuro(100));
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(quantity);
    }
}

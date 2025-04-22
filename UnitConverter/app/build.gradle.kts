package com.example.unitconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner fromUnit, toUnit;
    private TextView resultView;
    private Button convertButton;
    private Switch themeSwitch;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        inputValue = findViewById(R.id.inputValue);
        fromUnit = findViewById(R.id.fromUnit);
        toUnit = findViewById(R.id.toUnit);
        resultView = findViewById(R.id.resultView);
        convertButton = findViewById(R.id.convertButton);
        themeSwitch = findViewById(R.id.themeSwitch);

        themeSwitch.setChecked(isDarkMode);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Updated list of supported units
        String[] units = {"Meters", "Kilometers", "Feet", "Inches", "Yards", "Centimeters"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        fromUnit.setAdapter(adapter);
        toUnit.setAdapter(adapter);

        convertButton.setOnClickListener(v -> {
            String inputText = inputValue.getText().toString();

            if (!inputText.isEmpty()) {
                double value = Double.parseDouble(inputText);
                String from = fromUnit.getSelectedItem().toString();
                String to = toUnit.getSelectedItem().toString();

                double convertedValue = convertUnits(value, from, to);
                resultView.setText(String.format("%.4f %s", convertedValue, to));
            } else {
                resultView.setText("Please enter a value.");
            }
        });
    }

    // Updated conversion logic with Inches, Yards, and Centimeters
    private double convertUnits(double value, String from, String to) {
        // Convert input value to meters
        switch (from) {
            case "Kilometers":
                value *= 1000;
                break;
            case "Feet":
                value *= 0.3048;
                break;
            case "Inches":
                value *= 0.0254;
                break;
            case "Yards":
                value *= 0.9144;
                break;
            case "Centimeters":
                value *= 0.01;
                break;
        }

        // Convert meters to target unit
        switch (to) {
            case "Kilometers":
                value /= 1000;
                break;
            case "Feet":
                value /= 0.3048;
                break;
            case "Inches":
                value /= 0.0254;
                break;
            case "Yards":
                value /= 0.9144;
                break;
            case "Centimeters":
                value /= 0.01;
                break;
        }

        return value;
    }
}

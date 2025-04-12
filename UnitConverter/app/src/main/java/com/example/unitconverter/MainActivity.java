package com.example.unitconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner fromUnit, toUnit;
    private TextView resultView;
    private SwitchCompat themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme preference
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isNight = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        fromUnit = findViewById(R.id.fromUnit);
        toUnit = findViewById(R.id.toUnit);
        resultView = findViewById(R.id.resultView);
        themeSwitch = findViewById(R.id.themeSwitch);
        Button convertButton = findViewById(R.id.convertButton);

        themeSwitch.setChecked(isNight);

        String[] units = {"Kilometers", "Meters", "Centimeters", "Feet", "Inches", "Yards"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnit.setAdapter(adapter);
        toUnit.setAdapter(adapter);

        convertButton.setOnClickListener(v -> {
            String input = inputValue.getText().toString();
            if (!input.isEmpty()) {
                double value = Double.parseDouble(input);
                String from = fromUnit.getSelectedItem().toString();
                String to = toUnit.getSelectedItem().toString();

                double result = convert(value, from, to);
                resultView.setText(String.format(Locale.getDefault(), "%.2f %s", result, to));
            } else {
                resultView.setText(R.string.enter_value_warning);
            }
        });

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            recreate(); // restart activity to apply
        });
    }

    private double convert(double value, String from, String to) {
        double inMeters;
        switch (from) {
            case "Kilometers": inMeters = value * 1000; break;
            case "Centimeters": inMeters = value / 100; break;
            case "Feet": inMeters = value * 0.3048; break;
            case "Inches": inMeters = value * 0.0254; break;
            case "Yards": inMeters = value * 0.9144; break;
            case "Meters":
            default: inMeters = value; break;
        }

        switch (to) {
            case "Kilometers": return inMeters / 1000;
            case "Centimeters": return inMeters * 100;
            case "Feet": return inMeters / 0.3048;
            case "Inches": return inMeters / 0.0254;
            case "Yards": return inMeters / 0.9144;
            case "Meters":
            default: return inMeters;
        }
    }
}

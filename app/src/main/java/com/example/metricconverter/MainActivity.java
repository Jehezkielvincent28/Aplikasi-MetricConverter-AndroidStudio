package com.example.metricconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView metrikAutoCompleteTextView;
    private AutoCompleteTextView satuan1AutoCompleteTextView;
    private AutoCompleteTextView satuan2AutoCompleteTextView;
    private TextInputLayout satuan1TextInputLayout;
    private TextInputLayout satuan2TextInputLayout;
    private EditText nilaiAwalEditText;
    private TextView hasilTextView;
    private LinearLayout rootLayout;

    private String[] daftarMetrik;
    private String satuan1 = "";
    private String satuan2 = "";

    private void initComponents() {
        metrikAutoCompleteTextView = findViewById(R.id.metrikAutoCompleteTextView);
        satuan1AutoCompleteTextView = findViewById(R.id.satuan1AutoCompleteTextView);
        satuan2AutoCompleteTextView = findViewById(R.id.satuan2AutoCompleteTextView);
        nilaiAwalEditText = findViewById(R.id.nilaiAwalEditText);
        hasilTextView = findViewById(R.id.hasilTextView);
        satuan1TextInputLayout = findViewById(R.id.satuan1TextInputLayout);
        satuan2TextInputLayout = findViewById(R.id.satuan2TextInputLayout);
        rootLayout = findViewById(R.id.rootLayout);
    }

    private void initListener() {
        rootLayout.setOnClickListener(view -> {
            if (getCurrentFocus() != null) {
                getCurrentFocus().clearFocus();
            }
        });

        metrikAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = parent.getItemAtPosition(position).toString();
            if (selectedItem.equals(daftarMetrik[0])) setSatuanAdapter(R.array.select_panjang_item);
            if (selectedItem.equals(daftarMetrik[1])) setSatuanAdapter(R.array.select_massa_item);
            if (selectedItem.equals(daftarMetrik[2])) setSatuanAdapter(R.array.select_waktu_item);
            if (selectedItem.equals(daftarMetrik[3])) setSatuanAdapter(R.array.select_arus_listrik_item);
            if (selectedItem.equals(daftarMetrik[4])) setSatuanAdapter(R.array.select_suhu_item);
            if (selectedItem.equals(daftarMetrik[5])) setSatuanAdapter(R.array.select_intensitas_cahaya_item);
            if (selectedItem.equals(daftarMetrik[6])) setSatuanAdapter(R.array.select_jumlah_zat_item);

            metrikAutoCompleteTextView.clearFocus();
            resetAllInput();
            toggleSatuanSelect(true);
        });

        satuan1AutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            satuan1AutoCompleteTextView.clearFocus();
            satuan1 = parent.getItemAtPosition(position).toString();
            if (!satuan2.isEmpty() && !nilaiAwalEditText.getText().toString().isEmpty()) {
                convertMetrik(Double.parseDouble(nilaiAwalEditText.getText().toString()));
            }
        });

        satuan2AutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            satuan2AutoCompleteTextView.clearFocus();
            satuan2 = parent.getItemAtPosition(position).toString();
            if (!satuan1.isEmpty() && !nilaiAwalEditText.getText().toString().isEmpty()) {
                convertMetrik(Double.parseDouble(nilaiAwalEditText.getText().toString()));
            }
        });

        nilaiAwalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty() || s.toString().equals("0")) {
                    hasilTextView.setVisibility(View.GONE);
                    return;
                }
                if (satuan1.isEmpty() || satuan2.isEmpty()) return;
                convertMetrik(Double.parseDouble(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void resetAllInput() {
        satuan1AutoCompleteTextView.setText("");
        satuan2AutoCompleteTextView.setText("");
        nilaiAwalEditText.setText("");
        hasilTextView.setText("");
        hasilTextView.setVisibility(View.GONE);
    }

    private void convertMetrik(double nilaiAwal) {
        if (satuan1.equals(satuan2)) {
            setHasil(nilaiAwal, "");
            return;
        }
        if (satuan1.equals("Meter") && satuan2.equals("Kilometer")) setHasil(nilaiAwal / 1000, "KM");
        else if (satuan1.equals("Kilometer") && satuan2.equals("Meter")) setHasil(nilaiAwal * 1000, "M");
        else if (satuan1.equals("Gram") && satuan2.equals("Kilogram")) setHasil(nilaiAwal / 1000, "KG");
        else if (satuan1.equals("Kilogram") && satuan2.equals("Gram")) setHasil(nilaiAwal * 1000, "G");
        else if (satuan1.equals("Sekon") && satuan2.equals("Menit")) setHasil(nilaiAwal / 60, "MIN");
        else if (satuan1.equals("Menit") && satuan2.equals("Sekon")) setHasil(nilaiAwal * 60, "S");
        else if (satuan1.equals("Ampere") && satuan2.equals("Nanoampere")) setHasil(nilaiAwal * 1000000000, "NA");
        else if (satuan1.equals("Nanoampere") && satuan2.equals("Ampere")) setHasil(nilaiAwal / 1000000000, "A");
        else if (satuan1.equals("Celcius") && satuan2.equals("Fahrenheit")) setHasil((nilaiAwal * 9 / 5) + 32, "F");
        else if (satuan1.equals("Fahrenheit") && satuan2.equals("Celcius")) setHasil((nilaiAwal - 32) * 5 / 9, "C");
        else if (satuan1.equals("Candela") && satuan2.equals("Lumen")) setHasil(nilaiAwal * 12.56637, "LM");
        else if (satuan1.equals("Lumen") && satuan2.equals("Candela")) setHasil(nilaiAwal / 12.56637, "CD");
        else if (satuan1.equals("Mole") && satuan2.equals("Kilomole")) setHasil(nilaiAwal / 1000, "KMOL");
        else if (satuan1.equals("Kilomole") && satuan2.equals("Mole")) setHasil(nilaiAwal * 1000, "MOL");
    }

    private void setHasil(double res, String prefix) {
        hasilTextView.setText(String.format("%s %s", Double.toString(res).replaceAll("0*$", "").replaceAll("\\.$", ""), prefix));
        hasilTextView.setVisibility(View.VISIBLE);
    }

    private void setSatuanAdapter(int arrStringRes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_select_item, getResources().getStringArray(arrStringRes));
        satuan1AutoCompleteTextView.setAdapter(adapter);
        satuan2AutoCompleteTextView.setAdapter(adapter);
    }

    private void initMetrikDropdownAdapterAndListener() {
        daftarMetrik = getResources().getStringArray(R.array.daftar_metrik);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_select_item, daftarMetrik);
        metrikAutoCompleteTextView.setAdapter(adapter);
    }

    private void toggleSatuanSelect(boolean state) {
        satuan1TextInputLayout.setEnabled(state);
        satuan2TextInputLayout.setEnabled(state);
        nilaiAwalEditText.setEnabled(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initListener();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("By. ");
        }

        initMetrikDropdownAdapterAndListener();
        toggleSatuanSelect(false);
    }
}
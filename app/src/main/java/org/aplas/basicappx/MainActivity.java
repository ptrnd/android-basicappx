package org.aplas.basicappx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private AlertDialog startDialog;
    private Distance dist;
    private Weight weight;
    private Temperature temp;
    private Button convertBtn;
    private EditText inputTxt;
    private EditText outputTxt;
    private Spinner unitOri;
    private Spinner unitConv;
    private RadioGroup unitType;
    private CheckBox roundBox;
    private CheckBox formBox;
    private ImageView imgView;
    private ImageView imgFormula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convertBtn = (Button)findViewById(R.id.convertButton);
        inputTxt = (EditText)findViewById(R.id.inputText);
        outputTxt = (EditText)findViewById(R.id.outputText);
        unitOri = (Spinner)findViewById(R.id.oriList);
        unitConv = (Spinner)findViewById(R.id.convList);
        unitType = (RadioGroup)findViewById(R.id.radioGroup);
        roundBox = (CheckBox) findViewById(R.id.chkRounded);
        formBox = (CheckBox) findViewById(R.id.chkFormula);
        imgView = (ImageView) findViewById(R.id.img);
        imgFormula = (ImageView) findViewById(R.id.imgFormula);

        unitType.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        RadioButton rbChecked = (RadioButton)findViewById(checkedId);
                        String checked = rbChecked.getText().toString();

                        if (checked.equals("Distance")){
                            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(unitType.getContext(),
                                    R.array.distList, android.R.layout.simple_spinner_item);
                            imgView.setImageResource(R.drawable.distance);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            unitOri.setAdapter(arrayAdapter);
                            unitConv.setAdapter(arrayAdapter);
                        } else if (checked.equals("Temperature")){
                            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(unitType.getContext(),
                                    R.array.tempList, android.R.layout.simple_spinner_item);
                            imgView.setImageResource(R.drawable.temperature);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            unitOri.setAdapter(arrayAdapter);
                            unitConv.setAdapter(arrayAdapter);
                        } else if (checked.equals("Weight")){
                            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(unitType.getContext(),
                                    R.array.weightList, android.R.layout.simple_spinner_item);
                            imgView.setImageResource(R.drawable.weight);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            unitOri.setAdapter(arrayAdapter);
                            unitConv.setAdapter(arrayAdapter);
                        }

                        inputTxt.setText("0");
                        outputTxt.setText("0");
                    }
                }
        );

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doConvert();
            }
        });

        unitOri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doConvert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        unitConv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doConvert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        roundBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doConvert();
            }
        });

        formBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    imgFormula.setVisibility(View.VISIBLE);
                } else {
                    imgFormula.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startDialog = new AlertDialog.Builder(MainActivity.this).create();
        startDialog.setTitle("Application started");
        startDialog.setMessage("This app can use to convert any units");
        startDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        startDialog.show();
    }

    public MainActivity(){
        this.dist = new Distance();
        this.temp = new Temperature();
        this.weight = new Weight();
    }

    public MainActivity(Distance dist, Temperature temp, Weight weight){
        this.dist = dist;
        this.temp = temp;
        this.weight = weight;
    }

    protected double convertUnit(String type, String oriUnit, String convUnit, double value){
        if (type.equals("Distance")){
            return dist.convert(oriUnit, convUnit, value);
        } else if (type.equals("Temperature")){
            return temp.convert(oriUnit, convUnit, value);
        } else{
            return weight.convert(oriUnit, convUnit, value);
        }
    }

    protected String strResult(double val, boolean rounded){
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat df2 = new DecimalFormat("#.#####");
        if (rounded){
            return df.format(val);
        } else {
            return df2.format(val);
        }
    }

    protected void doConvert(){
        RadioButton rbChecked = (RadioButton)findViewById(unitType.getCheckedRadioButtonId());
        String typeChecked = rbChecked.getText().toString();

        unitOri = (Spinner)findViewById(R.id.oriList);
        String textOri = unitOri.getSelectedItem().toString();
        unitConv = (Spinner)findViewById(R.id.convList);
        String textConv = unitConv.getSelectedItem().toString();

        double value = Double.parseDouble(String.valueOf(inputTxt.getText()));
        double result = convertUnit(typeChecked, textOri, textConv, value);
        String resultText = strResult(result, roundBox.isChecked());

        outputTxt.setText(resultText);
    }
}
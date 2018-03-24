package com.example.team2.medicineguru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import medicineguru.dto.Medicine;

public class InsertMedicine extends AppCompatActivity {

    private ImageView medicineImage;
    private TextView etPath;
    private EditText name, title, description, size, symptom, dose_amount;
    private Spinner colorSpinner, formSpinner, unitSpinner;

    private static final int IMG_REQUEST_CODE = 13;
    private Uri filePath;
    private Bitmap bitmap;
    Medicine medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_medicine);
        medicineImage = findViewById(R.id.med_img);
       // etPath = findViewById(R.id.etPath);
        name = findViewById(R.id.nameTxt);
        title = findViewById(R.id.titletxt);
        description = findViewById(R.id.descriptionTxt);
        size = findViewById(R.id.sizeTxt);
       // symptom = findViewById(R.id.symptomTxt);
        dose_amount = findViewById(R.id.dosageAmountTxt);
        colorSpinner = findViewById(R.id.colorSpinner);
        formSpinner = findViewById(R.id.formSpinner);
        unitSpinner = findViewById(R.id.unitSpinner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filePath = data.getData();
            //String[] proj = {MediaStore.Images.Media.DATA};
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                medicineImage.setImageBitmap(bitmap);
                String path = filePath.getPath();
                /*Cursor cursor = getContentResolver().query(filePath,proj,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(proj[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();*/
                etPath.setText(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void chooseImage(View view)
    {
        Intent it = new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, "Select Image"),IMG_REQUEST_CODE);
    }

    public void insertMedicineInDatabase(View view)
    {
        String nameOfMed;
        String titleOfMed;
        String descriptionOfMed;
        String symptomAssociated;
        String color;
        String form;
        String dose;
        String path;
        String sizeMed;
        int sizeOfMed;
        nameOfMed = name.getText().toString();
        titleOfMed = title.getText().toString();
        descriptionOfMed = description.toString();
        sizeMed = size.getText().toString();
        sizeOfMed = Integer.parseInt(sizeMed);
        symptomAssociated = symptom.getText().toString();
        color = colorSpinner.getSelectedItem().toString();
        form = formSpinner.getSelectedItem().toString();
        dose = dose_amount+unitSpinner.getSelectedItem().toString();
        path = etPath.getText().toString();

        // medicine = new Medicine(nameOfMed,titleOfMed,descriptionOfMed,sizeOfMed,color,symptomAssociated,path, dose, form);
    }
}

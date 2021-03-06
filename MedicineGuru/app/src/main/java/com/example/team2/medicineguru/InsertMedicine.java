package com.example.team2.medicineguru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.dto.Dose;
import medicineguru.dto.Medicine;
import medicineguru.dto.Symptom;

public class InsertMedicine extends AppCompatActivity {

    private ImageView medicineImage;
    private TextView etPath;
    private EditText name, title, description, size, symptom, dose_amount,priceEdt;
    private Spinner colorSpinner, formSpinner, unitSpinner,prescrptionSpinner;

    private static final int IMG_REQUEST_CODE = 13;
    private Uri filePath;
    private Bitmap bitmap;
    Medicine medicine;


    FireBaseDatabaseHandler db;
    final String[] symptoms = {"Cold", "Cough", "Fever", "Anxiety", "Headache", "Drowsiness","Congestion","Body-ache", "Constipation","High Blood Pressure"
            ,"Agitation","Nausea","Confusion","Dizziness","Poor Coordination","Slowed Breathing","High Body Temperature","Diarrhea"};
    AutoCompleteTextView atSymp;
    TextView sympTxt;
    String symptomList="";
    List<Symptom> symptomsOfMedicine;



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
        priceEdt = findViewById(R.id.priceTxt);
        prescrptionSpinner = findViewById(R.id.prescriptionSpinner);

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

    public void uploadMedicineImage(){

        try{
            StorageReference imageRef=db.getStorageRefernce().child("images/"+ UUID.randomUUID().toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Error occured while uploading image",
                            Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                   String imgPath = taskSnapshot.getDownloadUrl().toString();
                    boolean uploaded=false;
                    try {
                        Dose dose1=new Dose(Integer.parseInt(dose_amount.getText().toString()),unitSpinner.getSelectedItem().toString());
                        List<String> imageList=new ArrayList<String>();
                        imageList.add(imgPath);

                        //List<Image> to be changed to Image.

                        medicine = new Medicine(name.getText().toString(),title.getText().toString(), description.getText().toString(),Integer.parseInt(size.getText().toString()),colorSpinner.getSelectedItem().toString(),symptomsOfMedicine,imageList,dose1,formSpinner.getSelectedItem().toString(),Double.parseDouble(priceEdt.getText().toString()),prescrptionSpinner.getSelectedItem().toString());
                        db.createMedicine(medicine);

                        uploaded=true;
                    }
                    catch (Exception e)
                    {
                        uploaded=false;
                    }
                    if(uploaded)
                    {
                        Toast.makeText(getApplicationContext(), "Medicine uploaded sucessfully",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error occured while uploading medicine",
                                Toast.LENGTH_LONG).show();
                    }


                }
            });
        }catch (Exception e){
            Log.d("Error",e.toString());
        }


    }

    public void insertMedicineInDatabase()
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
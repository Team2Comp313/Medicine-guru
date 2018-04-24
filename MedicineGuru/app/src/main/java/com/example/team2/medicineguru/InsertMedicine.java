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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Console;

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
    private TextView etPath,symptomtxt;
    private EditText name, title, description, size,price, symptom, dose_amount;
    private Spinner colorSpinner, formSpinner, unitSpinner,requirePrescriptionSpinner;
    String imgPath;

    private static final int IMG_REQUEST_CODE = 13;
    private Uri filePath;
    private Bitmap bitmap;
    Medicine medicine;
    Intent Imagedata;


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
        name = findViewById(R.id.nameTxt);
        title = findViewById(R.id.titletxt);
        description = findViewById(R.id.descriptionTxt);
        size = findViewById(R.id.sizeTxt);
        db=new FireBaseDatabaseHandler();
        setContentView(R.layout.activity_insert_medicine);
        medicineImage = findViewById(R.id.med_img);
        dose_amount = findViewById(R.id.dosageAmountTxt);
        colorSpinner = findViewById(R.id.colorSpinner);
        formSpinner = findViewById(R.id.formSpinner);
        unitSpinner = findViewById(R.id.unitSpinner);
        price = findViewById(R.id.priceTxt);
        requirePrescriptionSpinner = findViewById(R.id.prescriptionSpinner);
        symptomsOfMedicine=new ArrayList<Symptom>();
        symptomtxt=findViewById(R.id.symptomtxt);

        atSymp = findViewById(R.id.atSymptoms);
        atSymp.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, symptoms));
        atSymp.setThreshold(1);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.insert_medicine, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        insertMedicineInDatabase();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                medicineImage.setImageBitmap(bitmap);
                String path = filePath.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void add_symptoms(View view)
    {
        Symptom s = new Symptom(atSymp.getText().toString());
        String sy=symptomtxt.getText().toString();
        symptomsOfMedicine.add(s);
        atSymp.setText("");
        symptomtxt.setText(sy+s.getName()+',');
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
                   String imgPath = taskSnapshot.getDownloadUrl().toString();
                    boolean uploaded=false;
                    try {
                        Dose dose1=new Dose(Integer.parseInt(dose_amount.getText().toString()),unitSpinner.getSelectedItem().toString());
                        List<String> imageList=new ArrayList<String>();
                        imageList.add(imgPath);

                        //List<Image> to be changed to Image.

                        medicine = new Medicine(name.getText().toString(),title.getText().toString(), description.getText().toString(),Integer.parseInt(size.getText().toString()),colorSpinner.getSelectedItem().toString(),symptomsOfMedicine,imageList,dose1,formSpinner.getSelectedItem().toString(),Double.parseDouble(price.getText().toString()),requirePrescriptionSpinner.getSelectedItem().toString());
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
        uploadMedicineImage();
    }
}
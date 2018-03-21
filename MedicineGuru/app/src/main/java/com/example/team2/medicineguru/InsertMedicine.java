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
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import medicineguru.databasehandler.FireBaseDatabaseHandler;
import medicineguru.dto.Dose;
import medicineguru.dto.Image;
import medicineguru.dto.Medicine;
import medicineguru.dto.Symptom;

public class InsertMedicine extends AppCompatActivity {

    private ImageView medicineImage;
    private EditText name, title, description, size, dose_amount, price;
    private Spinner colorSpinner, formSpinner, unitSpinner;
    Uri imgPath;
    private static final int IMG_REQUEST_CODE = 13;
    private Uri filePath;
    private Bitmap bitmap;
    Medicine medicine;
    FireBaseDatabaseHandler db;
    final String[] symptoms = {"Cold", "Cough", "Fever", "Anxiety", "Headache", "Drowsiness", "Constipation","High Blood Pressure"
            ,"Agitation","Nausea","Body Ache","Neck pain","Confusion","Dizziness","Poor Coordination","Slowed Breathing","High Body Temperature","Diarrhea"};
    AutoCompleteTextView atSymp;
    TextView sympTxt;
    String symptomList="";
    List<Symptom> symptomsOfMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new FireBaseDatabaseHandler();
        setContentView(R.layout.activity_insert_medicine);
        medicineImage = findViewById(R.id.med_img);
        name = findViewById(R.id.nameTxt);
        title = findViewById(R.id.titletxt);
        description = findViewById(R.id.descriptionTxt);
        size = findViewById(R.id.sizeTxt);
        price = findViewById(R.id.priceTxt);
        dose_amount = findViewById(R.id.dosageAmountTxt);
        colorSpinner = findViewById(R.id.colorSpinner);
        formSpinner = findViewById(R.id.formSpinner);
        unitSpinner = findViewById(R.id.unitSpinner);
        atSymp = findViewById(R.id.atSymptoms);
        atSymp.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, symptoms));
        atSymp.setThreshold(1);
    }

    public void add_symptoms(View view)
    {
        String symptom = atSymp.getText().toString();
        atSymp.setText("");
        symptomList = symptomList+","+symptom;
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
                StorageReference imageRef=db.getStorageRefernce().child("images/"+ UUID.randomUUID().toString()+"."+data.getType());
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(imageData);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        imgPath = taskSnapshot.getDownloadUrl();
                    }
                });
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
        String color;
        String form;
        Dose dose;
        String sizeMed;
        int sizeOfMed;
        String priceMed;
        int priceOfMed;
        String doseQuantity;
        int quantityOfDose;
        String unit;
        nameOfMed = name.getText().toString();
        titleOfMed = title.getText().toString();
        descriptionOfMed = description.toString();
        sizeMed = size.getText().toString();
        sizeOfMed = Integer.parseInt(sizeMed);
        priceMed = price.getText().toString();
        priceOfMed = Integer.parseInt(priceMed);
        color = colorSpinner.getSelectedItem().toString();
        form = formSpinner.getSelectedItem().toString();
        doseQuantity = dose_amount.getText().toString();
        quantityOfDose = Integer.parseInt(doseQuantity);
        unit = unitSpinner.getSelectedItem().toString();
        dose = new Dose(quantityOfDose, unit);

        List<String> symps = Arrays.asList(symptomList.split("\\s*,\\s*"));

        for (int i=0; i<symps.size(); i++)
        {
            Symptom s = new Symptom(symps.get(i));
            symptomsOfMedicine.add(s);
        }
        List<Uri> imageList=new ArrayList<Uri>();
        imageList.add(imgPath);
        //List<Image> to be changed to Image.
         medicine = new Medicine(nameOfMed,titleOfMed,descriptionOfMed,sizeOfMed,color,symptomsOfMedicine, dose, form, priceOfMed, imageList);
        db.createMedicine(medicine);

    }
}

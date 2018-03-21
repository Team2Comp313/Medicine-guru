package medicineguru.databasehandler;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import medicineguru.dto.Medicine;
import medicineguru.dto.Order;

/**
 * Created by Brinder Saini on 10/02/2018.
 */

public class FireBaseDatabaseHandler {
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String medicineId;
    private String orderId;
    private List<Medicine> medicineList;
    FirebaseStorage storage;
    public FireBaseDatabaseHandler()
    {
        storage=FirebaseStorage.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
    }
    public StorageReference getStorageRefernce(){
        return storage.getReference();
    }
    public DatabaseReference getNodeReference(String nodeName)
    {
        mDatabase = mFirebaseInstance.getReference(nodeName);

        return mDatabase;
    }
    public void createMedicine(Medicine medicine)
    {
        getNodeReference("Medicine");
        if (TextUtils.isEmpty(medicineId)) {
            medicineId = mDatabase.push().getKey();
        }
        medicine.setMedId(medicineId);
        mDatabase.child(medicineId).setValue(medicine);
    }
    public void createNewOrder(Order order)
    {
        getNodeReference("Orders");
        if (TextUtils.isEmpty(orderId)) {
            orderId = mDatabase.push().getKey();
        }
        order.setOrderId(orderId);
        mDatabase.child(orderId).setValue(order);
    }
    public List<Medicine> getMedicines(){
        initializeMedicineListener();
        return medicineList;
    }
    public List<Medicine> getMedicines(String type,String value){
        initializeMedicineListener(type,value);
        return medicineList;
    }
    public List<Medicine> getMedicines(String node,String type,String value){
        initializeMedicineListener(node,type,value);
        return medicineList;
    }
    public void initializeMedicineListener(String type,String value){
        mDatabase=mFirebaseInstance.getReference();
        Query query = mDatabase.child("Medicine").orderByChild(type).equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicineList=new ArrayList<Medicine>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot medicine : dataSnapshot.getChildren()) {
                        Medicine data = medicine.getValue(Medicine.class);
                        medicineList .add(data);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void initializeMedicineListener(String node,String type,String value){
        mDatabase=mFirebaseInstance.getReference();
        Query query = mDatabase.child("Medicine").child("symptoms").orderByChild("name").equalTo("Neck pain");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicineList=new ArrayList<Medicine>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot medicine : dataSnapshot.getChildren()) {
                        Medicine data = medicine.getValue(Medicine.class);
                        medicineList .add(data);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void initializeMedicineListener()
    {
        mDatabase = mFirebaseInstance.getReference("Medicine");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                medicineList=new ArrayList<Medicine>();
                for (DataSnapshot medicine: dataSnapshot.getChildren()){
                    Medicine data = medicine.getValue(Medicine.class);
                    medicineList .add(data);
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });

    }
}

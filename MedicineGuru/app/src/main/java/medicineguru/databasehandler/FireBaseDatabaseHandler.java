package medicineguru.databasehandler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import medicineguru.dto.Medicine;
import medicineguru.dto.Medicines;

/**
 * Created by Brinder Saini on 10/02/2018.
 */

public class FireBaseDatabaseHandler {
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String medicineId;
    private List<Medicine> medicineList;
    public FireBaseDatabaseHandler()
    {
        initializeMedicineListener();
        mFirebaseInstance = FirebaseDatabase.getInstance();
    }
    public void getNodeReference(String nodeName)
    {
        mDatabase = mFirebaseInstance.getReference(nodeName);
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
    public List<Medicine> getAllMedicines(){
        return medicineList;
    }
    public void initializeMedicineListener()
    {
        mDatabase = mFirebaseInstance.getReference("Medicine");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Medicine data = noteSnapshot.getValue(Medicine.class);
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

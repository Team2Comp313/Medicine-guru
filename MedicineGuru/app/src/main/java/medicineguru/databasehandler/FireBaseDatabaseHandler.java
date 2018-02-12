package medicineguru.databasehandler;
import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import medicineguru.dto.Medicine;

/**
 * Created by Brinder Saini on 10/02/2018.
 */

public class FireBaseDatabaseHandler {
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String medicineId;
    public FireBaseDatabaseHandler()
    {

    }
    public DatabaseReference getNodeReference(String nodeName)
    {
        return FirebaseDatabase.getInstance().getReference(nodeName);
    }
    public void createMedicine(Medicine medicine)
    {
        mDatabase=getNodeReference("Medicine");
        if (TextUtils.isEmpty(medicineId)) {
            medicineId = mDatabase.push().getKey();
        }
        mDatabase.child(medicineId).setValue(medicine);
    }
}

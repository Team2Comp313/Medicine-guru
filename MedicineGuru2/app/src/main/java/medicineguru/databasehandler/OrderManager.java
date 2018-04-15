package medicineguru.databasehandler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import medicineguru.dto.Order;
import medicineguru.dto.ShoppingCart;
import medicineguru.dto.ShoppingCartItem;

/**
 * Created by Brinder Saini on 14/04/2018.
 */

public class OrderManager {
    public Order order;
    private  String userId;
    private int quantity=0;
    private ShoppingCartItem cartItem;
    final FireBaseDatabaseHandler db;
    String key;
    final String node="Orders";
    public OrderManager(String userId) {
        this.userId=userId;
        order=new Order();
        db=new FireBaseDatabaseHandler();
    }
    public void addOrder(Order order)
    {
        this.order=order;

        Query query = db.getmFirebaseInstance().getReference().child(node).child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    addOrderToUserOrderList ();
                }
                else
                {
                    createUserAndAddOrder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void createUserAndAddOrder(){
        db.getNodeReference(node).child(userId).setValue(order);
    }
    public void addOrderToUserOrderList(){
        db.getNodeReference(node).child(userId).push().setValue(order);
    }


}

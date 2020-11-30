package com.example.splitapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 *
 */

public class ObjectCreationUnitTests {
    private static final double DELTA = 1e-15;
    @Test
    public void testItemCreation() {
        String id = "12341234";
        String name = "test item";
        double price = 123.0;
        int split = 1;
        ItemItem test1 = new ItemItem(id, name ,price, split);
        assertEquals("12341234", test1.getId());
    }
    @Test
    public void testBillCreation() {
        String id ="321321321";
        String name ="Expensive Bill Test";
        double totalPrice = 100.0;
        double amountPaid = 100.0;
        double amountOwed = 50.0;
        BillItem bill = new BillItem(id, name ,totalPrice,amountPaid,amountOwed);
        assertEquals(100.0, bill.getAmountPaid(),DELTA);
        bill.setAmountOwed(0.0);
        assertEquals(0.0, bill.getAmountOwed(), DELTA);

    }
    @Test
    public void testProfileCreation() {
        String id = "43214321";
        String name = "Test Profile";
        String phoneNumber = "1234512345";
        String email = "testemail@profile.com";
        String address ="39 Test Address";
        String notes = "This is a test user";
        int totalSplit = 2;
        double amountOwed = 123.0;
        boolean isVisible = false;
        ProfileItem test1 = new ProfileItem(id, name ,phoneNumber, email,address,notes,totalSplit,amountOwed,isVisible);
        assertEquals("testemail@profile.com", test1.getEmail());
        test1.setEmail("changeTheEmail@test.com");
        assertEquals("changeTheEmail@test.com", test1.getEmail());
    }
}
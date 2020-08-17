package com.example.carshopfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carshopfirebase.models.Car;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText seatsView, priceView, statusView, modelView, dateReleasedView, categoriesView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seatsView = findViewById(R.id.etCarSeats);
        priceView = findViewById(R.id.etCarPrice);
        statusView = findViewById(R.id.etCarStatus);
        modelView = findViewById(R.id.etCarModel);
        dateReleasedView = findViewById(R.id.etCarDateReleased);
        categoriesView = findViewById(R.id.etCarCategories);

        initializeFirebase();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String Seats = seatsView.getText().toString();
        String Price = priceView.getText().toString();
        String Status = statusView.getText().toString();
        String Model = modelView.getText().toString();
        String DateReleased = dateReleasedView.getText().toString();
        String Category = categoriesView.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add: {
                if (Seats.equals("")||Price.equals("")||Status.equals("")||Model.equals("")
                    ||DateReleased.equals("")||Category.equals("")){
                    validation();
                }
                else {
                    Car car = new Car();
                    car.setId(UUID.randomUUID().toString());
                    car.setSeats(Seats);
                    car.setPrice(Price);
                    car.setStatus(Status);
                    car.setModel(Model);
                    car.setDateReleased(DateReleased);
                    car.setCategory(Category);
                    databaseReference.child("Car").child(car.getId()).setValue(car);

                    Toast.makeText(this, "Add", Toast.LENGTH_LONG).show();
                    clearBoxes();
                }
                break;
            }
            case R.id.icon_save: {
                Toast.makeText(this, "Save", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.icon_delete: {
                Toast.makeText(this, "Delete", Toast.LENGTH_LONG).show();
                break;
            }
        }
        return true;

    }

    private void clearBoxes() {
        seatsView.setText("");
        priceView.setText("");
        statusView.setText("");
        modelView.setText("");
        dateReleasedView.setText("");
        categoriesView.setText("");
    }
    private void validation() {
        String Seats = seatsView.getText().toString();
        String Price = priceView.getText().toString();
        String Status = statusView.getText().toString();
        String Model = modelView.getText().toString();
        String DateReleased = dateReleasedView.getText().toString();
        String Category = categoriesView.getText().toString();

        if (Seats.equals("")){
            seatsView.setError("Required");
        }
        else if (Price.equals("")){
            priceView.setError("Required");
        }
        else if (Status.equals("")){
            statusView.setError("Required");
        }
        else if (Model.equals("")){
            modelView.setError("Required");
        }
        else if (DateReleased.equals("")){
            dateReleasedView.setError("Required");
        }
        else if (Category.equals("")){
            categoriesView.setError("Required");
        }
    }
}
package com.example.carshopfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carshopfirebase.models.Car;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<Car> listCar = new ArrayList<Car>();
    ArrayAdapter<Car> arrayAdapterCar;

    ListView listV_cars;
    EditText seatsView, priceView, statusView, modelView, dateReleasedView, categoriesView;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Car carSelected;

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
        listV_cars = findViewById(R.id.car_list);

        initializeFirebase();
        listOfData();

        listV_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 carSelected = (Car) parent.getItemAtPosition(position);
                 seatsView.setText(carSelected.getSeats());
                 priceView.setText(carSelected.getPrice());
                 statusView.setText(carSelected.getStatus());
                 modelView.setText(carSelected.getModel());
                 dateReleasedView.setText(carSelected.getDateReleased());
                 categoriesView.setText(carSelected.getCategory());
            }
        });
    }

    private void listOfData() {
        databaseReference.child("Car").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listCar.clear();
                for (DataSnapshot objSnapshop : dataSnapshot.getChildren()){
                    Car car = objSnapshop.getValue(Car.class);
                    listCar.add(car);

                    arrayAdapterCar = new ArrayAdapter<Car>(MainActivity.this, android.R.layout.simple_list_item_1, listCar);
                    listV_cars.setAdapter(arrayAdapterCar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                Car car = new Car();
                car.setId(carSelected.getId());
                car.setSeats(seatsView.getText().toString().trim());
                car.setPrice(priceView.getText().toString().trim());
                car.setStatus(statusView.getText().toString().trim());
                car.setModel(modelView.getText().toString().trim());
                car.setCategory(categoriesView.getText().toString().trim());
                databaseReference.child("Car").child(car.getId()).setValue(car);

                Toast.makeText(this, "Save", Toast.LENGTH_LONG).show();

                clearBoxes();
                break;
            }
            case R.id.icon_delete: {
                Car car = new Car();
                car.setId(carSelected.getId());
                databaseReference.child("Car").child(car.getId()).removeValue();

                Toast.makeText(this, "Delete", Toast.LENGTH_LONG).show();

                clearBoxes();
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
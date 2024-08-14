package com.testing.foodmanagement;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        // Initialize the Places API
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize the search view
        SearchView searchView = findViewById(R.id.locationSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set a map click listener to capture the location
        mMap.setOnMapClickListener(latLng -> {
            // Clear previous markers
            mMap.clear();

            // Add a marker at the clicked location
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            // Fetch the address using Geocoder
            Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String fullAddress = address.getAddressLine(0); // Get the full address

                    // Return the selected location and address to the previous activity
                    Intent intent = new Intent();
                    intent.putExtra("location", latLng);
                    intent.putExtra("address", fullAddress);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(SelectLocationActivity.this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SelectLocationActivity.this, "Error fetching address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                // Return the selected location and address to the previous activity
                Intent intent = new Intent();
                intent.putExtra("location", latLng);
                intent.putExtra("address", address.getAddressLine(0));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error searching location", Toast.LENGTH_SHORT).show();
        }
    }
}

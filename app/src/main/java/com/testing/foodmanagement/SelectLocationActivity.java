package com.testing.foodmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
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

            // Return the selected location to the previous activity
            Intent intent = new Intent();
            intent.putExtra("location", latLng);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void searchLocation(String locationName) {
        // Use Places API to search for the location by name
        FindCurrentPlaceRequest placeRequest = FindCurrentPlaceRequest.newInstance(
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the User grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        placesClient.findCurrentPlace(placeRequest).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                if (response != null) {
                    // If a place is found, move the map and place a marker
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place place = placeLikelihood.getPlace();
                        if (Objects.requireNonNull(place.getName()).equalsIgnoreCase(locationName)) {
                            LatLng latLng = place.getLatLng();
                            if (latLng != null) {
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                // Return the selected location to the previous activity
                                Intent intent = new Intent();
                                intent.putExtra("location", latLng);
                                setResult(RESULT_OK, intent);
                                finish();
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
}

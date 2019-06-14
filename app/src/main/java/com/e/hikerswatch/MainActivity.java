package com.e.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;

    List<Address> addressList;

    public void updateLocationInfo(Location location) {

        Log.i("Location: ", location.toString());

        TextView latitudeText = (TextView) findViewById(R.id.latitudeText);

        TextView longitudeText = (TextView) findViewById(R.id.longitudeText);

        TextView accuracyText = (TextView) findViewById(R.id.accuracyText);

        TextView altitudeText = (TextView) findViewById(R.id.altitudeText);

        TextView addressText = (TextView) findViewById(R.id.addressText);

        latitudeText.setText("Latitude: " + location.getLatitude());

        longitudeText.setText("Location: " + location.getLongitude());

        altitudeText.setText("Altitude: " + location.getAltitude());

        accuracyText.setText("Accuracy: " + location.getAccuracy());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String address = "Could Not Find Address";

        try {

            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addressList != null && addressList.size() > 0) {

                address = "";

                Log.i("Address:", addressList.toString());

                if (addressList.get(0).getSubThoroughfare() != null) {

                    address += addressList.get(0).getSubThoroughfare() + " ";

                }

                if (addressList.get(0).getThoroughfare() != null) {

                    address += addressList.get(0).getThoroughfare() + ",\n";

                }

                if (addressList.get(0).getLocality() != null) {

                    address += addressList.get(0).getLocality() + ", ";

                }

                if (addressList.get(0).getSubAdminArea() != null) {

                    address += addressList.get(0).getSubAdminArea() + "\n";

                }

                if (addressList.get(0).getAdminArea() != null) {

                    address += addressList.get(0).getAdminArea() + ", ";

                }

                if (addressList.get(0).getCountryName() != null) {

                    address += addressList.get(0).getCountryName() + "\n";

                }

                if (addressList.get(0).getPostalCode() != null) {

                    address += addressList.get(0).getPostalCode() + ".";

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        addressText.setText(address);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // ask for permission

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            // we have permission

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

            Location lastKnownLocation  = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (lastKnownLocation != null) {

                updateLocationInfo(lastKnownLocation);

            }

        }

    }
}

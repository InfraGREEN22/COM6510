package uk.ac.shef.oak.com6510;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import uk.ac.shef.oak.com6510.database.Picture;

/**
 * Map activity of app which shows pictures on map.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
	/**
	 * Grant permission code.
	 */
	private static final int ACCESS_FINE_LOCATION = 123;
	/**
	 * Google map object.
	 */
	private static GoogleMap mMap;
	/**
	 * Pictures to be showed on the map.
	 */
	private static List<Picture> pictures;
	/**
	 * Markers to be showed on the map.
	 */
	private static HashMap<Marker, Picture> markers;
	/**
	 * Location callback.
	 */
	LocationCallback mLocationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
			super.onLocationResult(locationResult);
			Location mCurrentLocation = locationResult.getLastLocation();
			if (mMap != null)
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14.0f));
		}
	};
	/**
	 * The context the view is being created in.
	 */
	private Context context;
	/**
	 * Location request.
	 */
	private LocationRequest mLocationRequest;
	/**
	 * Fused location provider client.
	 */
	private FusedLocationProviderClient mFusedLocationClient;

	/**
	 * Getter method of mMap.
	 *
	 * @return Google map object.
	 */
	public static GoogleMap getMap() {
		return mMap;
	}

	/**
	 * Add marker on map.
	 * Set marker and picture into hashMap.
	 */
	public static void setMarker() {
		for (Picture p : pictures) {
			if (p.getLon() < 200 && p.getLat() < 200) {
				Marker myMarker = mMap.addMarker(
						new MarkerOptions()
								.position(new LatLng(p.getLat(), p.getLon()))
								.visible(true)
						//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_brightness_1_black_24dp))
				);
				markers.put(myMarker, p);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setTitle("Picture Manager - Map");

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}
		markers = new HashMap<>();
		initLocations();
		pictures = PictureAdapter.getAllPictures();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		this.context = context;
		return super.onCreateView(parent, name, context, attrs);
	}

	/**
	 * Initialize location.
	 */
	private void initLocations() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {

				// No explanation needed, we can request the permission.

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						ACCESS_FINE_LOCATION);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mLocationRequest = new LocationRequest();
//		mLocationRequest.setInterval(10000);
//		mLocationRequest.setFastestInterval(5000);
//		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//		startLocationUpdates(this);
	}

/*	private void startLocationUpdates(Context context) {
		Intent intent = new Intent(context, LocationIntent.class);
		mLocationPendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Task<Void> locationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest,
					mLocationPendingIntent);
			if (locationTask != null) {
				locationTask.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						if (e instanceof ApiException) {
							Log.w("MapActivity", ((ApiException) e).getStatusMessage());
						} else {
							Log.w("MapActivity", e.getMessage());
						}
					}
				});

				locationTask.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						Log.d("MapActivity", "restarting gps successful!");
					}
				});


			}
		}
	}*/

	/**
	 * {@inheritDoc}
	 */
	@SuppressLint("MissingPermission")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case ACCESS_FINE_LOCATION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
					mFusedLocationClient.requestLocationUpdates(mLocationRequest,
							mLocationCallback, null /* Looper */);
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	/**
	 * When map is ready, show my location and markers on map.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.getUiSettings().setZoomControlsEnabled(true);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mMap.setMyLocationEnabled(true);
		setMarker();
		mMap.setOnMarkerClickListener(marker -> {
//			marker.setTitle(markers.get(marker).getTitle());
			Intent intent = new Intent(context, ImageExtraActivity.class);
			intent.putExtra("pic", markers.get(marker));
			context.startActivity(intent);
			return false;
		});
	}
	// PUT EXTRA FOR ID AND SET ONCLICK LISTENER FOR EACH MARKER BASED ON ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

}

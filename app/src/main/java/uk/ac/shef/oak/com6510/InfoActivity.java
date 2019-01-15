package uk.ac.shef.oak.com6510;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.viewmodel.PictureViewModel;

/**
 * Info activity of app which shows information of picture.
 */
public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {
	/**
	 * Google map object.
	 */
	private static GoogleMap mMap;
	/**
	 * Picture to be shown.
	 */
	private Picture element = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		setTitle("Picture Manager - Edit");

		RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setNestedScrollingEnabled(false);

		element = (Picture) getIntent().getSerializableExtra("pic");
		if (element != null) {
			InfoAdapter mAdapter = new InfoAdapter(element);
			recyclerView.setAdapter(mAdapter);

			final EditText editTextTitle = findViewById(R.id.edit_text_title);
			final EditText editTextDescription = findViewById(R.id.edit_text_description);

			final PictureViewModel viewModel = ViewModelProviders.of(this).get(PictureViewModel.class);

			editTextTitle.setText(element.getTitle());
			editTextDescription.setText(element.getDescription());

			Button saveButton = findViewById(R.id.save_button);
			saveButton.setOnClickListener(v -> {
				element.setTitle(editTextTitle.getText().toString());
				element.setDescription(editTextDescription.getText().toString());

				viewModel.update(element);

				Toast.makeText(InfoActivity.this, "Save successfully!\nTitle: " + element.getTitle() + "\nDescription: " + element.getDescription(), Toast.LENGTH_LONG).show();
			});

			// Obtain the SupportMapFragment and get notified when the map is ready to be used.
			SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mini_map);
			if (element.getLon() < 200 && element.getLat() < 200) {
				if (mapFragment != null) {
					mapFragment.getMapAsync(this);
				}
			} else {
				mapFragment.getView().setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * When map is ready, show my location and markers on map.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMinZoomPreference(14);
		LatLng location = new LatLng(element.getLat(), element.getLon());
		mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
		mMap.getUiSettings().setZoomControlsEnabled(false);
		setMarker();
	}

	/**
	 * Add marker on map.
	 */
	public void setMarker() {
		if (element.getLon() < 200 && element.getLat() < 200) {
			mMap.addMarker(
					new MarkerOptions()
							.position(new LatLng(element.getLat(), element.getLon()))
							.visible(true)
			);
		}
	}
}

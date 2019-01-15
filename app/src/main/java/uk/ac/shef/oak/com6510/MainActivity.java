package uk.ac.shef.oak.com6510;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.viewmodel.PictureViewModel;

/**
 * Main activity of app. First page.
 */
public class MainActivity extends AppCompatActivity {
	/**
	 * The ViewModel of Picture.
	 */
	private PictureViewModel viewModel;
	/**
	 * Recycler view in app which shows pictures.
	 */
	private RecyclerView recyclerView;
	/**
	 * The adapter for Picture objects.
	 */
	private PictureAdapter adapter;
	/**
	 * Permission list. For request on first run.
	 */
	private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		int id = item.getItemId();
		Intent intent;
		switch (id) {
			case R.id.menu_map:
				intent = new Intent(this, MapActivity.class);
				this.startActivity(intent);
				return true;
			case R.id.menu_search:
				intent = new Intent(this, SearchActivity.class);
				this.startActivity(intent);
				return true;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Picture Manager - Gallery");

		final int numberOfColumns = 3;

		recyclerView = findViewById(R.id.grid_recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
		recyclerView.setHasFixedSize(true);
		recyclerView.setNestedScrollingEnabled(false);

		adapter = new PictureAdapter();
		recyclerView.setAdapter(adapter);

		if (!arePermissionsEnabled()) {
			requestMultiplePermissions();
		}

		initEasyImage();

		final FloatingActionButton fab = findViewById(R.id.fab_camera);
		fab.setOnClickListener(view -> EasyImage.openCamera(MainActivity.this, 0));

		FloatingActionButton fabGallery = findViewById(R.id.fab_gallery);
		fabGallery.setOnClickListener(view -> EasyImage.openGallery(MainActivity.this, 0));

		viewModel = ViewModelProviders.of(this).get(PictureViewModel.class);
		viewModel.getAllPictures().observe(this, pictures -> {
			//update recyclerView
			adapter.setPictures(pictures);
			PictureAdapter.setAllPictures(pictures);
		});

		recyclerView.addOnScrollListener(new MyScrollListener(fab));
		recyclerView.addOnScrollListener(new MyScrollListener(fabGallery));
	}

	/**
	 * Initialize easy image.
	 */
	private void initEasyImage() {
		EasyImage.configuration(this)
				.setCopyTakenPhotosToPublicGalleryAppFolder(false)
				.setCopyPickedImagesToPublicGalleryAppFolder(false)
				.setAllowMultiplePickInGallery(true);
	}

	/**
	 * Check if permissions were granted.
	 *
	 * @return True or false for granted or not.
	 */
	private boolean arePermissionsEnabled() {
		for (String permission : permissions) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
				return false;
		}
		return true;
	}

	/**
	 * Request permissions.
	 */
	private void requestMultiplePermissions() {
		List<String> remainingPermissions = new ArrayList<>();
		for (String permission : permissions) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				remainingPermissions.add(permission);
			}
		}
		requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 101) {
			for (int i = 0; i < grantResults.length; i++) {
				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
					if (shouldShowRequestPermissionRationale(permissions[i])) {
						new AlertDialog.Builder(this)
								.setMessage("Storage and location permissions are necessary")
								.setPositiveButton("Allow", (dialog, which) -> requestMultiplePermissions())
								.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
								.create()
								.show();
					}
					return;
				}
			}
			//all is good, continue flow
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
			/**
			 * Do something if occurs error when pick image.
			 */
			@Override
			public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
				//Some error handling
				e.printStackTrace();
			}

			/**
			 * Do something when some images were picked.
			 */
			@Override
			public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
				onPhotosReturned(imageFiles);
				adapter.onPhotosReturned(imageFiles, recyclerView);
			}

			/**
			 * Do something when user cancel operation.
			 */
			@Override
			public void onCanceled(EasyImage.ImageSource source, int type) {
				//Cancel handling, you might wanna remove taken photo if it was canceled
				if (source == EasyImage.ImageSource.CAMERA) {
					File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
					if (photoFile != null) photoFile.delete();
				}
			}
		});
	}

	/**
	 * Reaction after getting pictures.
	 *
	 * @param returnedPhotos Pictures given by user.
	 */
	private void onPhotosReturned(List<File> returnedPhotos) {
		addImageElements(returnedPhotos);
//		adapter.notifyDataSetChanged();
//		recyclerView.scrollToPosition(returnedPhotos.size() - 1);
	}

	/**
	 * Add pictures into database.
	 *
	 * @param returnedPhotos Pictures to be added.
	 */
	private void addImageElements(List<File> returnedPhotos) {
		for (File file : returnedPhotos) {
			Picture element = new Picture(file.getAbsolutePath(), file.getName());
			viewModel.insert(element);
		}
	}

	/**
	 * ScrollListener of recycler view for hiding fab.
	 */
	public class MyScrollListener extends RecyclerView.OnScrollListener {
		/**
		 * The fab to be hided.
		 */
		FloatingActionButton fab;

		/**
		 * Constructor method of MyScrollListener class.
		 *
		 * @param fab The fab to be hided.
		 */
		MyScrollListener(FloatingActionButton fab) {
			this.fab = fab;
		}

		/**
		 * Hide fab when scroll recycler view.
		 */
		@Override
		public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
			if (dy > 0 || dy < 0 && fab.isShown())
				fab.hide();
		}

		/**
		 * Show fab when stop scrolling recycler view.
		 */
		@Override
		public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

			if (newState == RecyclerView.SCROLL_STATE_IDLE) {
				fab.show();
			}
			super.onScrollStateChanged(recyclerView, newState);
		}
	}
}

package uk.ac.shef.oak.com6510;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.viewmodel.PictureViewModel;

public class MainActivity extends AppCompatActivity {

	private PictureViewModel viewModel;
	private RecyclerView recyclerView;
	private PictureAdapter adapter;
	private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

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

	public class MyScrollListener extends RecyclerView.OnScrollListener {
		FloatingActionButton fab;

		MyScrollListener(FloatingActionButton fab) {
			this.fab = fab;
		}

		@Override
		public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
			if (dy > 0 || dy < 0 && fab.isShown())
				fab.hide();
		}

		@Override
		public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

			if (newState == RecyclerView.SCROLL_STATE_IDLE) {
				fab.show();
			}
			super.onScrollStateChanged(recyclerView, newState);
		}
	}

	private void initEasyImage() {
		EasyImage.configuration(this)
				.setImagesFolderName("EasyImage sample")
				.setCopyTakenPhotosToPublicGalleryAppFolder(true)
				.setCopyPickedImagesToPublicGalleryAppFolder(false)
				.setAllowMultiplePickInGallery(true);
	}

	private boolean arePermissionsEnabled() {
		for (String permission : permissions) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
				return false;
		}
		return true;
	}

	private void requestMultiplePermissions() {
		List<String> remainingPermissions = new ArrayList<>();
		for (String permission : permissions) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				remainingPermissions.add(permission);
			}
		}
		requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
	}

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
			@Override
			public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
				//Some error handling
				e.printStackTrace();
			}

			@Override
			public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
				onPhotosReturned(imageFiles);
				adapter.onPhotosReturned(imageFiles, recyclerView);
			}

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

	private void onPhotosReturned(List<File> returnedPhotos) {
		addImageElements(returnedPhotos);
//		adapter.notifyDataSetChanged();
//		recyclerView.scrollToPosition(returnedPhotos.size() - 1);
	}

	private void addImageElements(List<File> returnedPhotos) {
		for (File file : returnedPhotos) {
			Picture element = new Picture(file.getAbsolutePath(), file.getName());
			viewModel.getRepository().insert(element);
		}
	}
}

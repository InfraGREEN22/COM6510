package uk.ac.shef.oak.com6510;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.viewmodel.PictureViewModel;

import static uk.ac.shef.oak.com6510.PictureAdapter.decodeSampledBitmapFromResource;

public class ShowImageActivity extends AppCompatActivity {
	Picture element = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		setTitle("Picture Manager - Show");

		ImageView imageView = findViewById(R.id.show_picture);
		FloatingActionButton fab = findViewById(R.id.fab_info);
		FloatingActionButton fabDelete = findViewById(R.id.fab_delete);

		element = (Picture) getIntent().getSerializableExtra("pic");

		if (element != null) {
			try {
				ExifInterface exifInterface = new ExifInterface(element.getPicturePath());

				int degree = 0;
				switch (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}

				Matrix matrix = new Matrix();
				matrix.postRotate(degree);

				Bitmap myBitmap = decodeSampledBitmapFromResource(element.getPicturePath(), 1000, 1000);
				Bitmap rotated = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);

				imageView.setImageBitmap(rotated);
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Something wrong:\n" + e.toString(),
						Toast.LENGTH_LONG).show();
			}

			fab.setOnClickListener(view -> {
				Intent intent = new Intent(ShowImageActivity.this, InfoActivity.class);
				intent.putExtra("pic", element);
				startActivity(intent);
			});

			PictureViewModel viewModel = ViewModelProviders.of(this).get(PictureViewModel.class);

			fabDelete.setOnClickListener(view -> new AlertDialog.Builder(this)
					.setMessage("Delete?")
					.setPositiveButton("Yes", (dialog, which) -> {
						viewModel.delete(element);
						ShowImageActivity.this.finish();
					})
					.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
					.create()
					.show());
		}
	}
}

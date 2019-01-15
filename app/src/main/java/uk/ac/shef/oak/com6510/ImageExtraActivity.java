package uk.ac.shef.oak.com6510;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import uk.ac.shef.oak.com6510.database.Picture;

import static uk.ac.shef.oak.com6510.PictureAdapter.decodeSampledBitmapFromResource;

/**
 * Image extra activity of app which shows picture and part of information.
 */
public class ImageExtraActivity extends AppCompatActivity {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_extra);
		setTitle("Picture Manager - Location Details");

		Picture element = (Picture) getIntent().getSerializableExtra("pic");

		if (element == null) return;

		try {
			ImageView imageView = findViewById(R.id.extra_picture);
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

			Bitmap bitmap = decodeSampledBitmapFromResource(element.getPicturePath(), 500, 500);

			Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


			imageView.setImageBitmap(rotated);
			TextView titleTextView = findViewById(R.id.extra_title);
			TextView descriptionTextView = findViewById(R.id.extra_description);
			TextView dateTextView = findViewById(R.id.extra_date);

			titleTextView.setText("Title: " + element.getTitle());
			if (element.getDescription() == null) {
				descriptionTextView.setText("Description: No description");
			} else
				descriptionTextView.setText("Description: " + element.getDescription());
			dateTextView.setText("Date: " + element.getDate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

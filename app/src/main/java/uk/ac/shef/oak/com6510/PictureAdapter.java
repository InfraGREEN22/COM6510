package uk.ac.shef.oak.com6510;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com6510.database.Picture;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {

	private List<Picture> pictures = new ArrayList<>();
	private static List<Picture> allPictures = new ArrayList<>();
	private Context context;

	@NonNull
	@Override
	public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.picture, parent, false);
		context = parent.getContext();
		return new PictureHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull PictureHolder pictureHolder, int position) {
		try {
			final Picture currentPicture = pictures.get(position);
			File file = new File(currentPicture.getPicturePath());

			ExifInterface exifInterface = new ExifInterface(currentPicture.getPicturePath());

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

			Bitmap bitmap = decodeSampledBitmapFromResource(file.getAbsolutePath(), 100, 100);
			Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			pictureHolder.imageViewPicture.setImageBitmap(rotated);
			pictureHolder.textViewTitle.setText(currentPicture.getTitle());

			pictureHolder.imageViewPicture.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("pic", currentPicture);
					context.startActivity(intent);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Bitmap decodeSampledBitmapFromResource(String filePath, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	@Override
	public int getItemCount() {
		return pictures.size();
	}

	void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
		notifyDataSetChanged();
	}

	static void setAllPictures(List<Picture> allPictures) {
		PictureAdapter.allPictures = allPictures;
	}

	void onPhotosReturned(List<File> returnedPhotos, RecyclerView recyclerView) {
		pictures.addAll(getImageElements(returnedPhotos));
		setPictures(pictures);
//		notifyDataSetChanged();
//		recyclerView.scrollToPosition(returnedPhotos.size() - 1);
	}

	private List<Picture> getImageElements(List<File> returnedPhotos) {
		List<Picture> imageElementList = new ArrayList<>();
		for (File file : returnedPhotos) {
			Picture element = new Picture(file.getAbsolutePath(), file.getName());
			imageElementList.add(element);
		}
		return imageElementList;
	}

	class PictureHolder extends RecyclerView.ViewHolder {
		private ImageView imageViewPicture;
		private TextView textViewTitle;

		PictureHolder(@NonNull View itemView) {
			super(itemView);
			imageViewPicture = itemView.findViewById(R.id.picture);
			textViewTitle = itemView.findViewById(R.id.picture_title);
		}
	}

	static List<Picture> getAllPictures() {
		return allPictures;
	}
}

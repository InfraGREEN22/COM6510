package uk.ac.shef.oak.com6510;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.shef.oak.com6510.database.Picture;

/**
 * Adapter for Picture in recycler view.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {
	/**
	 * All Picture objects retrieved from DB.
	 */
	private static List<Picture> allPictures = new ArrayList<>();
	/**
	 * Some Picture objects retrieved from DB.
	 */
	private List<Picture> pictures = new ArrayList<>();
	/**
	 * The context the view is being created in.
	 */
	private Context context;

	/**
	 * Decode sampled bitmap from resource.
	 *
	 * @param filePath  Path of file.
	 * @param reqWidth  Required width.
	 * @param reqHeight Required height.
	 * @return Bitmap with specified size.
	 */
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

	/**
	 * Calculate sample size.
	 *
	 * @param options   Options in calculation.
	 * @param reqWidth  Required width.
	 * @param reqHeight Required height.
	 * @return Bitmap decode inSampleSize option.
	 */
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

	/**
	 * Getter method for all pictures from DB.
	 *
	 * @return List of Picture objects retrieved from DB.
	 */
	static List<Picture> getAllPictures() {
		return allPictures;
	}

	/**
	 * Set all pictures for showing.
	 *
	 * @param allPictures Picture list.
	 */
	static void setAllPictures(List<Picture> allPictures) {
		PictureAdapter.allPictures = allPictures;
	}

	/**
	 * Create a view holder.
	 */
	@NonNull
	@Override
	public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.picture, parent, false);
		context = parent.getContext();
		return new PictureHolder(itemView);
	}

	/**
	 * Bind pictures to view holder.
	 */
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

			pictureHolder.imageViewPicture.setOnClickListener(v -> {
				Intent intent = new Intent(context, ShowImageActivity.class);
				intent.putExtra("pic", currentPicture);
				context.startActivity(intent);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get pictures count in view holder.
	 */
	@Override
	public int getItemCount() {
		return pictures.size();
	}

	/**
	 * Set pictures for showing.
	 *
	 * @param pictures Picture list.
	 */
	void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
		notifyDataSetChanged();
	}

	/**
	 * Reaction after getting pictures.
	 *
	 * @param returnedPhotos Pictures given by user.
	 * @param recyclerView   The recycler view to be adapted.
	 */
	void onPhotosReturned(List<File> returnedPhotos, RecyclerView recyclerView) {
		pictures.addAll(getImageElements(returnedPhotos));
		setPictures(pictures);
//		notifyDataSetChanged();
//		recyclerView.scrollToPosition(returnedPhotos.size() - 1);
	}

	/**
	 * Get picture list.
	 *
	 * @param returnedPhotos Given picture file.
	 * @return Picture list generated by picture file.
	 */
	private List<Picture> getImageElements(List<File> returnedPhotos) {
		List<Picture> imageElementList = new ArrayList<>();
		for (File file : returnedPhotos) {
			Picture element = new Picture(file.getAbsolutePath(), file.getName());
			imageElementList.add(element);
		}
		return imageElementList;
	}

	/**
	 * View holder class for Picture class.
	 */
	class PictureHolder extends RecyclerView.ViewHolder {
		/**
		 * Image view which show picture.
		 */
		private ImageView imageViewPicture;
		/**
		 * Text view which show title of picture.
		 */
		private TextView textViewTitle;

		/**
		 * Constructor method of PictureHolder class.
		 *
		 * @param itemView Item view.
		 */
		PictureHolder(@NonNull View itemView) {
			super(itemView);
			imageViewPicture = itemView.findViewById(R.id.picture);
			textViewTitle = itemView.findViewById(R.id.picture_title);
		}
	}
}

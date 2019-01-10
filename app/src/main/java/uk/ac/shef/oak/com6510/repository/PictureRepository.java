package uk.ac.shef.oak.com6510.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.database.PictureDAO;
import uk.ac.shef.oak.com6510.database.PictureDatabase;

public class PictureRepository {

	private PictureDAO pictureDAO;
	private LiveData<List<Picture>> allPictures;

	public PictureRepository(Application application) {
		PictureDatabase database = PictureDatabase.getInstance(application);
		pictureDAO = database.pictureDAO();
		allPictures = pictureDAO.getAllPictures();
	}

	public void insert(Picture picture) {
		new InsertPictureAsyncTask(pictureDAO).execute(picture);
	}

	public void update(Picture picture) {
		new UpdatePictureAsyncTask(pictureDAO).execute(picture);
	}

	public void delete(Picture picture) {
		new DeletePictureAsyncTask(pictureDAO).execute(picture);
	}

	public LiveData<List<Picture>> getAllPictures() {
		return allPictures;
	}

	public LiveData<List<Picture>> search(String key) {
		return pictureDAO.search(key);
	}

	/**
	 * Insert Async Task
	 */
	public static class InsertPictureAsyncTask extends AsyncTask<Picture, Void, Void> {
		private PictureDAO pictureDAO;

		InsertPictureAsyncTask(PictureDAO pictureDAO) {
			this.pictureDAO = pictureDAO;
		}

		@Override
		protected Void doInBackground(Picture... pictures) {
			pictureDAO.insert(pictures[0]);
			return null;
		}
	}

	/**
	 * Update Async Task
	 */
	public static class UpdatePictureAsyncTask extends AsyncTask<Picture, Void, Void> {
		private PictureDAO pictureDAO;

		UpdatePictureAsyncTask(PictureDAO pictureDAO) {
			this.pictureDAO = pictureDAO;
		}

		@Override
		protected Void doInBackground(Picture... pictures) {
			pictureDAO.update(pictures[0]);
			return null;
		}
	}

	/**
	 * Delete Async Task
	 */
	public static class DeletePictureAsyncTask extends AsyncTask<Picture, Void, Void> {
		private PictureDAO pictureDAO;

		DeletePictureAsyncTask(PictureDAO pictureDAO) {
			this.pictureDAO = pictureDAO;
		}

		@Override
		protected Void doInBackground(Picture... pictures) {
			pictureDAO.delete(pictures[0]);
			return null;
		}
	}
}

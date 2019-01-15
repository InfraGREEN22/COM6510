package uk.ac.shef.oak.com6510.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.database.PictureDAO;
import uk.ac.shef.oak.com6510.database.PictureDatabase;

/**
 * Repository modules handle data operations.
 */
public class PictureRepository {
	/**
	 * DAO of DB.
	 */
	private PictureDAO pictureDAO;
	/**
	 * All Picture objects retrieved from DB.
	 */
	private LiveData<List<Picture>> allPictures;

	/**
	 * Constructor method of PictureRepository class.
	 *
	 * @param application The context the view is being created in.
	 */
	public PictureRepository(Application application) {
		PictureDatabase database = PictureDatabase.getInstance(application);
		pictureDAO = database.pictureDAO();
		allPictures = pictureDAO.getAllPictures();
	}

	/**
	 * Insert picture into DB.
	 *
	 * @param picture Picture to be added.
	 */
	public void insert(Picture picture) {
		new InsertPictureAsyncTask(pictureDAO).execute(picture);
	}

	/**
	 * Update picture in DB.
	 *
	 * @param picture Picture to be updated.
	 */
	public void update(Picture picture) {
		new UpdatePictureAsyncTask(pictureDAO).execute(picture);
	}

	/**
	 * Delete picture in DB.
	 *
	 * @param picture Picture to be deleted.
	 */
	public void delete(Picture picture) {
		new DeletePictureAsyncTask(pictureDAO).execute(picture);
	}

	/**
	 * Getter method for all pictures from DB.
	 *
	 * @return List of Picture objects retrieved from DB.
	 */
	public LiveData<List<Picture>> getAllPictures() {
		return allPictures;
	}

	/**
	 * Search picture which title or description (or both) contains given keyword.
	 *
	 * @param key Given keyword.
	 * @return List of Picture objects which title or description (or both) matches given keyword.
	 */
	public LiveData<List<Picture>> search(String key) {
		return pictureDAO.search(key);
	}

	/**
	 * Insert Async Task.
	 */
	public static class InsertPictureAsyncTask extends AsyncTask<Picture, Void, Void> {
		/**
		 * DAO of DB.
		 */
		private PictureDAO pictureDAO;

		/**
		 * Constructor method of InsertPictureAsyncTask class.
		 *
		 * @param pictureDAO DAO of DB.
		 */
		InsertPictureAsyncTask(PictureDAO pictureDAO) {
			this.pictureDAO = pictureDAO;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Void doInBackground(Picture... pictures) {
			pictureDAO.insert(pictures[0]);
			return null;
		}
	}

	/**
	 * Update Async Task.
	 */
	public static class UpdatePictureAsyncTask extends AsyncTask<Picture, Void, Void> {
		/**
		 * DAO of DB.
		 */
		private PictureDAO pictureDAO;

		/**
		 * Constructor method of UpdatePictureAsyncTask class.
		 *
		 * @param pictureDAO DAO of DB.
		 */
		UpdatePictureAsyncTask(PictureDAO pictureDAO) {
			this.pictureDAO = pictureDAO;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Void doInBackground(Picture... pictures) {
			pictureDAO.update(pictures[0]);
			return null;
		}
	}

	/**
	 * Delete Async Task.
	 */
	public static class DeletePictureAsyncTask extends AsyncTask<Picture, Void, Void> {
		/**
		 * DAO of DB.
		 */
		private PictureDAO pictureDAO;

		/**
		 * Constructor method of DeletePictureAsyncTask class.
		 *
		 * @param pictureDAO DAO of DB.
		 */
		DeletePictureAsyncTask(PictureDAO pictureDAO) {
			this.pictureDAO = pictureDAO;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Void doInBackground(Picture... pictures) {
			pictureDAO.delete(pictures[0]);
			return null;
		}
	}
}

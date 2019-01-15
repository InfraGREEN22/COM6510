package uk.ac.shef.oak.com6510.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.repository.PictureRepository;

/**
 * The class that prepares the data for viewing and reacts to user interactions.
 */
public class PictureViewModel extends AndroidViewModel {
	/**
	 * The repository of Picture.
	 */
	private PictureRepository repository;
	/**
	 * All Picture objects retrieved from DB.
	 */
	private LiveData<List<Picture>> allPictures;

	/**
	 * Constructor method of PictureViewModel class.
	 *
	 * @param application The context the view is being created in.
	 */
	public PictureViewModel(@NonNull Application application) {
		super(application);
		repository = new PictureRepository(application);
		allPictures = repository.getAllPictures();
	}

	/**
	 * Insert picture into DB.
	 *
	 * @param picture Picture to be added.
	 */
	public void insert(Picture picture) {
		repository.insert(picture);
	}

	/**
	 * Update picture in DB.
	 *
	 * @param picture Picture to be updated.
	 */
	public void update(Picture picture) {
		repository.update(picture);
	}

	/**
	 * Delete picture in DB.
	 *
	 * @param picture Picture to be deleted.
	 */
	public void delete(Picture picture) {
		repository.delete(picture);
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
		return repository.search(key);
	}
}

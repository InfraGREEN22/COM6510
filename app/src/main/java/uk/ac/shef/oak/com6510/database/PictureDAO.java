package uk.ac.shef.oak.com6510.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Database access object for pictures in the database.
 */
@Dao
public interface PictureDAO {
	/**
	 * Insert picture into DB.
	 *
	 * @param picture Picture to be added.
	 */
	@Insert
	void insert(Picture picture);

	/**
	 * Update picture in DB.
	 *
	 * @param picture Picture to be updated.
	 */
	@Update
	void update(Picture picture);

	/**
	 * Delete picture in DB.
	 *
	 * @param picture Picture to be deleted.
	 */
	@Delete
	void delete(Picture picture);

	/**
	 * Getter method for all pictures from DB.
	 *
	 * @return List of Picture objects retrieved from DB.
	 */
	@Query("SELECT * FROM picture_table ORDER BY id")
	LiveData<List<Picture>> getAllPictures();

	/**
	 * Search picture which title or description (or both) contains given keyword.
	 *
	 * @param key Given keyword.
	 * @return List of Picture objects which title or description (or both) matches given keyword.
	 */
	@Query("SELECT * FROM picture_table WHERE title LIKE '%' || :key || '%' OR description LIKE '%' || :key || '%'")
	LiveData<List<Picture>> search(String key);
}

package uk.ac.shef.oak.com6510.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PictureDAO {

	@Insert
	void insert(Picture picture);

	@Update
	void update(Picture picture);

	@Delete
	void delete(Picture picture);

	@Query("SELECT * FROM picture_table ORDER BY id")
	LiveData<List<Picture>> getAllPictures();

	@Query("SELECT * FROM picture_table WHERE title LIKE '%' || :key || '%' OR description LIKE '%' || :key || '%'")
	LiveData<List<Picture>> search(String key);
}

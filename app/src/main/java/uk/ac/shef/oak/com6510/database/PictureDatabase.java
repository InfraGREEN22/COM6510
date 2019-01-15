package uk.ac.shef.oak.com6510.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


/**
 * Room database for Picture objects.
 */
@Database(entities = Picture.class, version = 3, exportSchema = false)
public abstract class PictureDatabase extends RoomDatabase {
	/**
	 * The instance of PictureDatabase.
	 */
	private static PictureDatabase instance;

	/**
	 * Getter method of instance of PictureDatabase.
	 *
	 * @param context The context the view is being created in.
	 * @return The instance of PictureDatabase.
	 */
	public static synchronized PictureDatabase getInstance(Context context) {
		if (instance == null) {
			instance = Room.databaseBuilder(context.getApplicationContext(),
					PictureDatabase.class, "picture_database")
					.fallbackToDestructiveMigration()
					.build();
		}
		return instance;
	}

	/**
	 * Getter method for DAO of DB.
	 *
	 * @return DAO of DB.
	 */
	public abstract PictureDAO pictureDAO();
}

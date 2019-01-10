package uk.ac.shef.oak.com6510.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = Picture.class, version = 3, exportSchema = false)
public abstract class PictureDatabase extends RoomDatabase {

	private static PictureDatabase instance;

	public static synchronized PictureDatabase getInstance(Context context) {
		if (instance == null) {
			instance = Room.databaseBuilder(context.getApplicationContext(),
					PictureDatabase.class, "picture_database")
					.fallbackToDestructiveMigration()
					.build();
		}
		return instance;
	}

	public abstract PictureDAO pictureDAO();
}

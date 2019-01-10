package uk.ac.shef.oak.com6510.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.media.ExifInterface;

import java.io.IOException;
import java.io.Serializable;

@Entity(tableName = "picture_table")
public class Picture implements Serializable {

	@PrimaryKey(autoGenerate = true)
	private int id;
	@ColumnInfo(name = "path")
	private String picturePath;
	@ColumnInfo(name = "title")
	private String title;
	@ColumnInfo(name = "description")
	private String description;
	@ColumnInfo(name = "latitude")
	private double lat;
	@ColumnInfo(name = "longitude")
	private double lon;
	@ColumnInfo(name = "date")
	private String date;

	public Picture(String picturePath, String title) {
		this.picturePath = picturePath;
		this.title = title;

		try {
			ExifInterface exifInterface = new ExifInterface(picturePath);

			double latLong[] = exifInterface.getLatLong();
			this.lat = latLong != null ? latLong[0] : 0;
			this.lon = latLong != null ? latLong[1] : 0;

			this.date = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLat() {
		return lat;
	}

	void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	void setLon(double lon) {
		this.lon = lon;
	}

	public String getDate() {
		return date;
	}

	void setDate(String date) {
		this.date = date;
	}

	public String[][] getInfo() {
		String[][] info = new String[][]{};
		try {
			ExifInterface exifInterface = new ExifInterface(picturePath);

			String width = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
			String height = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);

			info = new String[][]{
					{"Date", date},
					{"Size", width + "x" + height},
					{"Manufacturer", exifInterface.getAttribute(ExifInterface.TAG_MAKE)},
					{"Model", exifInterface.getAttribute(ExifInterface.TAG_MODEL)},
					{"Longitude", String.valueOf(lon)},
					{"Latitude", String.valueOf(lat)}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}
}

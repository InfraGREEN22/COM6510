package uk.ac.shef.oak.com6510.database;

import java.io.IOException;
import java.io.Serializable;

import androidx.exifinterface.media.ExifInterface;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Picture class. As a element of database.
 */
@Entity(tableName = "picture_table")
public class Picture implements Serializable {
	/**
	 * ID of picture.
	 */
	@PrimaryKey(autoGenerate = true)
	private int id;
	/**
	 * File path of picture.
	 */
	@ColumnInfo(name = "path")
	private String picturePath;
	/**
	 * Title of picture.
	 */
	@ColumnInfo(name = "title")
	private String title;
	/**
	 * Description of picture.
	 */
	@ColumnInfo(name = "description")
	private String description;
	/**
	 * Latitude of picture.
	 */
	@ColumnInfo(name = "latitude")
	private double lat;
	/**
	 * Longitude of picture.
	 */
	@ColumnInfo(name = "longitude")
	private double lon;
	/**
	 * Date of picture.
	 */
	@ColumnInfo(name = "date")
	private String date;

	/**
	 * Constructor of Picture class.
	 *
	 * @param picturePath File path of picture.
	 * @param title       Title of picture.
	 */
	public Picture(String picturePath, String title) {
		this.picturePath = picturePath;
		this.title = title;

		try {
			ExifInterface exifInterface = new ExifInterface(picturePath);

			double latLong[] = exifInterface.getLatLong();
			this.lat = latLong != null ? latLong[0] : 200;
			this.lon = latLong != null ? latLong[1] : 200;

			this.date = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Getter method for ID.
	 *
	 * @return ID of picture.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter method for ID.
	 *
	 * @param id ID of picture.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter method for picture's path.
	 *
	 * @return File path of picture.
	 */
	public String getPicturePath() {
		return picturePath;
	}

	/**
	 * Getter method for title of picture.
	 *
	 * @return Title of picture.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for title of picture.
	 *
	 * @param title Title of picture.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter method for description of picture.
	 *
	 * @return Description of picture.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter method for description of picture.
	 *
	 * @param description Description of picture.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter method for latitude of picture.
	 *
	 * @return Latitude of picture.
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Setter method for latitude of picture.
	 *
	 * @param lat Latitude of picture.
	 */
	void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * Getter method for longitude of picture.
	 *
	 * @return Longitude of picture.
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * Setter method for longitude of picture.
	 *
	 * @param lon Longitude of picture.
	 */
	void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * Getter method for date of picture.
	 *
	 * @return Date of picture.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Setter method for date of picture.
	 *
	 * @param date Date of picture.
	 */
	void setDate(String date) {
		this.date = date;
	}

	/**
	 * Getter method for information of picture.
	 * Date, size, manufacturer, model, longitude, latitude.
	 *
	 * @return Information of picture.
	 */
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
					{"Longitude", lon < 200 ? String.valueOf(lon) : null},
					{"Latitude", lat < 200 ? String.valueOf(lat) : null}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}
}

package uk.ac.shef.oak.com6510.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.repository.PictureRepository;

public class PictureViewModel extends AndroidViewModel {

	private PictureRepository repository;
	private LiveData<List<Picture>> allPictures;

	public PictureViewModel(@NonNull Application application) {
		super(application);
		repository = new PictureRepository(application);
		allPictures = repository.getAllPictures();
	}

	public void insert(Picture picture) {
		repository.insert(picture);
	}

	public void update(Picture picture) {
		repository.update(picture);
	}

	public void delete(Picture picture) {
		repository.delete(picture);
	}

	public LiveData<List<Picture>> getAllPictures() {
		return allPictures;
	}

	public LiveData<List<Picture>> search(String key) {
		return repository.search(key);
	}

	public PictureRepository getRepository() {
		return repository;
	}
}

package uk.ac.shef.oak.com6510;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.viewmodel.PictureViewModel;

public class ResultActivity extends AppCompatActivity {

	private PictureAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		setTitle("Picture Manager - Result");

		final int numberOfColumns = 3;

		RecyclerView recyclerView = findViewById(R.id.grid_recycler_view_result);
		recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
		recyclerView.setHasFixedSize(true);
		recyclerView.setNestedScrollingEnabled(false);

		adapter = new PictureAdapter();
		recyclerView.setAdapter(adapter);

		PictureViewModel viewModel = ViewModelProviders.of(this).get(PictureViewModel.class);

		String key = getIntent().getStringExtra("key");

		viewModel.search(key).observe(this, new Observer<List<Picture>>() {
			@Override
			public void onChanged(@Nullable List<Picture> pictures) {
				//update recyclerView
				adapter.setPictures(pictures);
			}
		});
	}
}

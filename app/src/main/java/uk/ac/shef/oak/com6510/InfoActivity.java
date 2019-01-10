package uk.ac.shef.oak.com6510;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uk.ac.shef.oak.com6510.database.Picture;
import uk.ac.shef.oak.com6510.viewmodel.PictureViewModel;

public class InfoActivity extends AppCompatActivity {
	private Picture element = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		setTitle("Picture Manager - Edit");

		RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);
		recyclerView.setNestedScrollingEnabled(false);

		element = (Picture) getIntent().getSerializableExtra("pic");
		if (element != null) {
			InfoAdapter mAdapter = new InfoAdapter(element);
			recyclerView.setAdapter(mAdapter);

			final EditText editTextTitle = findViewById(R.id.edit_text_title);
			final EditText editTextDescription = findViewById(R.id.edit_text_description);

			final PictureViewModel viewModel = ViewModelProviders.of(this).get(PictureViewModel.class);

			editTextTitle.setText(element.getTitle());
			editTextDescription.setText(element.getDescription());

			Button saveButton = findViewById(R.id.save_button);
			saveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					element.setTitle(editTextTitle.getText().toString());
					element.setDescription(editTextDescription.getText().toString());

					viewModel.update(element);

					Toast.makeText(InfoActivity.this, "Save successfully!\nTitle: " + element.getTitle() + "\nDescription: " + element.getDescription(), Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}

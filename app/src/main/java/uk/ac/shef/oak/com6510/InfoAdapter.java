package uk.ac.shef.oak.com6510;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.shef.oak.com6510.database.Picture;

/**
 * Adapter for text view in recycler view.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {
	/**
	 * Picture's information.
	 */
	private String[][] info;

	/**
	 * Constructor method of InfoAdapter class.
	 *
	 * @param element Picture object.
	 */
	InfoAdapter(Picture element) {
		this.info = element.getInfo();
	}

	/**
	 * Create a view holder.
	 */
	@NonNull
	@Override
	public InfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_text_view, parent, false);
		return new MyViewHolder(v);
	}

	/**
	 * Bind string to view holder.
	 */
	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		holder.name.setText(info[position][0]);
		holder.value.setText(info[position][1]);
	}

	/**
	 * Get string array count in view holder.
	 */
	@Override
	public int getItemCount() {
		return info.length;
	}

	/**
	 * View holder class for picture's info.
	 */
	static class MyViewHolder extends RecyclerView.ViewHolder {
		/**
		 * Text view which attribute name.
		 */
		private TextView name;
		/**
		 * Text view which show value.
		 */
		private TextView value;

		/**
		 * Constructor method of MyViewHolder class.
		 *
		 * @param v Item view.
		 */
		MyViewHolder(View v) {
			super(v);
			name = v.findViewById(R.id.info_name);
			value = v.findViewById(R.id.info_value);
		}
	}
}

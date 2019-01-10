package uk.ac.shef.oak.com6510;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.shef.oak.com6510.database.Picture;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {
	private String[][] info;

	InfoAdapter(Picture element) {
		this.info = element.getInfo();
	}

	@NonNull
	@Override
	public InfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_text_view, parent, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		holder.name.setText(info[position][0]);
		holder.value.setText(info[position][1]);
	}

	@Override
	public int getItemCount() {
		return info.length;
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView name;
		private TextView value;

		MyViewHolder(View v) {
			super(v);
			name = v.findViewById(R.id.info_name);
			value = v.findViewById(R.id.info_value);
		}
	}
}

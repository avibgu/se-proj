package movaProj.sampleApplication;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MyArrayAdapter extends ArrayAdapter<String> {

	private int[] colors = new int[] { Color.parseColor("#E0F5FF"),
			Color.parseColor("#D1F0FF") };

	public MyArrayAdapter(Context pContext, int pTextViewResourceId,
			String[] pObjects) {
		super(pContext, pTextViewResourceId, pObjects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int colorPos = position % colors.length;
		view.setBackgroundColor(colors[colorPos]);
		return view;
	}
}

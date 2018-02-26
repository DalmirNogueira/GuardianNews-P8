package xyz.dnglabs.guardiannews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NeewAdapter extends ArrayAdapter<Neew> {

    public NeewAdapter(Context context, List<Neew> neews) {
        super(context, 0, neews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.new_list_item, parent, false);
        }

        Neew currentNeew = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title_view);
        titleView.setText(currentNeew.getTitle());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_view);
        sectionView.setText(currentNeew.getSection());

        TextView authorView = (TextView) listItemView.findViewById(R.id.author_view);
        authorView.setText(currentNeew.getAuthor());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date_view);
        dateView .setText(currentNeew.getDate());


        return listItemView;
    }
}

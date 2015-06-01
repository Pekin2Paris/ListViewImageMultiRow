package ye.tian.listviewimagemultirow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ye Tian on 29/05/2015.
 */
public class MyAdapter extends BaseAdapter {

    private static ArrayList<SearchResults> searchArrayList;

    private Integer[] imgId = {
            R.drawable.smiley1a,
            R.drawable.smiley2a,
            R.drawable.smiley3a,
            R.drawable.ic_ble_scanner
    };

    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, ArrayList<SearchResults> results) {
        searchArrayList = results;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_list_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtCityState = (TextView) convertView.findViewById(R.id.cityState);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.phone);
            holder.imgPhoto = (ImageView) convertView.findViewById(R.id.photo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtCityState.setText(searchArrayList.get(position).getCityState());
        holder.txtPhone.setText(searchArrayList.get(position).getPhone());
        holder.imgPhoto.setImageResource(imgId[searchArrayList.get(position).getImageNumber() - 1]);

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
        TextView txtPhone;
        ImageView imgPhoto;
    }
}
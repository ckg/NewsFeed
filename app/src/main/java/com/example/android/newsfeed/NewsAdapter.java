package com.example.android.newsfeed;

/**
 * Created by Christos on 21-Jun-17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Christos on 05-Jun-17.
 */

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each News
 * in the data source (a list of {@link News} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {
     /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param newsList is the list of News, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    /**
     * Returns a list item view that displays information about the News at the given position
     * in the list of News.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the News at the given position in the list of News
        News currentNews = getItem(position);

        // Find the TextView with view ID title_text_view
        TextView titleView = (TextView) listItemView.findViewById(R.id.title_text_view);
        // Display the title of the current News in that TextView
        titleView.setText(currentNews.getTitle());

        // Find the TextView with view ID published_date_text_view
        TextView publishedDateView = (TextView) listItemView.findViewById(R.id.
                published_date_text_view);
        // Display the publishedDate of the current News in that TextView
        publishedDateView.setText(currentNews.getPublishedDate());

        // Find the TextView with view ID section_text_view
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_text_view);
        // Display the section of the current News in that TextView
        sectionView.setText(currentNews.getSection());



        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}


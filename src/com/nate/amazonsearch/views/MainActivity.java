package com.nate.amazonsearch.views;

import com.nate.amazonsearch.models.*;
import com.nate.amazonsearch.helpers.WebServicesHelper;
import com.nate.amazonsearch.interfaces.ItemUpdater;
import com.nate.amazonsearch.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is the starting activity for AmazonSearch. Last updated: 1/26/2013
 * 
 * @author Nate
 * 
 */
public class MainActivity extends Activity implements OnClickListener,
		ItemUpdater {

	// instance variables
	private EditText searchInput;
	private Gallery itemGallery;
	private Context context;
	private String lastSearchString;
	private ProgressBar progressSpinner;

	// onCreate is invoked when an activity is instantiated
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = getBaseContext();

		/*
		 * this class, MainActivity, implements the OnClickListener interface so
		 * we handle button clicks in the overridden onClick method
		 */
		ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(this);

		// set our search input field
		searchInput = (EditText) findViewById(R.id.search_text);

		// set up a spinning wheel
		progressSpinner = (ProgressBar) findViewById(R.id.progress);

		// set up to display the search results in a gallery of cards
		itemGallery = (Gallery) findViewById(R.id.gallery);

		lastSearchString = null;
	}

	// the search button invokes this method when it is clicked
	@Override
	public void onClick(View v) {

		// get the user's search text
		String userInput = searchInput.getText().toString();

		// check for empty input
		if (userInput.equals("")) {

			Toast.makeText(context, "please enter text", Toast.LENGTH_SHORT)
					.show();

			// check that we are not duplicating the last search
		} else if (userInput.equals(lastSearchString)) {

			Toast.makeText(context, "You previously searched for this text",
					Toast.LENGTH_SHORT).show();
		} else {

			startSearch(userInput);
			lastSearchString = userInput;
		}
	}

	private void startSearch(String query) {

		// download a list of Items from amazon
		ItemDownloader download = new ItemDownloader(query, context, this);
		WebServicesHelper helper = new WebServicesHelper(context);

		// start downloading only if a data connection is available
		if (helper.dataConnectionAvailable()) {

			download.execute();
			startProgressSpinner();
			/* the download object invokes interface methods
			  onUpdateItems or onUpdateItemsFailed  */
			
		} else {

			// alert the user if they have no network connection
			Toast.makeText(context, "Network Connection Unavailable",
					Toast.LENGTH_SHORT).show();
		}
	}

	// ItemUpdater interface method
	@Override
	public void onUpdateItems(Item[] items) {

		stopProgressSpinner();

		GallaryViewAdapter galleryAdapter = new GallaryViewAdapter(context,
				items);
		itemGallery.setAdapter(galleryAdapter);
	}

	// ItemUpdater interface method
	@Override
	public void onUpdateItemsFailed() {

		Toast.makeText(context, "Search Failed!", Toast.LENGTH_SHORT).show();

		stopProgressSpinner();
	}

	// show the progress spinner and animate
	private void startProgressSpinner() {

		progressSpinner.setIndeterminate(true);
		progressSpinner.setVisibility(View.VISIBLE);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
	}

	// hide the progress spinner and stop animation
	private void stopProgressSpinner() {

		progressSpinner.setIndeterminate(false);
		progressSpinner.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Fill the gallery with an array of items. One "card" view is added to the
	 * gallery for each item in the array.
	 */
	private class GallaryViewAdapter extends BaseAdapter {

		private Item[] items;
		private Context context;

		/**
		 * Instantiate a new GallaryViewAdapter
		 * 
		 * @param context
		 * @param items
		 */
		public GallaryViewAdapter(Context context, Item[] items) {

			this.items = items;
			this.context = context;
		}

		/**
		 * returns the amount of items in the list
		 */
		@Override
		public int getCount() {

			return items.length;
		}

		/**
		 * return the Item at specified position
		 */
		@Override
		public Item getItem(int position) {

			return items[position];
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// set each view's fields to fields of the item which corresponds to
			// it's position in the list
			Item item = items[position];

			// make the view for this item
			convertView = new View(context);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.gallary_layout, null);
			convertView.setLayoutParams(new Gallery.LayoutParams(250, 300));

			// set the view's fields
			if (item != null) {

				TextView text1 = (TextView) convertView
						.findViewById(R.id.gridview_text1);
				text1.setText(item.getName());

				TextView text2 = (TextView) convertView
						.findViewById(R.id.gridview_text2);
				text2.setText("ASIN: " + item.getAsin());
			}

			return convertView;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}
	}
}

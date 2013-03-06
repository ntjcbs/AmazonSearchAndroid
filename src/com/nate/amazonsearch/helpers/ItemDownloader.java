/*
 * Developer : Nathaniel Jacobs (ntjcbs@gmail.com)
 * All code (c)2011 Nathaniel Jacobs all rights reserved
 * 
 * THIS CODE IS PROVIDED WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED.
 * 
 * DO NOT DISTRIBUTE
 */
package com.nate.amazonsearch.helpers;


import android.content.Context;
import android.os.AsyncTask;
import com.nate.amazonsearch.interfaces.ItemUpdater;
import com.nate.amazonsearch.models.Item;

/**
 * This class is used to download item information from amazon.com 
 * 
 * Last updated: 1/9/2012
 * 
 * @author Nate
 **/

public class ItemDownloader extends AsyncTask<Void, Void, Item[]> {

	// declare instance variables
	private boolean success;
	private String searchQuery, downloadContent;
	private int attempts;
	private WebServicesHelper webServices;
	private Context mContext;
	ItemUpdater updater;

	// maximum amount of times the download will be retried
	private final int MAX_DOWNLOAD_ATTEMPTS = 3;

	/***
	 * Create a new ItemDownloader.
	 * 
	 * @param searchQuery
	 * @param context
	 */
	public ItemDownloader(String searchQuery, Context context,
			ItemUpdater updater) {

		// Initialize instance variables
		webServices = new WebServicesHelper(context);
		success = false;
		attempts = 0;
		downloadContent = null;

		// set fields from explicit parameters
		this.searchQuery = searchQuery;
		mContext = context;
		this.updater = updater;
	}

	public String getParameters() {

		return searchQuery;
	}

	public String getContent() {

		return downloadContent;
	}

	private void setSucessful(boolean successful) {

		success = successful;
	}

	public boolean isSucessful() {

		return success;
	}

	@Override
	protected Item[] doInBackground(Void... params) {

		Item[] searchItems = null;
		// download content
		String resultXml = null;

		// retry download if unsuccessful
		while (attempts < MAX_DOWNLOAD_ATTEMPTS && !success) {

			AmazonUrlCreator urlMaker = new AmazonUrlCreator(mContext);
			String url = urlMaker.searchProducts(searchQuery);

			try {
				resultXml = webServices.httpGetRequest(url);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} 

			if (resultXml != null) {
				setSucessful(true);
				downloadContent = resultXml;
			}

			attempts++;
		}

		// process the downloaded information
		if (isSucessful()) {
			XmlParser parser = new XmlParser();
			searchItems = parser.parseSearchXml(getContent());
			
		} else {
			updater.onUpdateItemsFailed();
		}
		

		return searchItems;
	}

	@Override
	protected void onPostExecute(Item[] result) {

		if (result != null) {

			// use the interface reference type to set the result in the
			// MainActivity while keeping the classes loosely coupled
			updater.onUpdateItems(result);
		}
	}
}

/*
 * Developer : Nathaniel Jacobs (ntjcbs@gmail.com)
 * All code (c)2011 Nathaniel Jacobs all rights reserved
 * 
 * THIS CODE IS PROVIDED WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED.
 * 
 * DO NOT DISTRIBUTE
 */
package com.nate.amazonsearch.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class is used perform web services. Last updated: 1/9/2012
 * 
 * @author Nate
 **/
public class WebServicesHelper {

	private Context mContext;

	/**
	 * Instantiate a new WebServicesHelper object
	 * 
	 * @param context
	 */
	public WebServicesHelper(Context context) {

		mContext = context;
	}

	/**
	 * Check if Internet access is available;
	 * 
	 * @return true when data connection is available
	 */
	public boolean dataConnectionAvailable() {

		ConnectivityManager connection = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connection
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo cellular = connection
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isConnected()) {
			return true;
		} else if (!cellular.isConnected()) {
			return false;
		} else if (cellular.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * Perform HTTP get request.
	 * 
	 * @param url
	 * @return Complete contents of get response in a string
	 * 
	 * @throws IllegalStateException
	 */
	public String httpGetRequest(String url) throws IllegalStateException {

		if (url != null && url != "") {

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = null;
			StringBuilder total = null;
			BufferedReader buffReader = null;

			try {
				response = client.execute(request);
				InputStream content = response.getEntity().getContent();

				// read content
				buffReader = new BufferedReader(new InputStreamReader(content));
				total = new StringBuilder();
				String line;

				while ((line = buffReader.readLine()) != null) {
					total.append(line);
				}

			} catch (IOException e) {
				// ignore
			} finally {
				try {
					buffReader.close();
				} catch (IOException e) {
					// ignore
				}
			}

			String getResponse = total.toString();
			// Log.i("response", getResponse);

			return getResponse;
		}
		return null;
	}

	/**
	 * Download and return a bitmap specified by url.
	 * 
	 * @param url
	 *            of the image.
	 * @return Bitmap
	 * @throws IOException
	 *             If connection error
	 */
	public Bitmap DownloadBitmapFromUrl(String url) {

		if (url != null && url != "") {

			// use the BitmapFactory class to load an image from an InputStream
			InputStream is = null;
			Bitmap temporaryBitmap = null;
			try {
				is = connect(url);
				temporaryBitmap = BitmapFactory.decodeStream(is);
			} catch (MalformedURLException e) {
				// ignore
			} catch (IOException e) {
				
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}

			return temporaryBitmap;

		} else {
			return null;
		}
	}

	private InputStream connect(String url) throws MalformedURLException,
			IOException {

		URL mUrl = new URL(url);
		InputStream is = null;

		URLConnection ucon = mUrl.openConnection();
		ucon.setConnectTimeout(1500);
		ucon.setReadTimeout(3000);
		is = ucon.getInputStream();

		return is;
	}
}

/*
 * Developer : Nathaniel Jacobs (ntjcbs@gmail.com)
 * All code (c)2011 Nathaniel Jacobs all rights reserved
 * 
 * THIS CODE IS PROVIDED WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED.
 * 
 * DO NOT DISTRIBUTE
 */
package com.nate.amazonsearch.models;

import android.graphics.Bitmap;

/**
 * Objects of this class represent Items found on Amazon.com
 * 
 * Last updated: 1/9/2012
 * 
 * @author Nate
 **/
public class Item {

	private String asin, mUrl, mName, mBitmapUrl;
	private Bitmap mBitmap;

	/**
	 * Instantiate a new Item object
	 */
	public Item() {

		// Amazon Standard Identification Number
		asin = null;
		mUrl = null;
		mName = null;
		mBitmapUrl = null;
		mBitmap = null;
	}

	public void setAsin(String ASIN) {

		asin = ASIN;
	}

	public String getAsin() {

		return asin;
	}

	public void setUrl(String url) {

		mUrl = url;
	}

	public String getUrl() {

		return mUrl;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {

		return mName;
	}

	public void setBitmapUrl(String url) {

		mBitmapUrl = url;
	}

	public String getBitmapUrl() {

		return mBitmapUrl;
	}

	public void setBitmap(Bitmap bitmap) {

		mBitmap = bitmap;
	}

	public Bitmap getBitmap() {

		return mBitmap;
	}

}

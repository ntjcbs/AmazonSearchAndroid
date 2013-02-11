/*
 * Developer : Nathaniel Jacobs (ntjcbs@gmail.com)
 * All code (c)2011 Nathaniel Jacobs all rights reserved
 * 
 * THIS CODE IS PROVIDED WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED.
 * 
 * DO NOT DISTRIBUTE
 */
package com.nate.amazonsearch.helpers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.nate.amazonsearch.config.Constants;

import android.content.Context;
import android.util.Log;

/**
 * Helper class for making Amazon urls.  
 * Last updated: 1/9/2012
 * @author Nate
 **/
public class AmazonUrlCreator {
	
	private Context context;
	
	/**
	 * Instantiates a new AmazonUrlCreator object
	 * @param context
	 */
	public AmazonUrlCreator(Context context){
		
		this.context = context;
	}

	/**
	 * Creates a signed string for searching all amazon products
	 * @param searchString
	 * @return a signed, URL encoded string
	 */
	public String searchProducts(String searchString){
		
        String signedUrl = null;
        String url = Constants.ENDPOINT;
        
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("AWSAccessKeyId", Constants.AWS_ACCESS_KEY_ID));
        params.add(new BasicNameValuePair("Keywords", searchString));
        params.add(new BasicNameValuePair("Operation", "ItemSearch"));
        params.add(new BasicNameValuePair("Service", "AWSECommerceService"));
        params.add(new BasicNameValuePair("AssociateTag", "tag"));
        params.add(new BasicNameValuePair("SearchIndex", "All"));
        params.add(new BasicNameValuePair("Availability", "Available"));
        
    	String paramString = URLEncodedUtils.format(params, "utf-8");
	  
    	url += paramString;
    	Log.i("unsigned url", url);
    	
        	SignedRequestsHelper signer;
        	
			try {
				
				signer = SignedRequestsHelper.getInstance(
						Constants.ENDPOINT, Constants.AWS_ACCESS_KEY_ID, Constants.AWS_SECRET_KEY);
				
	        	signedUrl = signer.sign(url);
	        	
	        	Log.i("signned url", signedUrl);
			} catch (InvalidKeyException e) {
				// if the key is invalid due to a programming error
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}

		return signedUrl;
	}
}

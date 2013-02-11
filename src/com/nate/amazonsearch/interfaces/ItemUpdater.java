package com.nate.amazonsearch.interfaces;

import com.nate.amazonsearch.models.Item;

/***
 * Classes which implement this interface should be able to update their list of items. 
 * @author nate
 *
 */
public interface ItemUpdater {

	/**
	 * Update the list of items.
	 * @param items
	 */
	public void onUpdateItems(Item[] items);
	
	/**
	 * Items were not loaded.
	 * 
	 */
	public void onUpdateItemsFailed();
}

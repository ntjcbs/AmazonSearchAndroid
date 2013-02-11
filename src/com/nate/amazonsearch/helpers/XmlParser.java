/*
 * Developer : Nathaniel Jacobs (ntjcbs@gmail.com)
 * All code (c)2011 Nathaniel Jacobs all rights reserved
 * 
 * THIS CODE IS PROVIDED WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED.
 * 
 * DO NOT DISTRIBUTE
 */
package com.nate.amazonsearch.helpers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nate.amazonsearch.models.Item;

import android.util.Xml;

/**
 * Helper class for parsing XML returned by amazon
 * 
 * Last updated: 1/9/2012
 * 
 * @author Nate
 **/
public class XmlParser {

	/**
	 * Takes XML from amazon and returns array of items
	 * 
	 * @param xml
	 *            from amazon product search
	 * @return Array of Items parsed from XMl
	 */
	public Item[] parseSearchXml(String xml) {

		XMLSearchHandler handler = new XMLSearchHandler();

		try {

			Xml.parse(xml, handler);
		} catch (SAXException e) {

			e.printStackTrace();
		}

		return handler.getXMLData();
	}

	// this class decodes XML and returns a list of items
	private class XMLSearchHandler extends DefaultHandler {

		final static int MAX_ELEMENTS = 10;

		String elementValue;
		Boolean elementOn;
		Item item;
		Item[] items;
		int incrementor;

		/**
		 * Instantiate a new XMLSearchHandler object
		 */
		public XMLSearchHandler() {

			elementValue = null;
			elementOn = false;
			item = null;
			items = new Item[MAX_ELEMENTS];
			incrementor = 0;
		}

		/**
		 * Get an array of Items which were loaded from xml
		 * 
		 * @return an array of Items
		 */
		public Item[] getXMLData() {

			return items;
		}

		/**
		 * This will be called when the XML tags start.
		 **/
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			elementOn = true;

			if (localName.equals("Item")) {

				item = new Item();
			}
		}

		/**
		 * This will be called when the XML tags end.
		 **/
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			elementOn = false;

			// Set values after retrieving them from the XML tags
			if (localName.equalsIgnoreCase("ASIN")) {

				item.setAsin(elementValue);
			} else if (localName.equalsIgnoreCase("DetailPageURL")) {

				item.setUrl(elementValue);
			} else if (localName.equalsIgnoreCase("Title")) {

				item.setName(elementValue);
			} else if (localName.equalsIgnoreCase("MediumImage")) {

				item.setBitmapUrl((elementValue));
			} else if (localName.equals("Item") && incrementor < MAX_ELEMENTS) {

				if (item.getAsin() != null) {

					items[incrementor] = item;
					incrementor++;
					item = null;
				}
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			if (elementOn) {

				elementValue = new String(ch, start, length);
				elementOn = false;
			}
		}
	}
}

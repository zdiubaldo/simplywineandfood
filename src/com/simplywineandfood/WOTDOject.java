package com.simplywineandfood;

public class WOTDOject {
	public String WINE_NAME;
	public String WINE_GRAPE;
	public String PRONUNCIATION;
	public String WINE_COLOR;
	public String WINE_DETAILS;
	public String WINE_PAIRING;
	public String WINE_BRANDS;
	public String WEBSITE_LINK;
	public String DESSERTS;
	public String CHEESE;
	public String SOUPS;
	public String SALADS;
	public String APPETISERS;
	
	WOTDOject()
	{
		clearData();		
	}
	
	public void clearData()
	{
		WINE_NAME = "";	
		WINE_GRAPE = "";
		PRONUNCIATION = "";
		WINE_COLOR = "";
		WINE_DETAILS = "";
		WINE_PAIRING = "";
		WINE_BRANDS = "";
		WEBSITE_LINK = "";
		CHEESE = "";
		DESSERTS = "";
		SOUPS = "";
		SALADS = "";
		APPETISERS = "";			
	}
}

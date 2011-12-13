package com.simplywineandfood;


import com.admob.android.ads.AdView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WineDetailsActivity extends Activity 
{	
	private String wineChoice;
	DbManager mydb = null;
	String LOG_TAG = "SimplyWineFood";
	String foodWineDetails [];
	String spiceWineDetails [];
	String soupWineDetails [];
	WebView mwebView = null;
	LinearLayout topLayout;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winedetails_layout);
        
        topLayout = (LinearLayout) this.findViewById(R.id.wineDetailsTopLayout);
        
        // grab passed in args from parent activity
        Bundle myBundle = this.getIntent().getExtras();
        if (myBundle != null)
        {        	
        	wineChoice = myBundle.getString("wineDetailsChoice");
        }
              
        mwebView = new WebView(this);
                   
        // work with the DB
        try
        {
        	mydb = DbManager.getInstance(this);
        }
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "WineDetailsActivity: could not get DB");
        }        
        
        AdView adView = (AdView)this.findViewById(R.id.ad);
        try
        {
	        // request ad
	        adView.requestFreshAd();
        }
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not get Ad");
        }
        
        if (wineChoice.equals("wotd"))
        {
        	displayWOTD();        	
        }
        else
        {
        	displayWineDetails();        	
        }
        
         
    }
    
    private void displayWineDetails()
    {
    	try
        {
	        // look up details of the wine selected
	        String mySQLQuery = "SELECT WINE_TYPE._id, WINE_TYPE.NAME, WINE_TYPE.DETAILS, WINE_TYPE.COLOR, WINE_TYPE.DESCRIPTION, WINE_TYPE.GEOGRAPHY, WINE_TYPE.PRONUNCIATION, WINE_TYPE.APP, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE WHERE WINE_TYPE.NAME=" + "\""+ wineChoice + "\"";
	        mydb.getSQLDataFromWineTable(mySQLQuery);
	        
	        // grab all the pairing to list
	        mySQLQuery = "SELECT SPICE_TYPE.NAME FROM SPICE_TYPE JOIN PAIR_SPICE ON SPICE_TYPE._id = PAIR_SPICE.spice_id  WHERE PAIR_SPICE.wine_id=" + mydb.dbManagerWineTableSQLQueryID;
	        spiceWineDetails =  mydb.getSQLData(mySQLQuery);
	        
	        mySQLQuery = "SELECT FOOD_TYPE.NAME FROM FOOD_TYPE JOIN PAIR_FOOD ON FOOD_TYPE._id = PAIR_FOOD.food_id  WHERE PAIR_FOOD.wine_id=" + mydb.dbManagerWineTableSQLQueryID;
	        foodWineDetails = mydb.getSQLData(mySQLQuery);
	        
	        mySQLQuery = "SELECT SOUP_TYPE.NAME FROM SOUP_TYPE JOIN PAIR_SOUP ON SOUP_TYPE._id = PAIR_SOUP.soup_id  WHERE PAIR_SOUP.wine_id=" + mydb.dbManagerWineTableSQLQueryID;
	        soupWineDetails = mydb.getSQLData(mySQLQuery);
	        
        }
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "WineDetailsActivity: could not wine details");
        }  
        
        // set the color of the text to match the wine color
        String colorOfText = "";
        if (mydb.dbManagerWineTableSQLQueryCOLOR.equalsIgnoreCase("RED"))
        {
        	colorOfText = "#FFCCCC";        	
        }
        else
        {
        	colorOfText = "#FFFFCC";        	
        }        
        
        mwebView.setBackgroundColor(0);
        String webText ="";
        webText = "<html><style type=\"text/css\"> dd { margin: 0; }</style><body TEXT=";
        webText = webText + "\"";
        webText = webText + colorOfText;
        webText = webText + "\">";
        webText = webText +	"<dl>";
        webText = webText +	"<dt>Wine Name:</dt>";
        webText = webText +	"<dd><ul TYPE=DISK>";
        webText = webText + "<li>" + mydb.dbManagerWineTableSQLQueryName + " (" +
        					         mydb.dbManagerWineTableSQLQueryGRAPE + ") <i>" + 
        					         mydb.dbManagerWineTableSQLQueryPRONUNCIATION + "</i></li>";
        webText = webText + "</ul></dd>";
        webText = webText +	"<dl><dt>Wine Details:</dt>";
        webText = webText +	"<dd><ul TYPE=DISK>";
        if(mydb.dbManagerWineTableSQLQueryGEOGRAPHY != null)
        {
        	webText = webText + "<li>REGION: " + mydb.dbManagerWineTableSQLQueryGEOGRAPHY + "</li>";
        }
        if (mydb.dbManagerWineTableSQLQueryDESCRIPTION != null)
        {
        	webText = webText + "<li>DESCRIPTION: " + mydb.dbManagerWineTableSQLQueryDESCRIPTION + "</li>";
        }
        if (foodWineDetails.length > 0)
        {
        	webText = webText + "<li>FOODS: ";
	        int i = 0;
	        while (i < foodWineDetails.length)
	        {
	        	webText = webText + foodWineDetails[i];
	        	if (i < foodWineDetails.length-1)
	        	{
	        		webText = webText + ", ";
	        	}
	        	i++;
	        }
	        webText = webText + "</li>";
        }
        if (spiceWineDetails.length > 0)
        {
	        webText = webText + "<li>SPICES: ";
	        int i = 0;
	        while (i < spiceWineDetails.length)
	        {
	        	webText = webText + spiceWineDetails[i];
	        	if (i < spiceWineDetails.length-1)
	        	{
	        		webText = webText + ", ";
	        	}
	        	i++;
	        }
	        webText = webText + "</li>";
        }  
        if (soupWineDetails.length > 0)
        {
            webText = webText + "<li>SOUPS: ";
	        int i = 0;
	        while (i < soupWineDetails.length)
	        {
	        	webText = webText + soupWineDetails[i];
	        	if (i < soupWineDetails.length-1)
	        	{
	        		webText = webText + ", ";
	        	}
	        	i++;
	        }
	        webText = webText + "</li>";
        }
        webText = webText + "<li>APPETIZER IDEA: " + mydb.dbManagerWineTableSQLQueryAPP + "</li>";
        webText = webText + "</ul></dd>";
        webText = webText + "</dl></body></html>";
        mwebView.loadData(webText, "text/html", "utf-8");
        topLayout.addView(mwebView);
    }
    
    private void displayWOTD()
    {
    	WOTDOject wotdObj = null;
    	try
        {
    		wotdObj = mydb.getWOTDTableData(1);
	        
        }
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "WineDetailsActivity: could not wine details");
        }  
        
        // set the color of the text to match the wine color
        String colorOfText = "";
        if (wotdObj.WINE_COLOR.equalsIgnoreCase("RED"))
        {
        	colorOfText = "#FFCCCC";        	
        }
        else
        {
        	colorOfText = "#FFFFCC";        	
        }        
        
        mwebView.setBackgroundColor(0);
        String webText ="";
        webText = "<html><style type=\"text/css\"> dd { margin: 0; }</style><body TEXT=";
        webText = webText + "\"";
        webText = webText + colorOfText;
        webText = webText + "\">";
        webText = webText +	"<dl>";
        webText = webText +	"<dt>Wine Name:</dt>";
        webText = webText +	"<dd><ul TYPE=DISK>";
        webText = webText + "<li>" + wotdObj.WINE_NAME + " (" +
        					         wotdObj.WINE_GRAPE + ") <i>" + 
        					         wotdObj.PRONUNCIATION + "</i></li>";
        webText = webText + "</ul></dd>";
        webText = webText +	"<dl><dt>Wine Details:</dt>";
        webText = webText +	"<dd><ul TYPE=DISK>";
        if(wotdObj.WINE_COLOR != "")
        {
        	webText = webText + "<li>COLOR: " + wotdObj.WINE_COLOR + "</li>";
        }
        if (wotdObj.WINE_DETAILS != "")
        {
        	webText = webText + "<li>DETAILS: " + wotdObj.WINE_DETAILS + "</li>";
        }
        if (wotdObj.WINE_PAIRING != "")
        {
        	webText = webText + "<li>PAIRING: " + wotdObj.WINE_PAIRING + "</li>";
        }
        if (wotdObj.WINE_BRANDS != "")
        {
        	webText = webText + "<li>BRANDS: " + wotdObj.WINE_BRANDS + "</li>";
        }
        if (wotdObj.WEBSITE_LINK != "")
        {
        	webText = webText + "<li>WEB LINK: " + wotdObj.WEBSITE_LINK + "</li>";
        }
        if (wotdObj.CHEESE_DESSERTS != "")
        {
        	webText = webText + "<li>CHEESE DESSERTS: " + wotdObj.CHEESE_DESSERTS + "</li>";
        }
        if (wotdObj.SOUPS != "")
        {
        	webText = webText + "<li>SOUPS: " + wotdObj.SOUPS + "</li>";
        }
        if (wotdObj.SALADS_APPETISERS != "")
        {
        	webText = webText + "<li>SALADS APPETISERS: " + wotdObj.SALADS_APPETISERS + "</li>";
        }
        webText = webText + "</ul></dd>";
        webText = webText + "</dl></body></html>";
        mwebView.loadData(webText, "text/html", "utf-8");
        topLayout.addView(mwebView);
    }
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optional_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle item selection
        switch (item.getItemId()) 
        {
        case R.id.optional_quit:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    */
}

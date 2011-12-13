package com.simplywineandfood;


import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SimplyWineActivity extends Activity 
{
	private DbManager mydb;
	String LOG_TAG = "SimplyWineFood";
	//private SWFXMLHandler myXMLHandler;
	//private ListView todList;
	//private ArrayAdapter<String> todListAdapter  = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        
        // work with the DB
        try
        {
        	mydb = DbManager.getInstance(this);
        }
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SimplyWineActivity: could not get DbManager.getInstance");
        }
        
        // get admob working
        //AdManager.setTestDevices( new String[] {AdManager.TEST_EMULATOR, "012345678994814751742145548AAAAAAA"});
        
        // get handles to UI objects
        Button spiceNoteButton = (Button) this.findViewById(R.id.spicenoteBtn);
        Button cheeseButton = (Button) this.findViewById(R.id.cheeseBtn);
        Button soupButton = (Button) this.findViewById(R.id.soupBtn);
        Button wineButton = (Button) this.findViewById(R.id.wineBtn); 
        Button dessertButton = (Button) this.findViewById(R.id.dessertBtn);
        Button wineOfTheDayButton = (Button) this.findViewById(R.id.wineOfTheDayBtn); 
        
      
//        //Button wineLogButton = (Button) this.findViewById(R.id.wineLogBtn); 
//        AdView adView = (AdView)this.findViewById(R.id.ad);
//      
//        
//        // request ad
//        try
//        {
//        	adView.requestFreshAd();
//        }
//        catch (Exception e)
//        {
//        	Log.v(LOG_TAG, "SimplyWineActivity: could not get Ad");
//        }
        
        
        // grab tip of the day
//        try 
//        {       	
//        	/** Handling XML */
//        	SAXParserFactory spf = SAXParserFactory.newInstance();
//        	SAXParser sp = spf.newSAXParser();
//        	XMLReader xr = sp.getXMLReader();
//
//        	/** Send URL to parse XML Tags */
//        	URL sourceUrl = new URL(
//        	"http://192.168.1.153/test.xml");
//
//        	/** Create handler to handle XML Tags ( extends DefaultHandler ) */
//        	myXMLHandler = new SWFXMLHandler();
//        	xr.setContentHandler(myXMLHandler);
//        	xr.parse(new InputSource(sourceUrl.openStream()));
//
//         } 
//         catch (Exception e) 
//         {        	
//        	 Log.v(LOG_TAG, "SimplyWineActivity: parsing eception = " + e);
//         }
        	
        
        // action jackson
        spiceNoteButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "food";
                launchSpiceNoteActivity(buttonSelect);
            }
        });
        
        // action jackson
        cheeseButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "cheese";
            	launchSpiceNoteActivity(buttonSelect);
            }
        });
        
        // action jackson
        soupButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "soup";
            	launchSpiceNoteActivity(buttonSelect);
            }
        });
        
        // action jackson
        wineButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "wine";
            	launchSpiceNoteActivity(buttonSelect);
            }
        });
        
     // action jackson
        dessertButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "dessert";
            	launchSpiceNoteActivity(buttonSelect);
            }
        });
        
        // action jackson
        /*
        dessertButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "dessert";
            	launchSpiceNoteActivity(buttonSelect);
            }
        });
        */
     
        // action 	jackson
        wineOfTheDayButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "wotd";
            	launchWineDetailsActivity(buttonSelect);
            }
        });
     
     
        /*
        // action jackson
        wineLogButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	String buttonSelect = "winelog";
            	launchWineLogActivity(buttonSelect);
            }
        });
        */
        
        // create listener for resultsView View
    }
    /*
    @Override
    public void onDestroy() 
    {
    	try
    	{
    		mydb.closeDataBase();
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SimplyWineActivity: failed to close DB");
        }
    }
    */
    
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
    
    /**
     * Launches the SpiceNote activity to pair wine with spice note
     */
    protected void launchSpiceNoteActivity(String buttonSelect) 
    { 	    	    	
    	try
    	{
	        Intent i = new Intent(this, SpiceNoteActivity.class);
	        i.putExtra("buttonChoice", buttonSelect);
	        startActivity(i);
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SimplyWineActivity: could not start SpiceNoteActivity");
        }
    }
    
    /**
     * Launches the SpiceNote activity to pair wine with spice note
     */
    protected void launchWineDetailsActivity(String buttonSelect) 
    { 	    	    	
    	try
    	{
	        Intent i = new Intent(this, WineDetailsActivity.class);
	        i.putExtra("wineDetailsChoice", buttonSelect);
	        startActivity(i);
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SimplyWineActivity: could not start SpiceNoteActivity");
        }
    }   
    
    
    protected void launchWineLogActivity(String buttonSelect) 
    { 	    	    	
//    	try
//    	{
//	        Intent i = new Intent(this, WineLogActivity.class);
//	        i.putExtra("buttonChoice", buttonSelect);
//	        startActivity(i);
//    	}
//        catch (Exception e)
//        {
//        	Log.v(LOG_TAG, "SimplyWineActivity: could not start SpiceNoteActivity");
//        }
    }
    
    
}
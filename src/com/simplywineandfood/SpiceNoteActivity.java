package com.simplywineandfood;


import com.admob.android.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpiceNoteActivity extends Activity 
{		
	String LOG_TAG = "SimplyWineFood";
	private String buttonChoice = "";
	private int foodTypeChoice = 0;
	private int spiceTypeChoice = 0;
	private Spinner spiceSelectSpinner;
	private Spinner foodSelectSpinner;
	private ListView resultsView  = null;
	private DbManager mydb  = null;
	private ArrayAdapter<String> adapter  = null;
	private ArrayAdapter<String> spiceAdapter  = null;
	private ArrayAdapter<String> wineListAdapter  = null;
	private String mySQLQuery = "";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spicenote_layout);
           
        // grab passed in args from parent activity
        Bundle myBundle = this.getIntent().getExtras();
        if (myBundle != null)
        {        	
        	buttonChoice = myBundle.getString("buttonChoice");
        }
        
        // get handles to UI objects
        foodSelectSpinner = (Spinner) findViewById(R.id.foodSelect);        
        spiceSelectSpinner = (Spinner) findViewById(R.id.spiceSelect); 
        // first time through this spinner is disabled
        // we enable it the first time the user
        // clicks the food type spinner
        spiceSelectSpinner.setVisibility(View.GONE);
        resultsView = (ListView) findViewById(R.id.resultsView);
        // hide results until we have some
        resultsView.setVisibility(View.GONE);
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
        
        try
        {
	        // work with the DB
	        mydb = DbManager.getInstance(this);     	
        }
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not get DB");
        }
	        
        // setup each adapters
        if (buttonChoice.equals("food"))
        {
        	setupFoodAdapter();
        }
        else if (buttonChoice.equals("cheese"))
        {
        	setupCheeseAdapter();
        }
        else if (buttonChoice.equals("soup"))
        {
        	setupSoupAdapter();
        }
        else if (buttonChoice.equals("wine"))
        {  		
        	setupWineAdapter();
        }
        else if (buttonChoice.equals("dessert"))
        {  		
        	setupDessertAdapter();
        }
        else
        {
        	return;        	
        }
        
        wineListAdapter = new ArrayAdapter<String>(this,
        										   android.R.layout.simple_list_item_1);        
        resultsView.setAdapter(wineListAdapter);
        
        // create listener for foodtype spinner
        foodSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        { 
        	 public void onItemSelected(AdapterView<?> parent, 
        			                    View view, 
        			                    int position, 
        			                    long id)
        	 {
        		 foodTypeChoice = position;
        		 
        		 // we don't care about the first position since its
        		 // static and the database _id start at 1
        		 if (foodTypeChoice == 0)
        		 {
        			 if (buttonChoice.equals("food"))
        			 {
            			 if (foodTypeChoice == 0 &&
            				 spiceTypeChoice == 0)
                		 { 
            				 wineListAdapter.clear();
            				 wineListAdapter.add("No Results");
            		  		 resultsView.setClickable(false);
            		  		 
            		  		 spiceAdapter.clear();
        	       			 spiceAdapter.add("Select Spice");
            		  		 
            		  		 return;
                		 }
        			 }
        			 else
        			 {
        				 return;
        			 }
        		 } 		 
        		 
        		 //if (spiceTypeChoice != 0)
        		 {
        			// on the first time through this we will enable the
    	       		// spice spinner, only if this is the food/spice button
        			 if (buttonChoice.equals("food"))
        			 {        			
        				 spiceSelectSpinner.setVisibility(View.VISIBLE); 
        				 
        				 // we deleted some food items so the array count and
        				 // id pairing is off, so a more accurate way to get
        				 // the correct food item is to look it up using the 
        				 // string picked by the user then getting the id to 
        				 // pair        				    	       			
    	       			 String foodChoiceQuery = "SELECT FOOD_TYPE._id FROM FOOD_TYPE WHERE FOOD_TYPE.NAME=\""
    	       				 + (String) foodSelectSpinner.getItemAtPosition(position)
    	       				 + "\"";
    	       			 foodTypeChoice = mydb.getSQLDataIDFromString(foodChoiceQuery);
    	       			
    	       			 // only show spices that will pair with the wines we will get below
    	       			 foodChoiceQuery = "SELECT WINE_TYPE._id FROM WINE_TYPE JOIN PAIR_FOOD ON WINE_TYPE._id=PAIR_FOOD.wine_id WHERE PAIR_FOOD.food_id="
    	       				 + foodTypeChoice;
    	       			 int wineIDArray [] = mydb.getSQLArrayID(foodChoiceQuery);    	  

    	       			   	       			  
    	       			 String spiceSQLQuery = "SELECT DISTINCT SPICE_TYPE.NAME AS NAME FROM SPICE_TYPE JOIN PAIR_SPICE ON SPICE_TYPE._id = PAIR_SPICE.spice_id WHERE PAIR_SPICE.wine_id IN (";
    	       			 for(int i = 0; i < wineIDArray.length; i++)
        	       		 {
    	       				spiceSQLQuery = spiceSQLQuery + wineIDArray[i];
    	       				if ( i != (wineIDArray.length-1))
    	       				{
    	       					spiceSQLQuery =  spiceSQLQuery + ",";
    	       				}
    	       			 }
    	       			spiceSQLQuery = spiceSQLQuery + ")";
    	       			String tmp [];
    	       			tmp = mydb.getSQLData(spiceSQLQuery);
    	       			spiceAdapter.clear();
    	       			spiceAdapter.add("Select Spice");
    	       			for(int i = 0; i < tmp.length; i++)
   	       			 	{
    	       				spiceAdapter.add(tmp[i]);
   	       			 	}
    	       			
    	       			// reset spice spinner to zero
    	       			spiceSelectSpinner.setSelection(0);
    	       			
    	       			// reset food query to all results for specific food type
    	       			mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_FOOD ON WINE_TYPE._id=PAIR_FOOD.wine_id WHERE PAIR_FOOD.food_id=%d";    	       		  	       			
    	            }        			 
        		 }
        		 
        		 if (buttonChoice.equals("wine"))
        	     {        			 
        			 String wineChoice = (String) foodSelectSpinner.getItemAtPosition(position);
        	     }
        		 
        		 setResultsList(foodTypeChoice, spiceTypeChoice);
        		 resultsView.setVisibility(View.VISIBLE); 
        	 } 
        	 
        	 public void onNothingSelected(AdapterView<?> parent) 
        	 { 
        	 } 
        });
                
        // create listener for spice spinner	
        spiceSelectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        { 
	       	 public void onItemSelected(AdapterView<?> parent, 
	       			                    View view, 
	       			                    int position, 
	       			                    long id)
	       	 {
	       		 spiceTypeChoice = position;
	       		 
	       		 // we don't care about the first position since its
        		 // static and the database _id start at 1
	       		 if (buttonChoice.equals("food"))
	       		 {
	       			 if (foodTypeChoice == 0)
	       			 { 
	       				 wineListAdapter.clear();
	       				 wineListAdapter.add("No Results");
	       				 resultsView.setClickable(false);
	       				 return;
	       			 }
	       			 
	       			 if (spiceTypeChoice == 0)
	       			 {
	       				 return;	       				 
	       			 }
	       		 }  		 
	       		 
	       		 // since we are out of order, grab the string name of the spice,
	       		 // lookup the _id, and setup the final wine list query
	       		 String spiceChoiceQuery = "SELECT SPICE_TYPE._id FROM SPICE_TYPE WHERE SPICE_TYPE.NAME=\"" 
	       			                      + (String) spiceSelectSpinner.getItemAtPosition(position)
	       			                      + "\"";
	       		 spiceTypeChoice = mydb.getSQLDataIDFromString(spiceChoiceQuery);
	       		 mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_FOOD ON WINE_TYPE._id=PAIR_FOOD.wine_id JOIN PAIR_SPICE ON WINE_TYPE._id=PAIR_SPICE.wine_id WHERE PAIR_FOOD.food_id=%d and PAIR_SPICE.spice_id=%d";
	       		 setResultsList(foodTypeChoice, spiceTypeChoice);
	       		 resultsView.setVisibility(View.VISIBLE);
	       	 } 
	       	 
	       	 public void onNothingSelected(AdapterView<?> parent) 
	       	 { 
	       	 } 
        });
        
        // create listener for resultsView View
        resultsView.setOnItemClickListener(new OnItemClickListener() 
        {          	 
        	public void onItemClick(AdapterView<?> a, 
        							View v, 
        							int position, 
        							long id) 
        	{
        		String wineChoice = (String) resultsView.getItemAtPosition(position);
        		launchWineDetailsActivity(wineChoice);
        	}  
        }); 
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

    private void setupFoodAdapter()
    {
    	if (mydb.foodType == null)
    	{
    		return;        		
    	}
    	
    	if (mydb.foodType.length <= 0)
    	{
    		return;        		
    	}
    	
    	try
    	{
	        adapter = new ArrayAdapter<String>(this,
	        								   android.R.layout.simple_spinner_item, 
	        								   mydb.foodType);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	        foodSelectSpinner.setAdapter(adapter);
	        spiceAdapter = new ArrayAdapter<String>(this,
													android.R.layout.simple_spinner_item);
	        
	        // no need to add anything to spice adpater since it will be filled
	        // in later
	        spiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                
	        spiceSelectSpinner.setAdapter(spiceAdapter);
	        
	        // setup base SQL query for food/spice
	        mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_FOOD ON WINE_TYPE._id=PAIR_FOOD.wine_id WHERE PAIR_FOOD.food_id=%d";
	        //mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_FOOD ON WINE_TYPE._id=PAIR_FOOD.wine_id JOIN PAIR_SPICE ON WINE_TYPE._id=PAIR_SPICE.wine_id WHERE PAIR_FOOD.food_id=%d and PAIR_SPICE.spice_id=%d";
	        //mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.COLOR FROM WINE_TYPE JOIN PAIR ON WINE_TYPE._id = PAIR.wine_id WHERE PAIR.food_id=%d AND PAIR.spice_id=%d";
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not setup food spinners");
        }    	
    }
    
    private void setupCheeseAdapter()
    {
    	if (mydb.cheeseType == null)
    	{
    		Log.v(LOG_TAG, "SpiceNoteActivity: cheese info not in DB");
    		return;
    	}
    	
    	if (mydb.cheeseType.length <= 0)
    	{
    		return;        		
    	}
    	
    	try
    	{
    		// change the prompt to match what we are selling
    		foodSelectSpinner.setPromptId(R.string.cheese_select_prompt);
    		
        	adapter = new ArrayAdapter<String>(this,
					   android.R.layout.simple_spinner_item, 
					   mydb.cheeseType);
        	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            foodSelectSpinner.setAdapter(adapter);
            
            // set this because we don't care about the spice note spin box
            spiceTypeChoice = 1;
            
            // setup base SQL query for recipe
            mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_CHEESE ON WINE_TYPE._id=PAIR_CHEESE.wine_id WHERE PAIR_CHEESE.cheese_id=%d";
            //mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.COLOR FROM WINE_TYPE JOIN PAIR ON WINE_TYPE._id = PAIR.wine_id WHERE PAIR.cheese_id=%d";
            
            // on the single items no need to have the user
            // select on first display of spinner
            foodSelectSpinner.performClick();
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not setup cheese spinner");
        }    	
    }
    
    private void setupSoupAdapter()
    {
    	if (mydb.soupType == null)
    	{
    		Log.v(LOG_TAG, "SpiceNoteActivity: soup info not in DB");
    		return;
    	}
    	
    	if (mydb.soupType.length <= 0)
    	{
    		return;
    	}
    	
    	try
    	{       		
    		// change the prompt to match what we are selling
    		foodSelectSpinner.setPromptId(R.string.soup_select_prompt);
    		
        	adapter = new ArrayAdapter<String>(this,
					   android.R.layout.simple_spinner_item, 
					   mydb.soupType);
        	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            foodSelectSpinner.setAdapter(adapter);
            
            // set this because we don't care about the spice note spin box
            spiceTypeChoice = 1;
            
            // setup base SQL query for recipe
            mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_SOUP ON WINE_TYPE._id=PAIR_SOUP.wine_id WHERE PAIR_SOUP.soup_id=%d";
            //mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.COLOR FROM WINE_TYPE JOIN PAIR ON WINE_TYPE._id = PAIR.wine_id WHERE PAIR_SOUP.soup_id=%d";
            
            // on the single items no need to have the user
            // select on first display of spinner
            foodSelectSpinner.performClick();
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not setup soup spinner");
        }
    }
    
    private void setupWineAdapter()
    {
    	try
    	{
    		// change the prompt to match what we are selling
    		foodSelectSpinner.setPromptId(R.string.wine_select_prompt);
    		
    		String wineColors [] = {"Select Wine Color", "Red", "White"};
        	adapter = new ArrayAdapter<String>(this,
					   android.R.layout.simple_spinner_item, 
					   wineColors);
        	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            foodSelectSpinner.setAdapter(adapter);
            
            // set this because we don't care about the spice note spin box
            spiceTypeChoice = 1;
            
            // setup base SQL query for recipe
            mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE WHERE WINE_TYPE.COLOR=%s";
            
            // on the single items no need to have the user
            // select on first display of spinner
            foodSelectSpinner.performClick();
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not setup wine spinner");
        }
    }
    
    private void setupDessertAdapter()
    {
    	if (mydb.dessertType == null)
    	{
    		Log.v(LOG_TAG, "SpiceNoteActivity: d info not in DB");
    		return;
    	}
    	
    	if (mydb.dessertType.length <= 0)
    	{
    		return;
    	}
    	
    	try
    	{
    		// change the prompt to match what we are selling
    		foodSelectSpinner.setPromptId(R.string.dessert_select_prompt);
    		
        	adapter = new ArrayAdapter<String>(this,
					   android.R.layout.simple_spinner_item, 
					   mydb.dessertType);
        	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
            foodSelectSpinner.setAdapter(adapter);
            
            // set this because we don't care about the spice note spin box
            spiceTypeChoice = 1;
            
            // setup base SQL query for recipe
            mySQLQuery = "SELECT WINE_TYPE.NAME, WINE_TYPE.GRAPE_NAME FROM WINE_TYPE JOIN PAIR_DESSERT ON WINE_TYPE._id=PAIR_DESSERT.wine_id WHERE PAIR_DESSERT.dessert_id=%d";
            
            // on the single items no need to have the user
            // select on first display of spinner
            foodSelectSpinner.performClick();
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not setup dessert spinner");
        }		
	}
    
    // grab the reults of the wine pairings
    private void setResultsList(int pos1, int pos2)
    {    	
    	try
    	{
    		// clear out old results
	  		wineListAdapter.clear();
	  		
	    	String completeSQL = "";
	    	if (buttonChoice.equals("food"))
			{    			
	    		if (pos2 == 0)
	    		{
	    			completeSQL = String.format(mySQLQuery, pos1);
	    		}
	    		else
	    		{
	    			completeSQL = String.format(mySQLQuery, pos1, pos2);
	    		}
	        }
	    	else if (buttonChoice.equals("wine"))
			{ 
	    		// 1 = List all Red
	    		// 2 = List all White
	    		if (pos1 == 1)
	    		{
	    			completeSQL = String.format(mySQLQuery, "\"Red\"");
	    		}
	    		else
	    		{
	    			completeSQL = String.format(mySQLQuery, "\"White\"");
	    		}
			}
	    	else
	    	{
	    		completeSQL = String.format(mySQLQuery, pos1);
	    	}
	    	 
	    	// grab the pairings from DB
	  		String[] wineListResults = mydb.getSQLData(completeSQL);
	  		
	  		if (wineListResults.length <= 0)
	  		{
	  			wineListAdapter.add("No Results");
	  			resultsView.setClickable(false);
	  			return;
	  		}
	  		
	  		resultsView.setClickable(true);
	  		// clear out old results and add new
	  		int i = 0;
	  		while (i < wineListResults.length)
	  		{
	  			wineListAdapter.add(wineListResults[i]);
	  			i++;
	  		}
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not get results");
        }    	
    }   
    
    /**
     * Launches the SpiceNote activity to pair wine with spice note
     */
    protected void launchWineDetailsActivity(String wineChoice) 
    {
    	try
    	{
	        Intent i = new Intent(this, WineDetailsActivity.class);
	        // pass the choice of wine to the subactivity so that the details 
	        // can be looked up in the wine DB
	        String resultsFromSplit [] = wineChoice.split("\\(");
	        String wineChoiceStripped = resultsFromSplit[0].trim();
	        i.putExtra("wineDetailsChoice", wineChoiceStripped);
	        startActivity(i);
    	}
        catch (Exception e)
        {
        	Log.v(LOG_TAG, "SpiceNoteActivity: could not start WineDetailsActivity");
        }
    }
}

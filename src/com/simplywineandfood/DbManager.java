package com.simplywineandfood;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbManager extends SQLiteOpenHelper {
	 
	private static final String LOG_TAG = "SimplyWineFood";
    private static final String DB_NAME = "simplywineandfood_v4";
    private static final String DB_PATH = "/data/data/com.simplywineandfood/databases/";
    private static final Integer DB_VERSION = 1;
    private static final String TAG = "DbManager";
    private static final String WINE_TYPE_TABLE = "WINE_TYPE";
    private static final String FOOD_TYPE_TABLE = "FOOD_TYPE";
    private static final String SPICE_TYPE_TABLE = "SPICE_TYPE";
    private static final String CHEESE_TYPE_TABLE = "CHEESE_TYPE";
    private static final String SOUP_TYPE_TABLE = "SOUP_TYPE";
    private static final String SALAD_TYPE_TABLE = "SALAD_TYPE";
    private static final String GRAPE_TYPE_TABLE = "GRAPE_TYPE";
    private static final String APP_TYPE_TABLE = "APP_TYPE";
    private static final String WOTD_TYPE_TABLE = "WOTD_TYPE";
    private static final String DESSERT_TYPE_TABLE = "DESSERT_TYPE";
    private final Context context;
    private SQLiteDatabase db = null;
    private static DbManager dbManager = null;
    private static WOTDOject wotdObject = new WOTDOject();
    
    public String[] wineType;
    public String[] foodType;
    public String[] spiceType;
    public String[] soupType;
    public String[] saladType;
    public String[] dessertType;
    public String[] recipeType;
    public String[] cheeseType;
    public int dbManagerWineTableSQLQueryID = 0;
    public String dbManagerWineTableSQLQueryName = "";
    public String dbManagerWineTableSQLQueryDetails = "";
    public String dbManagerWineTableSQLQueryCOLOR = "";
    public String dbManagerWineTableSQLQueryGRAPE = "";
    public String dbManagerWineTableSQLQueryDESCRIPTION = "";
    public String dbManagerWineTableSQLQueryGEOGRAPHY = "";
    public String dbManagerWineTableSQLQueryPRONUNCIATION = "";
    public String dbManagerWineTableSQLQueryAPP = "";
 
    public DbManager(Context context) 
    {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        createDataBase();
        openDataBase();
        getTableData();
    }
       
    // make this a singleton class so
    // we don't have to redo all the DB
    // stuff for the other activities
    public static DbManager getInstance(Context context)  
    {  
    	if(dbManager == null)  
    	{ 
    		dbManager = new DbManager(context);  
    	}
    	return dbManager;
    }
 
    public void createDataBase() 
    {    	
    	try
    	{
	    	if(!checkDataBaseExist())
	    	{
	    		this.getWritableDatabase();
	   			copyDataBase(); 
	    	}
    	}
    	catch (Exception e)
    	{    	
    		Log.v(LOG_TAG, "DbManager: failed to create DB");
    	}
     }
    
    public void openDataBase() 
    {      	
    	try 
    	{
	    	String myPath = DB_PATH + DB_NAME;
	    	db = SQLiteDatabase.openDatabase(myPath,
	    			                          null, 
	    			                          SQLiteDatabase.OPEN_READONLY);
	    }
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to open DB");
    	}
    }
    
    public void closeDataBase()
    {
    	db.close();    	
    }
    
    private boolean checkDataBaseExist()
    {
    	SQLiteDatabase checkDB = null;
 
    	try
    	{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     	}
    	catch(SQLiteException e)
    	{
     		//database does't exist yet.
     	}
 
    	if(checkDB != null)
    	{
     		checkDB.close();
     	}
 
    	return checkDB != null ? true : false;
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) 
    {
    
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) 
    {
    	createDataBase(); 
    }
 
    public void copyDataBase() 
    {
        InputStream assetsDB = null;
        
        try 
        {
            assetsDB = context.getAssets().open(DB_NAME);
            OutputStream dbOut = new FileOutputStream(DB_PATH + DB_NAME);
 
            byte[] buffer = new byte[1024];
            int length;
            while ((length = assetsDB.read(buffer)) > 0) 
            {
                dbOut.write(buffer, 0, length);
            }
 
            dbOut.flush();
            dbOut.close();
            assetsDB.close();    
        } 
        catch (IOException e) 
        {
            Log.e(TAG, "Could not create new database...");
            e.printStackTrace();
        }
    }
    
    public void getTableData()
    {
    	getWineTableData();
    	getFoodTableData();
    	getSpiceTableData();
    	getCheeseTableData();
    	getSoupTableData();
    	getDessertTableData();
    }
    
    public void getWineTableData()
    {    	
    	try
    	{
	    	Cursor cur = db.rawQuery("SELECT WINE_TYPE.NAME, WINE_TYPE.DETAILS, WINE_TYPE.COLOR FROM " +
	    			                  WINE_TYPE_TABLE, 
	    			                  null);
	    	wineType = new String [cur.getCount()+1];
	        cur.moveToFirst();
	        wineType[0] = "Select Wine";
	        while (cur.isAfterLast() == false) 
	        {
	        	wineType[cur.getPosition()] = cur.getString(cur.getColumnIndex("NAME"));
	    	    cur.moveToNext();
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get wine data");
    	}
    }
    
    public void getFoodTableData()
    {    	
    	try
    	{    		
	    	Cursor cur = db.rawQuery("SELECT FOOD_TYPE.NAME FROM " +
	    			                 FOOD_TYPE_TABLE, 
	    			                 null);
	    	if (cur == null)
	    	{
	    		foodType = null;
	    	}
	    	
	    	if (foodType != null)
	    	{
	    		foodType = null;    		
	    	}
	    	
	    	foodType = new String [cur.getCount()+1];
	        cur.moveToFirst();
	        // set the first entry to a static string
	        // this is because in the spinner box we want to always ignore
	        // the position 0 or the first entry
	        foodType[0] = "Select Food";
	        while (cur.isAfterLast() == false) 
	        {
	        	// do getPosition()+1 because Cursor starts at 0
	        	// and we ignor 0, plus it matches better with the
	        	// database where all _id start at 1
	        	foodType[cur.getPosition()+1] = cur.getString(cur.getColumnIndex("NAME"));
	    	    cur.moveToNext();
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get food data");
    	}
    }
    
   public void getSpiceTableData()
    { 
    	try
    	{
	    	Cursor cur = db.rawQuery("SELECT SPICE_TYPE.NAME FROM " +
	    			                 SPICE_TYPE_TABLE, 
	    			                 null);
	    	spiceType = new String [cur.getCount()+1];
	        cur.moveToFirst();
	        // set the first entry to a static string
	        // this is because in the spinner box we want to always ignore
	        // the position 0 or the first entry
	        spiceType[0] = "Select Spice";
	        while (cur.isAfterLast() == false) 
	        {
	        	// do getPosition()+1 because Cursor starts at 0
	        	// and we ignor 0, plus it matches better with the
	        	// database where all _id start at 1
	        	spiceType[cur.getPosition()+1] = cur.getString(cur.getColumnIndex("NAME"));
	    	    cur.moveToNext();
	        }
	        
	        cur.close(); 
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get spice data");
    	}
    }
    
    public void getCheeseTableData()
    {    	
    	try
    	{
	    	Cursor cur = db.rawQuery("SELECT CHEESE_TYPE.NAME FROM " +
	    			                 CHEESE_TYPE_TABLE, 
	    			                 null);
	    	cheeseType = new String [cur.getCount()+1];
	        cur.moveToFirst();
	        // set the first entry to a static string
	        // this is because in the spinner box we want to always ignore
	        // the position 0 or the first entry
	        cheeseType[0] = "Select Cheese";
	        while (cur.isAfterLast() == false) 
	        {
	        	// do getPosition()+1 because Cursor starts at 0
	        	// and we ignor 0, plus it matches better with the
	        	// database where all _id start at 1
	        	cheeseType[cur.getPosition()+1] = cur.getString(cur.getColumnIndex("NAME"));
	    	    cur.moveToNext();
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get cheese data");
    	}
    }
    
    public void getSoupTableData()
    {    	
    	try
    	{
	    	Cursor cur = db.rawQuery("SELECT SOUP_TYPE.NAME FROM " +
	    			                 SOUP_TYPE_TABLE, 
	    			                 null);
	    	soupType = new String [cur.getCount()+1];
	        cur.moveToFirst();
	        // set the first entry to a static string
	        // this is because in the spinner box we want to always ignore
	        // the position 0 or the first entry
	        soupType[0] = "Select Soup";
	        while (cur.isAfterLast() == false) 
	        {
	        	// do getPosition()+1 because Cursor starts at 0
	        	// and we ignor 0, plus it matches better with the
	        	// database where all _id start at 1
	        	soupType[cur.getPosition()+1] = cur.getString(cur.getColumnIndex("NAME"));
	    	    cur.moveToNext();
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get soup data");
    	}
    }
    
    public void getDessertTableData()
    {    	
    	try
    	{
	    	Cursor cur = db.rawQuery("SELECT DESSERT_TYPE.NAME FROM " +
	    			                 DESSERT_TYPE_TABLE, 
	    			                 null);
	    	dessertType = new String [cur.getCount()+1];
	        cur.moveToFirst();
	        // set the first entry to a static string
	        // this is because in the spinner box we want to always ignore
	        // the position 0 or the first entry
	        dessertType[0] = "Select Soup";
	        while (cur.isAfterLast() == false) 
	        {
	        	// do getPosition()+1 because Cursor starts at 0
	        	// and we ignor 0, plus it matches better with the
	        	// database where all _id start at 1
	        	dessertType[cur.getPosition()+1] = cur.getString(cur.getColumnIndex("NAME"));
	    	    cur.moveToNext();
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get dessert data");
    	}
    }
    
    public WOTDOject getWOTDTableData(int day)
    { 
    	try
    	{
	    	Cursor cur = db.rawQuery("SELECT * FROM " +
	    			                 WOTD_TYPE_TABLE + 
	    			                 " WHERE _id=" + day, 
	    			                 null);
	    	cur.moveToFirst();
	    	wotdObject.clearData();
	        while (cur.isAfterLast() == false) 
	        {
	        	if (cur.getColumnIndex("CHEESE_DESSERTS") >= 0)
	        	{
	        		wotdObject.CHEESE_DESSERTS = cur.getString(cur.getColumnIndex("CHEESE_DESSERTS"));
	        	}
	        	if (cur.getColumnIndex("PRONUNCIATION") >= 0)
	        	{
	        		wotdObject.PRONUNCIATION = cur.getString(cur.getColumnIndex("PRONUNCIATION"));
	        	}	        	
	        	if (cur.getColumnIndex("SALADS_APPETISERS") >= 0)
	        	{
	        		wotdObject.SALADS_APPETISERS = cur.getString(cur.getColumnIndex("SALADS_APPETISERS"));
	        	}
	        	if (cur.getColumnIndex("SOUPS") >= 0)
	        	{
	        		wotdObject.SOUPS = cur.getString(cur.getColumnIndex("SOUPS"));
	        	}
	        	if (cur.getColumnIndex("WEBSITE_LINK") >= 0)
	        	{
	        		wotdObject.WEBSITE_LINK = cur.getString(cur.getColumnIndex("WEBSITE_LINK"));
	        	}
	        	if (cur.getColumnIndex("WINE_BRANDS") >= 0)
	        	{
	        		wotdObject.WINE_BRANDS = cur.getString(cur.getColumnIndex("WINE_BRANDS"));
	        	}
	        	if (cur.getColumnIndex("WINE_COLOR") >= 0)
	        	{
	        		wotdObject.WINE_COLOR = cur.getString(cur.getColumnIndex("WINE_COLOR"));
	        	}
	        	if (cur.getColumnIndex("WINE_GRAPE") >= 0)
	        	{
	        		wotdObject.WINE_GRAPE = cur.getString(cur.getColumnIndex("WINE_GRAPE"));
	        	}
	        	if (cur.getColumnIndex("WINE_NAME") >= 0)
	        	{
	        		wotdObject.WINE_NAME = cur.getString(cur.getColumnIndex("WINE_NAME"));
	        	}
	        	if (cur.getColumnIndex("WINE_PAIRING") >= 0)
	        	{
	        		wotdObject.WINE_PAIRING = cur.getString(cur.getColumnIndex("WINE_PAIRING"));
	        	}
	    	    cur.moveToNext();
	        }
	        
	        cur.close(); 
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get WOTD data");
    	}
    	
    	return wotdObject;
    }
   
            
    public String[] getSQLData(final String queryString)
    {    
    	String [] results = null;
    	
    	try
    	{ 	
	    	if (queryString.length() <= 0)
	    	{
	    		return null;    		
	    	}
	    	
	    	Cursor cur = db.rawQuery(queryString, 
	    			                 null);
	    	results = new String [cur.getCount()];
	        cur.moveToFirst();
	        int i = 0;
	        while (cur.isAfterLast() == false) 
	        {
	        
	        	String tmp [] = cur.getColumnNames();
	        	if (cur.getColumnIndex("GRAPE_NAME") >= 0)
	        	{
	        		results[cur.getPosition()] = cur.getString(cur.getColumnIndex("NAME")) + 
                    										   " (" + 
                    										   cur.getString(cur.getColumnIndex("GRAPE_NAME")) +
                    										   ")";
	        	}
	        	else if (cur.getColumnIndex("SPICE_TYPE.NAME") >= 0)
	        	{
	        		results[cur.getPosition()] = cur.getString(cur.getColumnIndex("SPICE_TYPE.NAME"));
	        	}
	        	else
	        	{	        		
	        		results[cur.getPosition()] = cur.getString(cur.getColumnIndex("NAME"));
	        	}
	        	
	    	    cur.moveToNext();
	    	    i++;
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get SQL data");
    	}
        return results;
    }
    
    public String[] getSQLDataTest(final String queryString)
    {    
    	String [] results = null;
    	
    	try
    	{ 	
	    	if (queryString.length() <= 0)
	    	{
	    		return null;    		
	    	}
	    	
	    	Cursor cur = db.rawQuery(queryString, 
	    			                 null);
	    	results = new String [cur.getCount()];
	        cur.moveToFirst();
	        int i = 0;
	        while (cur.isAfterLast() == false) 
	        {
	        
	        	String tmp [] = cur.getColumnNames();
	        	results[cur.getPosition()] = cur.getString(cur.getColumnIndex("SPICE_TYPE.NAME"));
	                	
	    	    cur.moveToNext();
	    	    i++;
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get SQL data");
    	}
        return results;
    }
    
    public int [] getSQLArrayID(final String queryString)
    {    
    	int [] results = null;
    	
    	try
    	{ 	
	    	if (queryString.length() <= 0)
	    	{
	    		return null;    		
	    	}
	    	
	    	Cursor cur = db.rawQuery(queryString, 
	    			                 null);
	    	results = new int [cur.getCount()];
	        cur.moveToFirst();	        
	        while (cur.isAfterLast() == false) 
	        {
	        	if (cur.getColumnIndex("_id") >= 0)
	        	{
	        		results[cur.getPosition()] = cur.getInt(cur.getColumnIndex("_id"));
	        	}	        	
	    	    cur.moveToNext();
	        }	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get SQL data");
    	}
        return results;
    }
    
    public int getSQLDataIDFromString(final String queryString)
    {    
    	int results = 0;
    	
    	try
    	{ 	
	    	if (queryString.length() <= 0)
	    	{
	    		return results;    		
	    	}
	    	
	    	Cursor cur = db.rawQuery(queryString, 
	    			                 null);
	    	
	        cur.moveToFirst();
	        while (cur.isAfterLast() == false) 
	        {
	        	if (cur.getColumnIndex("_id") >= 0)
	        	{
	        		results = cur.getInt(cur.getColumnIndex("_id"));
	        	}        	
	        	
	    	    cur.moveToNext();
	        }	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get SQL data");
    	}
        return results;
    }
    
    public void getSQLDataFromWineTable(final String queryString)
    {   	
    	try
    	{
	    	if (queryString.length() <= 0)
	    	{
	    		return;    		
	    	}
	    
	    	dbManagerWineTableSQLQueryDetails = "";
	        dbManagerWineTableSQLQueryCOLOR = "";
	        dbManagerWineTableSQLQueryGRAPE = "";
	        dbManagerWineTableSQLQueryDESCRIPTION = "";
	        dbManagerWineTableSQLQueryGEOGRAPHY = "";
	        dbManagerWineTableSQLQueryPRONUNCIATION = "";
	        dbManagerWineTableSQLQueryAPP = "";
	    	
	    	Cursor cur = db.rawQuery(queryString, 
	    			                 null);    	
	        cur.moveToFirst();        
	        while (cur.isAfterLast() == false) 
	        {
	        	if (cur.getColumnIndex("_id") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryID = cur.getInt(cur.getColumnIndex("_id"));
	        	}	        	
	        	if (cur.getColumnIndex("NAME") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryName = cur.getString(cur.getColumnIndex("NAME"));
	        	}
	        	if (cur.getColumnIndex("DETAILS") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryDetails = cur.getString(cur.getColumnIndex("DETAILS"));
	        	}
	        	if (cur.getColumnIndex("COLOR") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryCOLOR = cur.getString(cur.getColumnIndex("COLOR"));
	        	}
	        	if (cur.getColumnIndex("DESCRIPTION") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryDESCRIPTION = cur.getString(cur.getColumnIndex("DESCRIPTION"));
	        	}
	        	if (cur.getColumnIndex("GEOGRAPHY") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryGEOGRAPHY = cur.getString(cur.getColumnIndex("GEOGRAPHY"));
	        	}
	        	if (cur.getColumnIndex("PRONUNCIATION") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryPRONUNCIATION = cur.getString(cur.getColumnIndex("PRONUNCIATION"));
	        	}	        	
	        	if (cur.getColumnIndex("APP") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryAPP = cur.getString(cur.getColumnIndex("APP"));
	        	}
	        	if (cur.getColumnIndex("GRAPE_NAME") >= 0)
	        	{
	        		dbManagerWineTableSQLQueryGRAPE = cur.getString(cur.getColumnIndex("GRAPE_NAME"));
	        	}
	    	    cur.moveToNext();    	 
	        }
	        
	        cur.close();
    	}
    	catch (Exception e)
    	{
    		Log.v(LOG_TAG, "DbManager: failed to get SQL wine data");
    	}
    }
    
}

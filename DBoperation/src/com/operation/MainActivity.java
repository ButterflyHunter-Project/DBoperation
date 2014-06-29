package com.operation;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
/**
 * http://blog.csdn.net/liuhe688/article/details/6715983
 * http://www.eoeandroid.com/thread-96421-1-1.html
 * 
 * http://www.cnblogs.com/walkingp/archive/2011/03/28/1997437.html
 * check the sqlite database through DDMS
 * @author
 * when using 'adb pull' to transfer files, you should not get into 'adb shell'
 * 
 * the statements of 'System.out.println()' are used for testing, they could be commented
 *
 */

public class MainActivity extends ActionBarActivity {
	
	// _id is auto-increment
	private String createTablePerson = "CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)";
	
	public void dropTable(SQLiteDatabase db, String table)
	{
		db.execSQL("DROP TABLE IF EXISTS " + table);
	}
	
	public void createTable(SQLiteDatabase db, String instruction)
	{
		db.execSQL(instruction);
	}
	
	// if there are more info, we need to add more values. // not used so far
	public void insertValue(SQLiteDatabase db, String table, Object[] info)
	{
        db.execSQL("INSERT INTO "+table+" VALUES (NULL, ?, ?)", info);  

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		//open or create butterfly.db
        SQLiteDatabase db = openOrCreateDatabase("butterfly.db", Context.MODE_PRIVATE, null);  

        
 /*
  * comment the following part ( create table) to test whether the 'butterfly.db' will always 
  * be there before it's deleted
  */
    //    db.execSQL("DROP TABLE IF EXISTS person"); 
        dropTable(db, "person");
        
        System.out.println("create a database");
        //create table person  
        //db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");  
        createTable(db, createTablePerson);
        
        Person person = new Person();  
        person.name = "john";  
        person.age = 30;  
        //insert values for a person 
        db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{person.name, person.age});  
          
        person.name = "david";  
        person.age = 33;  
        
        // another method to insert values
        //ContentValues save data of a person 
        ContentValues cv = new ContentValues();  
        cv.put("name", person.name);  
        cv.put("age", person.age);  
        //insert ContentValues
        db.insert("person", null, cv);  //insert(String table, String nullColumnHack, ContentValues values)
          
        cv = new ContentValues();  
        cv.put("age", 35);  
        //update the age of 'john'
        db.update("person", cv, "name = ?", new String[]{"john"});  
        //update(String table, ContentValues values, String whereClause, String[] whereArgs)
    /*
     * comment the above code to test whether butterfly.db is always there before it's deleted
     * 
     */
        
        
        Cursor c = db.rawQuery("SELECT * FROM person WHERE age >= ?", new String[]{"33"});  
        while (c.moveToNext()) {  
            int _id = c.getInt(c.getColumnIndex("_id"));  
            System.out.println(c.getColumnIndex("_id")); //0
            String name = c.getString(c.getColumnIndex("name"));  //getString(int columnIndex)
            System.out.println(c.getColumnIndex("name"));//1
            int age = c.getInt(c.getColumnIndex("age"));  
            Log.i("db data", "_id=>" + _id + ", name=>" + name + ", age=>" + age); 
            System.out.println("_id=>" + _id + ", name=>" + name + ", age=>" + age);
        }  
        c.close();  
          
        //delete a row  
//        db.delete("person", "age < ?", new String[]{"35"});  
          
        //close the database   
        db.close();  
          
        //delete butterfly.db  
  //    deleteDatabase("butterfly.db");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}

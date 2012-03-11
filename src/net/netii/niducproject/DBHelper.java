package net.netii.niducproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "niduc_project_01.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String NIDUC_TABLE = "niduc_project";
	public static final String CASHDESK_ID = "cashdesk_id";
	public static final String CARD_PAID = "card_paid";
	public static final String SIZE = "shopping_size";
	public static final String DATE = "shopping_date";
	public static final String TIME = "shopping_time";	
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
	}

	@Override
	public void onCreate(SQLiteDatabase base) {
		try {
			Log.i("sql", "not-created");
			base.execSQL("CREATE TABLE " + NIDUC_TABLE + "("
						+ CASHDESK_ID + " int(2), "
						+ CARD_PAID + " int(1), "
						+ SIZE + " int(1), "
						+ DATE + " datetime, "
						+ TIME + " int(2), "
					    + " PRIMARY KEY (" 
					    + CASHDESK_ID + "," +  CARD_PAID + ","
					    + SIZE + "," + DATE + "," + TIME + "));");
			Log.i("sql", "created");
		} catch (SQLiteException ex) {
			Log.i("sql", ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase base, int arg1, int arg2) {

	}	
	
	
	public String insertData(String cashdeskId, String cardPaid, String size, String date, String time){
		String query = "INSERT INTO " + NIDUC_TABLE
					+ " VALUES(" + cashdeskId + "," + cardPaid + "," + size
				          + ",'" + date + "'," + time + ");";
				
		try{
			SQLiteDatabase base = getWritableDatabase();			
			base.execSQL(query);
		}catch(SQLiteException ex){
			Log.i("sql", ex.getMessage());
			return "Database error";
		}           
            
		new DataSenderThread(cashdeskId, cardPaid, size, date, time);
		return query;
	}
}
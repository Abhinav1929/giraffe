package org.splitbrain.eventplanner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
    private static final String DATABASE_NAME = "EventPlanner";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db = null;;

    public DBAdapter(Context context){
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    /**
     * Open the Database
     * 
     * @return
     * @throws SQLException
     */
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close the database
     */
    public void close(){
        if(db != null) DBHelper.close();
        db = null;
    }

    /**
     * Begin a transaction
     */
    public void begin(){
	if(db == null) open();
	db.beginTransaction();
    }
    
    /**
     * Commit all changes made during the current transaction and end it
     */
    public void commit(){
	db.setTransactionSuccessful();
	db.endTransaction();
    }

    /**
     * Revert all changes made during the current transaction and end it
     */
    public void rollback(){
	db.endTransaction();
    }
    
    
    //FIXME add params to pass WHERE clauses
    public ArrayList<EventRecord> getEvents(){
	ArrayList<EventRecord> records = new ArrayList<EventRecord>();
	
	if(db == null) open();
	Cursor result = db.query("events",
				 new String[] {
					"id",
					"starts",
					"ends",
					"title",
					"description",
					"location",
					"speaker"
				 },
				 null,
				 null,
				 null,
				 null,
				 "starts");
	
	while(result.moveToNext()){
	    EventRecord record = new EventRecord();
	    record.id = result.getString(0);
	    record.starts = result.getLong(1);
	    record.ends = result.getLong(2);
	    record.title = result.getString(3);
	    record.description = result.getString(4);
	    record.location = result.getString(5);
	    record.speaker = result.getString(6);
	    
	    records.add(record);
	}
	result.close();
	
	return records;
    }
    
    //FIXME add event name later
    public void deleteEvents(){
	if(db == null) open();
	db.delete("events", "1", null);
    }
    
    public void addEventRecord(EventRecord record){
	if(db == null) open();
	
        ContentValues row = new ContentValues();
        row.put("event", record.event);
        row.put("id", record.id);
        row.put("starts", record.starts);
        row.put("ends", record.ends);
        row.put("title", record.title);
        row.put("description", record.description);
        row.put("location", record.location);
        row.put("speaker", record.speaker);
        
        db.insert("events", null, row);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{
        private final Context context;

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        /**
         *
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            if(executeQueryFile(db, "db/1.sql")){
                Toast toast = Toast.makeText(context, "Database created", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        /**
         *
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for(int version=oldVersion+1; version<=newVersion; version++){
        	if(executeQueryFile(db, "db/"+version+".sql")){
                    Toast toast = Toast.makeText(context, "Database updated from "+oldVersion+" to "+newVersion, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
        
        /**
         * Execute the given file as a series of SQL queries within a transaction
         * 
         * @param db - the SQLite database reference
         * @param filename - the path of the file to load from assets
         * @return true if the execution was successful
         */
        private boolean executeQueryFile(SQLiteDatabase db, String filename){
            boolean ok = false;
            String[] queries = loadQueryFile(filename);
            if(queries == null) return ok;
            
            db.beginTransaction();
            String sql = "";
            try{
        	for(int i=0; i<queries.length; i++){
        	    sql = queries[i].trim();
        	    if(sql.length() == 0) continue;
        	    db.execSQL(sql);
        	}
        	db.setTransactionSuccessful();
        	ok = true;
            }catch(Exception e){
        	Log.e("db","Failed to execute SQL: "+e.toString());
        	Log.e("db","Query was: '"+sql+"'");
            }finally{
        	db.endTransaction();
            }
            
            return ok;
        }
        
        /**
         * Load SQL queries from the given Assetfile
         *
         * @return separate queries
         */
        private String[] loadQueryFile(String filename) {
           
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = null;
            try{
                inputStream = assetManager.open(filename);
            } catch (IOException e) {
                Log.e("db","Failed to open Asset File. "+e.toString());
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                Log.e("db","Failed to read Asset File "+e.toString());
            }
            
            String[] queries = outputStream.toString().split(";[\r\n]+");
            return queries;
        }

    }
}

package org.vkedco.mobappdev.how_to_populate_db_with_content_provider;

/*
 * ========================================================================
 * File: BookNotesDBHelper.java
 * Description: DB Helper class for the table in book_notes.db
 * -----------------------------------------------------------------------
 * _id|Full Title|Short Title|Authors|ISBN_10|ISBN_13|Site|Category|Notes 
 * -----------------------------------------------------------------------
 * bugs to vladimir dot kulyukin at gmail dot com
 * ========================================================================
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BookNotesDBHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "book_notes.db";
	static final int DATABASE_VERSION = 1;

	public BookNotesDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// called when db is created
	@Override
	public void onCreate(SQLiteDatabase database) {
		BookTable.onCreate(database);
	}

	// db is upgraded when its version is increased
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		BookTable.onUpgrade(database, oldVersion, newVersion);
	}
}


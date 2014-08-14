package org.vkedco.mobappdev.how_to_populate_db_with_content_provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*
 * ========================================================================
 * File: BookTable.java
 * Description: Utility class for the book_note_tbl table in book_notes.db
 * -----------------------------------------------------------------------
 * _id|Full Title|Short Title|Authors|ISBN_10|ISBN_13|Site|Category|Notes 
 * -----------------------------------------------------------------------
 * bugs to vladimir dot kulyukin at gmail dot com
 * ========================================================================
 */

public class BookTable {
	// Table name and column names
	public static final String BOOK_NOTE_TABLE = "book_note_tbl";
	public static final String COLUMN_ID = "_id"; // 1
	public static final String COLUMN_FULL_TITLE = "FullTitle"; // 2
	public static final String COLUMN_SHORT_TITLE = "ShortTitle"; // 3
	public static final String COLUMN_AUTHORS = "Authors"; // 4
	public static final String COLUMN_ISBN_10 = "ISBN_10"; // 5
	public static final String COLUMN_ISBN_13 = "ISBN_13"; // 6
	public static final String COLUMN_WEB_LINK = "Site"; // 7
	public static final String COLUMN_CATEGORY = "Category"; // 8
	public static final String COLUMN_NOTES = "Notes"; // 9

	// SQL statement to create the table
	private static final String DATABASE_CREATE = "create table "
			+ BOOK_NOTE_TABLE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_FULL_TITLE
			+ " text not null, " + COLUMN_SHORT_TITLE + " text, "
			+ COLUMN_AUTHORS + " text, " + COLUMN_ISBN_10 + " text, "
			+ COLUMN_ISBN_13 + " text, " + COLUMN_WEB_LINK + " text, "
			+ COLUMN_CATEGORY + " text, " + COLUMN_NOTES + " text " + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.v(BookTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion);
		Log.v(BookTable.class.getName(), "Old data will be eliminated");
		database.execSQL("DROP TABLE IF EXISTS " + BOOK_NOTE_TABLE);
		onCreate(database);
	}
}


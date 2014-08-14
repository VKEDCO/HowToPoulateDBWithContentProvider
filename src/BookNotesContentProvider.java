package org.vkedco.mobappdev.how_to_populate_db_with_content_provider;

/*
 * ========================================================================
 * File: BookNotesContentProvider.java
 * Description: ContentProvider for inserting, deleting, upgrading, and
 * querring book_notes.db
 * -----------------------------------------------------------------------
 * _id|Full Title|Short Title|Authors|ISBN_10|ISBN_13|Site|Category|Notes 
 * -----------------------------------------------------------------------
 * bugs to vladimir dot kulyukin at gmail dot com
 * ========================================================================
 */

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BookNotesContentProvider extends ContentProvider {

	// mSQLiteDB
	private BookNotesDBHelper mSQLiteDB = null;

	// UriMacher's arbitrary constants
	private static final int BOOK_NOTES   = 300;
	private static final int BOOK_NOTE_ID = 500;

	private static final String AUTHORITY = 
			"org.vkedco.mobappdev.how_to_populate_db_with_cp.book_notes_contentprovider";

	private static final String BASE_PATH = "book_notes";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/book_notes";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/book_note";
	
	private static final String[] BOOK_TABLE_COLUMNS = {
		 BookTable.COLUMN_ID, 
		 BookTable.COLUMN_FULL_TITLE,
		 BookTable.COLUMN_SHORT_TITLE,
		 BookTable.COLUMN_AUTHORS,
		 BookTable.COLUMN_ISBN_10,
		 BookTable.COLUMN_ISBN_13,
		 BookTable.COLUMN_WEB_LINK,
		 BookTable.COLUMN_CATEGORY,
		 BookTable.COLUMN_NOTES
	};
	
	private static final HashSet<String> AVAILABLE_BOOK_TABLE_COLUMNS = 
			new HashSet<String>(Arrays.asList(BOOK_TABLE_COLUMNS));

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, BOOK_NOTES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", BOOK_NOTE_ID);
	}

	@Override
	public boolean onCreate() {
		mSQLiteDB = new BookNotesDBHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// SQLiteQueryBuilder to build queries
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Validate all columns in the projection
		validateProjectionColumns(projection);

		// Set the table
		queryBuilder.setTables(BookTable.BOOK_NOTE_TABLE);

		// Get the uri type
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case BOOK_NOTES:
			break;
		case BOOK_NOTE_ID:
			// adding the ID to the original query
			queryBuilder.appendWhere(BookTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Illegal URI: " + uri);
		}

		// Open a writable db
		SQLiteDatabase db = mSQLiteDB.getWritableDatabase();
		// Build a query
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Notify all potential listeners waiting on the cursor
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		// Return cursor
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mSQLiteDB.getWritableDatabase();
		//int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case BOOK_NOTES:
			id = sqlDB.insert(BookTable.BOOK_NOTE_TABLE, null, values);
			break;
		default:
			throw new IllegalArgumentException("Illegal URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mSQLiteDB.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case BOOK_NOTES:
			rowsDeleted = sqlDB.delete(BookTable.BOOK_NOTE_TABLE, selection,
					selectionArgs);
			break;
		case BOOK_NOTE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(BookTable.BOOK_NOTE_TABLE,
						BookTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(BookTable.BOOK_NOTE_TABLE,
						BookTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Illegal URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mSQLiteDB.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case BOOK_NOTES:
			rowsUpdated = sqlDB.update(BookTable.BOOK_NOTE_TABLE, values, selection,
					selectionArgs);
			break;
		case BOOK_NOTE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(BookTable.BOOK_NOTE_TABLE, values,
						BookTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(BookTable.BOOK_NOTE_TABLE, values,
						BookTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Illegal URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	// validate all columns which are requested are available
	private void validateProjectionColumns(String[] projection) {
		if (projection != null) {
			HashSet<String> projected_columns = new HashSet<String>(Arrays.asList(projection));

			if (!AVAILABLE_BOOK_TABLE_COLUMNS.containsAll(projected_columns)) {
				throw new IllegalArgumentException("Projection contains illegal columns");
			}
		}
	}

}

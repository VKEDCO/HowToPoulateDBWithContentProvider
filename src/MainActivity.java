package org.vkedco.mobappdev.how_to_populate_db_with_content_provider;

/*
 * ========================================================================
 * File: MainActivity.java
 * Description: Activity that populates the books database.
 * -----------------------------------------------------------------------
 * _id|Full Title|Short Title|Authors|ISBN_10|ISBN_13|Site|Category|Notes 
 * -----------------------------------------------------------------------
 * bugs to vladimir dot kulyukin at gmail dot com
 * ========================================================================
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Resources mRes = null;
	private Uri mBookNotesUri = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRes = this.getResources();
		this.populateInitialBooksDB();
		TextView main_tv = (TextView) this.findViewById(R.id.main_tv);
		main_tv.setText("Database has been populated;" + "\n" + "Use adb to explore it");
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
	
	private void populateInitialBooksDB() {
		final String[] full_titles = mRes.getStringArray(R.array.full_titles);
		final String[] short_titles = mRes.getStringArray(R.array.short_titles);
		final String[] isbn_10 = mRes.getStringArray(R.array.isbn_10);
		final String[] isbn_13 = mRes.getStringArray(R.array.isbn_13);
		final String[] authors = mRes.getStringArray(R.array.authors);
		final String[] web_links = mRes.getStringArray(R.array.web_links);
		final String[] book_cats = mRes.getStringArray(R.array.book_categories);
		final int array_len = full_titles.length;
		
		StringBuffer sbuf = new StringBuffer();
		for(int i = 0; i < array_len; i++) {
			sbuf.append("full_title = " + full_titles[i] + "\n");
			sbuf.append("short_title = " + short_titles[i] + "\n");
			sbuf.append("isbn_10 = " + isbn_10[i] + "\n");
			sbuf.append("isbn_13 = " + isbn_13[i] + "\n");
			sbuf.append("authors = " + authors[i] + "\n");
			sbuf.append("web_link = " + web_links[i] + "\n");
			sbuf.append("book_cat = " + book_cats[i] + "\n");
			Toast.makeText(this, sbuf.toString(), Toast.LENGTH_LONG).show();
			insertBookNote(full_titles[i],
					short_titles[i],
					isbn_10[i],
					isbn_13[i],
					authors[i],
					web_links[i],
					book_cats[i]);
			sbuf.delete(0, sbuf.length()-1);
		}		
	}
	
	private void insertBookNote(String full_title, String short_title,
			String isbn_10, String isbn_13, String authors, String web_link, 
			String book_cat) {
		ContentValues cv = new ContentValues();
		cv.put(BookTable.COLUMN_FULL_TITLE, full_title);
		cv.put(BookTable.COLUMN_SHORT_TITLE, short_title);
		cv.put(BookTable.COLUMN_ISBN_10, isbn_10);
		cv.put(BookTable.COLUMN_ISBN_13, isbn_13);
		cv.put(BookTable.COLUMN_AUTHORS, authors);
		cv.put(BookTable.COLUMN_WEB_LINK, web_link);
		cv.put(BookTable.COLUMN_CATEGORY, book_cat);
		this.mBookNotesUri = getContentResolver().insert(BookNotesContentProvider.CONTENT_URI, cv);
		Toast.makeText(this, "inserted " + mBookNotesUri.toString(), Toast.LENGTH_LONG).show();
	}
}

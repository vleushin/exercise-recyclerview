package in.leush.exercise.recyclerview;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.atomic.AtomicInteger;

import in.leush.exercise.recyclerview.db.Contract;
import in.leush.exercise.recyclerview.ui.adapter.MessagesAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private MessagesAdapter mAdapter;
    private InputMethodManager mInputMethodManager;
    private ContentResolver mResolver;
    private final AtomicInteger mCounter = new AtomicInteger(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEditText = (EditText) findViewById(R.id.edit_text);
        Button mWriteMessageButton = (Button) findViewById(R.id.write_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // TODO
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        //mChatLayoutManager.setStackFromEnd(true);
        //mChatLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MessagesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mResolver = getContentResolver();
        getSupportLoaderManager().initLoader(0, null, this);

        mEditText.clearFocus();
        mWriteMessageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString().trim();
                mEditText.setText("");
                if (!text.isEmpty()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.MessagesEntry.COLUMN_NAME_MESSAGE, text);
                    contentValues.put(Contract.MessagesEntry.COLUMN_NAME_CREATED_TIME, System.currentTimeMillis());
                    mResolver.insert(Contract.MessagesEntry.CONTENT_URI, contentValues);
                    mResolver.notifyChange(Contract.MessagesEntry.CONTENT_URI, null);
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        mEditText.clearFocus();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_1_message: {
                ContentValues contentValues = new ContentValues();
                int andIncrement = mCounter.getAndIncrement();
                contentValues.put(Contract.MessagesEntry.COLUMN_NAME_MESSAGE, String.valueOf(andIncrement));
                contentValues.put(Contract.MessagesEntry.COLUMN_NAME_CREATED_TIME, System.currentTimeMillis());
                mResolver.insert(Contract.MessagesEntry.CONTENT_URI, contentValues);
                mResolver.notifyChange(Contract.MessagesEntry.CONTENT_URI, null);
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            }
            case R.id.action_add_20_messages: {
                final int count = 20;
                ContentValues[] contentValuesArray = new ContentValues[count];
                long currentTime = System.currentTimeMillis();
                for (int i = 0; i < count; i++) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.MessagesEntry.COLUMN_NAME_MESSAGE, String.valueOf(mCounter.getAndIncrement()));
                    contentValues.put(Contract.MessagesEntry.COLUMN_NAME_CREATED_TIME, currentTime + i);
                    contentValuesArray[i] = contentValues;
                }
                mResolver.bulkInsert(Contract.MessagesEntry.CONTENT_URI, contentValuesArray);
                mResolver.notifyChange(Contract.MessagesEntry.CONTENT_URI, null);
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            }
            case R.id.action_clear: {
                mResolver.delete(Contract.MessagesEntry.CONTENT_URI, null, null);
                mResolver.notifyChange(Contract.MessagesEntry.CONTENT_URI, null);
                return true;
            }
            case R.id.action_restart: {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            }
            case R.id.actions_instruction: {
                int[] strings = new int[]{
                        R.string.instruction1,
                        R.string.instruction2,
                        R.string.instruction3,
                        R.string.instruction4,
                        R.string.instruction5,
                        R.string.instruction6,
                };
                ContentValues[] contentValuesArray = new ContentValues[strings.length];
                long currentTime = System.currentTimeMillis();
                for (int i = 0; i < strings.length; i++) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.MessagesEntry.COLUMN_NAME_MESSAGE, getString(strings[i]));
                    contentValues.put(Contract.MessagesEntry.COLUMN_NAME_CREATED_TIME, currentTime + i);
                    contentValuesArray[i] = contentValues;
                }
                mResolver.bulkInsert(Contract.MessagesEntry.CONTENT_URI, contentValuesArray);
                mResolver.notifyChange(Contract.MessagesEntry.CONTENT_URI, null);
                mRecyclerView.smoothScrollToPosition(0);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this);
        cursorLoader.setUri(Contract.MessagesEntry.CONTENT_URI);
        cursorLoader.setProjection(new String[]{
                "rowid",
                Contract.MessagesEntry.COLUMN_NAME_MESSAGE
        });
        cursorLoader.setSortOrder(Contract.MessagesEntry.ORDER_BY_CREATED_TIME_DESC);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        data.setNotificationUri(getContentResolver(), Contract.MessagesEntry.CONTENT_URI);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

package com.example.deian.todo;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private FloatingActionButton mFloatingActionButton;
    private ArrayList<String> mStringArray = new ArrayList<>();
    private EditText mEditText;
    private KeyListener mKeyListener;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mEditText = findViewById(R.id.plain_text_input);
        mEditText.setVisibility(View.GONE);
        mKeyListener = mEditText.getKeyListener();
        mEditText.setKeyListener(null);

        mFloatingActionButton = findViewById(R.id.fab);

        mStringArray = FileHelper.readData(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mStringArray);
        mListView = findViewById(R.id.listView);
        mListView.setAdapter(adapter);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setVisibility(View.VISIBLE);
                openKeyboard();
            }
        });


        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    closeKeyboard();
                }
            }
        });

        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    adapter.add(mEditText.getText().toString());
                    FileHelper.writeData(mStringArray, getApplicationContext());
                    closeKeyboard();

                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    private void openKeyboard(){
        mEditText.setKeyListener(mKeyListener);
        mEditText.requestFocus();
        mEditText.setBackgroundColor(getResources().getColor(R.color.PrimaryGrey));
        mEditText.setTextColor(Color.WHITE);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        mEditText.setKeyListener(null);
        mEditText.setText("");
        mEditText.setVisibility(View.GONE);
    }

}

package id.ac.umn.prauts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordET = findViewById(R.id.passwordET);
        final Spinner roleS = findViewById(R.id.roleS);
        final ArrayAdapter<CharSequence> rolesAA = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        roleS.setAdapter(rolesAA);
        Button cancelB = findViewById(R.id.cancelB);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddEditActivity.this, AdminActivity.class));
                finish();
            }
        });
        Button addEditB = findViewById(R.id.addEditB);
        final Intent i = getIntent();
        if(i.getStringExtra("DO").equals("ADD")){
            addEditB.setText("Add");
        }else{
            String id = i.getStringExtra("ID");
            Cursor cur = new DbContract.DbHelper(this).getReadableDatabase().query(
                    DbContract.Entry.TABLE_NAME,
                    new String[]{
                            DbContract.Entry._ID,
                            DbContract.Entry.COLUMN_NAME_USERNAME,
                            DbContract.Entry.COLUMN_NAME_PASSWORD,
                            DbContract.Entry.COLUMN_NAME_ROLE
                    },
                    String.format("%s = ?", DbContract.Entry._ID),
                    new String[]{id},
                    null, null, null
            );
            cur.moveToFirst();
            usernameET.setText(cur.getString(cur.getColumnIndex(DbContract.Entry.COLUMN_NAME_USERNAME)));
            passwordET.setText(cur.getString(cur.getColumnIndex(DbContract.Entry.COLUMN_NAME_PASSWORD)));
            roleS.setSelection(rolesAA.getPosition(cur.getString(cur.getColumnIndex(DbContract.Entry.COLUMN_NAME_ROLE))));
            addEditB.setText("Update");
        }
        addEditB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues val = new ContentValues();
                val.put(DbContract.Entry.COLUMN_NAME_USERNAME, usernameET.getText().toString());
                val.put(DbContract.Entry.COLUMN_NAME_PASSWORD, passwordET.getText().toString());
                val.put(DbContract.Entry.COLUMN_NAME_ROLE    , rolesAA.getItem(roleS.getSelectedItemPosition()).toString());
                if(i.getStringExtra("DO").equals("ADD")){
                    (new DbContract.DbHelper(AddEditActivity.this)).getWritableDatabase().insert(
                            DbContract.Entry.TABLE_NAME,
                            null, val
                    );
                }else{
                    (new DbContract.DbHelper(AddEditActivity.this)).getWritableDatabase().update(
                            DbContract.Entry.TABLE_NAME,
                            val,
                            String.format("%s = ?", DbContract.Entry._ID),
                            new String[]{i.getStringExtra("ID")}
                    );
                }
                startActivity(new Intent(AddEditActivity.this, AdminActivity.class));
                finish();
            }
        });
    }
}

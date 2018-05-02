package id.ac.umn.prauts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button addB = findViewById(R.id.addB);
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddEditActivity.class).putExtra("DO", "ADD"));
                finish();
            }
        });
        TextView loginTV = findViewById(R.id.loginTV);
        loginTV.setText(String.format("Welcome, %s!", getSharedPreferences("LOGIN", MODE_PRIVATE).getString("USERNAME", "")));
        ListView usersLV = findViewById(R.id.usersLV);
        SQLiteDatabase db = new DbContract.DbHelper(this).getReadableDatabase();
        Cursor cur = db.query(
                DbContract.Entry.TABLE_NAME,
                new String[]{
                        DbContract.Entry._ID,
                        DbContract.Entry.COLUMN_NAME_USERNAME,
                        DbContract.Entry.COLUMN_NAME_ROLE
                }, null, null, null, null, null, null
        );
        cur.moveToFirst();
        ArrayAdapter<String> usersAA = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView item = (TextView) super.getView(position, convertView, parent);
                item.setTextColor(Color.BLACK);
                return item;
            }
        };
        for(int i=0; i<cur.getCount(); i++, cur.moveToNext()){
            usersAA.add(String.format("%s: %s - %s",
                    cur.getString(cur.getColumnIndex(DbContract.Entry._ID)),
                    cur.getString(cur.getColumnIndex(DbContract.Entry.COLUMN_NAME_USERNAME)),
                    cur.getString(cur.getColumnIndex(DbContract.Entry.COLUMN_NAME_ROLE))
            ));
        }
        usersLV.setAdapter(usersAA);
        usersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(AdminActivity.this, AddEditActivity.class).putExtra("DO", "UPDATE").putExtra("ID", adapterView.getItemAtPosition(i).toString().split(":")[0]));
                finish();
            }
        });
        usersLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                (new DbContract.DbHelper(AdminActivity.this)).getWritableDatabase().delete(
                        DbContract.Entry.TABLE_NAME,
                        String.format("%s = ?", DbContract.Entry._ID),
                        new String[]{adapterView.getItemAtPosition(i).toString().split(":")[0]}
                );
                startActivity(new Intent(AdminActivity.this, AdminActivity.class));
                finish();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logoutI){
            startActivity(new Intent(AdminActivity.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

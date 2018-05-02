package id.ac.umn.prauts;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        final EditText usernameET = getView().findViewById(R.id.usernameET);
        final EditText passwordET = getView().findViewById(R.id.passwordET);
        final CheckBox rememberMeCB = getView().findViewById(R.id.rememberMeCB);
        if(pref.getBoolean("REMEMBER_ME", false)){
            usernameET.setText(pref.getString("USERNAME",""));
            passwordET.setText(pref.getString("PASSWORD",""));
            rememberMeCB.setChecked(pref.getBoolean("REMEMBER_ME", false));
        }
        Button cancelB = getView().findViewById(R.id.cancelB);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        Button loginB = getView().findViewById(R.id.loginB);
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor prefEd = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE).edit();
                prefEd.putBoolean("REMEMBER_ME", rememberMeCB.isChecked());
                prefEd.putString("USERNAME", usernameET.getText().toString());
                prefEd.putString("PASSWORD", passwordET.getText().toString());
                prefEd.commit();
                Cursor cur = (new DbContract.DbHelper(getActivity())).getReadableDatabase().query(
                        DbContract.Entry.TABLE_NAME,
                        new String[]{
                                DbContract.Entry._ID
                        },
                        String.format("%s = ? AND %s = ?", DbContract.Entry.COLUMN_NAME_USERNAME, DbContract.Entry.COLUMN_NAME_PASSWORD),
                        new String[]{usernameET.getText().toString(), passwordET.getText().toString()},
                        null, null, null
                );
                cur.moveToFirst();
                if(usernameET.getText().toString().equals("admin")&&passwordET.getText().toString().equals("admin")||cur.getCount()>0){
                    Toast.makeText(getActivity(), String.format("Welcome %s!", usernameET.getText().toString()), Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainFrame, new VerifyFragment());
                    fragmentTransaction.commit();
                }else{
                    Toast.makeText(getActivity(), "Incorrect username and password combination!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

}

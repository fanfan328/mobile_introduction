package id.ac.umn.prauts;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerifyFragment extends Fragment {


    public VerifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final int rand1 = (int)(Math.random()*100);
        final int rand2 = (int)(Math.random()*100);
        TextView questionTV = getActivity().findViewById(R.id.questionTV);
        questionTV.setText(String.format("%d - %d =", rand1, rand2));
        ((NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(0, new Notification.Builder(getActivity()).setContentTitle("PraUTS Verification").setContentText(questionTV.getText().toString()).setSmallIcon(R.mipmap.ic_launcher).build());
        final EditText answerET = getActivity().findViewById(R.id.answerET);
        final Button cancelB = getActivity().findViewById(R.id.cancelB);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        Button verifyB = getActivity().findViewById(R.id.verifyB);
        verifyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answerET.getText().toString().equals("") && Integer.parseInt(answerET.getText().toString())==rand1-rand2){
                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "Nope", Toast.LENGTH_SHORT).show();
                    cancelB.callOnClick();
                }
            }
        });
    }
}

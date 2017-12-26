package hung.jiawa.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.R;

public class FindPasswordActivity extends AppCompatActivity implements AsyncTaskCallBack, View.OnClickListener {
    public final String TAG = "Prototype";
    public final String NAME = "FindPasswordActivity - ";
    private EditText email;
    private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        //find view
        email = (EditText) findViewById(R.id.email);
        btnSend = (Button) findViewById(R.id.btn_send);

        //set listener
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult："+result+":"+mode);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String strResult = jsonData.getString("result");
                Toast.makeText(this, strResult, Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    void sendEmail(String email) {
        DBConnector mDBConnector = new DBConnector(this);
        mDBConnector.setCallBack(this);
        mDBConnector.executeSendEmail(email);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public void onClick(View v) {
        boolean cancel = false;
        View focusView = null;
        String srtEmail;
        srtEmail = email.getText().toString();
        email.setError(null);
        // Check for a valid email address.
        if (TextUtils.isEmpty(srtEmail)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(srtEmail)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            sendEmail(srtEmail);
        }
    }
}

package edu.skku.cs.pa3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.skku.cs.pa3.contract.AuthContract;
import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.model.User;
import edu.skku.cs.pa3.presenter.AuthPresenter;

public class MainActivity extends AppCompatActivity implements AuthContract.contractForView {

    String idStr;
    String pwd;
    Button loginBtn;
    Button regBtn;
    EditText idTxt;
    EditText pwdTxt;

    public static final String EXT_NAME = "name";
    public static final String EXT_USER_SEQ = "SEQ";
    public static final String EXT_CATEGORY = "category";

    private AuthContract.contractForPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new AuthPresenter(this);
        initView();
    }


    private void initView() {
        idTxt = (EditText) findViewById(R.id.id);
        pwdTxt = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        regBtn = (Button) findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(view -> {
            idStr = idTxt.getText().toString();
            pwd = pwdTxt.getText().toString();
            if (idStr == "") {
                Toast.makeText(this, "ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pwd == "") {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.onLoginBtnTouched(idStr, pwd);
        });


        regBtn.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void showResult(User response) {
        if(response.getCheck() == "true")
        {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, NewslistActivity.class);
                    intent.putExtra(EXT_NAME, response.getName());
                    intent.putExtra(EXT_CATEGORY, response.getCategories());
                    intent.putExtra(EXT_USER_SEQ, response.getUserSeq());
                    startActivity(intent);
                }
            });
        }
        else
        {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    idTxt.setText("");
                    pwdTxt.setText("");
                }
            });
        }
    }


}
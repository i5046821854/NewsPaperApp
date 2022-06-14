package edu.skku.cs.pa3.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.contract.AuthContract;
import edu.skku.cs.pa3.model.User;
import edu.skku.cs.pa3.presenter.AuthPresenter;

public class RegisterActivity extends AppCompatActivity implements AuthContract.contractForView {

    Button idcheck;
    Button register;
    String idStr;
    String pwd;
    Button showJob;
    EditText idEditText;
    EditText pwdEditText;
    Boolean idChecked = false;
    String field;
    private AuthContract.contractForPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        presenter = new AuthPresenter(this);
        initView();
    }

    private void initView(){
        idcheck = (Button) findViewById(R.id.idCheckBtn);
        idEditText = (EditText) findViewById(R.id.idEditTxt);
        pwdEditText = (EditText)findViewById(R.id.pwdTxt);
        final ArrayList<String> selected = new ArrayList<String>();
        register = (Button) findViewById(R.id.regBtn);
        showJob = (Button) findViewById(R.id.jobBtn);
        showJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[]{"regional", "technology", "lifestyle", "business", "general", "entertainment", "world", "sports", "finance", "academia", "politics"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                dialog.setTitle("관심 분야를 선택하세요")
                        .setSingleChoiceItems(items
                                , 0
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                            field = items[i];
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(RegisterActivity.this, "관심 분야가 선택되었습니다", Toast.LENGTH_SHORT).show();
                                if(field != null)
                                    showJob.setText("선택 분야 :" + field);
                            }
                        }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(RegisterActivity.this, "취소되었습니다", Toast.LENGTH_SHORT).show();
                            field = "";
                            showJob.setText("관심 분야를 선택해세요");
                        }
                    });
                dialog.create();
                dialog.show();
            }
        });
        idcheck.setOnClickListener(view -> {
                idStr = idEditText.getText().toString();
                presenter.onIdCheckBtnTouched(idStr);
        });

        register.setOnClickListener(view -> {
            if (!idChecked) {
                Toast.makeText(RegisterActivity.this, "id 중복 확인을 실시해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            idStr = idEditText.getText().toString();
            pwd = pwdEditText.getText().toString();
            if (pwd.equals("")) {
                Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if(field == null)
                field = " ";
            presenter.onRegBtnTouched(idStr, pwd, field);
        });
    }

    @Override
    public void showResult(User response) {
        final String checked = response.getCheck();
        if(response.getType() == "reg")
        {
            if(checked == "true")
            {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "계정 생성이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        idChecked = false;
                        finish();
                    }
                });
            }
            else
            {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "계정 생성에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        idChecked = false;
                    }
                });
            }
        }
        else {
            if (checked == "true") {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "올바른 id입니다", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        idChecked = true;
                    }
                });
            } else {
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "해당 id가 이미 사용 중입니다", Toast.LENGTH_SHORT).show();
                        System.out.println("fail");
                        idChecked = false;
                    }
                });
            }
        }
    }

}

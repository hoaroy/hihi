package com.example.Sachpee.Activity;

import static androidx.constraintlayout.motion.widget.TransitionBuilder.validate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Sachpee.Model.Partner;
import com.example.Sachpee.Model.User;
import com.example.Sachpee.R;
import com.example.Sachpee.Service.ApiClient;
import com.example.Sachpee.Service.ApiService;
import com.example.Sachpee.constant.Profile;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "SignInActivity";
    private LinearLayout layoutSignUp;
    private TextInputLayout formEmail, formPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private List<Partner> list;
    private CheckBox mChkRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUI();
        getDataSpf();

        list = getAllPartner();
        Log.d(TAG, "onCreate: " + list.toString());
        getSupportActionBar().hide();
    }

    private void setOnclickListener() {
        layoutSignUp.setOnClickListener(this::onClick);
        btnSignIn.setOnClickListener(this::onClick);
    }


    private void initUI() {
        layoutSignUp = findViewById(R.id.layout_SignInActivity_signIn);
        btnSignIn = findViewById(R.id.btn_SignInActivity_signIn);
        progressBar = findViewById(R.id.progressBar_SignInActivity_loadingLogin);
        progressBar.setVisibility(View.INVISIBLE);
        formEmail = findViewById(R.id.form_SignInActivity_email);
        formPassword = findViewById(R.id.form_SignInActivity_password);
        formPassword.setErrorEnabled(true);
        formEmail.setErrorEnabled(true);
        mChkRemember = findViewById(R.id.chk_sign_in_activity_remember);
        setOnclickListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_SignInActivity_signIn:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_SignInActivity_signIn:
                if (!logins()){
                    userLogin();
                }
                logins();
                break;
            default:
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void userLogin() {
        formEmail.setError(null);
        formPassword.setError(null);

        // Lấy số điện thoại và mật khẩu từ giao diện người dùng
        String phoneNumber = formEmail.getEditText().getText().toString().trim();
        String passwordUser = formPassword.getEditText().getText().toString().trim();

        // Kiểm tra tính hợp lệ của dữ liệu
        if (!validate(phoneNumber, passwordUser)) return;

        // Hiển thị thanh tiến trình
        progressBar.setVisibility(View.VISIBLE);

        // Gọi API lấy thông tin người dùng
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<User> call = apiService.getUserByPhoneNumber(phoneNumber); // Gọi API lấy thông tin người dùng
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // Ẩn thanh tiến trình
                progressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    // Lấy mật khẩu từ đối tượng User trả về
                    User user = response.body();
                    String password = user.getPassword();

                    // So sánh mật khẩu
                    if (password.equals(passwordUser)) {
                        // Đăng nhập thành công
                        remember("user", phoneNumber);
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        // Thông báo mật khẩu không đúng
                        formPassword.setError("Mật khẩu không đúng");
                    }
                } else {
                    // Thông báo tài khoản không tồn tại
                    formEmail.setError("Tài khoản không tồn tại");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public boolean logins(){
        //TODO validate partner login
        String email = formEmail.getEditText().getText().toString().trim();
        String password = formPassword.getEditText().getText().toString().trim();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserPartner().equals(email) && list.get(i).getPasswordPartner().equals(password)){
                remember("partner", String.valueOf(list.get(i).getIdPartner()));
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
                return true;
            }
        }
        if (email.equals("admin") && password.equals("admin") ){
            remember("admin", "admin");
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
            return true;
        }
        return false;
    }

    public void remember(String role, String id){
        String email = formEmail.getEditText().getText().toString().trim();
        String password = formPassword.getEditText().getText().toString().trim();
        SharedPreferences sharedPreferences = getSharedPreferences("My_User",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", email);
        editor.putString("password", password);
        editor.putString("role", role);
        editor.putString("id", id);
        editor.putBoolean("remember", mChkRemember.isChecked());
        editor.apply();
    }

    public void getDataSpf(){
        SharedPreferences sharedPreferences = getSharedPreferences("My_User",MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("remember",false);
        if (isRemember) {
            formEmail.getEditText().setText(sharedPreferences.getString("username",""));
            formPassword.getEditText().setText(sharedPreferences.getString("password",""));
        }
    }

    private boolean validate(String email, String password) {
        try {
            if (email.isEmpty() && password.isEmpty()) throw new IllegalArgumentException("email and password is empty");
            else if (email.isEmpty()) throw new IllegalArgumentException("email is empty");
            else if (password.isEmpty()) throw new IllegalArgumentException("password is empty");
            else if (password.length() < 6) throw new IllegalArgumentException(Profile.PASSWORD_INVALID);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("email and password is empty")) {
                formEmail.setError("Không được bỏ trống số điện thoại");
                formPassword.setError("Không được bỏ trống mật khẩu");
            } else if (e.getMessage().equals("email is empty")) {
                formEmail.setError("Không được bỏ trống số điện thoại");
            } else if (e.getMessage().equals("password is empty")) {
                formPassword.setError("Không được bỏ trống mật khẩu");
            } else if (e.getMessage().equals(Profile.PASSWORD_INVALID)) {
                formPassword.setError("Mật khẩu phải từ 6 kí tự trở lên");
            }
            Log.e(TAG, "validate: ", e);
            return false;
        }
        return true;

    }
    public List<Partner> getAllPartner() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        List<Partner> list1 = new ArrayList<>(); // Danh sách đối tác sẽ được lưu ở đây

        // Hiển thị ProgressDialog khi dữ liệu đang được tải
        progressDialog.show();

        // Gọi API lấy danh sách đối tác (Partner)
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Partner>> call = apiService.getAllPartners(); // Gọi API từ server
        call.enqueue(new Callback<List<Partner>>() {
            @Override
            public void onResponse(Call<List<Partner>> call, Response<List<Partner>> response) {
                // Ẩn ProgressDialog khi nhận được phản hồi
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    // Xóa dữ liệu cũ trong list1
                    list1.clear();

                    // Thêm danh sách đối tác mới vào list1
                    list1.addAll(response.body());

                    // TODO: Cập nhật UI hoặc xử lý danh sách đối tác trong `list1`
                    // Ví dụ: adapter.notifyDataSetChanged() nếu đang dùng RecyclerView.
                } else {
                    // Xử lý khi không nhận được dữ liệu thành công
                    Log.e("Error", "Không lấy được dữ liệu đối tác");
                }
            }

            @Override
            public void onFailure(Call<List<Partner>> call, Throwable t) {
                // Ẩn ProgressDialog khi có lỗi
                progressDialog.dismiss();
                Log.e("Error", "onFailure: " + t.getMessage());
            }
        });

        // Trả về danh sách list1 (mặc dù sẽ được cập nhật không đồng bộ sau khi API phản hồi)
        return list1;
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: đang stop");
    }
}
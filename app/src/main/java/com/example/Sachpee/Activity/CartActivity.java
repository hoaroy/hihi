package com.example.Sachpee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import com.example.Sachpee.Adapter.CartAdapter;
import com.example.Sachpee.Model.Bill;
import com.example.Sachpee.Model.Cart;
import com.example.Sachpee.Model.ProductTop;
import com.example.Sachpee.Model.Voucher;
import com.example.Sachpee.R;
import com.example.Sachpee.Service.ApiClient;
import com.example.Sachpee.Service.ApiService;
  

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//TODO Check this later next cartadapter
public class CartActivity extends AppCompatActivity {

    public static final String TAG = "CartActivity";
    private RecyclerView rvCart;
    private List<Cart> list;
    private List<ProductTop> listTop = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private CartAdapter adapter;
    private TextView tvTotalPrice, tvEmptyProduct,tv1, tvHide1, tvHide2, tvVoucher;
    private Button btn_senBill, btnEmptyProduct;
    private List<Bill> listBill;
    private Spinner spinner;
    private List<Voucher> listVoucher = new ArrayList<>();
    private String[] arr = {"Không có ưu đãi","Giảm 50%","Giảm 30%", "Giảm 20%"};
    private NumberFormat numberFormat = new DecimalFormat("#,##0");
    private String voucher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4ec4de")));
        getSupportActionBar().setTitle("Giỏ hàng");
        unitUi();
        btn_senBill.setOnClickListener(view -> {
        addBill();
        for (int i = 0; i < list.size(); i++) {
            addProductTop(list.get(i).getIdProduct(),list.get(i).getNumberProduct(),list.get(i).getIdCategory());
        }
        deleteCart();
        });
    }
    public void unitUi(){

        getProductTop();
        spinner = findViewById(R.id.spinner_voucher_cart);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,arr);
        spinner.setAdapter(adapter1);
        voucher = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                voucher = spinner.getSelectedItem().toString();
                getCartProduct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rvCart = findViewById(R.id.recyclerView_CartActivity_listCart);
        list = getCartProduct();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCart.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(list);
        rvCart.setAdapter(adapter);
        tvTotalPrice = findViewById(R.id.tv_CartActivity_totalPrice);
        tv1 = findViewById(R.id.tv1_CartActivity_totalPrice);
        btn_senBill = findViewById(R.id.btn_CartActivity_btnPay);
        tvEmptyProduct = findViewById(R.id.tv_CartActivity_emptyProduct);
        btnEmptyProduct = findViewById(R.id.btn_CartActivity_emptyProduct);
        listBill = getAllBill();
        tvHide1 = findViewById(R.id.tvHide1);
        tvHide2 = findViewById(R.id.tvHide2);
        tvVoucher = findViewById(R.id.tvVoucher);

    }


    public List<Cart> getCartProduct() {
        SharedPreferences preferences = getSharedPreferences("My_User", MODE_PRIVATE);
        String user = preferences.getString("username", "");
        List<Cart> list1 = new ArrayList<>();

        Log.d(TAG, "getCartProduct: Starting to retrieve cart products for user: " + user);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Cart>> call = apiService.getCartProduct(user);

        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list1.clear();
                    list1.addAll(response.body());
                    Log.d(TAG, "onResponse: Total cart items for user " + user + ": " + list1.size());

                    int sum = 0;
                    for (Cart cart : list1) {
                        sum += cart.getTotalPrice();
                    }

                    if (voucher.equals("Giảm 50%")) {
                        sum = (int) (sum - sum * 0.5);
                    } else if (voucher.equals("Giảm 30%")) {
                        sum = (int) (sum - sum * 0.3);
                    } else if (voucher.equals("Giảm 20%")) {
                        sum = (int) (sum - sum * 0.2);
                    }

                    tvTotalPrice.setText(numberFormat.format(sum));
                    tv1.setText("" + sum);

                    if (list1.isEmpty()) {
                        Log.d(TAG, "onResponse: Cart is empty. Hiding UI elements.");
                        tvHide1.setVisibility(View.GONE);
                        tvHide2.setVisibility(View.GONE);
                        btn_senBill.setVisibility(View.GONE);
                        tvTotalPrice.setVisibility(View.GONE);
                        tvEmptyProduct.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);
                        btnEmptyProduct.setVisibility(View.VISIBLE);
                        tvVoucher.setVisibility(View.GONE);
                        btnEmptyProduct.setOnClickListener(view -> {
                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                            finish();
                        });
                        rvCart.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(TAG, "onResponse: Cart has items. Showing UI elements.");
                        tvHide1.setVisibility(View.VISIBLE);
                        tvHide2.setVisibility(View.VISIBLE);
                        tvVoucher.setVisibility(View.VISIBLE);
                        btn_senBill.setVisibility(View.VISIBLE);
                        tvEmptyProduct.setVisibility(View.INVISIBLE);
                        btnEmptyProduct.setVisibility(View.INVISIBLE);
                        rvCart.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onResponse: Failed to retrieve cart items. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error retrieving cart data: " + t.getMessage());
            }
        });

        return list1;
    }

    // Gọi API với Retrofit
    public void addBill() {
        Bill bill = new Bill();
        SharedPreferences preferences = getSharedPreferences("My_User", MODE_PRIVATE);
        String user = preferences.getString("username", "");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:mm");
        String time = timeFormat.format(Calendar.getInstance().getTime());

        // Thiết lập thông tin Bill
        bill.setIdClient(user);
        bill.setDayOut(date);
        bill.setTimeOut(time);
        bill.setIdPartner(list.get(0).getIdPartner());
        bill.setTotal(Integer.parseInt(tv1.getText().toString()));

        if (user.equals("admin")) {
            bill.setStatus("Yes");
        } else {
            bill.setStatus("No");
        }

        Log.d(TAG, "addBill: Sending bill for user: " + user);
        // Gọi API qua Retrofit để gửi bill và list (giỏ hàng)
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Bill> call = apiService.addBill(bill);

        call.enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Bill created successfully.");
                } else {
                    Log.e(TAG, "onResponse: Failed to create bill. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e(TAG, "onFailure: Error: " + t.getMessage());
            }
        });
    } //TODO CHECK THIS LATE

    public void deleteCart() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        for (Cart cart : list) {
            Call<Void> call = apiService.deleteCartItem(cart.getIdCart());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse: Cart item deleted successfully.");
                    } else {
                        Log.e(TAG, "onResponse: Failed to delete cart item. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "onFailure: Error deleting cart item: " + t.getMessage());
                }
            });
        }

        btn_senBill.setEnabled(list.isEmpty());
    } //TODO CHECK THIS LATE

    public List<Bill> getAllBill() {
        List<Bill> list1 = new ArrayList<>();

        Log.d(TAG, "getAllBill: Starting to retrieve all bills via API.");

        // Khởi tạo API service
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Gọi API để lấy danh sách Bill
        Call<List<Bill>> call = apiService.getAllBills();

        // Thực hiện enqueue để gọi API không đồng bộ
        call.enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful()) {
                    // Xóa danh sách cũ và thêm dữ liệu mới từ API vào list1
                    list1.clear();
                    List<Bill> bills = response.body();

                    if (bills != null) {
                        list1.addAll(bills);
                        Log.d(TAG, "onResponse: Total bills retrieved: " + list1.size());

                        for (Bill bill : list1) {
                            Log.d(TAG, "onResponse: Retrieved bill: " + bill.toString());
                        }
                    } else {
                        Log.d(TAG, "onResponse: No bills retrieved. List is null.");
                    }
                } else {
                    Log.e(TAG, "onResponse: Failed to retrieve bills. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error retrieving bills: " + t.getMessage());
            }
        });

        return list1; // Trả về list, nhưng lưu ý Retrofit là bất đồng bộ nên kết quả sẽ có độ trễ.
    }

    public void getProductTop() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<ProductTop>> call = apiService.getProductTop();

        Log.d(TAG, "getProductTop: Starting to retrieve product tops via API.");

        call.enqueue(new Callback<List<ProductTop>>() {
            @Override
            public void onResponse(Call<List<ProductTop>> call, Response<List<ProductTop>> response) {
                if (response.isSuccessful()) {
                    List<ProductTop> listTop = response.body();
                    Log.d(TAG, "onResponse: Total product tops retrieved: " + (listTop != null ? listTop.size() : 0));

                    if (listTop != null) {
                        for (ProductTop top : listTop) {
                            Log.d(TAG, "onResponse: Retrieved product top: " + top.toString());
                        }
                    } else {
                        Log.d(TAG, "onResponse: No product tops retrieved. List is null.");
                    }

                    // Cập nhật UI hoặc làm việc với dữ liệu ở đây.
                } else {
                    Log.e(TAG, "onResponse: Failed to retrieve product tops. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductTop>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error retrieving product tops: " + t.getMessage());
            }
        });
    }

    // ADDPRODUCTTOP GENATE
    public void addProductTop(int id, int amount, int category) {
        // Khởi tạo đối tượng ProductTop
        ProductTop top = new ProductTop();
        top.setIdProduct(id);
        top.setIdCategory(category);
        top.setAmountProduct(amount);

        Log.d(TAG, "addProductTop: Adding or updating product top via API.");

        // Kiểm tra xem listTop có trống không
        if (listTop.size() == 0) {
            // Thêm mới sản phẩm vào danh sách top
            createProductTop(top);
        } else {
            // Kiểm tra xem sản phẩm đã tồn tại chưa
            boolean exists = false;
            for (ProductTop productTop : listTop) {
                if (productTop.getIdProduct() == id) {
                    exists = true;
                    Log.d(TAG, "addProductTop: Product top already exists, updating existing record.");
                    updateProductTop(top);
                    break;
                }
            }

            // Nếu sản phẩm chưa tồn tại thì thêm mới
            if (!exists) {
                Log.d(TAG, "addProductTop: Product top does not exist, creating a new one.");
                createProductTop(top);
            }
        }
    }

    // ADDPRODUCTTOP CREATE
    private void createProductTop(ProductTop top) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.createProductTop(top);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "createProductTop: Successfully added product top.");
                } else {
                    Log.e(TAG, "createProductTop: Failed to add product top. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "createProductTop: Error adding product top: " + t.getMessage());
            }
        });
    }

    // ADDPRODUCTTOP UPDATE
    private void updateProductTop(ProductTop top) {

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        int id = top.getIdProduct();
        Call<Void> call = apiService.updateProductTop(id, top);   // Gửi toàn bộ đối tượng ProductTop
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "updateProductTop: Successfully updated product top.");
                } else {
                    Log.e(TAG, "updateProductTop: Failed to update product top. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "updateProductTop: Error updating product top: " + t.getMessage());
            }
        });
    }




}
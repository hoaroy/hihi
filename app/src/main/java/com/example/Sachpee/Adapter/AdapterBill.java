package com.example.Sachpee.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Sachpee.Model.Bill;
import com.example.Sachpee.Model.Cart;
import com.example.Sachpee.Model.User;
import com.example.Sachpee.R;
import com.example.Sachpee.Service.ApiClient;
import com.example.Sachpee.Service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBill extends RecyclerView.Adapter<AdapterBill.viewHolder> {
    private List<Bill> list;
    private Context context;
    private AdapterItemBill adapterItemBill;
    private LinearLayoutManager linearLayoutManager;
    private ApiService apiService; // Thêm ApiService

    public AdapterBill(List<Bill> list, Context context, ApiService apiService) {
        this.list = list;
        this.context = context;
        this.apiService = apiService; // Khởi tạo ApiService
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences("My_User", Context.MODE_PRIVATE);
        String role = preferences.getString("role", "");
        Bill bill = list.get(position);

        // Gọi phương thức để lấy danh sách Cart

        getAllCart(bill.getIdBill(), holder.rvItemOrder, holder.tvNameClient);

        holder.tvidBill.setText("Mã HD :" + bill.getIdBill());
        holder.tvPhone.setText("Số điện thoại : " + bill.getIdClient());
        holder.tvTime.setText("Thời gian: " + bill.getTimeOut());
        holder.tvDay.setText(String.valueOf(bill.getDayOut()));
        holder.tvTotal.setText(String.valueOf(bill.getTotal()));

        holder.linearLayout_item_product.setOnClickListener(view -> {
            if (holder.rvItemOrder.getVisibility() == View.GONE) {
                holder.rvItemOrder.setVisibility(View.VISIBLE);
                holder.img_drop_up.setImageResource(R.drawable.ic_arrow_drop_down);
            } else {
                holder.rvItemOrder.setVisibility(View.GONE);
                holder.img_drop_up.setImageResource(R.drawable.ic_arrow_drop_up);
            }
        });

        holder.card_bill.setOnClickListener(view -> {
            if (!role.equals("user")) {
                if (holder.btn_updateStatusBill.getVisibility() == View.GONE) {
                    holder.btn_updateStatusBill.setVisibility(View.VISIBLE);
                } else {
                    holder.btn_updateStatusBill.setVisibility(View.GONE);
                }
            }
            if (bill.getStatus().equals("Yes")) {
                holder.btn_updateStatusBill.setVisibility(View.GONE);
            }
        });

        holder.btn_updateStatusBill.setOnClickListener(view -> {
            // Chuyển idBill sang kiểu String nếu cần thiết
            updateBillStatus(String.valueOf(bill.getIdBill())); // Chuyển đổi int sang String
        });
    }

    private void getAllCart(int idBill, RecyclerView recyclerView, TextView tvName) {
        Call<List<Cart>> call = apiService.getCartsByBillId(idBill);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cart> listCart = response.body();
                    Log.d("BookFragment", "Cart data retrieved successfully: " + listCart);
                    adapterItemBill = new AdapterItemBill(listCart);
                    recyclerView.setAdapter(adapterItemBill);
                    adapterItemBill.notifyDataSetChanged();
                } else {
                    Log.e("BookFragment", "Error fetching cart data: " + response.message());
                    Log.d("BookFragment", "Fetching cart data for idBill: " + idBill);

                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("BookFragment", "Failed to retrieve cart data: " + t.getMessage());
            }
        });
    }

    private void updateBillStatus(String idBill) {
        // Cập nhật trạng thái hóa đơn qua API
        Call<Void> call = apiService.updateBillStatus(idBill, "Yes");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("BookFragment", "Bill status updated successfully");
                } else {
                    Log.e("BookFragment", "Error updating bill status: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("BookFragment", "Failed to update bill status: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
        public class viewHolder extends RecyclerView.ViewHolder{
            private TextView tvidBill,tvNameClient,tvTotal, tvTime, tvDay,tvPhone;
            private LinearLayout linearLayout_item_product;
            private ImageView img_drop_up;
            private Button btn_updateStatusBill;
            private RecyclerView rvItemOrder;
            private CardView card_bill;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                tvidBill = itemView.findViewById(R.id.tv_idBill_item);
                tvPhone = itemView.findViewById(R.id.tv_phone);
                tvTotal = itemView.findViewById(R.id.tv_totalOrder_item);
                linearLayout_item_product = itemView.findViewById(R.id.linear_layout_item_product);
                img_drop_up = itemView.findViewById(R.id.img_drop_up);
                btn_updateStatusBill = itemView.findViewById(R.id.btn_updateStatusBill_item);
                rvItemOrder = itemView.findViewById(R.id.rv_order);
                card_bill = itemView.findViewById(R.id.card_bill);
                tvTime = itemView.findViewById(R.id.tv_time_item);
                tvDay = itemView.findViewById(R.id.tv_day_item);
                tvNameClient = itemView.findViewById(R.id.tv_name_client_item);
            }
        }
        private List<Cart> getAllCart(int i) {
            List<Cart> list1 = new ArrayList<>();

            // Thêm log để theo dõi bước bắt đầu lấy dữ liệu
            Log.d("BookFragment", "Fetching cart for Bill ID: " + list.get(i).getIdBill());

            //  ApiService và ApiClient
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

            //  API trả về danh sách các cart từ một bill theo ID
            Call<List<Cart>> call = apiService.getCartsByBillId(list.get(i).getIdBill());

            call.enqueue(new Callback<List<Cart>>() {
                @Override
                public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        list1.clear();
                        list1.addAll(response.body());

                        // Thêm log để kiểm tra dữ liệu trả về
                        Log.d("BookFragment", "Cart size: " + list1.size());

                        adapterItemBill.notifyDataSetChanged();
                    } else {
                        // Thêm log nếu phản hồi không thành công
                        Log.e("BookFragment", "Failed to fetch cart: Response code " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Cart>> call, Throwable t) {
                    // Thêm log nếu có lỗi xảy ra trong quá trình gọi API
                    Log.e("BookFragment", "Error fetching cart: " + t.getMessage());
                }
            });

            return list1;
        }

    private void getUser(String userId, TextView tvName) {
        // Thêm log để theo dõi việc bắt đầu lấy thông tin người dùng
        Log.d("BookFragment", "Fetching user with ID: " + userId);

        // Giả sử bạn đã tạo ApiService và ApiClient
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Giả sử API trả về một đối tượng User theo userId
        Call<User> call = apiService.getUserById(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // Thêm log để kiểm tra thông tin người dùng trả về
                    Log.d("BookFragment", "User found: " + user.getName());

                    // Cập nhật tên người dùng vào TextView
                    tvName.setText("Tên khách hàng: " + user.getName());

                    // Notify adapter nếu cần
                    adapterItemBill.notifyDataSetChanged();
                } else {
                    // Thêm log nếu phản hồi không thành công
                    Log.e("BookFragment", "Failed to fetch user: Response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Thêm log nếu có lỗi xảy ra trong quá trình gọi API
                Log.e("BookFragment", "Error fetching user: " + t.getMessage());
            }
        });
    }


}


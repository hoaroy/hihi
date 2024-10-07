package com.example.Sachpee.Fragment.BottomNav;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Sachpee.Adapter.ImageSliderAdapter;
import com.example.Sachpee.Adapter.ProductAdapter;
import com.example.Sachpee.Fragment.ProductFragments.GiaokhoaFragment;
import com.example.Sachpee.Fragment.ProductFragments.HoikyFragment;
import com.example.Sachpee.Fragment.ProductFragments.KinhteFragment;
import com.example.Sachpee.Fragment.ProductFragments.NgoainguFragment;
import com.example.Sachpee.Fragment.ProductFragments.TamlyFragment;
import com.example.Sachpee.Fragment.ProductFragments.ProductFragment;
import com.example.Sachpee.Fragment.ProductFragments.ThieunhiFragment;
import com.example.Sachpee.Fragment.ProductFragments.VanhocFragment;
import com.example.Sachpee.Model.ImageSlider;
import com.example.Sachpee.Model.Product;
import com.example.Sachpee.Model.ProductTop;
import com.example.Sachpee.R;
import com.example.Sachpee.Service.ApiClient;
import com.example.Sachpee.Service.ApiService;
  

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomePageFragment extends Fragment {

    private List<ProductTop> productToplistVanhoc = new ArrayList<>();
    private List<ProductTop> productToplistTamly= new ArrayList<>();
    private List<ProductTop> productToplistKinhte= new ArrayList<>();
    private List<ProductTop> productTopListFood= new ArrayList<>();
    private List<ProductTop> productToplistThieunhi = new ArrayList<>();
    private List<ProductTop> productToplistHoiky = new ArrayList<>();
    private List<ProductTop> productToplistGiaokhoa = new ArrayList<>();
    private List<ProductTop> productToplistNgoaingu = new ArrayList<>();
    private List<Product> listVanhoc = new ArrayList<>();
    private List<Product> listTamly= new ArrayList<>();
    private List<Product> listKinhte= new ArrayList<>();
    private List<Product> listFood= new ArrayList<>();
    private List<Product> listThieunhi= new ArrayList<>();
    private List<Product> listHoiky= new ArrayList<>();
    private List<Product> listGiaokhoa= new ArrayList<>();
    private List<Product> listNgoaingu= new ArrayList<>();
    private List<Product> listProduct = new ArrayList<>();
    ProductAdapter adapter;

    CardView card_vanhoc_home,card_kinhte_home,card_tamly_home,card_giaoduc_home, card_thieunhi_home, card_hoiky_home, card_giaokhoa_home, card_ngoaingu_home;

    CardView card_trending_home,card_Top_Fruit,card_Top_Meat,card_Top_Food;

    RecyclerView rv_trending_home,rv_FruitTop_Home,rv_MeatTop_Home,rv_FoodTop_Home;

    ImageView arrow1,arrow2,arrow3,arrow4;
    private ProductFragment fragment = new ProductFragment();

    ///imgview
    private List<ImageSlider> list = new ArrayList<>();
    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private CircleIndicator circleIndicator;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        //viewslider
        viewPager = view.findViewById(R.id.main_slider_image);
        circleIndicator = view.findViewById(R.id.circle_indicator);

        list.add(new ImageSlider(R.drawable.banner_0803));
        list.add(new ImageSlider(R.drawable.banner_book));
        list.add(new ImageSlider(R.drawable.banner_kim_dong));
        list.add(new ImageSlider(R.drawable.banner_fahasa));

        imageSliderAdapter = new ImageSliderAdapter(getContext(), list);
        viewPager.setAdapter(imageSliderAdapter);
        circleIndicator.setViewPager(viewPager);

        adapter = new ProductAdapter(listVanhoc,fragment,getContext());
        getTopProduct();
        getProduct();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);

        card_vanhoc_home = view.findViewById(R.id.card_vanhoc_home);
        card_kinhte_home = view.findViewById(R.id.card_kinhte_home);
        card_tamly_home = view.findViewById(R.id.card_tamly_home);
        card_giaoduc_home = view.findViewById(R.id.card_giaoduc_home);
        card_thieunhi_home = view.findViewById(R.id.card_thieunhi_home);
        card_hoiky_home = view.findViewById(R.id.card_hoiky_home);
        card_giaokhoa_home = view.findViewById(R.id.card_giaokhoa_home);
        card_ngoaingu_home = view.findViewById(R.id.card_ngoaingu_home);

        card_trending_home = view.findViewById(R.id.card_trending_home);
        card_Top_Fruit = view.findViewById(R.id.card_Top_Fruit);
        card_Top_Meat = view.findViewById(R.id.card_Top_Meat);
        card_Top_Food = view.findViewById(R.id.card_Top_Food);

        rv_trending_home = view.findViewById(R.id.rv_trending_home);
        rv_FruitTop_Home = view.findViewById(R.id.rv_FruitTop_Home);
        rv_MeatTop_Home = view.findViewById(R.id.rv_MeatTop_Home);
        rv_FoodTop_Home = view.findViewById(R.id.rv_FoodTop_Home);
        card_vanhoc_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new VanhocFragment(),null).addToBackStack(null).commit();

        });
        card_kinhte_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new KinhteFragment(),null).addToBackStack(null).commit();
        });
        card_tamly_home.setOnClickListener(view1 -> {

            fragmentManager.beginTransaction().replace(R.id.frame_Home, new TamlyFragment(),null).addToBackStack(null).commit();
        });
        card_giaoduc_home.setOnClickListener(view1 -> {

            fragmentManager.beginTransaction().replace(R.id.frame_Home, new PartnerBookFragment(),null).addToBackStack(null).commit();
        });
        card_thieunhi_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new ThieunhiFragment(),null).addToBackStack(null).commit();

        });
        card_hoiky_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new HoikyFragment(),null).addToBackStack(null).commit();

        });
        card_giaokhoa_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new GiaokhoaFragment(),null).addToBackStack(null).commit();

        });
        card_ngoaingu_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new NgoainguFragment(),null).addToBackStack(null).commit();

        });

        card_trending_home.setOnClickListener(view1 -> {
            onClickItemCart(listVanhoc,rv_trending_home);
        });
        card_Top_Fruit.setOnClickListener(view1 -> {
            onClickItemCart(listKinhte,rv_FruitTop_Home);
        });
        card_Top_Meat.setOnClickListener(view1 -> {
            onClickItemCart(listTamly,rv_MeatTop_Home);
        });
        card_Top_Food.setOnClickListener(view1 -> {

            onClickItemCart(listFood,rv_FoodTop_Home);

        });


        return view;
    }

    public void getTopProduct() {
        // Giả định bạn có ApiService và phương thức getTopProducts() trả về danh sách ProductTop
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<List<ProductTop>> call = apiService.getProductTop();
        call.enqueue(new Callback<List<ProductTop>>() {
            @Override
            public void onResponse(Call<List<ProductTop>> call, Response<List<ProductTop>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Xóa sạch danh sách trước khi thêm mới
                    productToplistVanhoc.clear();
                    productTopListFood.clear();
                    productToplistKinhte.clear();
                    productToplistTamly.clear();
                    productToplistThieunhi.clear();
                    productToplistHoiky.clear();
                    productToplistGiaokhoa.clear();
                    productToplistNgoaingu.clear();

                    // Lặp qua danh sách sản phẩm top nhận được từ API
                    for (ProductTop top : response.body()) {
                        // Phân loại sản phẩm theo idCategory
                        if (top.getIdCategory() == 1) {
                            productToplistVanhoc.add(top);
                        } else if (top.getIdCategory() == 2) {
                            productToplistKinhte.add(top);
                        } else if (top.getIdCategory() == 3) {
                            productToplistTamly.add(top);
                        } else if (top.getIdCategory() == 4){
                            productTopListFood.add(top);
                        } else if (top.getIdCategory() == 5) {
                            productToplistThieunhi.add(top);
                        } else if (top.getIdCategory() == 6) {
                            productToplistHoiky.add(top);
                        } else if (top.getIdCategory() == 7) {
                            productToplistGiaokhoa.add(top);
                        } else{
                            productToplistNgoaingu.add(top);
                        }

                    }

                    // Gọi hàm getProduct() sau khi hoàn tất việc lọc
                    getProduct();
                    Log.d("getTopProduct", "Sản phẩm văn học: " + productToplistVanhoc.size() +
                            ", Kinh tế: " + productToplistKinhte.size() +
                            ", Tâm lý: " + productToplistTamly.size() +
                            ", Giáo dục: " + productTopListFood.size() +
                            ", Thiếu nhi: " + productToplistThieunhi.size() +
                            ", Hồi ký: " + productToplistHoiky.size() +
                            ", Giáo khoa: " + productToplistGiaokhoa.size() +
                            ", Ngoại ngữ: " + productToplistNgoaingu.size());
                } else {
                    Log.e("getTopProduct", "Phản hồi không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductTop>> call, Throwable t) {
                Log.e("getTopProduct", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    public void getProduct() {
        //  phương thức getAllProducts() trả về danh sách Product
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<List<Product>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Xóa sạch các danh sách trước khi thêm mới
                    listTamly.clear();
                    listKinhte.clear();
                    listVanhoc.clear();
                    listFood.clear();
                    listThieunhi.clear();
                    listHoiky.clear();
                    listGiaokhoa.clear();
                    listNgoaingu.clear();
                    listProduct.clear();  // Làm sạch danh sách product

                    // Lặp qua danh sách sản phẩm nhận được từ API
                    for (Product top : response.body()) {
                        listProduct.add(top);
                    }

                    // Sắp xếp danh sách top sản phẩm nếu SDK >= Android N
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(productToplistVanhoc, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productToplistKinhte, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productTopListFood, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productToplistTamly, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productToplistThieunhi, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productToplistHoiky, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productToplistGiaokhoa, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                        Collections.sort(productToplistNgoaingu, Comparator.comparing(ProductTop::getAmountProduct).reversed());
                    }

                    // Thêm các sản phẩm vào danh sách tương ứng
                    add(productToplistVanhoc, listProduct, listVanhoc);
                    add(productToplistKinhte, listProduct, listKinhte);
                    add(productTopListFood, listProduct, listFood);
                    add(productToplistTamly, listProduct, listTamly);
                    add(productToplistThieunhi, listProduct, listThieunhi);
                    add(productToplistHoiky, listProduct, listHoiky);
                    add(productToplistGiaokhoa, listProduct, listGiaokhoa);
                    add(productToplistNgoaingu, listProduct, listNgoaingu);

                    // Thực hiện các xử lý bổ sung với các danh sách đã lọc
                    collections(listVanhoc);
                    collections(listKinhte);
                    collections(listFood);
                    collections(listTamly);
                    collections(listThieunhi);
                    collections(listHoiky);
                    collections(listGiaokhoa);
                    collections(listNgoaingu);

                    // Cập nhật adapter
                    adapter.notifyDataSetChanged();

                    Log.d("getProduct", "Danh sách sản phẩm đã cập nhật.");
                } else {
                    Log.e("getProduct", "Phản hồi không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("getProduct", "Lỗi khi gọi API: " + t.getMessage());
            }
        });
    }

    public void add(List<ProductTop> listTop, List<Product> listProduct ,List<Product> listProductTop ){
    for (int i = 0; i < listTop.size(); i++) {
        for (int j = 0; j < listProduct.size(); j++) {
            if (listTop.get(i).getIdProduct() == listProduct.get(j).getCodeProduct() ){
                listProductTop.add(listProduct.get(j));
            }
        }

    }
}
    public void collections(List<Product> listProductTop ){
//        for (int i = 0; i < listTop.size(); i++) {
//            for (int j = 0; j < listProduct.size(); j++) {
//                if (listTop.get(i).getIdProduct() == listProduct.get(j).getCodeProduct() ){
//                    listProductTop.add(listProduct.get(j));
//                }
//            }
//
//        }
        try {
            for (int i = 0; i < listProductTop.size(); i++) {
                for (int j = 1; j < listProductTop.size(); j++) {
                    if (listProductTop.get(i).getCodeProduct() == listProductTop.get(j).getCodeProduct() ){
                        listProductTop.remove(listProductTop.get(i));
                    }
                }
            }
        }catch (Exception e){

        }

    }

    public void onClickItemCart(List<Product> list,RecyclerView recyclerView){
        if (recyclerView.getVisibility() == View.GONE){
            arrow1.setImageResource(R.drawable.ic_arrow_drop_down);
            arrow2.setImageResource(R.drawable.ic_arrow_drop_down);
            arrow3.setImageResource(R.drawable.ic_arrow_drop_down);
            arrow4.setImageResource(R.drawable.ic_arrow_drop_down);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ProductAdapter(list,fragment,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            recyclerView.setVisibility(View.GONE);
            arrow1.setImageResource(R.drawable.ic_arrow_drop_up);
            arrow2.setImageResource(R.drawable.ic_arrow_drop_up);
            arrow3.setImageResource(R.drawable.ic_arrow_drop_up);
            arrow4.setImageResource(R.drawable.ic_arrow_drop_up);
        }
    }







}
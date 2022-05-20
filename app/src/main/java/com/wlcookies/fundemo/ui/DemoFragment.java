package com.wlcookies.fundemo.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wlcookies.fundemo.R;
import com.wlcookies.fundemo.ui.demo.BluetoothActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 示例
 *
 * @author wl
 * @version V1.0
 * @date 2022/5/13
 */
public class DemoFragment extends Fragment {

    private static final String TAG = "DemoFragment";

    private static class FunctionBean {
        private int id;
        private String title;
        private int icon;

        public FunctionBean(int id, String title, int icon) {
            this.id = id;
            this.title = title;
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

    public static DemoFragment newInstance() {
        return new DemoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView functionRv = view.findViewById(R.id.function_rv);

        List<FunctionBean> functionBeans = new ArrayList<>();
        functionBeans.add(new FunctionBean(
                1, "蓝牙相关", R.drawable.ic_bluetooth
        ));
        functionBeans.add(new FunctionBean(
                2, "WIFI相关", R.drawable.ic_wifi
        ));
        BaseQuickAdapter<FunctionBean, BaseViewHolder> adapter = new BaseQuickAdapter<FunctionBean, BaseViewHolder>(R.layout.demo_function_list_item, functionBeans) {
            @Override
            protected void convert(@NonNull BaseViewHolder holder, FunctionBean functionBean) {
                holder.setText(R.id.title_tv, functionBean.getTitle());
                holder.setImageDrawable(R.id.icon_iv, ContextCompat.getDrawable(requireActivity(), functionBean.getIcon()));
            }
        };
        functionRv.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        functionRv.setAdapter(adapter);

        adapter.setOnItemClickListener((a, v, position) -> {
            FunctionBean functionBean = functionBeans.get(position);
            switch (functionBean.id) {
                case 1: // 蓝牙相关
                    startActivity(BluetoothActivity.newInstance(requireActivity()));
                    break;
                case 2: // wifi相关

                    break;
                default:
                    break;
            }
        });
    }
}
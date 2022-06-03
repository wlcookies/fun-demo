package com.wlcookies.fundemo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.wlcookies.fundemo.R;
import com.wlcookies.fundemo.databinding.FragmentWidgetBinding;

/**
 * 控件使用Demo
 *
 * @author wl
 * @version V1.0
 * @date 2022/5/12
 */
public class WidgetFragment extends Fragment {

    private FragmentWidgetBinding fragmentWidgetBinding;

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentWidgetBinding = FragmentWidgetBinding.inflate(inflater, container, false);
        return fragmentWidgetBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentWidgetBinding = null;
    }
}
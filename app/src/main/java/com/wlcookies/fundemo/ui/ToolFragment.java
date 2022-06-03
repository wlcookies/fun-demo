package com.wlcookies.fundemo.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wlcookies.fundemo.R;
import com.wlcookies.fundemo.databinding.FragmentToolBinding;

/**
 * 工具
 *
 * @author wl
 * @version V1.0
 * @date 2022/5/13
 */
public class ToolFragment extends Fragment {

    private FragmentToolBinding fragmentToolBinding;

    public static ToolFragment newInstance() {
        return new ToolFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentToolBinding = FragmentToolBinding.inflate(inflater, container, false);

        return fragmentToolBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentToolBinding = null;
    }
}
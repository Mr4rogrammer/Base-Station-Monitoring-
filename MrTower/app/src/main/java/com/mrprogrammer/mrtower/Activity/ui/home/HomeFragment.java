package com.mrprogrammer.mrtower.Activity.ui.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mrprogrammer.mrtower.Adapter.NetWorkInfoAdapter;
import com.mrprogrammer.mrtower.Model.NetWorkInfo;
import com.mrprogrammer.mrtower.Utils.CommonFunctions;
import com.mrprogrammer.mrtower.Utils.Signal;
import com.mrprogrammer.mrtower.databinding.FragmentHomeBinding;
import com.mrprogrammer.shop.MrToast.MrToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment  {
    private FragmentHomeBinding binding;
    private Handler mHandler;
    private Runnable mRunnable;
    private NetWorkInfoAdapter adapter;
    private List<NetWorkInfo> data = new ArrayList<>();
    Signal phoneStateListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initAdapter();
        signalDetect();
        mHandler = new Handler(Looper.myLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (CommonFunctions.Companion.isConnected(requireContext())) {
                    updateDetails();
                    trembleData(6);
                } else {
                    updateDetails();
                    setvalueAsZero();
                    trembleData(0);
                }
                findSpeed();
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(mRunnable);
        return binding.getRoot();
    }

    private void signalDetect() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new Signal();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }
    private void initAdapter() {
        adapter = new NetWorkInfoAdapter(data);
        binding.listOfNetworkData.setAdapter(adapter);
    }

    private void trembleData(int degree) {
        binding.uploadSpeed.setTrembleData(degree, 1000);
        binding.downloadSpeed.setTrembleData(degree, 1000);
    }

    private void updateDetails() {
        data.clear();
        data.addAll(CommonFunctions.Companion.getNetworkDetails(requireContext()));
        data.addAll(phoneStateListener.getSignalStrength());
        adapter.notifyDataSetChanged();
    }

    private void setvalueAsZero() {
        binding.downloadSpeed.speedTo((float) (0 / 1000.0));
        binding.uploadSpeed.speedTo((float) (0 / 1000.0));
    }

    private void findSpeed() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                double downloadSpeed = networkCapabilities.getLinkDownstreamBandwidthKbps();
                double uploadSpeed = networkCapabilities.getLinkUpstreamBandwidthKbps();
                binding.downloadSpeed.speedTo((float) (downloadSpeed / 1000.0));
                binding.uploadSpeed.speedTo((float) (uploadSpeed / 1000.0));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable); // Stop the Runnable when the activity is destroyed
    }
}
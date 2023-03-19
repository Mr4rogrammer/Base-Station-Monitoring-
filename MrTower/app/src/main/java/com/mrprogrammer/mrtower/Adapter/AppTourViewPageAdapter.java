package com.mrprogrammer.mrtower.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mrprogrammer.mrtower.Activity.AppTourFragment.First;
import com.mrprogrammer.mrtower.Activity.AppTourFragment.Four;
import com.mrprogrammer.mrtower.Activity.AppTourFragment.Third;
import com.mrprogrammer.mrtower.Activity.AppTourFragment.second;

public class AppTourViewPageAdapter extends FragmentStateAdapter {
    public AppTourViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new second();
            case 2:
                return new Third();
            case 3:
                return new Four();
        }
        return new First();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

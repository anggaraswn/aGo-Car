package edu.bluejack22_2.agocar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> listFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> listFragment) {
        super(fragmentActivity);
        this.listFragment = listFragment;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return listFragment.size();
    }
}

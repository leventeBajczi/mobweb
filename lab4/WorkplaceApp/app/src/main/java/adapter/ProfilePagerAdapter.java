package adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fragments.DetailsProfileFragment;
import fragments.MainProfileFragment;

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 2;

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainProfileFragment();
            case 1:
                return new DetailsProfileFragment();
            default:
                return new MainProfileFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
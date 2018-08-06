package com.duelit.ui.maintabscreen;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duelit.R;
import com.duelit.SessionManager;
import com.duelit.ui.BaseActivity;
import com.duelit.ui.maintabscreen.credits.CreditsFragment;
import com.duelit.ui.maintabscreen.friends.FriendsFragment;
import com.duelit.ui.maintabscreen.games.GamesFragment;
import com.duelit.utils.views.CustomTab;

public class MainTabbedActivity extends BaseActivity implements FragmentListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //maps an icon to a text
    private List<CustomTab> tabsMap = new ArrayList<>();
    //this layout can be requested using {@link FragmentListener.requestOpenDrawer}
    private DrawerLayout mDrawerLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);


        //Layout manager that allows the user to flip left and right through pages
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //add tabs
        tabsMap.add(new CustomTab(getString(R.string.games), R.drawable.game, R.drawable.game_white));
        tabsMap.add(new CustomTab(getString(R.string.dings), R.drawable.d_ico, R.drawable.ding_white));
        tabsMap.add(new CustomTab(getString(R.string.friends), R.drawable.friends, R.drawable.friends_white_ico));
        tabsMap.add(new CustomTab(getString(R.string.settings), R.drawable.settings_icon_gray, R.drawable.settings_icon_white));

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        OnTabSelectedListener onTabSelectedListener = new OnTabSelectedListener(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        setupTabIcons();
        onTabSelectedListener.onTabSelected(tabLayout.getTabAt(0));

        setDefaultToolbar();

        //navigation drawer setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_tabbed_drawer_layout);
        //disable swipe; the drawer will be opened by fragment request only
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void setDefaultToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * adds the icons for each tab
     */
    private void setupTabIcons() {

        int count = 0;
        for (CustomTab tab : tabsMap) {
            Integer icon = tab.getUnselectedIcon();

            ViewGroup tabViews = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            final ImageView tabIcon = (ImageView) tabViews.findViewById(R.id.tab_icon);
            tabIcon.setImageResource(icon);

            tabLayout.getTabAt(count).setCustomView(tabViews);

            count++;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d("Lifecycle", "MainTabbedActivity SetUp");
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new GamesFragment(), "ONE");
        adapter.addFrag(new CreditsFragment(), "TWO");
        adapter.addFrag(new FriendsFragment(), "THREE");
        adapter.addFrag(SettingsFragment.newInstance(SessionManager.getInstance().getSessionUser()), "FOUR");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void requestOpenDrawer(Fragment requester) {
        mDrawerLayout.openDrawer(Gravity.RIGHT); //Edit Gravity.End need API 14
        if (requester instanceof ActivityListener) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            //"reset" navigation view to avoid duplicates
            if (navigationView.getHeaderCount() > 0) {
                navigationView.removeHeaderView(navigationView.getHeaderView(0));
            }
            if (navigationView.getMenu() != null) {
                navigationView.getMenu().clear();
            }
            navigationView.removeAllViews();

            //let fragment render the drawer
            ((ActivityListener) requester).renderDrawer(mDrawerLayout, navigationView);
        }
    }

    @Override
    public void requestCloseDrawer() {
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public void requestToggleBackBtn(boolean show, View.OnClickListener listener) {
        toolbar.findViewById(R.id.btn_toolbar_back).setVisibility(show ? View.VISIBLE : View.GONE);
        toolbar.findViewById(R.id.btn_toolbar_back).setOnClickListener(listener);
    }

    class OnTabSelectedListener extends TabLayout.ViewPagerOnTabSelectedListener {

        public OnTabSelectedListener(ViewPager viewPager) {
            super(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            super.onTabSelected(tab);
            //this is done to avoid a NullPointerException exception
            //when a fragment changes something of the toolbar
            //like {@link FriendsFragment} that changes it for a {@link SearchView}
            setDefaultToolbar();

            Log.d("Lifecycle", "viewPager y " + viewPager.getScrollY());
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.activity_main_tabbed_appbarlayout);
            Log.d("Lifecycle", "appBarLayout y " + appBarLayout.getScrollY());
            updateTab(tab, true);
            Fragment frag = adapter.getItem(tab.getPosition());
            if (frag instanceof ActivityListener) {
                ActivityListener finalFrag = (ActivityListener) frag;
                finalFrag.onFullView();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            super.onTabUnselected(tab);
            //this is done to avoid a NullPointerException exception
            //when a fragment changes something of the toolbar
            //like {@link FriendsFragment} that changes it for a {@link SearchView}
            Fragment frag = adapter.getItem(tab.getPosition());
            if (frag instanceof ActivityListener) {
                ActivityListener finalFrag = (ActivityListener) frag;
                finalFrag.clearToolbar();
                finalFrag.clearState();
            }
            updateTab(tab, false);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            super.onTabSelected(tab);
            updateTab(tab, true);
        }

        private void updateTab(TabLayout.Tab tab, boolean select) {
            CustomTab cTab = tabsMap.get(tab.getPosition());
            ViewGroup viewGroup = (ViewGroup) tab.getCustomView();
            if (viewGroup != null) {
                final ImageView tabIcon = (ImageView) viewGroup.findViewById(R.id.tab_icon);
                tabIcon.setImageResource(select ? cTab.getSelectedIcon() : cTab.getUnselectedIcon());

                ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(cTab.getText());
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);


        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
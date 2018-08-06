package com.duelit.ui.maintabscreen;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;
import com.roughike.bottombar.OnTabClickListener;

import java.util.ArrayList;
import java.util.List;

import com.duelit.R;
import com.duelit.SessionManager;
import com.duelit.ui.BaseActivity;
import com.duelit.ui.maintabscreen.credits.CreditsFragment;
import com.duelit.ui.maintabscreen.friends.FriendsFragment;
import com.duelit.ui.maintabscreen.games.GamesFragment;
import com.duelit.utils.views.CustomTab;

public class MainBottomTabsActivity extends BaseActivity implements FragmentListener {

    private Toolbar toolbar;
    //maps an icon to a text
    private List<CustomTab> tabsMap = new ArrayList<>();
    //this layout can be requested using {@link FragmentListener.requestOpenDrawer}
    private DrawerLayout mDrawerLayout;
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_tabs);

        //add tabs
        tabsMap.add(new CustomTab(getString(R.string.games), R.drawable.game, R.drawable.game_white));
        tabsMap.add(new CustomTab(getString(R.string.dings), R.drawable.d_ico, R.drawable.ding_white));
        tabsMap.add(new CustomTab(getString(R.string.friends), R.drawable.friends, R.drawable.friends_white_ico));
        tabsMap.add(new CustomTab(getString(R.string.settings), R.drawable.settings_icon_gray, R.drawable.settings_icon_white));

        mBottomBar = BottomBar.attach(findViewById(R.id.fragmentContainer), savedInstanceState);

        mBottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(new GamesFragment(), R.color.statusBarColor, ContextCompat.getDrawable(this, R.drawable.bar_icon_games_active), R.drawable.bar_icon_games_inactive, "Games"),
                new BottomBarFragment(new FriendsFragment(), R.color.statusBarColor, ContextCompat.getDrawable(this, R.drawable.bar_icon_friends_active), R.drawable.bar_icon_friends_inactive, "Friends"),
                new BottomBarFragment(new CreditsFragment(), R.color.statusBarColor, ContextCompat.getDrawable(this, R.drawable.bar_icon_activity_active), R.drawable.bar_icon_activity_inactive, "Activity"),
                new BottomBarFragment(SettingsFragment.newInstance(SessionManager.getInstance().getSessionUser()), R.color.statusBarColor, ContextCompat.getDrawable(this, R.drawable.bar_icon_profile_active), R.drawable.bar_icon_profile_inactive, "Profile")
        );

        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                mBottomBar.getChildAt(position);
            }

            @Override
            public void onTabReSelected(int position) {

            }
        });
        /*mBottomBar.setItemsFromMenu(R.menu.menu_main_tabbed_bottom, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });*/

        setupTabIcons();

        //navigation drawer setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_tabbed_drawer_layout);
        //disable swipe; the drawer will be opened by fragment request only
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

            //tabLayout.getTabAt(count).setCustomView(tabViews);

            count++;
        }
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

    private void updateTab(int position, boolean select) {
        CustomTab cTab = tabsMap.get(position);
        /*ViewGroup viewGroup = (ViewGroup) tab.getCustomView();
        if (viewGroup != null) {
            final ImageView tabIcon = (ImageView) viewGroup.findViewById(R.id.tab_icon);
            tabIcon.setImageResource(select ? cTab.getSelectedIcon() : cTab.getUnselectedIcon());

            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(cTab.getText());
        }*/
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
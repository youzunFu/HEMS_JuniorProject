package net.ddns.b505.hems;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.b505.hems.AboutFragment.ControlAirAboutFragment;
import net.ddns.b505.hems.AboutFragment.ControlLightAboutFragment;
import net.ddns.b505.hems.AboutFragment.ControlPlugAboutFragment;
import net.ddns.b505.hems.AboutFragment.DRAboutFragment;
import net.ddns.b505.hems.AboutFragment.HistoryAboutFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private ViewPager myViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int[] IconResID = {R.drawable.selector_home, R.drawable.selector_two, R.drawable.selector_three};
    private int[] TollBarTitle = {R.string.tabhost1, R.string.tabhost2, R.string.tabhost3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        tabLayout = (TabLayout) findViewById(R.id.TabLayout);
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        toolbar.setTitle(TollBarTitle[0]);
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);

        //drawerLayout 整合 toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //------------

        setViewPager();
        tabLayout.setupWithViewPager(myViewPager);
        setTabLayoutIcon();

        //更改側滑選單header內容方式
        // 取得Header
        View header = navigation_view.getHeaderView(0);
        // 取得Header中的TextView
        TextView txtHeader = (TextView) header.findViewById(R.id.txtHeader);
        txtHeader.setText("使用者資訊");

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);

                // 取得選項id
                int id = item.getItemId();

                // 依照id判斷點了哪個項目並做相應事件
                if (id == R.id.action_home) {
                    // 按下「首頁」要做的事
                    Toast.makeText(MainActivity.this, "首頁", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_help) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(MainActivity.this, "使用說明", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_logout) {
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        SharedPreferences pref = getSharedPreferences("PREF_NFC", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        boolean autoisRemember = pref.getBoolean("auto_check", false);  //判斷LoginInfo內記錄的自動登入是否有被勾選
                        boolean memoryisRemember = pref.getBoolean("login_check", false);  //查看app中是否已經儲存過帳號密碼
                        editor.putBoolean("logout", true);
                        editor.putBoolean("auto_check", autoisRemember);
                        editor.putBoolean("login_check", memoryisRemember);
                        editor.commit();

                        startActivity(intent);
                    }
                }
                // 略..

                return false;
            }
        });

    }

    //設計工具列的圖示
    public void setTabLayoutIcon() {
        for (int i = 0; i < 3; i++) {
            tabLayout.getTabAt(i).setIcon(IconResID[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbar.getMenu().clear();
                switch (tab.getPosition()) {
                    case 0:
                        toolbar.inflateMenu(R.menu.menu_one);
                        toolbar.setTitle(TollBarTitle[0]);
                        break;
                    case 1:
                        toolbar.inflateMenu(R.menu.menu_two);
                        toolbar.setTitle(TollBarTitle[1]);
                        break;
                    case 2:
                        toolbar.inflateMenu(R.menu.menu_three);
                        toolbar.setTitle(TollBarTitle[2]);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    //設計分頁
    private void setViewPager() {
        IndexFragment myFragment1 = new IndexFragment();
        FragmentList_Two myFragment2 = new FragmentList_Two();
        FragmentList_Three myFragment3 = new FragmentList_Three();
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);
        fragmentList.add(myFragment3);
        ViewPagerFragmentAdapter myFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
    }

    //Activity開啟時預設工作列
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        switch (item_id) {
            //air about
            case R.id.ItemControlAir:
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                android.app.Fragment prev1 = getFragmentManager().findFragmentByTag("ItemControlAir");
                if (prev1 != null) {
                    ft1.remove(prev1);
                }
                ft1.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment1 = new ControlAirAboutFragment();
                newFragment1.show(ft1, "ItemControlAir");
                break;

            //plug about
            case R.id.ItemControlPlug:
                FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                android.app.Fragment prev2 = getFragmentManager().findFragmentByTag("ItemControlPlug");
                if (prev2 != null) {
                    ft2.remove(prev2);
                }
                ft2.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment2 = new ControlPlugAboutFragment();
                newFragment2.show(ft2, "ItemControlPlug");
                break;
            //Light about
            case R.id.ItemControlLight:
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                android.app.Fragment prev3 = getFragmentManager().findFragmentByTag("ItemControlLight");
                if (prev3 != null) {
                    ft3.remove(prev3);
                }
                ft3.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment3 = new ControlLightAboutFragment();
                newFragment3.show(ft3, "ItemControlLight");
                break;
            //History about
            case R.id.ItemHistory:
                FragmentTransaction ft4 = getFragmentManager().beginTransaction();
                android.app.Fragment prev4 = getFragmentManager().findFragmentByTag("ItemHistory");
                if (prev4 != null) {
                    ft4.remove(prev4);
                }
                ft4.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment4 = new HistoryAboutFragment();
                newFragment4.show(ft4, "ItemHistory");
                break;
            //DR about
            case R.id.ItemDR:
                FragmentTransaction ft5 = getFragmentManager().beginTransaction();
                android.app.Fragment prev5 = getFragmentManager().findFragmentByTag("ItemDR");
                if (prev5 != null) {
                    ft5.remove(prev5);
                }
                ft5.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment5 = new DRAboutFragment();
                newFragment5.show(ft5, "ItemDR");
                break;

            default: return false;
        }
        return true;
    }
}
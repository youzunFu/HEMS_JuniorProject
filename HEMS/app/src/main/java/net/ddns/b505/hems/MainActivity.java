package net.ddns.b505.hems;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager myViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int[] IconResID = {R.drawable.selector_home,R.drawable.selector_two,R.drawable.selector_three};
    private int[] TollBarTitle = {R.string.tabhost1,R.string.tabhost2,R.string.tabhost3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        tabLayout = (TabLayout) findViewById(R.id.TabLayout);
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        toolbar.setTitle(TollBarTitle[0]);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        setViewPager();
        tabLayout.setupWithViewPager(myViewPager);
        setTabLayoutIcon();
    }
    //設計工具列的圖示
    public void setTabLayoutIcon(){
        for(int i =0; i < 3;i++){
            tabLayout.getTabAt(i).setIcon(IconResID[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbar.getMenu().clear();
                switch(tab.getPosition()){
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
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }
    //設計分頁
    private void setViewPager(){
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
}

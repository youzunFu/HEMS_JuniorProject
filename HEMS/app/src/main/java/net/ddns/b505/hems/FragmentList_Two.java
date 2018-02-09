package net.ddns.b505.hems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_Two extends Fragment {
    private TextView tvHistoryPlug1,tvHistoryPlug2,tvHistoryPlug3,tvHistoryPlug4,tvHistoryLight1,tvHistoryLight2,tvHistoryLight3,tvHistoryLight4;
    private LinearLayout LayoutHistoryPlug1,LayoutHistoryPlug2,LayoutHistoryPlug3,LayoutHistoryPlug4,LayoutHistoryLight1,LayoutHistoryLight2,LayoutHistoryLight3,LayoutHistoryLight4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentlist_two, container, false);

       /* tvHistoryPlug1 = (TextView) view.findViewById(R.id.HistoryPlug1);
        tvHistoryPlug2 = (TextView) view.findViewById(R.id.HistoryPlug2);
        tvHistoryPlug3 = (TextView) view.findViewById(R.id.HistoryPlug3);
        tvHistoryPlug4 = (TextView) view.findViewById(R.id.HistoryPlug4);
        tvHistoryLight1 = (TextView) view.findViewById(R.id.HistoryLight1);
        tvHistoryLight2 = (TextView) view.findViewById(R.id.HistoryLight2);
        tvHistoryLight3 = (TextView) view.findViewById(R.id.HistoryLight3);
        tvHistoryLight4 = (TextView) view.findViewById(R.id.HistoryLight4);
*/

        LayoutHistoryPlug1=(LinearLayout) view.findViewById(R.id.LayoutHistoryPlug1);
        LayoutHistoryPlug2=(LinearLayout) view.findViewById(R.id.LayoutHistoryPlug2);
        LayoutHistoryPlug3=(LinearLayout) view.findViewById(R.id.LayoutHistoryPlug3);
        LayoutHistoryPlug4=(LinearLayout) view.findViewById(R.id.LayoutHistoryPlug4);
        LayoutHistoryLight1=(LinearLayout) view.findViewById(R.id.LayoutHistoryLight1);
        LayoutHistoryLight2=(LinearLayout) view.findViewById(R.id.LayoutHistoryLight2);
        LayoutHistoryLight3=(LinearLayout) view.findViewById(R.id.LayoutHistoryLight3);
        LayoutHistoryLight4=(LinearLayout) view.findViewById(R.id.LayoutHistoryLight4);

        //Plug Click
        LayoutHistoryPlug1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPlug1Click(view);
            }
        });

        LayoutHistoryPlug2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPlug2Click(view);
            }
        });

        LayoutHistoryPlug3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPlug3Click(view);
            }
        });

        LayoutHistoryPlug4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryPlug4Click(view);
            }
        });


        //Light Click
        LayoutHistoryLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryLight1Click(view);
            }
        });
        LayoutHistoryLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryLight2Click(view);
            }
        });
        LayoutHistoryLight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryLight3Click(view);
            }
        });
        LayoutHistoryLight4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryLight4Click(view);
            }
        });

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        /*
        txtUser.setSelected(true);  //跑馬燈動起來動起來！
        GridView grid = (GridView) getView().findViewById(R.id.grid);
        IndexFragment.IconAdapter gAdapter = new IndexFragment.IconAdapter();
        grid.setAdapter(gAdapter);

        //GridView功能項目觸發
        grid.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;
                        switch ((int) id){
                            //點擊電網
                            case R.drawable.aircon:
                                intent = new Intent(getActivity(), airb505left.class);
                                startActivity(intent);
                                break;
                            case R.drawable.plug:
                                intent = new Intent(getActivity(), Pluginfo.class);
                                startActivity(intent);
                                break;
                            case R.drawable.light:
                                intent = new Intent(getActivity(), Lightinfo.class);
                                startActivity(intent);
                                break;
                        }
                    }// onItemSelected
                } //new OnItemClickListener
        ); //grid.setOnItemClickListener

        */

    }

    public void HistoryPlug1Click(View view){
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","001");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void HistoryPlug2Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","002");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void HistoryPlug3Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","003");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void HistoryPlug4Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","004");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public  void HistoryLight1Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","001");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public  void HistoryLight2Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","002");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public  void HistoryLight3Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","003");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public  void HistoryLight4Click(View view) {
        Intent intent = new Intent(getActivity(),MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","004");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
package net.ddns.b505.hems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;


public class IndexFragment extends Fragment {
    private TextView Date, Weather, Probability, txtUser;
    String[] func = {"冷氣控制", "智慧插座", "智慧燈具"};
    int[] icons = {R.drawable.icon_aircondictioner,
            R.drawable.icon_plug,
            R.drawable.icon_light};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);


        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtUser = (TextView) getView().findViewById(R.id.tv_username);
        txtUser.setSingleLine();
        txtUser.setText("");
        txtUser.setSelected(true);  //跑馬燈動起來動起來！
        GridView grid = (GridView) getView().findViewById(R.id.grid);
        IconAdapter gAdapter = new IconAdapter();
        grid.setAdapter(gAdapter);

        //GridView功能項目觸發
        grid.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;
                        switch ((int) id){
                            //點擊電網
                            case R.drawable.icon_aircondictioner:
                                intent = new Intent(getActivity(), airb505left.class);
                                startActivity(intent);
                                break;
                            case R.drawable.icon_plug:
                                intent = new Intent(getActivity(), PluginfoV2.class);
                                startActivity(intent);
                                break;
                            case R.drawable.icon_light:
                                intent = new Intent(getActivity(), Lightinfo.class);
                                startActivity(intent);
                                break;
                        }
                    }// onItemSelected
                } //new OnItemClickListener
        ); //grid.setOnItemClickListener
    }

    class IconAdapter extends BaseAdapter {

        //回傳GridView中項目的個數
        @Override
        public int getCount() {
            return func.length;
        }

        //回傳position所對應的資源
        @Override
        public Object getItem(int position) {
            return func[position];
        }

        //回傳position所對應的id值
        @Override
        public long getItemId(int position) {
            return icons[position];
        }

        //依照position產生相對應的功能項目
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null){
                row = getActivity().getLayoutInflater().inflate(R.layout.item_row, null);
                ImageView image = (ImageView) row.findViewById(R.id.item_image);
                TextView text = (TextView) row.findViewById(R.id.item_text);
                image.setImageResource(icons[position]);
                image.setMinimumHeight(50);
                image.setMinimumWidth(50);

                text.setText(func[position]);
            }
            return row;
        }
    }
}

package net.ddns.b505.hems;


import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DrSetFragment extends DialogFragment {
    //private static final String Appliances = "http://192.168.1.100/sl_demo_api/appliances.php";
    private String Appliances = "sl_demo_api/appliances.php";
    private Switch swChe1_ac1, swChe1_light1, swChe1_light2, swChe1_light3, swChe1_light4,
            swRec1_ac1, swRec1_light1, swRec1_light2, swRec1_light3, swRec1_light4,
            swChe2_ac1, swChe2_light1, swChe2_light2, swChe2_light3, swChe2_light4,
            swRec2_ac1, swRec2_light1, swRec2_light2, swRec2_light3, swRec2_light4,
            swChe3_ac1, swChe3_ac2, swChe3_light1, swChe3_light2, swChe3_light3, swChe3_light4, swChe3_light5, swChe3_light6,
            swRec3_ac1, swRec3_ac2, swRec3_light1, swRec3_light2, swRec3_light3, swRec3_light4, swRec3_light5, swRec3_light6;
    private SharedPreferences setting;
    private TextView tvCtrl1total, tvCtrl1ac1, tvCtrl1light1, tvCtrl1light2, tvCtrl1light3, tvCtrl1light4,
            tvCtrl2total, tvCtrl2ac1, tvCtrl2light1, tvCtrl2light2, tvCtrl2light3, tvCtrl2light4,
            tvCtrl3total, tvCtrl3ac1, tvCtrl3ac2, tvCtrl3light1, tvCtrl3light2, tvCtrl3light3, tvCtrl3light4, tvCtrl3light5, tvCtrl3light6;
    private LinearLayout Control1, Control2, Control3;
    private double Total1, Total2, Total3;
    private String token, check;
    private JsonArrayRequest requestArray;
    private JsonObjectRequest requestOb;
    private RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dr_set, container, false);
    }
    @Override
    public void onStart() {
        getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels;
        super.onStart();
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        String ip = setting.getString("ip","");
        int Comparesurl = Appliances.indexOf("//");
        if(Comparesurl == -1) {
            Appliances = ip + Appliances;
        }
        findviewbyid();
        check = setting.getString("Controlnum","");

        final ViewGroup dr = (ViewGroup) getView().findViewById(R.id.Layout_dr_set);
        Control1 = (LinearLayout) dr.findViewById(R.id.control1);
        Control2 = (LinearLayout) dr.findViewById(R.id.control2);
        Control3 = (LinearLayout) dr.findViewById(R.id.control3);
        if(check.equals("1")){
            Control1.setVisibility(View.VISIBLE);
        }else if(check.equals("2")){
            Control2.setVisibility(View.VISIBLE);
        }else if(check.equals("3")){
            Control3.setVisibility(View.VISIBLE);
        }
        switch1();
        switch2();
        switch3();
        getAppliances();
    }
    public void findviewbyid() {
        tvCtrl1total = (TextView) getView().findViewById(R.id.tv_ctrl1_total);
        tvCtrl1ac1 = (TextView) getView().findViewById(R.id.tv_Ctrl1_ac1);
        tvCtrl1light1 = (TextView) getView().findViewById(R.id.tv_Ctrl1_light1);
        tvCtrl1light2 = (TextView) getView().findViewById(R.id.tv_Ctrl1_light2);
        tvCtrl1light3 = (TextView) getView().findViewById(R.id.tv_Ctrl1_plug1);
        tvCtrl1light4 = (TextView) getView().findViewById(R.id.tv_Ctrl1_plug2);
        tvCtrl2total = (TextView) getView().findViewById(R.id.tv_ctrl2_total);
        tvCtrl2ac1 = (TextView) getView().findViewById(R.id.tv_Ctrl2_ac1);
        tvCtrl2light1 = (TextView) getView().findViewById(R.id.tv_Ctrl2_light1);
        tvCtrl2light2 = (TextView) getView().findViewById(R.id.tv_Ctrl2_light2);
        tvCtrl2light3 = (TextView) getView().findViewById(R.id.tv_Ctrl2_light3);
        tvCtrl2light4 = (TextView) getView().findViewById(R.id.tv_Ctrl2_light4);
        tvCtrl3total = (TextView) getView().findViewById(R.id.tv_ctrl3_total);
        tvCtrl3ac1 = (TextView) getView().findViewById(R.id.tv_Ctrl3_ac1);
        tvCtrl3ac2 = (TextView) getView().findViewById(R.id.tv_Ctrl3_ac2);
        tvCtrl3light1 = (TextView) getView().findViewById(R.id.tv_Ctrl3_light1);
        tvCtrl3light2 = (TextView) getView().findViewById(R.id.tv_Ctrl3_light2);
        tvCtrl3light3 = (TextView) getView().findViewById(R.id.tv_Ctrl3_light3);
        tvCtrl3light4 = (TextView) getView().findViewById(R.id.tv_Ctrl3_light4);
        tvCtrl3light5 = (TextView) getView().findViewById(R.id.tv_Ctrl3_light5);
        tvCtrl3light6 = (TextView) getView().findViewById(R.id.tv_Ctrl3_light6);
        swChe1_ac1 = (Switch) getView().findViewById(R.id.sw_Che1_ac1);
        swChe1_light1 = (Switch) getView().findViewById(R.id.sw_Che1_light1);
        swChe1_light2 = (Switch) getView().findViewById(R.id.sw_Che1_light2);
        swChe1_light3 = (Switch) getView().findViewById(R.id.sw_Che1_plug1);
        swChe1_light4 = (Switch) getView().findViewById(R.id.sw_Che1_plug2);
        swRec1_ac1 = (Switch) getView().findViewById(R.id.sw_Rec1_ac1);
        swRec1_light1 = (Switch) getView().findViewById(R.id.sw_Rec1_light1);
        swRec1_light2 = (Switch) getView().findViewById(R.id.sw_Rec1_light2);
        swRec1_light3 = (Switch) getView().findViewById(R.id.sw_Rec1_plug1);
        swRec1_light4 = (Switch) getView().findViewById(R.id.sw_Rec1_plug2);
        swChe2_ac1 = (Switch) getView().findViewById(R.id.sw_Che2_ac1);
        swChe2_light1 = (Switch) getView().findViewById(R.id.sw_Che2_light1);
        swChe2_light2 = (Switch) getView().findViewById(R.id.sw_Che2_light2);
        swChe2_light3 = (Switch) getView().findViewById(R.id.sw_Che2_light3);
        swChe2_light4 = (Switch) getView().findViewById(R.id.sw_Che2_light4);
        swRec2_ac1 = (Switch) getView().findViewById(R.id.sw_Rec2_ac1);
        swRec2_light1 = (Switch) getView().findViewById(R.id.sw_Rec2_light1);
        swRec2_light2 = (Switch) getView().findViewById(R.id.sw_Rec2_light2);
        swRec2_light3 = (Switch) getView().findViewById(R.id.sw_Rec2_light3);
        swRec2_light4 = (Switch) getView().findViewById(R.id.sw_Rec2_light4);
        swChe3_ac1 = (Switch) getView().findViewById(R.id.sw_Che3_ac1);
        swChe3_ac2 = (Switch) getView().findViewById(R.id.sw_Che3_ac2);
        swChe3_light1 = (Switch) getView().findViewById(R.id.sw_Che3_light1);
        swChe3_light2 = (Switch) getView().findViewById(R.id.sw_Che3_light2);
        swChe3_light3 = (Switch) getView().findViewById(R.id.sw_Che3_light3);
        swChe3_light4 = (Switch) getView().findViewById(R.id.sw_Che3_light4);
        swChe3_light5 = (Switch) getView().findViewById(R.id.sw_Che3_light5);
        swChe3_light6 = (Switch) getView().findViewById(R.id.sw_Che3_light6);
        swRec3_ac1 = (Switch) getView().findViewById(R.id.sw_Rec3_ac1);
        swRec3_ac2 = (Switch) getView().findViewById(R.id.sw_Rec3_ac2);
        swRec3_light1 = (Switch) getView().findViewById(R.id.sw_Rec3_light1);
        swRec3_light2 = (Switch) getView().findViewById(R.id.sw_Rec3_light2);
        swRec3_light3 = (Switch) getView().findViewById(R.id.sw_Rec3_light3);
        swRec3_light4 = (Switch) getView().findViewById(R.id.sw_Rec3_light4);
        swRec3_light5 = (Switch) getView().findViewById(R.id.sw_Rec3_light5);
        swRec3_light6 = (Switch) getView().findViewById(R.id.sw_Rec3_light6);
    }
    public void switch1() {
        swChe1_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1ac1.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","1","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","1","0");
                }
            }
        });
        swChe1_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light1.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","10","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","10","0");
                }
            }
        });
        swChe1_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light2.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","11","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","11","0");
                }
            }
        });
        swChe1_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light3.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","12","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","12","0");
                }
            }
        });
        swChe1_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light4.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","13","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","13","0");
                }
            }
        });
        swRec1_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","1","1");
                } else {
                    setAppliances("setApplianceRecover","1","0");
                }
            }
        });
        swRec1_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","10","1");
                } else {
                    setAppliances("setApplianceRecover","10","0");
                }
            }
        });
        swRec1_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","11","1");
                } else {
                    setAppliances("setApplianceRecover","11","0");
                }
            }
        });
        swRec1_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","12","1");
                } else {
                    setAppliances("setApplianceRecover","12","0");
                }
            }
        });
        swRec1_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","13","1");
                } else {
                    setAppliances("setApplianceRecover","13","0");
                }
            }
        });
    }
    public void switch2() {
        swChe2_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl2ac1.getText().toString());
                if (isChecked) {
                    Total2 += i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","2","1");
                } else {
                    Total2 -= i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","2","0");
                }
            }
        });
        swChe2_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl2light1.getText().toString());
                if (isChecked) {
                    Total2 += i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","14","1");
                } else {
                    Total2 -= i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","14","0");
                }
            }
        });
        swChe2_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl2light2.getText().toString());
                if (isChecked) {
                    Total2 += i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","15","1");
                } else {
                    Total2 -= i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","15","0");
                }
            }
        });
        swChe2_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl2light3.getText().toString());
                if (isChecked) {
                    Total2 += i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","16","1");
                } else {
                    Total2 -= i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","16","0");
                }
            }
        });
        swChe2_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl2light4.getText().toString());
                if (isChecked) {
                    Total2 += i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","17","1");
                } else {
                    Total2 -= i;
                    tvCtrl2total.setText(String.format("%.3f", Total2));
                    setAppliances("setApplianceChecked","17","0");
                }
            }
        });
        swRec2_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","1","1");
                } else {
                    setAppliances("setApplianceRecover","1","0");
                }
            }
        });
        swRec2_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","10","1");
                } else {
                    setAppliances("setApplianceRecover","10","0");
                }
            }
        });
        swRec2_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","11","1");
                } else {
                    setAppliances("setApplianceRecover","11","0");
                }
            }
        });
        swRec2_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","12","1");
                } else {
                    setAppliances("setApplianceRecover","12","0");
                }
            }
        });
        swRec2_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","13","1");
                } else {
                    setAppliances("setApplianceRecover","13","0");
                }
            }
        });
    }
    public void switch3() {
        swChe3_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3ac1.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","3","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","3","0");
                }
            }
        });
        swChe3_ac2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3ac2.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","4","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","4","0");
                }
            }
        });
        swChe3_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3light1.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","18","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","18","0");
                }
            }
        });
        swChe3_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3light2.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","19","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","19","0");
                }
            }
        });
        swChe3_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3light3.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","20","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","20","0");
                }
            }
        });
        swChe3_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3light4.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","21","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","21","0");
                }
            }
        });
        swChe3_light5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3light5.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","22","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","22","0");
                }
            }
        });
        swChe3_light6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl3light6.getText().toString());
                if (isChecked) {
                    Total3 += i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","23","1");
                } else {
                    Total3 -= i;
                    tvCtrl3total.setText(String.format("%.3f", Total3));
                    setAppliances("setApplianceChecked","23","0");
                }
            }
        });
        swRec3_ac1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","3","1");
                } else {
                    setAppliances("setApplianceRecover","3","0");
                }
            }
        });
        swRec3_ac2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","4","1");
                } else {
                    setAppliances("setApplianceRecover","4","0");
                }
            }
        });
        swRec3_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","18","1");
                } else {
                    setAppliances("setApplianceRecover","18","0");
                }
            }
        });
        swRec3_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","19","1");
                } else {
                    setAppliances("setApplianceRecover","19","0");
                }
            }
        });
        swRec3_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","20","1");
                } else {
                    setAppliances("setApplianceRecover","20","0");
                }
            }
        });
        swRec3_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","21","1");
                } else {
                    setAppliances("setApplianceRecover","21","0");
                }
            }
        });
        swRec3_light5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","22","1");
                } else {
                    setAppliances("setApplianceRecover","22","0");
                }
            }
        });
        swRec3_light6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","23","1");
                } else {
                    setAppliances("setApplianceRecover","23","0");
                }
            }
        });
    }
    public void getAppliances(){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getAppliances");
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        requestArray = new JsonArrayRequest(Request.Method.POST, Appliances, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    tvCtrl1ac1.setText(response.getJSONObject(0).getString("power"));
                    tvCtrl1light1.setText(response.getJSONObject(8).getString("power"));
                    tvCtrl1light2.setText(response.getJSONObject(9).getString("power"));
                    tvCtrl1light3.setText(response.getJSONObject(10).getString("power"));
                    tvCtrl1light4.setText(response.getJSONObject(11).getString("power"));
                    tvCtrl2ac1.setText(response.getJSONObject(1).getString("power"));
                    tvCtrl2light1.setText(response.getJSONObject(12).getString("power"));
                    tvCtrl2light2.setText(response.getJSONObject(13).getString("power"));
                    tvCtrl2light3.setText(response.getJSONObject(14).getString("power"));
                    tvCtrl2light4.setText(response.getJSONObject(15).getString("power"));
                    tvCtrl3ac1.setText(response.getJSONObject(2).getString("power"));
                    tvCtrl3ac2.setText(response.getJSONObject(3).getString("power"));
                    tvCtrl3light1.setText(response.getJSONObject(16).getString("power"));
                    tvCtrl3light2.setText(response.getJSONObject(17).getString("power"));
                    tvCtrl3light3.setText(response.getJSONObject(18).getString("power"));
                    tvCtrl3light4.setText(response.getJSONObject(19).getString("power"));
                    tvCtrl3light5.setText(response.getJSONObject(20).getString("power"));
                    tvCtrl3light6.setText(response.getJSONObject(21).getString("power"));
                    if(response.getJSONObject(0).getString("checked").equals("1"))
                    {swChe1_ac1.setChecked(true);
                    }
                    if(response.getJSONObject(1).getString("checked").equals("1"))
                    {swChe2_ac1.setChecked(true);
                    }
                    if(response.getJSONObject(2).getString("checked").equals("1"))
                    {swChe3_ac1.setChecked(true);
                    }
                    if(response.getJSONObject(3).getString("checked").equals("1"))
                    {swChe3_ac2.setChecked(true);
                    }
                    if(response.getJSONObject(8).getString("checked").equals("1"))
                    {swChe1_light1.setChecked(true);
                    }
                    if(response.getJSONObject(9).getString("checked").equals("1"))
                    {swChe1_light2.setChecked(true);
                    }
                    if(response.getJSONObject(10).getString("checked").equals("1"))
                    {swChe1_light3.setChecked(true);
                    }
                    if(response.getJSONObject(11).getString("checked").equals("1"))
                    {swChe1_light4.setChecked(true);
                    }
                    if(response.getJSONObject(12).getString("checked").equals("1"))
                    {swChe2_light1.setChecked(true);
                    }
                    if(response.getJSONObject(13).getString("checked").equals("1"))
                    {swChe2_light2.setChecked(true);
                    }
                    if(response.getJSONObject(14).getString("checked").equals("1"))
                    {swChe2_light3.setChecked(true);
                    }
                    if(response.getJSONObject(15).getString("checked").equals("1"))
                    {swChe2_light4.setChecked(true);
                    }
                    if(response.getJSONObject(16).getString("checked").equals("1"))
                    {swChe3_light1.setChecked(true);
                    }
                    if(response.getJSONObject(17).getString("checked").equals("1"))
                    {swChe3_light2.setChecked(true);
                    }
                    if(response.getJSONObject(18).getString("checked").equals("1"))
                    {swChe3_light3.setChecked(true);
                    }
                    if(response.getJSONObject(19).getString("checked").equals("1"))
                    {swChe3_light4.setChecked(true);
                    }
                    if(response.getJSONObject(20).getString("checked").equals("1"))
                    {swChe3_light5.setChecked(true);
                    }
                    if(response.getJSONObject(21).getString("checked").equals("1"))
                    {swChe3_light6.setChecked(true);
                    }
                    if(response.getJSONObject(0).getString("recover").equals("1"))
                    {swRec1_ac1.setChecked(true);
                    }
                    if(response.getJSONObject(1).getString("recover").equals("1"))
                    {swRec2_ac1.setChecked(true);
                    }
                    if(response.getJSONObject(2).getString("recover").equals("1"))
                    {swRec3_ac1.setChecked(true);
                    }
                    if(response.getJSONObject(3).getString("recover").equals("1"))
                    {swRec3_ac2.setChecked(true);
                    }
                    if(response.getJSONObject(8).getString("recover").equals("1"))
                    {swRec1_light1.setChecked(true);
                    }
                    if(response.getJSONObject(9).getString("recover").equals("1"))
                    {swRec1_light2.setChecked(true);
                    }
                    if(response.getJSONObject(10).getString("recover").equals("1"))
                    {swRec1_light3.setChecked(true);
                    }
                    if(response.getJSONObject(11).getString("recover").equals("1"))
                    {swRec1_light4.setChecked(true);
                    }
                    if(response.getJSONObject(12).getString("recover").equals("1"))
                    {swRec2_light1.setChecked(true);
                    }
                    if(response.getJSONObject(13).getString("recover").equals("1"))
                    {swRec2_light2.setChecked(true);
                    }
                    if(response.getJSONObject(14).getString("recover").equals("1"))
                    {swRec2_light3.setChecked(true);
                    }
                    if(response.getJSONObject(15).getString("recover").equals("1"))
                    {swRec2_light4.setChecked(true);
                    }
                    if(response.getJSONObject(16).getString("recover").equals("1"))
                    {swRec3_light1.setChecked(true);
                    }
                    if(response.getJSONObject(17).getString("recover").equals("1"))
                    {swRec3_light2.setChecked(true);
                    }
                    if(response.getJSONObject(18).getString("recover").equals("1"))
                    {swRec3_light3.setChecked(true);
                    }
                    if(response.getJSONObject(19).getString("recover").equals("1"))
                    {swRec3_light4.setChecked(true);
                    }
                    if(response.getJSONObject(20).getString("recover").equals("1"))
                    {swRec3_light5.setChecked(true);
                    }
                    if(response.getJSONObject(21).getString("recover").equals("1"))
                    {swRec3_light6.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(requestArray);
    }
    public void setAppliances(String... params){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction(params[0]);
        mJsonStr.setToken(token);
        switch (params[0]){
            case "setApplianceChecked":
                mJsonStr.setId(params[1]);
                mJsonStr.setChecked(params[2]);
                break;
            case "setApplianceRecover":
                mJsonStr.setId(params[1]);
                mJsonStr.setRecover(params[2]);
                break;
        }
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        requestOb = new JsonObjectRequest(Request.Method.POST, Appliances, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*try {
                    Toast.makeText(getActivity(),"Result： "+response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error" , Toast.LENGTH_SHORT).show();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(requestOb);
    }
}
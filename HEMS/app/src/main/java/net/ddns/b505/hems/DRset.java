package net.ddns.b505.hems;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by RED on 2018/2/4.
 */

public class DRset extends AppCompatActivity {
    private String url = "http://163.18.57.43/HEMSphp/plugschedule.php";
    //private String Appliances = "HEMSphp/plugschdule.php";
    private Switch swChe1_ac1, swChe1_light1, swChe1_light2, swChe1_plug1, swChe1_plug2,
            swRec1_ac1, swRec1_light1, swRec1_light2, swRec1_plug1, swRec1_plug2,
            swChe2_ac1, swChe2_light1, swChe2_light2, swChe2_light3, swChe2_light4,
            swRec2_ac1, swRec2_light1, swRec2_light2, swRec2_light3, swRec2_light4,
            swChe3_ac1, swChe3_ac2, swChe3_light1, swChe3_light2, swChe3_light3, swChe3_light4, swChe3_light5, swChe3_light6,
            swRec3_ac1, swRec3_ac2, swRec3_light1, swRec3_light2, swRec3_light3, swRec3_light4, swRec3_light5, swRec3_light6;
    private SharedPreferences setting;
    private TextView tvCtrl1total, tvCtrl1ac1, tvCtrl1light1, tvCtrl1light2, tvCtrl1plug1, tvCtrl1plug2,
            tvCtrl2total, tvCtrl2ac1, tvCtrl2light1, tvCtrl2light2, tvCtrl2light3, tvCtrl2light4,
            tvCtrl3total, tvCtrl3ac1, tvCtrl3ac2, tvCtrl3light1, tvCtrl3light2, tvCtrl3light3, tvCtrl3light4, tvCtrl3light5, tvCtrl3light6;
    private LinearLayout Control1, Control2, Control3;
    private double Total1, Total2, Total3;
    private String token, check;
    private JsonArrayRequest requestArray;
    private JsonObjectRequest requestOb;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dr_set);

        requestQueue = Volley.newRequestQueue(this);
        setting = this.getSharedPreferences("auto",0);
        //token = setting.getString("TOKEN","");
        /*
        String ip = setting.getString("ip","");
        int Comparesurl = Appliances.indexOf("//");
        if(Comparesurl == -1) {
            Appliances = ip + Appliances;
        }
        */
        findviewbyid();
        check = setting.getString("Controlnum","");

        final ViewGroup dr = (ViewGroup) findViewById(R.id.Layout_dr_set);
        Control1 = (LinearLayout) dr.findViewById(R.id.control1);
        Control2 = (LinearLayout) dr.findViewById(R.id.control2);
        Control3 = (LinearLayout) dr.findViewById(R.id.control3);
        Control1.setVisibility(View.VISIBLE);

        if(check.equals("1")){
            Control1.setVisibility(View.VISIBLE);
        }else if(check.equals("2")){
            Control2.setVisibility(View.VISIBLE);
        }else if(check.equals("3")){
            Control3.setVisibility(View.VISIBLE);
        }
        switch1();
       // switch2();
        //switch3();
        getAppliances();
    }

    public void findviewbyid() {
        tvCtrl1total = (TextView) findViewById(R.id.tv_ctrl1_total);
        tvCtrl1ac1 = (TextView) findViewById(R.id.tv_Ctrl1_ac1);
        tvCtrl1light1 = (TextView) findViewById(R.id.tv_Ctrl1_light1);
        tvCtrl1light2 = (TextView) findViewById(R.id.tv_Ctrl1_light2);
        tvCtrl1plug1 = (TextView) findViewById(R.id.tv_Ctrl1_plug1);
        tvCtrl1plug2 = (TextView) findViewById(R.id.tv_Ctrl1_plug2);
        tvCtrl2total = (TextView) findViewById(R.id.tv_ctrl2_total);
        tvCtrl2ac1 = (TextView) findViewById(R.id.tv_Ctrl2_ac1);
        tvCtrl2light1 = (TextView) findViewById(R.id.tv_Ctrl2_light1);
        tvCtrl2light2 = (TextView) findViewById(R.id.tv_Ctrl2_light2);
        tvCtrl2light3 = (TextView) findViewById(R.id.tv_Ctrl2_light3);
        tvCtrl2light4 = (TextView) findViewById(R.id.tv_Ctrl2_light4);
        tvCtrl3total = (TextView) findViewById(R.id.tv_ctrl3_total);
        tvCtrl3ac1 = (TextView) findViewById(R.id.tv_Ctrl3_ac1);
        tvCtrl3ac2 = (TextView) findViewById(R.id.tv_Ctrl3_ac2);
        tvCtrl3light1 = (TextView) findViewById(R.id.tv_Ctrl3_light1);
        tvCtrl3light2 = (TextView) findViewById(R.id.tv_Ctrl3_light2);
        tvCtrl3light3 = (TextView) findViewById(R.id.tv_Ctrl3_light3);
        tvCtrl3light4 = (TextView) findViewById(R.id.tv_Ctrl3_light4);
        tvCtrl3light5 = (TextView) findViewById(R.id.tv_Ctrl3_light5);
        tvCtrl3light6 = (TextView) findViewById(R.id.tv_Ctrl3_light6);
        swChe1_ac1 = (Switch) findViewById(R.id.sw_Che1_ac1);
        swChe1_light1 = (Switch) findViewById(R.id.sw_Che1_light1);
        swChe1_light2 = (Switch) findViewById(R.id.sw_Che1_light2);
        swChe1_plug1 = (Switch) findViewById(R.id.sw_Che1_plug1);
        swChe1_plug2 = (Switch) findViewById(R.id.sw_Che1_plug2);
        swRec1_ac1 = (Switch) findViewById(R.id.sw_Rec1_ac1);
        swRec1_light1 = (Switch) findViewById(R.id.sw_Rec1_light1);
        swRec1_light2 = (Switch) findViewById(R.id.sw_Rec1_light2);
        swRec1_plug1 = (Switch) findViewById(R.id.sw_Rec1_plug1);
        swRec1_plug2 = (Switch) findViewById(R.id.sw_Rec1_plug2);
        swChe2_ac1 = (Switch) findViewById(R.id.sw_Che2_ac1);
        swChe2_light1 = (Switch) findViewById(R.id.sw_Che2_light1);
        swChe2_light2 = (Switch) findViewById(R.id.sw_Che2_light2);
        swChe2_light3 = (Switch) findViewById(R.id.sw_Che2_light3);
        swChe2_light4 = (Switch) findViewById(R.id.sw_Che2_light4);
        swRec2_ac1 = (Switch) findViewById(R.id.sw_Rec2_ac1);
        swRec2_light1 = (Switch) findViewById(R.id.sw_Rec2_light1);
        swRec2_light2 = (Switch) findViewById(R.id.sw_Rec2_light2);
        swRec2_light3 = (Switch) findViewById(R.id.sw_Rec2_light3);
        swRec2_light4 = (Switch) findViewById(R.id.sw_Rec2_light4);
        swChe3_ac1 = (Switch) findViewById(R.id.sw_Che3_ac1);
        swChe3_ac2 = (Switch) findViewById(R.id.sw_Che3_ac2);
        swChe3_light1 = (Switch) findViewById(R.id.sw_Che3_light1);
        swChe3_light2 = (Switch) findViewById(R.id.sw_Che3_light2);
        swChe3_light3 = (Switch) findViewById(R.id.sw_Che3_light3);
        swChe3_light4 = (Switch) findViewById(R.id.sw_Che3_light4);
        swChe3_light5 = (Switch) findViewById(R.id.sw_Che3_light5);
        swChe3_light6 = (Switch) findViewById(R.id.sw_Che3_light6);
        swRec3_ac1 = (Switch) findViewById(R.id.sw_Rec3_ac1);
        swRec3_ac2 = (Switch) findViewById(R.id.sw_Rec3_ac2);
        swRec3_light1 = (Switch) findViewById(R.id.sw_Rec3_light1);
        swRec3_light2 = (Switch) findViewById(R.id.sw_Rec3_light2);
        swRec3_light3 = (Switch) findViewById(R.id.sw_Rec3_light3);
        swRec3_light4 = (Switch) findViewById(R.id.sw_Rec3_light4);
        swRec3_light5 = (Switch) findViewById(R.id.sw_Rec3_light5);
        swRec3_light6 = (Switch) findViewById(R.id.sw_Rec3_light6);
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
                    setAppliances("setApplianceChecked","001","1","Light");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","001","0","Light");
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
                    setAppliances("setApplianceChecked","002","1","Light");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","002","0","Light");
                }
            }
        });
        swChe1_plug1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1plug1.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","001","1","Plug");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","001","0","Plug");
                }
            }
        });
        swChe1_plug2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1plug2.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","002","1","Plug");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","002","0","Plug");
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
                    setAppliances("setApplianceRecover","001","1","Light");
                } else {
                    setAppliances("setApplianceRecover","001","0","Light");
                }
            }
        });
        swRec1_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","002","1","Light");
                } else {
                    setAppliances("setApplianceRecover","002","0","Light");
                }
            }
        });
        swRec1_plug1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","001","1","Plug");
                } else {
                    setAppliances("setApplianceRecover","001","0","Plug");
                }
            }
        });
        swRec1_plug2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","002","1","Plug");
                } else {
                    setAppliances("setApplianceRecover","002","0","Plug");
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
        Log.d("Response","getAppliances");
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getAppliances");
        mJsonStr.setToken("mDSbpZrRXACEsBE8WR34");
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        Log.d("Response","getAppliances1");
        requestArray = new JsonArrayRequest(Request.Method.POST, url, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response","2");
                try {

                    Log.d("Response",response.toString());

                   // tvCtrl1ac1.setText(response.getJSONObject(8).getString("power"));
                    tvCtrl1light1.setText(response.getJSONObject(4).getString("power"));
                    tvCtrl1light2.setText(response.getJSONObject(5).getString("power"));
                    tvCtrl1plug1.setText(response.getJSONObject(0).getString("power"));
                    tvCtrl1plug2.setText(response.getJSONObject(1).getString("power"));

                   // if(response.getJSONObject(8).getString("checked").equals("1"))
                   // {swChe1_ac1.setChecked(true);}
                    if(response.getJSONObject(4).getString("checked").equals("1"))
                    {swChe1_light1.setChecked(true);
                    }
                    if(response.getJSONObject(5).getString("checked").equals("1"))
                    {swChe1_light2.setChecked(true);
                    }
                    if(response.getJSONObject(0).getString("checked").equals("1"))
                    {swChe1_plug1.setChecked(true);}
                    if(response.getJSONObject(1).getString("checked").equals("1"))
                    {swChe1_plug2.setChecked(true);}


                   // if(response.getJSONObject(8).getString("recover").equals("1"))
                  //  {swRec1_ac1.setChecked(true);}

                    if(response.getJSONObject(4).getString("recover").equals("1"))
                    {swRec1_light1.setChecked(true);}
                    if(response.getJSONObject(5).getString("recover").equals("1"))
                    {swRec1_light2.setChecked(true);}
                    if(response.getJSONObject(0).getString("recover").equals("1"))
                    {swRec1_plug1.setChecked(true);}
                    if(response.getJSONObject(1).getString("recover").equals("1"))
                    {swRec1_plug2.setChecked(true);}



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException",e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError",error.toString());
            }
        });
        requestQueue.add(requestArray);
    }
    public void setAppliances(String... params){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction(params[0]);
        mJsonStr.setToken("mDSbpZrRXACEsBE8WR34");
        mJsonStr.setEquipment(params[3]);
        switch (params[0]){
            case "setApplianceChecked":
                mJsonStr.setName(params[1]);
                mJsonStr.setChecked(params[2]);
                break;
            case "setApplianceRecover":
                mJsonStr.setName(params[1]);
                mJsonStr.setRecover(params[2]);
                break;
        }
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        requestOb = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response_setAppliances",response.toString());
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

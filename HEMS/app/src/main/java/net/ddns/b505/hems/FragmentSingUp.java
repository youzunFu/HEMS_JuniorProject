package net.ddns.b505.hems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentSingUp extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String url = "http://163.18.57.43/HEMSphp/plugschedule.php";
    private JsonArrayRequest ArrayRequest;
    private JsonObjectRequest ObjectRequest;
    private RequestQueue requestQueue;

    private  EditText etsignup_nickname,etsignup_username,etsignup_password,etsignup_password_confirm;
    private Button btn_signup;
    private  String token,StrNickname,StrUsername,StrPassword,StrPasswordConfirm;
    public FragmentSingUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSingUp.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSingUp newInstance(String param1, String param2) {
        FragmentSingUp fragment = new FragmentSingUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        etsignup_nickname = (EditText) getView().findViewById(R.id.etsignup_nickname);
        etsignup_username = (EditText) getView().findViewById(R.id.etsignup_username);
        etsignup_password= (EditText) getView().findViewById(R.id.etsignup_password);
        etsignup_password_confirm= (EditText) getView().findViewById(R.id.etsignup_password_confirm);
        btn_signup = (Button) getView().findViewById(R.id.btn_signup);
        //etsignup_password.getText().toString()
        //etsignup_password_confirm.getText().toString()
        StrNickname =etsignup_nickname.getText().toString();
        StrUsername = etsignup_username.getText().toString();
        StrPassword = etsignup_password.getText().toString();
        StrPasswordConfirm = etsignup_password_confirm.getText().toString();

        requestQueue = Volley.newRequestQueue(getContext());
        token = "mDSbpZrRXACEsBE8WR34";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_up, container, false);

        // Inflate the layout for this fragment
        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*findviewbyid ?*/

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void SignUp(final String name){

        if(StrPassword.equals(StrPasswordConfirm)){

        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("SignUp");
        mJsonStr.setToken(token);
        mJsonStr.setNickname(StrNickname);
        mJsonStr.setUsername(StrUsername);
        mJsonStr.setPassword(StrPassword);

        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        ArrayRequest = new JsonArrayRequest(Request.Method.POST, url/*"http://163.18.57.43/HEMSphp/plugschedule.php"*/, json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try { //20180130 down there should fix to
                            //找如何 接收response值 toast出成功(跳轉登入) 或username已使用 (請重新輸入)
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(ArrayRequest);
    }

    else{
            //密碼 與 確認密碼不相符 2018/01/30 X
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

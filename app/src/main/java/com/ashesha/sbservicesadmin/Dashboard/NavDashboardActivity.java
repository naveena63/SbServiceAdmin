package com.ashesha.sbservicesadmin.Dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashesha.sbservicesadmin.BalanceSheet.BalanceSheetActivity;
import com.ashesha.sbservicesadmin.BalanceSheet.BalanceSheetadminActivity;
import com.ashesha.sbservicesadmin.Logins.MainLoginsActivity;
import com.ashesha.sbservicesadmin.Profile.ProfileActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.SideNavAllJobs.AllEmployeesActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.AssignjobsActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.CancelledJobActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.ClosedJobsActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.OpenJobsActivity;
import com.ashesha.sbservicesadmin.SideNavAllJobs.PendingJobsActivity;
import com.ashesha.sbservicesadmin.Utils.AppConstants;
import com.ashesha.sbservicesadmin.Utils.PrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NavDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String EMP_ID = "emp_id";
    TextView nameTv, ratingTv, openJobsTv, empAmount, navEmpName, empMessage;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String logintype;
    String PREFERENCE = "AGENT";
    Context context;
    ImageView navEmpImageView;
    RequestQueue requestQueue;
    String totalCount;
    NavigationView navigationView;
    int amount = 2000;
      public static int selected_spinner_pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_dashboard);

        context = NavDashboardActivity.this;
        prefManager = new PrefManager(this);
        nameTv = findViewById(R.id.nameTv);
        ratingTv = findViewById(R.id.ratingTv);
        openJobsTv = findViewById(R.id.openJobsTv);
        empAmount = findViewById(R.id.textTeam);
        empMessage = findViewById(R.id.empMsg);
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        navEmpName = (TextView) hView.findViewById(R.id.Admin);
        navEmpImageView = (ImageView) hView.findViewById(R.id.navEmpImg);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setIcon(R.drawable.sb_toolbar);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        openJobsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetn = new Intent(NavDashboardActivity.this, OpenJobsActivity.class);
                startActivity(intetn);
            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Todays"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(R.color.black, R.color.red);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerDashboarAdapter adapter = new PagerDashboarAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        logintype = sharedPreferences.getString("login_type", "");

        if (logintype.equalsIgnoreCase("admin")) {
            ratingTv.setVisibility(View.INVISIBLE);
            adminOpenJobCount();
            adminClosedJobCount();
            adminCanacledCount();
            adminPendingCount();
            adminDetails();
            adminProfile();

            empMessage.setVisibility(View.GONE);
            empAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intetn = new Intent(NavDashboardActivity.this, AllEmployeesActivity.class);
                    startActivity(intetn);
                }
            });
        }
        if (logintype.equalsIgnoreCase("employee")) {
            ratingTv.setVisibility(View.VISIBLE);
            empOpenJobCount();
            empClosedJobCount();
            empCanacledCount();
            empPendingCount();

            empDetails();
            empProfile();
            empAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intetn = new Intent(NavDashboardActivity.this, BalanceSheetActivity.class);
                    startActivity(intetn);
                }
            });

        }
        firebaseNotification();







    }

    private void firebaseNotification() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.NOTIFICTAION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




               Log.d("notificationRes","Notification res:\n"+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", prefManager.getEmployeeId());
                Log.d("emp_id","Emp_id"+prefManager.getEmployeeId());
                params.put("device_id", prefManager.getDeviceId());
                Log.d("device_id","device_id"+prefManager.getDeviceId());
                params.put("token", prefManager.getToken());
                Log.d("PrefToken","PrefToken"+prefManager.getToken());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (logintype.equalsIgnoreCase("admin")) {
            ratingTv.setVisibility(View.INVISIBLE);
            adminOpenJobCount();
            adminClosedJobCount();
            adminCanacledCount();
            adminPendingCount();
            adminDetails();
            adminProfile();

            empMessage.setVisibility(View.GONE);
            empAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intetn = new Intent(NavDashboardActivity.this, AllEmployeesActivity.class);
                    startActivity(intetn);
                }
            });
        }
        if (logintype.equalsIgnoreCase("employee")) {
            ratingTv.setVisibility(View.VISIBLE);
            empOpenJobCount();
            empClosedJobCount();
            empCanacledCount();
            empPendingCount();

            empDetails();
            empProfile();
            empAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intetn = new Intent(NavDashboardActivity.this, BalanceSheetActivity.class);
                    startActivity(intetn);
                }
            });
        }
    }

    private void empOpenJobCount() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_OPEN_COUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");


                    if (status.equals("success")) {

                        totalCount = jsonObject.getString("Total_Open_jobs");
                    } else if (status.equals("fail")) {
                        totalCount = jsonObject.getString("msg");
                    }
                    Menu menu = navigationView.getMenu();
                    MenuItem open_jobs = menu.findItem(R.id.open_jobs);
                    open_jobs.setTitle("open Jobs" + "(" + totalCount + ")");
                    Log.e("jobcount", "jddls" + open_jobs);
                    navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this);
                    Log.e("hhhhhh", "gsggs" + totalCount);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("hhhhhh", "exception" + e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void empClosedJobCount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_CLOSED_COUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        totalCount = jsonObject.getString("Total_Closed_jobs");
                    } else if (status.equals("fail")) {
                        totalCount = jsonObject.getString("msg");
                    }
                    Menu menu = navigationView.getMenu();
                    MenuItem closeed_jobs = menu.findItem(R.id.closed_jobs);
                    closeed_jobs.setTitle("Closed Jobs" + "(" + totalCount + ")");
                    Log.e("jobcount", "jddls" + closeed_jobs);
                    navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                    );
                    Log.e("hhhhhh", "gsggs" + totalCount);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("hhhhhh", "exception" + e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void empCanacledCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_CANCELED_COUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        totalCount = jsonObject.getString("Total_Cancelled_jobs");
                    } else if (status.equals("fail")) {
                        totalCount = jsonObject.getString("msg");
                    }
                    Menu menu = navigationView.getMenu();
                    MenuItem canceled_count = menu.findItem(R.id.canceld_jobs);
                    canceled_count.setTitle("Cancelled Jobs" + "(" + totalCount + ")");
                    Log.e("jobcount", "jddls" + canceled_count);
                    navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                    );
                    Log.e("hhhhhh", "gsggs" + totalCount);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("hhhhhh", "exception" + e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void empPendingCount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_PENDING_COUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        totalCount = jsonObject.getString("Total_Pending_jobs");
                    } else if (status.equals("fail")) {
                        totalCount = jsonObject.getString("msg");
                    }
                    Menu menu = navigationView.getMenu();
                    MenuItem pending_count = menu.findItem(R.id.pending_jobs);
                    pending_count.setTitle("Pending Jobs" + "(" + totalCount + ")");
                    Log.e("jobcount", "jddls" + pending_count);
                    navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                    );
                    Log.e("hhhhhh", "gsggs" + totalCount);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("hhhhhh", "exception" + e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void adminPendingCount() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_PENDING_COUNT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {

                                totalCount = response.getString("Total_Pending_jobs");
                            } else if (status.equals("fail")) {
                                totalCount = response.getString("msg");
                            }
                            Menu menu = navigationView.getMenu();
                            MenuItem pendind_Jobs = menu.findItem(R.id.pending_jobs);
                            pendind_Jobs.setTitle("Pending Jobs" + "(" + totalCount + ")");
                            Log.e("jobcount", "jddls" + pendind_Jobs);
                            navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                            );
                            Log.e("hhhhhh", "gsggs" + totalCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("hhhhhh", "exception" + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hhhhhh", "gsggs" + error);

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void adminCanacledCount() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_CANCELED_COUNT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {

                                totalCount = response.getString("Total_Cancelled_jobs");
                            } else if (status.equals("fail")) {
                                totalCount = response.getString("msg");
                            }


                            Menu menu = navigationView.getMenu();
                            MenuItem canceled_jobs = menu.findItem(R.id.canceld_jobs);
                            canceled_jobs.setTitle("Cancelled Jobs" + "(" + totalCount + ")");
                            Log.e("jobcount", "jddls" + canceled_jobs);
                            navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                            );
                            Log.e("hhhhhh", "gsggs" + totalCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("hhhhhh", "exception" + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hhhhhh", "gsggs" + error);

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void adminClosedJobCount() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_CLOSED_COUNT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {

                                totalCount = response.getString("Total_Closed_jobs");
                            } else if (status.equals("fail")) {
                                totalCount = response.getString("msg");
                            }
//                            totalCount = response.getString("Total_Closed_jobs");

                            Menu menu = navigationView.getMenu();
                            MenuItem closed_jobs = menu.findItem(R.id.closed_jobs);
                            closed_jobs.setTitle("Closed Jobs" + "(" + totalCount + ")");
                            Log.e("jobcount", "jddls" + closed_jobs);
                            navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                            );
                            Log.e("hhhhhh", "gsggs" + totalCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("hhhhhh", "exception" + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hhhhhh", "gsggs" + error);

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void adminOpenJobCount() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.ADMIN_OPEN_COUNT_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {

                                totalCount = response.getString("Total_Open_jobs");
                            } else if (status.equals("fail")) {
                                totalCount = response.getString("msg");
                            }
                            /* totalCount = response.getString("Total_Open_jobs");*/

                            Menu menu = navigationView.getMenu();
                            MenuItem open_jobs = menu.findItem(R.id.open_jobs);
                            open_jobs.setTitle("open Jobs" + "(" + totalCount + ")");
                            Log.e("jobcount", "jddls" + open_jobs);
                            navigationView.setNavigationItemSelectedListener(NavDashboardActivity.this
                            );
                            Log.e("hhhhhh", "gsggs" + totalCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("hhhhhh", "exception" + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hhhhhh", "gsggs" + error);

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void adminProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ADMIN_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("employee_data");
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            String image = jsonObject1.getString("profile_pic");
                            navEmpName.setText(name);

                            Glide.with(NavDashboardActivity.this)
                                    .load("https://sbservices.in/" + image)
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                                            error(R.drawable.ic_launcher_background))
                                    .into(navEmpImageView);

                            Log.e("imagepath", "https://sbservices.in/" + image);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void empProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMP_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("empResponse", "emp" + response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("employee_data");
                        String name = jsonObject1.getString("emp_name");
                        String image = jsonObject1.getString("emp_img");
                        navEmpName.setText(name);

                        Glide.with(NavDashboardActivity.this)
                                .load("https://sbservices.in/" + image)
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).
                                        error(R.drawable.ic_launcher_background))
                                .into(navEmpImageView);

                        Log.e("imagepath", "https://sbservices.in/" + image);

                        navEmpName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intetn = new Intent(NavDashboardActivity.this, ProfileActivity.class);
                                startActivity(intetn);
                            }
                        });
                        navEmpImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intetn = new Intent(NavDashboardActivity.this, ProfileActivity.class);
                                startActivity(intetn);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", new PrefManager(NavDashboardActivity.this).getEmployeeId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //Admin details
    private void adminDetails() {
        final RequestQueue requestQueue = Volley.newRequestQueue(NavDashboardActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.ADMIN_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("employee_data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String name = jsonObject1.getString("name");
                            Log.e("admindetails", "admindetails" + response);
                            nameTv.setText(name);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                return map;
            }

            ;
        };
        requestQueue.add(stringRequest);
    }

    private void empDetails() {

        final RequestQueue requestQueue = Volley.newRequestQueue(NavDashboardActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.EMPLOYEE_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("employee_data");
                        Log.e("empdetails", "empdetails" + response);
                        String name = jsonObject1.getString("emp_name");
                        int money = jsonObject1.getInt("emp_accnt_money");
                        Log.e("money", "money" + money);
                        String rate = jsonObject1.getString("emp_rating");

                        nameTv.setText(name);
                        ratingTv.setText(rate);
                        empAmount.setText("" + money);

                        if (money < 2000) {
                            empMessage.setVisibility(View.VISIBLE);
                        } else
                            empMessage.setVisibility(View.INVISIBLE);


                        empMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NavDashboardActivity.this);
                                builder.setTitle("Your balance is less than 2000 please contact your Admin");
                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(EMP_ID, prefManager.getEmployeeId());
                return map;
            };
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                ((NavDashboardActivity) context).refresh(context);
                return true;
            }
            case R.id.notifications: {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void refresh(Context context) {
        Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NavDashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.open_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, OpenJobsActivity.class);
            startActivity(intent);
        } else if (id == R.id.closed_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, ClosedJobsActivity.class);
            startActivity(intent);
        } else if (id == R.id.canceld_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, CancelledJobActivity.class);
            startActivity(intent);
        } else if (id == R.id.pending_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, PendingJobsActivity.class);
            startActivity(intent);
        } else if (id == R.id.payment_pending_jobs) {
            Intent intent = new Intent(NavDashboardActivity.this, PendingJobsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_assign) {
            if (logintype.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(NavDashboardActivity.this, AssignjobsActivity.class);
                startActivity(intent);
            }
            if (logintype.equalsIgnoreCase("employee")) {
                Toast.makeText(context, "This Field  Not Availble For Employee", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_emp) {
            if (logintype.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(NavDashboardActivity.this, AllEmployeesActivity.class);
                startActivity(intent);
            }
            if (logintype.equalsIgnoreCase("employee")) {
                Toast.makeText(context, "This Field  Not Availble For Employee", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_logout) {
            prefManager.logout();
            Intent intent = new Intent(NavDashboardActivity.this, MainLoginsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

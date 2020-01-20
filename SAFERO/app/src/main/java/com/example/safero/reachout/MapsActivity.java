package com.example.safero.reachout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public ApiInterface apiInterface; //Interface which connects with 000webhost.com
    public int valueForFloodLvl = 0;

    PolygonOptions regionOption; //regigin polygen
    PolygonOptions rectOption; //rect option for polyon
    PolygonOptions rectOption2; //rect option for polyon
    PolygonOptions rectOption3; //rect option for polyon

    FirebaseDatabase database;
    DatabaseReference myRef;
    int sensor;
    User user;
    ArrayList<User> user_list;

    boolean nopath = false;
    Timer updateTrainLocation = new Timer();

    double region[][] = {
            {6.061984, 80.41128},
            {6.039761, 80.412405},
            {6.040342, 80.385889},
            {6.060315, 80.386232},
    };

    //polygen arrays

    double poly1[][] = {
            {6.058443, 80.394831},
            {6.057612, 80.394670},
            {6.057036, 80.394316},
            {6.055742, 80.395168},
            {6.055326, 80.396036},
            {6.055118, 80.396326},
            {6.054219, 80.396605},
            {6.053456, 80.396679},
            {6.052321, 80.396309},
            {6.051681, 80.396326},
            {6.051426, 80.397515},
            {6.050802, 80.398544},
            {6.050906, 80.399235},
            {6.050419, 80.400192},
            {6.049795, 80.400650},
            {6.049100, 80.400891},
            {6.047441, 80.400731},
            {6.045435, 80.403866},
            {6.045934, 80.405443},
            {6.044482, 80.407094},
            {6.041917, 80.407905},
            {6.040946, 80.408056},
            {6.040912, 80.407038},
            {6.042233, 80.406973},
            {6.042775, 80.406346},
            {6.043311, 80.406126},
            {6.043997, 80.406373},
            {6.044589, 80.406006},
            {6.044951, 80.405499},
            {6.044601, 80.404122},
            {6.046025, 80.401442},
            {6.046780, 80.400156},
            {6.048031, 80.400029},
            {6.049892, 80.399901},
            {6.050392, 80.397879},
            {6.050675, 80.395921},
            {6.052733, 80.395515},
            {6.053604, 80.396096},
            {6.054950, 80.395523},
            {6.055140, 80.394321},
            {6.057048, 80.393414},

    }; //poli line for the main flood area

    double poly2[][] = {
            {6.058923, 80.395116},
            {6.057450, 80.394905},
            {6.056514, 80.395084},
            {6.056003, 80.396420},
            {6.054003, 80.397193},
            {6.052923, 80.397256},
            {6.052197, 80.397141},
            {6.052150, 80.400579},
            {6.051047, 80.402245},
            {6.047675, 80.402332},
            {6.046534, 80.403739},
            {6.046326, 80.406114},
            {6.045624, 80.407258},
            {6.043889, 80.407738},
            {6.042089, 80.408450},
            {6.040674, 80.408220},
            {6.040534, 80.407631},
            {6.042991, 80.405664},
            {6.044319, 80.405452},
            {6.043228, 80.402706},
            {6.045646, 80.399298},
            {6.047592, 80.399010},
            {6.049155, 80.399000},
            {6.050956, 80.394787},
            {6.053392, 80.395156},
            {6.056073, 80.393197},
            {6.057483, 80.392514},
            {6.058465, 80.393222}
    }; //poli line for the main flood area


    double pointsArray[][] = {
            { 6.04415 ,  80.40984499999999 },
            { 6.045168 ,  80.409382 },
            { 6.04582 ,  80.409594 },
            { 6.045532 ,  80.408861 },
            { 6.0455571 ,  80.406255 },
            { 6.045532 ,  80.407625 },
            { 6.04655 ,  80.405734 },
            { 6.047529 ,  80.405097 },
            { 6.047586 ,  80.40475 },
            { 6.047778 ,  80.404943 },
            { 6.048527 ,  80.403437 },
            { 6.049851 ,  80.400387 },
            { 6.051694 ,  80.399113 },
            { 6.052537999999999 ,  80.39930600000001 },
            { 6.053287 ,  80.399113 },
            { 6.054093 ,  80.39891999999999 },
            { 6.054688 ,  80.398785 },
            { 6.054285 ,  80.399191 },
            { 6.054515 ,  80.402028 },
            { 6.05439 ,  80.40254 },
            { 6.054189 ,  80.402182 },
            { 6.052548000000001 ,  80.40298299999999 },
            { 6.052711 ,  80.405773 },
            { 6.052912999999999 ,  80.406052 },
            { 6.05274 ,  80.406207 },
            { 6.054419 ,  80.40578199999999 },
            { 6.0538050000000005 ,  80.403775 },
            { 6.052951 ,  80.407268 },
            { 6.052615 ,  80.407268 },
            { 6.051185 ,  80.406429 },
            { 6.048987 ,  80.404885 },
            { 6.048891 ,  80.40515500000001 },
            { 6.0494 ,  80.40664100000001 },
            { 6.049947 ,  80.41014399999999 },
            { 6.047879 ,  80.41046300000001 },
            { 6.048588 ,  80.405026 },
            { 6.052773 ,  80.407022 },
            { 6.050838 ,  80.399537 },
            { 6.049362 ,  80.40093900000001 },
            { 6.0568029999999995 ,  80.397981 },
            { 6.0579339999999995 ,  80.396715 },
            { 6.0586269999999995 ,  80.39483299999999 },
            { 6.0611586 ,  80.393649 },
            { 6.0417510000000005 ,  80.40640400000001 },
            { 6.0573760000000005 ,  80.391922 },
            { 6.051615 ,  80.392738 },
            { 6.048607 ,  80.396128 },
            { 6.043405 ,  80.400189 },
    };

    final double graph[][] = new double[][] {
            {1e-16, 0.12427493257843944, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.4648350307610758, 0, 0, 0, 0, },
            {0.12427493257843944, 1e-16, 0.07621877607972777, 0.07042933437878135, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0.07621877607972777, 1e-16, 0.08717702205614895, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.24837554726503527, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0.07042933437878135, 0.08717702205614895, 1e-16, 0, 0.13671548040987538, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 1e-16, 0.1515630977265741, 0.12457145719599426, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0.13671548040987538, 0.1515630977265741, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0.12457145719599426, 0, 1e-16, 0.12970118556725913, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0.12970118556725913, 1e-16, 0.03890207191799309, 0.0325152327567522, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0.03890207191799309, 1e-16, 0.030196295996345154, 0.17901824857730378, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0.0325152327567522, 0.030196295996345154, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.09056270727082441, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0.17901824857730378, 0, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.2914972281886464, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.14454068746397197, 0.0817702448266762, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0.09627460517282757, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.10613646895275727, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.09627460517282757, 1e-16, 0.08600273348275288, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.08600273348275288, 1e-16, 0.09215784173614568, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.09215784173614568, 1e-16, 0.06784538940528978, 0.03680481789298806, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.06784538940528978, 1e-16, 0.06345086627493852, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.25149866593292525, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.03680481789298806, 0.06345086627493852, 1e-16, 0.31484002302479147, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.31484002302479147, 1e-16, 0.058313796327097024, 0.04006253643305511, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.058313796327097024, 1e-16, 0.04547372875302719, 0, 0, 0, 0, 0, 0.15130873273938902, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.04006253643305511, 0.04547372875302719, 1e-16, 0.20289452575425618, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.20289452575425618, 1e-16, 0.3091334085478511, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.3091334085478511, 1e-16, 0.03817298840280785, 0.04811288719089236, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.03817298840280785, 1e-16, 0.025772428246732332, 0.1701534795873238, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.04811288719089236, 0.025772428246732332, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.09022164607242078, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.1701534795873238, 0, 1e-16, 0.23226108334671436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.15130873273938902, 0, 0, 0, 0, 0, 0.23226108334671436, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0.0373732239651305, 0, 0, 0, 0, 0, 0, 0, 0.03365086339602795, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0373732239651305, 1e-16, 0.18415161886502832, 0, 0, 0, 0, 0, 0, 0.03239198540238858, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.18415161886502832, 1e-16, 0.2982257810239941, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.2982257810239941, 1e-16, 0.03171639887620596, 0, 0, 0, 0.047041310030501195, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.03171639887620596, 1e-16, 0.1738445939124076, 0, 0, 0.036598709472368436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.1738445939124076, 1e-16, 0.3922160381841716, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.3922160381841716, 1e-16, 0.23271383868259263, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0.24837554726503527, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.23271383868259263, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0.09056270727082441, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.047041310030501195, 0.036598709472368436, 0, 0, 0, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.09022164607242078, 0, 0, 0.03365086339602795, 0.03239198540238858, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.14454068746397197, 0.10613646895275727, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.2914972281886464, 0.0817702448266762, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0, 0, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.25149866593292525, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 0.18824050558487673, 0, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.18824050558487673, 1e-16, 0.22197864976011522, 0, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.22197864976011522, 1e-16, 0.3105530455995126, 0, 0, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.3105530455995126, 1e-16, 0, 0.46207073022279493, 1.0663048033713907, 1.4227844794053583, 2.1030571169390178, },
            {0.4648350307610758, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e-16, 2.36357386590498, 1.8678192400089486, 1.3687587827051078, 0.7116442167546485, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.46207073022279493, 2.36357386590498, 1e-16, 0, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.0663048033713907, 1.8678192400089486, 0, 1e-16, 0, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1.4227844794053583, 1.3687587827051078, 0, 0, 1e-16, 0, },
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2.1030571169390178, 0.7116442167546485, 0, 0, 0, 1e-16, },
    };

    //double poly1[][] = {{7.042224, 80.256629}, {7.039272, 80.258028}, {7.037071, 80.257561}, {7.034882, 80.258220}, {7.032946, 80.259051}, {7.030365, 80.260421}, {7.046163, 80.279685}, {7.055620, 80.271671}, {7.050200, 80.256742}, {7.045254, 80.255997}};
    //double poly1[][] = {{7.042224, 80.256629}, {7.039272, 80.258028}, {7.037071, 80.257561}, {7.034882, 80.258220}, {7.032946, 80.259051}, {7.021895,80.260144}, {7.016997, 80.241233},{7.033572, 80.221376}, {7.054635, 80.221479}, {7.061935, 80.245120}, {7.045077, 80.252719}};
    //double poly2[][] = {{7.042332, 80.258459}, {7.039580, 80.259187}, {7.037415, 80.259161}, {7.034862, 80.260399}, {7.033490, 80.260543}, {7.021789, 80.263301}, {7.006619,80.248283}, {7.001654,80.226351}, {7.021604,80.173689}, {7.078408,80.195208}, {7.071966,80.252446}};
    //double poly3[][] = {{7.042111, 80.260329}, {7.039052, 80.260573}, {7.037630, 80.261303}, {7.036664, 80.262844}, {7.034169, 80.264871}, {7.026060,80.270443}, {6.967475,80.295402}, {6.911139,80.209818}, {6.945658,80.121270}, {7.114064,80.104028}, {7.141787,80.258605}};
/*
    double pointsArray[][] = {
            {7.041769,80.256497},
            {7.040925,80.256387},
            {7.041331,80.257988},
            {7.041276,80.258607},
            {7.040958,80.259346},
            {7.039632,80.260705},
            {7.038481,80.260859},
            {7.037922,80.261080},
            {7.037615,80.260694},
            {7.037297,80.262284},
            {7.036749,80.262151},
            {7.036278,80.260981},
            {7.037392,80.259921},
            {7.037405,80.260315},
            {7.037190,80.260178},
            {7.037659,80.258823},
            {7.036642,80.257984},
            {7.036278,80.256835},
            {7.036440,80.256579},
            {7.038897,80.256510}}; */
/*
    double graph[][] = new double[][] {{0, 0.09, 0.17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0.09, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.23},
            {0.17, 0, 0, 0.07, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0.07, 0, 0.09, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0.09, 0, 0.21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0.21, 0, 0.13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0.13, 0, 0.07, 0.1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0.07, 0, 0, 0.15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 0, 0, 0.05, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0.15, 0, 0, 0.06, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0.06, 0, 0.14, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.14, 0, 0, 0, 0.13, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.04, 0.04, 0.12, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0.05, 0, 0, 0, 0.04, 0, 0.03, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.13, 0.04, 0.03, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.12, 0, 0, 0, 0.15, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.15, 0, 0.13, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.13, 0, 0.03, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.03, 0, 0.27},
            {0, 0.23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.27, 0}}; */

    int graphSize = 48;
    double graphFiltered[][] = new double[graphSize][graphSize];

    int pol1Size = 41;
    int pol2Size = 28;

    ArrayList<LatLng> pathArray = new ArrayList<LatLng>(); //Array to hold the waypoints
    //String wayPath = "";

    SearchView searchView;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;

    LatLng gstart, gstop;
    int graphStart = 0;
    int graphStop = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initiate the instance of the api client interface
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //firebase start
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users"); //reference to the database
        user = new User();

        //initializing the array list
        user_list = new ArrayList<User>();
        //firebase end

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        searchView = (SearchView)findViewById(R.id.location);
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.049825, 80.401559), 16));

        algo();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                makerMethodDin(latLng);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address>addressList=null;
                if(location != null || !location.equals("")){

                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    if(addressList.size()!=0) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        makerMethodDin(latLng);
                        //mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        //Adding markers to the points
        MarkerOptions markerOptionsForPoints = new MarkerOptions();
        markerOptionsForPoints.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        for (int i = 0; i < graphSize; i++){
            markerOptionsForPoints.position(new LatLng(pointsArray[i][0], pointsArray[i][1]));
            mMap.addMarker(markerOptionsForPoints);
        }

        //Timer to take the train location every 10 sec

        updateTrainLocation.schedule(new TimerTask() {
            @Override
            public void run() {
                getFloodLvl();
            }
        },1000,5000);
    }
    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param+"&key=AIzaSyAk8PF3GLBPFLXX624jOccqxOdTtkMwkKo";
        String wayPoint = stringWayPointMaker();
        System.out.println(wayPoint);
        url = url + wayPoint;
        Log.d("aaaaaaaaaaaaaaaaaaaa",url);
        System.out.println(url);
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;
            ArrayList pointsNew = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();

                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                System.out.println(points.size());
                if (nopath == false) {
                    polylineOptions.addAll(points);
                    polylineOptions.width(15);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                }
                else {
                    Toast.makeText(MapsActivity.this, "No Path Found", Toast.LENGTH_SHORT).show();
                }
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void algo() { //test function for polygen
        rectOption = new PolygonOptions();
        rectOption2 = new PolygonOptions();
        regionOption = new PolygonOptions();

        for (int i = 0; i < 4; i++){
            regionOption.add(new LatLng(region[i][0], region[i][1]));
        }

        Polygon regionP = mMap.addPolygon(regionOption.strokeColor(Color.GREEN).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(8));

        ArrayList<Point> pol = new ArrayList<Point>();

        if (valueForFloodLvl == 0) {
            for (int i = 0; i < pol1Size; i++){
                rectOption.add(new LatLng(poly1[i][0], poly1[i][1]));
            }
            for (int i = 0; i < pol2Size; i++){
                rectOption2.add(new LatLng(poly2[i][0], poly2[i][1]));
            }
            for (int i = 0; i < pol1Size; i++) {
                pol.add(new Point(poly1[i][0], poly1[i][1], i));
            }
            Polygon polygon = mMap.addPolygon(rectOption.strokeColor(Color.RED).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(5));
            Polygon polygon2 = mMap.addPolygon(rectOption2.strokeColor(Color.DKGRAY).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(5));
        }
        else if (valueForFloodLvl == 1) {
            for (int i = 0; i < pol2Size; i++){
                rectOption.add(new LatLng(poly2[i][0], poly2[i][1]));
            }
            for (int i = 0; i < pol2Size; i++) {
                pol.add(new Point(poly2[i][0], poly2[i][1], i));
            }
            Polygon polygon = mMap.addPolygon(rectOption.strokeColor(Color.RED).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(5));

        }



        //rectOption2 = new PolygonOptions();
        //rectOption3 = new PolygonOptions();

        Position_Point_WRT_Polygon inPoly = new Position_Point_WRT_Polygon();

        System.out.println("ddddddddddddddddddddddddddddddddddd   " + Double.toString(graph[4][4]));
        copyGraph();
        for (int i = 0; i < graphSize; i++) {
            boolean val = inPoly.isInside(pol, pol.size(), new Point(pointsArray[i][0], pointsArray[i][1], i));
            if (val == true) {
                for (int j = 0; j < graphSize; j++) {
                    graphFiltered[i][j] = 0;
                }
                for (int j = 0; j < graphSize; j++) {
                    graphFiltered[j][i] = 0;
                }
            }

            Log.d("Array Val = " + Integer.toString(i), Boolean.toString(val));
            //Toast.makeText(MainActivity.this, "Value = " + val, Toast.LENGTH_SHORT).show();
        }



        /*
        for (int i = 0; i < 11; i++){
            rectOption2.add(new LatLng(poly2[i][0], poly2[i][1]));
        }
        for (int i = 0; i < 11; i++){
            rectOption3.add(new LatLng(poly3[i][0], poly3[i][1]));
        }

 */

        ArrayList<Point> testP = new ArrayList<Point>();
        for (int i = 0; i < graphSize; i++){
            testP.add(new Point(pointsArray[i][0], pointsArray[i][1], i));
        }
        ShortestPath t = new ShortestPath(graphStart, graphStop, testP, graphFiltered, MapsActivity.this);
        this.pathArray = t.PathArray;
        String wayPoint = stringWayPointMaker();
        System.out.println(wayPoint);
    }

    public void makerMethodDin (LatLng latLng) {
        Position_Point_WRT_Polygon inPoly2 = new Position_Point_WRT_Polygon();

        ArrayList<Point> reg = new ArrayList<Point>();
        for (int i = 0; i < 4; i++){
            reg.add(new Point(region[i][0], region[i][1], i));
        }

        if(inPoly2.isInside(reg, reg.size(), new Point(latLng.latitude, latLng.longitude, 1))){
            //Reset marker when already 2
            if (listPoints.size() == 2) {
                listPoints.clear();
                mMap.clear();
                algo();
                //Polygon polygon = mMap.addPolygon(rectOption.strokeColor(Color.RED).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(5));
                //Polygon polygon2 = mMap.addPolygon(rectOption2.strokeColor(Color.YELLOW).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(5));
                //Polygon polygon3 = mMap.addPolygon(rectOption3.strokeColor(Color.GREEN).fillColor(Color.TRANSPARENT).visible(true).strokeWidth(5));

                //Adding markers to the points
                MarkerOptions markerOptionsForPoints = new MarkerOptions();
                markerOptionsForPoints.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                for (int i = 0; i < graphSize; i++){
                    markerOptionsForPoints.position(new LatLng(pointsArray[i][0], pointsArray[i][1]));
                    mMap.addMarker(markerOptionsForPoints);
                }

            }
            //Save first point select
            listPoints.add(latLng);
            //Create marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            if (listPoints.size() == 1) {
                //Add first marker to the map
                gstart = latLng;
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                //Add second marker to the map
                gstop = latLng;
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            mMap.addMarker(markerOptions);

            if (listPoints.size() == 2) {
                magnetPoint();
                algo();

                //Create the URL to get request from first marker to second marker
                String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }

        }
        else{
            Toast.makeText(getApplicationContext(), "Out of the Region", Toast.LENGTH_SHORT).show();
        }
    }



    public String stringWayPointMaker(){
        String wayPath = "";

        if (pathArray.size() != 0 && graphStart != graphStop){

            //creating maker option for the way path markers
            MarkerOptions pathMarker = new MarkerOptions();
            pathMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            nopath = false;
            wayPath = "&waypoints=";
            for (int i = 1; i < pathArray.size() - 1; i++){
                wayPath = wayPath + "via:" + pathArray.get(i).latitude + "%2C"+ pathArray.get(i).longitude + "%7C";

                pathMarker.position(pathArray.get(i));
                mMap.addMarker(pathMarker);
            }
            wayPath = wayPath + pathArray.get(pathArray.size()-2).latitude + "%2C" + pathArray.get(pathArray.size()-2).longitude;

            pathMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            pathMarker.position(new LatLng(pointsArray[graphStart][0], pointsArray[graphStart][1]));
            mMap.addMarker(pathMarker);
        }
        else if (graphStart != graphStop) {
            nopath = true;
        }
        return wayPath;
    }

    public void magnetPoint(){
        double min = Double.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < graphSize; i++){ //code for g start
            double dis = distance(gstart.latitude, gstart.longitude, pointsArray[i][0], pointsArray[i][1], "K");
            System.out.println("Dis" + dis);
            if (min > dis){
                min = dis;
                //System.out.println("Min" + dis);
                index = i;
            }
        }
        graphStart = index;
        System.out.println("Graph Start - " + Integer.toString(graphStart));

        //Adding markers to the Start magnet points
        MarkerOptions markerOptionsStartPoint = new MarkerOptions();
        markerOptionsStartPoint.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptionsStartPoint.position(new LatLng(pointsArray[graphStart][0], pointsArray[graphStart][1]));
        mMap.addMarker(markerOptionsStartPoint);

        min = Double.MAX_VALUE;
        index = 0;

        for (int i = 0; i < graphSize; i++){ //code for g start
            double dis = distance(gstop.latitude, gstop.longitude, pointsArray[i][0], pointsArray[i][1], "K");
            System.out.println("Dis" + dis);
            if (min > dis){
                min = dis;
                System.out.println("Min" + dis);
                index = i;
            }
        }
        graphStop = index;
        System.out.println("Graph Stop - " + Integer.toString(graphStop));

        //Adding markers to End magnet points
        MarkerOptions markerOptionsEndPoint = new MarkerOptions();
        markerOptionsEndPoint.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerOptionsEndPoint.position(new LatLng(pointsArray[graphStop][0], pointsArray[graphStop][1]));
        mMap.addMarker(markerOptionsEndPoint);

    }

    public double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    public void getFloodLvl (){

        Call<F_lvl> call = apiInterface.RetriveData();

        call.enqueue(new Callback<F_lvl>() {
            @Override
            public void onResponse(Call<F_lvl> call, Response<F_lvl> response) {
                System.out.println("Awaaaaaaaaaaaaaaaaaaaaaaaaaaaaa   - " + Integer.toString(response.body().getFloodLvl()));
                if (response.body().getFloodLvl() != valueForFloodLvl){
                    valueForFloodLvl = response.body().getFloodLvl();
                    mMap.clear();
                    algo();
                }

            }

            @Override
            public void onFailure(Call<F_lvl> call, Throwable t) {
                System.out.println("Error on Getting Data");
            }
        });

    }

    public void copyGraph(){
        for (int i = 0;i < graphSize; i++){
            for (int j = 0; j < graphSize; j++){
                graphFiltered[i][j] = graph[i][j];
            }
        }
    }
}


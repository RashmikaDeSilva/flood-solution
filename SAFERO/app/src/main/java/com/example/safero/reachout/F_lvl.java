package com.example.safero.reachout;
import com.google.gson.annotations.SerializedName;

public class F_lvl {

    @SerializedName("fn")
    private String fn;

    public int getFloodLvl() {
        return  Integer.parseInt(fn);
    }


}

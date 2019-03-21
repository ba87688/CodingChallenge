package weatherapp.cccevan.com.evan.evanmarogecodechallenge.models;


import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Font {

    private String url;
    private String family;

    @SerializedName("bold")
    private boolean boldOrNot;
    @SerializedName("italic")
    private boolean italicOrNot;

    public Font(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getFamily() {
        return family;
    }

    public boolean isBoldOrNot() {
        return boldOrNot;
    }

    public boolean isItalicOrNot() {
        return italicOrNot;
    }
}

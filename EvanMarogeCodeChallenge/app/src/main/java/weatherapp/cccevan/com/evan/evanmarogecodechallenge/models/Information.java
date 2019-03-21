package weatherapp.cccevan.com.evan.evanmarogecodechallenge.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("appid")
    @Expose
    private String appid;

    @SerializedName("fontFamilyName")
    @Expose
    private String fontFamilyName;

    @SerializedName("bold")
    @Expose
    private boolean bold;

    @SerializedName("italic")
    @Expose
    private boolean italic;

    @SerializedName("textTyped")
    @Expose
    private String textTyped;

    @SerializedName("url")
    @Expose
    private String url;

    private Integer id;

    public Information(String appid, String fontFamilyName, boolean bold, boolean italic, String textTyped, String url) {
        this.appid = appid;
        this.fontFamilyName = fontFamilyName;
        this.bold = bold;
        this.italic = italic;
        this.textTyped = textTyped;
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public String getFontFamilyName() {
        return fontFamilyName;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public String getTextTyped() {
        return textTyped;
    }

    public String getUrl() {
        return url;
    }

    public Integer getId() {
        return id;
    }
}

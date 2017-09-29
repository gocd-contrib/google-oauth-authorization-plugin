package cd.go.authorization.google;

import cd.go.authorization.google.utils.Util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static cd.go.authorization.google.utils.Util.GSON;

public class GoogleUser {
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("verified_email")
    private boolean verifiedEmail;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("given_name")
    private String givenName;

    @Expose
    @SerializedName("family_name")
    private String familyName;

    @Expose
    @SerializedName("picture")
    private String picture;

    @Expose
    @SerializedName("locale")
    private String locale;

    @Expose
    @SerializedName("hd")
    private String hd;

    GoogleUser() {
    }

    public GoogleUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPicture() {
        return picture;
    }

    public String getLocale() {
        return locale;
    }

    public String getHd() {
        if (Util.isNotBlank(hd)) {
            return hd;
        }

        return getEmail().substring(getEmail().indexOf("@") + 1);
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public static GoogleUser fromJSON(String json) {
        return GSON.fromJson(json, GoogleUser.class);
    }
}

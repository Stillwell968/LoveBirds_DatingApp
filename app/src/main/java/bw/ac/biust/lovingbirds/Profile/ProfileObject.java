package bw.ac.biust.lovingbirds.Profile;

public class ProfileObject {
    private String userName;
    private String about;
    private String profileImg;

    public ProfileObject(String userName,  String profileImg, String about){
        this.userName= userName;
        this.profileImg=profileImg;
        this.about=about;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}

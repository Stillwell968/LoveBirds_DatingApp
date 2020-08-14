package bw.ac.biust.lovingbirds.Cards;

public class cards {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String info;

    public cards (String userId, String name, String profileImageUrl, String info){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.info= info;

    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public String getInfo(){
        return info;
    }
    public void setInfo(String info){
        this.info = info;
    }



}


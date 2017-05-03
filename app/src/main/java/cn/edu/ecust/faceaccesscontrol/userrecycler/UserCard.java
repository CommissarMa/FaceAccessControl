package cn.edu.ecust.faceaccesscontrol.userrecycler;

/**
 * Created by CommissarMa on 2017/5/3.
 */

public class UserCard {
    private String stringUserNo;
    private String stringUserFacePath;

    public UserCard(String stringUserNo,String stringUserFacePath){
        this.stringUserNo=stringUserNo;
        this.stringUserFacePath=stringUserFacePath;
    }

    public String getStringUserNo(){
        return stringUserNo;
    }

    public String getStringUserFacePath(){
        return stringUserFacePath;
    }
}

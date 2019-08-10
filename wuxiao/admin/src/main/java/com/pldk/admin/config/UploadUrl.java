package com.pldk.admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "model")
public class UploadUrl {

    private String leaderUrlLinux;//带头人免冠照片Url存放路径

    private String leaderUrlMysql;//带头人免冠照片Url mysql 存放路径

    //****************************************************************************

    private String studioVideoLinux;//创新工作室视频地址 服务器存储地址

    private String studioVideoMysql;//创新工作室视频地址 mysql存储地址

    //****************************************************************************

    private String studioImgLinux;//创新工作室图片地址 服务器存储地址

    private String studioImgMysql;//创新工作室图片地址 mysql存储地址

    //****************************************************************************

    private String studioFileLinux;//创新工作室附件地址 服务器存储地址

    private String studioFileMysql;//创新工作室附件地址 mysql存储地址

    //****************************************************************************

    private String laborUnionLinux;//创新工作室工会意见图片地址 服务器存储地址

    private String laborUnionMysql;//创新工作室工会意见图片地址 mysql存储地址

    //****************************************************************************

    private String creativePlanVideoLinux;//创新计划视频地址 服务器存储地址

    private String creativePlanVideoMysql;//创新计划视频地址 mysql存储地址

    //****************************************************************************

    private String creativePlanImgsLinux;//创新计划图片地址 服务器存储地址

    private String creativePlanImgsMysql;//创新计划图片地址 mysql存储地址

    //****************************************************************************

    private String creativePlanFilesLinux;//创新计划附件地址 服务器存储地址

    private String creativePlanFilesMysql;//创新计划附件地址 mysql存储地址

    //****************************************************************************

    private String leaderAccessoryLinux;//带头人附件地址  服务器存储地址

    private String leaderAccessoryMysql;//带头人附件地址  mysql存储地址

    //****************************************************************************

    public String getLeaderUrlLinux() {
        return leaderUrlLinux;
    }

    public void setLeaderUrlLinux(String leaderUrlLinux) {
        this.leaderUrlLinux = leaderUrlLinux;
    }

    public String getLeaderUrlMysql() {
        return leaderUrlMysql;
    }

    public void setLeaderUrlMysql(String leaderUrlMysql) {
        this.leaderUrlMysql = leaderUrlMysql;
    }

    public String getStudioVideoLinux() {
        return studioVideoLinux;
    }

    public void setStudioVideoLinux(String studioVideoLinux) {
        this.studioVideoLinux = studioVideoLinux;
    }

    public String getStudioVideoMysql() {
        return studioVideoMysql;
    }

    public void setStudioVideoMysql(String studioVideoMysql) {
        this.studioVideoMysql = studioVideoMysql;
    }

    public String getStudioImgLinux() {
        return studioImgLinux;
    }

    public void setStudioImgLinux(String studioImgLinux) {
        this.studioImgLinux = studioImgLinux;
    }

    public String getStudioImgMysql() {
        return studioImgMysql;
    }

    public void setStudioImgMysql(String studioImgMysql) {
        this.studioImgMysql = studioImgMysql;
    }

    public String getStudioFileLinux() {
        return studioFileLinux;
    }

    public void setStudioFileLinux(String studioFileLinux) {
        this.studioFileLinux = studioFileLinux;
    }

    public String getStudioFileMysql() {
        return studioFileMysql;
    }

    public void setStudioFileMysql(String studioFileMysql) {
        this.studioFileMysql = studioFileMysql;
    }

    public String getLaborUnionLinux() {
        return laborUnionLinux;
    }

    public void setLaborUnionLinux(String laborUnionLinux) {
        this.laborUnionLinux = laborUnionLinux;
    }

    public String getLaborUnionMysql() {
        return laborUnionMysql;
    }

    public void setLaborUnionMysql(String laborUnionMysql) {
        this.laborUnionMysql = laborUnionMysql;
    }

    public String getCreativePlanVideoLinux() {
        return creativePlanVideoLinux;
    }

    public void setCreativePlanVideoLinux(String creativePlanVideoLinux) {
        this.creativePlanVideoLinux = creativePlanVideoLinux;
    }

    public String getCreativePlanVideoMysql() {
        return creativePlanVideoMysql;
    }

    public void setCreativePlanVideoMysql(String creativePlanVideoMysql) {
        this.creativePlanVideoMysql = creativePlanVideoMysql;
    }

    public String getCreativePlanImgsLinux() {
        return creativePlanImgsLinux;
    }

    public void setCreativePlanImgsLinux(String creativePlanImgsLinux) {
        this.creativePlanImgsLinux = creativePlanImgsLinux;
    }

    public String getCreativePlanImgsMysql() {
        return creativePlanImgsMysql;
    }

    public void setCreativePlanImgsMysql(String creativePlanImgsMysql) {
        this.creativePlanImgsMysql = creativePlanImgsMysql;
    }

    public String getCreativePlanFilesLinux() {
        return creativePlanFilesLinux;
    }

    public void setCreativePlanFilesLinux(String creativePlanFilesLinux) {
        this.creativePlanFilesLinux = creativePlanFilesLinux;
    }

    public String getCreativePlanFilesMysql() {
        return creativePlanFilesMysql;
    }

    public void setCreativePlanFilesMysql(String creativePlanFilesMysql) {
        this.creativePlanFilesMysql = creativePlanFilesMysql;
    }

    public String getLeaderAccessoryLinux() {
        return leaderAccessoryLinux;
    }

    public void setLeaderAccessoryLinux(String leaderAccessoryLinux) {
        this.leaderAccessoryLinux = leaderAccessoryLinux;
    }

    public String getLeaderAccessoryMysql() {
        return leaderAccessoryMysql;
    }

    public void setLeaderAccessoryMysql(String leaderAccessoryMysql) {
        this.leaderAccessoryMysql = leaderAccessoryMysql;
    }
}

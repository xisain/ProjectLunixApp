package com.sain.projectlunixapp.model;


public class Agent {
    private String Name;
    private String deskripsi;
    private String img;
    private Skill skill;
    private String id;
    private String key;

    public Agent(String id,String agentName,String deskripsi, String img, Skill skill) {
        this.id = id;
        this.Name = agentName;
        this.deskripsi = deskripsi;
        this.img = img;
        this.skill = skill;
    }


    public String getName() {
        return Name;
    }
    public void setName(String agentName) {
        this.Name = agentName;
    }
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Agent() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

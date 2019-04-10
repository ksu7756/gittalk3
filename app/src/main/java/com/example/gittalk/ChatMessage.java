package com.example.gittalk;

public class ChatMessage {
    private String id;
    private String text;
    private String name;
    private String phtoUrl;
    private String imageurl; // 첨부파일

    public ChatMessage() {
    }
    //기본생성자생성 - 파이어베이스 실시간 데이터베이스 사용시 제약사양 중 하나


    public ChatMessage(String text, String name, String phtoUrl, String imageurl) {
        this.text = text;
        this.name = name;
        this.phtoUrl = phtoUrl;
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhtoUrl() {
        return phtoUrl;
    }

    public void setPhtoUrl(String phtoUrl) {
        this.phtoUrl = phtoUrl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}

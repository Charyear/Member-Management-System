package com.membermanagement.model;

public class Member {
    private Integer id;  // 修改为Integer类型，与数据库类型匹配
    private String memberId;      // 6位会员ID
    private String name;          // 会员姓名
    private String phone;         // 手机号
    private String type;          // 会员类型（金、银）
    private double consumption;   // 消费金额
    private int points;           // 会员积分

    // 构造函数
    public Member() {}

    public Member(Integer id, String memberId, String name, String phone, String type, double consumption) {
        this.id = id;
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.consumption = consumption;
        this.points = (int)(consumption * 10); // 1元 = 10积分
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
        this.points = (int)(consumption * 10); // 更新积分
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getMemberType() {
        return type;
    }

    public void setMemberType(String memberType) {
        this.type = memberType;
    }

    public double getSpendAmount() {
        return this.consumption;
    }

    public void setSpendAmount(double spendAmount) {
        this.consumption = spendAmount;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }
}

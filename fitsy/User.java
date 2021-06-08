package com.example.fitsy;

public class User {
    private String age;
    private String weight;
    private String height;
    private String name;
    private String sex;
    private String M_bicepcurl;
    private String M_bench;
    private String M_deadlift;
    private String T_freestyle;
    private String T_butterfly;
    private String T_backstroke;



    private int fav_num;



    public User() {
    }

    public User(String age, String weight, String height, String sex,
                String name, String m_bicepcurl, String m_bench, String m_deadlift,
                String t_freestyle, String t_butterfly, String t_backstroke, int fav_num) {
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.name = name;
        this.M_bicepcurl = m_bicepcurl;
        this.M_bench = m_bench;
        this.M_deadlift = m_deadlift;
        this.T_freestyle = t_freestyle;
        this.T_butterfly = t_butterfly;
        this.T_backstroke = t_backstroke;
        this.fav_num = fav_num;
    }
    public String getT_freestyle() {
        return T_freestyle;
    }

    public int getFav_num() {
        return fav_num;
    }

    public void setFav_num(int fav_num) {
        this.fav_num = fav_num;
    }
    public void setT_freestyle(String t_freestyle) {
        T_freestyle = t_freestyle;
    }

    public String getT_butterfly() {
        return T_butterfly;
    }

    public void setT_butterfly(String t_butterfly) {
        T_butterfly = t_butterfly;
    }

    public String getT_backstroke() {
        return T_backstroke;
    }

    public void setT_backstroke(String t_backstroke) {
        T_backstroke = t_backstroke;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getM_bicepcurl() {
        return M_bicepcurl;
    }

    public void setM_bicepcurl(String m_bicepcurl) {
        M_bicepcurl = m_bicepcurl;
    }

    public String getM_bench() {
        return M_bench;
    }

    public void setM_bench(String m_bench) {
        M_bench = m_bench;
    }

    public String getM_deadlift() {
        return M_deadlift;
    }

    public void setM_deadlift(String m_deadlift) {
        M_deadlift = m_deadlift;
    }
}

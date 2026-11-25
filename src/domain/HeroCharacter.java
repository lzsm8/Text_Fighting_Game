package domain;

import java.util.ArrayList;
import java.util.Scanner;

public class HeroCharacter extends Character{
    public ArrayList<String> skillList;
    public int MP;
    public int maxMP;

    public HeroCharacter() {
        skillList = new ArrayList<>();
    }

    public HeroCharacter(String name, int HP, int MP, int ATK, int DEF) {
        super(name, HP, ATK, DEF);
        this.MP = MP;
        maxMP = MP;
        skillList = new ArrayList<>();
    }

    // 展示技能
    public String showSkill() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < skillList.size(); i++) {
            sb.append(skillList.get(i));
            if (i != skillList.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    // 重写show方法，加上蓝条
    @Override
    public String show() {
        return name + "[HP: " + HP + "/" + maxHP + ", MP: " + MP + "/" + maxMP + ", ATK: " + ATK + ", DEF: " + DEF + "]";
    }

    // 恢复蓝条
    public int restoreMP(int mp) {
        int actualRestore = Math.min(mp, maxMP - MP);
        MP += actualRestore;
        return actualRestore;
    }
}

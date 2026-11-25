package domain;

public class Character {
    public String name;  // 角色名
    public int HP;  // 生命值
    public int maxHP;  // 最大生命值
    public int ATK; // 攻击力
    public int DEF; // 防御力

    public Character() {
    }

    public Character(String name, int HP, int ATK, int DEF) {
        this.name = name;
        this.HP = HP;
        this.maxHP = HP;
        this.ATK = ATK;
        this.DEF = DEF;
    }

    // 判断是否存活
    public boolean isAlive() {
        return HP > 0;
    }

    // 回复血量
    public int heal(int hp) {
        int actualRestore = Math.min(hp, maxHP - HP);
        HP += actualRestore;
        return actualRestore;
    }

    // 造成伤害
    public void takeDamage(int damage) {
        HP -= damage;
        if (HP < 0) {
            HP = 0;
        }
    }

    // 展示数值
    public String show() {
        return name + "[HP: " + HP + "/" + maxHP + ", ATK: " + ATK + ", DEF: " + DEF + "]";
    }
}

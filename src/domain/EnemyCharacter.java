package domain;

public class EnemyCharacter extends Character{
    public String skill;
    public boolean defendBuff;

    public EnemyCharacter() {
        defendBuff = false;
    }

    public EnemyCharacter(String name, int HP, int ATK, int DEF, String skill) {
        super(name, HP, ATK, DEF);
        this.skill = skill;
        defendBuff = false;
    }

    @Override
    public void takeDamage(int damage) {
        if (defendBuff) {
            damage = damage / 2 > 1 ? damage / 2 : 1;
            defendBuff = false;
        }

        super.takeDamage(damage);
    }
}

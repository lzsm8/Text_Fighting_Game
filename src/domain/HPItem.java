package domain;

public class HPItem extends Item {
    private int hp;

    public HPItem() {
    }

    public HPItem(String name, String function, int hp) {
        super(name, function);
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public void show() {
        super.show();
        System.out.println("增加血量：" + hp);
    }
}

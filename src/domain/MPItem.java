package domain;

public class MPItem extends Item {
    private int mp;

    public MPItem() {
    }

    public MPItem(String name, String function, int mp) {
        super(name, function);
        this.mp = mp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    @Override
    public void show() {
        super.show();
        System.out.println("增加魔法值：" + mp);
    }
}

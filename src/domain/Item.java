package domain;

public class Item {
    private String name;
    private String function;

    public Item() {
    }

    public Item(String name, String function) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void show() {
        System.out.println("道具名称：" + name);
        System.out.println("道具功能：" + function);
    }
}

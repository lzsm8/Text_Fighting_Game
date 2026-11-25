package ui;

import domain.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class FightingGame {
    public void gameStart(String username) {
        System.out.print("╔");
        for (int i = 0; i < username.length(); i++) {
            System.out.print("═");
        }
        System.out.println("════════════════════╗");
        System.out.println(" " + username + ", 欢迎来到文字格斗游戏");
        System.out.print("╚");
        for (int i = 0; i < username.length(); i++) {
            System.out.print("═");
        }
        System.out.println("════════════════════╝");

        HeroCharacter player = createHeroCharacter(username);

        // 展示属性
        System.out.println("角色创建成功！");
        System.out.println("初始属性：" + player.show());
        System.out.println("拥有技能：" + player.showSkill());

        // 创建敌人列表
        ArrayList<EnemyCharacter> enemyList = new ArrayList<>();
        enemyList.add(new EnemyCharacter("初级战士", 80, 15, 10, "猛击"));
        enemyList.add(new EnemyCharacter("敏捷刺客", 60, 20, 5, "快速攻击"));
        enemyList.add(new EnemyCharacter("重装坦克", 120, 10, 20, "防御姿态"));
        enemyList.add(new EnemyCharacter("神秘法师", 70, 25, 8, "火球术"));

        // 创建道具列表
        ArrayList<Item> itemList = new ArrayList<>();

        int count = 1; // 记录第几个敌人
        int wins = 0; // 记录胜利场数

        // 开始战斗
        // 外循环整场战斗
        while (player.isAlive()) {
            if (wins != 0) {
                for (int i = 0; i < enemyList.size(); i++) {
                    EnemyCharacter c = enemyList.get(i);
                    c.maxHP += 10;
                    c.HP = c.maxHP;
                    c.ATK += 3;
                    c.DEF += 2;
                    c.defendBuff = false;
                }
            }

            Random r = new Random();
            int index = r.nextInt(enemyList.size());
            EnemyCharacter enemy = enemyList.get(index);
            System.out.println("════════════════════════════════════");
            System.out.println("⚔️ 第" + count + "场战斗开始！对手：" + enemy.show());

            // 记录回合数
            int round = 1;
            // 内循环整个战斗的一场
            while (player.isAlive()) {
                System.out.println("-----------------------------");
                System.out.println("⚔️ 第" + round + "回合开始！");
                System.out.println(printHPBar(player.name, player.HP, player.maxHP, "HP"));
                System.out.println(printHPBar(player.name, player.MP, player.maxMP, "MP"));
                System.out.println(printHPBar(enemy.name, enemy.HP, enemy.maxHP, "HP"));

                playerTurn(player, enemy, itemList);

                // 判断敌人是否存活
                if (!enemy.isAlive()) {
                    System.out.println("🎉 你击败了 " + enemy.name + "!");
                    wins++;
                    break;
                }

                enemyTurn(enemy, player);

                // 判断玩家是否存活
                if (!player.isAlive()) {
                    System.out.println("💀 你被" + enemy.name + "击败了...");
                    break;
                }

                // 玩家胜利
                round++;
            }

            // 一场战斗结束
            if (player.isAlive()) {
                // 恢复20 - 40血量和30%最大能量值的能量
                int healHP = r.nextInt(20, 41);
                int heal = player.heal(healHP);
                int restoreMP = (int) (player.maxMP * 0.3);
                int restore = player.restoreMP(restoreMP);

                System.out.println("💚 战斗结束！你恢复了 " + healHP + " 点生命值，" + restore + " 点能量值");

                // 每场战斗结束后10%概率掉落大药水，16%概率掉落中药水，24%概率掉落小药水，50%概率不掉落
                int itemProbability = r.nextInt(0, 100);
                if(itemProbability < 5) {
                    // 掉落大生命药水
                    itemList.add(new HPItem("大生命药水", "恢复生命值", 100));
                    System.out.println("🎁 恭喜！你获得了大生命药水！");
                } else if(itemProbability < 10) {
                    // 掉落大魔法药水
                    itemList.add(new MPItem("大魔法药水", "恢复魔法值", 50));
                    System.out.println("🎁 恭喜！你获得了大魔法药水！");
                } else if(itemProbability < 18) {
                    // 掉落中生命药水
                    itemList.add(new HPItem("中生命药水", "恢复生命值", 50));
                    System.out.println("🎁 恭喜！你获得了中生命药水！");
                } else if(itemProbability < 26) {
                    // 掉落中魔法药水
                    itemList.add(new MPItem("中魔法药水", "恢复魔法值", 30));
                    System.out.println("🎁 恭喜！你获得了中魔法药水！");
                } else if(itemProbability < 38) {
                    // 掉落小生命药水
                    itemList.add(new HPItem("小生命药水", "恢复生命值", 20));
                    System.out.println("🎁 恭喜！你获得了小生命药水！");
                } else if(itemProbability < 50) {
                    // 掉落小魔法药水
                    itemList.add(new MPItem("小魔法药水", "恢复魔法值", 10));
                    System.out.println("🎁 恭喜！你获得了小魔法药水！");
                }

                System.out.println("🏆 当前胜场：" + wins);
                System.out.println("════════════════════════════════════");
            }

            // 每3场战斗提升属性
            // 最大生命值加30，最大能量值加5，攻击力加5，防御力加3
            if (player.isAlive() && wins > 0 && wins % 3 == 0) {
                System.out.println("✨ 恭喜！你获得了属性提升！");
                player.maxHP += 30;
                player.HP += 30;
                player.maxMP += 5;
                player.MP += 5;
                player.ATK += 5;
                player.DEF += 3;
                System.out.println("最大生命值 + 30，最大能量值 + 5，攻击力 + 5，防御力 + 3");
                System.out.println("当前属性：" + player.show());
            }

            // 询问是否继续
            if (player.isAlive()) {
                System.out.println("继续下一场战斗？(y/n)");
                Scanner sc = new Scanner(System.in);
                String choice = sc.next();
                if ("y".equalsIgnoreCase(choice)) {
                    count++;
                    continue;
                } else if ("n".equalsIgnoreCase(choice)) {
                    break;
                } else {
                    System.out.println("没有这个选项，默认游戏继续");
                    continue;
                }
            }


        }

        // 游戏的最终结算
        System.out.println("════════════════════════════════════");
        System.out.println("游戏结束！");
        System.out.println("总胜场：" + wins);
        System.out.println("感谢游玩文字版格斗游戏");
        System.exit(0);

    }

    // 敌方回合
    private void enemyTurn(EnemyCharacter enemy, HeroCharacter player) {
        System.out.println("════" + enemy.name + "的回合════");
        // 50%普通攻击，50%技能
        // 先默认为普通攻击
        String action = "普通攻击";

        Random r = new Random();
        int num = r.nextInt(10);

        if (num >= 5) {
            action = enemy.skill;
        }

        switch (action) {
            case "普通攻击":
                int damage1 = calculateDamage(enemy.ATK, player.DEF);
                System.out.println("⚔️ " + enemy.name + "对你使用了普通攻击，造成" + damage1 + "点伤害！");
                player.takeDamage(damage1);
                break;
            case "猛击":
                int damage2 = calculateDamage((int) (enemy.ATK * 1.5), player.DEF);
                System.out.println("💥 " + enemy.name + "对你使用了猛击，造成" + damage2 + "点伤害！");
                player.takeDamage(damage2);
                break;
            case "快速攻击":
                int damage3 = 0;
                for (int i = 0; i < 2; i++) {
                    int temp = calculateDamage(enemy.ATK / 2, player.DEF);
                    damage3 += temp;
                }
                System.out.println("🌀 " + enemy.name + "对你使用了快速攻击，造成" + damage3 + "点伤害！");
                player.takeDamage(damage3);
                break;
            case "防御姿态":
                enemy.defendBuff = true;
                System.out.println("🛡️ " + enemy.name + "摆出了防御姿态！");
                break;
            case "火球术":
                int damage4 = calculateDamage((int) (enemy.ATK * 1.8), player.DEF);
                System.out.println("🔥 " + enemy.name + "对你使用了火球术，造成" + damage4 + "点伤害！");
                player.takeDamage(damage4);
                break;
        }
    }

    // 玩家回合
    public void playerTurn(HeroCharacter player, EnemyCharacter enemy, ArrayList<Item> itemList) {
        System.out.println("════你的回合════");
        System.out.println("1. 普通攻击");
        System.out.println("2. 强力一击（消耗10MP）");
        System.out.println("3. 生命汲取（消耗10MP，恢复生命）");
        System.out.println("4. 使用道具");
        System.out.println("选择行动（1 - 4）：");
        Scanner sc = new Scanner(System.in);
        String choice = sc.next();

        switch (choice) {
            default:
                System.out.println("无效输入，默认使用普通攻击");
                // 普通攻击
            case "1":
                int damage1 = calculateDamage(player.ATK, enemy.DEF);
                System.out.println("⚔️ 你对 " + enemy.name + " 使用了普通攻击，造成 " + damage1 + " 点伤害！");
                enemy.takeDamage(damage1);
                break;
            // 强力一击
            case "2":
                if (player.MP > 10) {
                    player.MP -= 10;
                    int damage2 = calculateDamage((int) (player.ATK * 1.8), enemy.DEF);
                    System.out.println("💥 消耗10MP，你对 " + enemy.name + " 使用了强力一击，造成 " + damage2 + " 点伤害！");
                    enemy.takeDamage(damage2);
                } else {
                    System.out.println("能量不足，攻击失败~");
                }

                break;
            // 生命汲取
            case "3":
                if (player.MP > 10) {
                    player.MP -= 10;
                    Random r = new Random();
                    int healHP = player.heal(r.nextInt(5, 36));
                    System.out.println("💖 消耗10MP，你使用生命汲取，恢复了 " + healHP + " 点生命值！");
                } else {
                    System.out.println("能量不足，恢复生命失败~");
                }

                break;
            // 使用道具
            case "4":
                while(true) {
                    System.out.println("你拥有的道具：");
                    System.out.println("0. 道具详情");
                    for (int i = 0; i < itemList.size(); i++) {
                        System.out.println((i + 1) + ". " + itemList.get(i).getName());
                    }
                    System.out.println("选择使用的道具（输入序号）：");
                    int itemChoice = sc.nextInt();
                    if (itemChoice < 0 || itemChoice > itemList.size()) {
                        System.out.println("无效输入，默认不使用道具");
                    } else if (itemChoice == 0) {
                        System.out.println("你想了解哪个道具的详情？（输入序号）");
                        int detailChoice = sc.nextInt();
                        if (detailChoice >= 1 && detailChoice <= itemList.size()) {
                            Item item = itemList.get(detailChoice - 1);

                            if (item instanceof HPItem) {
                                HPItem hpItem = (HPItem) item;
                                hpItem.show();
                                continue;
                            } else if (item instanceof MPItem) {
                                MPItem mpItem = (MPItem) item;
                                mpItem.show();
                                continue;
                            }
                        } else {
                            System.out.println("无效输入！");
                            continue;
                        }
                    } else {
                        Item item = itemList.get(itemChoice - 1);
                        if (item instanceof HPItem) {
                            HPItem hpItem = (HPItem) item;
                            int healHP = player.heal(hpItem.getHp());
                            System.out.println("💖 你使用了 " + hpItem.getName() + "，恢复了 " + healHP + " 点生命值！");

                            itemList.remove(itemChoice - 1);
                            break;
                        } else if (item instanceof MPItem) {
                            MPItem mpItem = (MPItem) item;
                            int restoreMP = player.restoreMP(mpItem.getMp());
                            System.out.println("🔵 你使用了 " + mpItem.getName() + "，恢复了 " + restoreMP + " 点能量值！");

                            itemList.remove(itemChoice - 1);
                            break;
                        }
                    }
                    break;
                }
        }
    }

    // 计算伤害
    public int calculateDamage(int ATK, int DEF) {
        int damage = ATK - DEF;
        if (damage < 1) {
            damage = 1;
        }

        return damage;
    }

    // 打印角色血条或蓝条
    public String printHPBar(String name, int current, int max, String barType) {
        // 满血状态下血条的长度
        int barLength = 20;

        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": [");

        // 当前血条的长度
        int filled = (int) (current * 1.0 / max * barLength);

        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                sb.append("█");
            } else {
                sb.append(" ");
            }
        }

        sb.append("] ").append(current).append("/").append(max).append(" ").append(barType);

        return sb.toString();
    }

    // 创建角色
    public HeroCharacter createHeroCharacter(String username) {
        System.out.println("创建你的角色：");
        System.out.println("你的角色名为：" + username);
        System.out.println("请分配属性点（共25点）：");
        System.out.println("1. 生命值（每点+10 HP）");
        System.out.println("1. 能量值（每点+5 MP）");
        System.out.println("2. 攻击力（每点+2 ATK）");
        System.out.println("3. 防御力（每点+10 DEF）");

        // 分配点数
        int points = 25;

        // 用数组存储属性和分配的属性点
        String[] attr = {"生命值", "能量值", "攻击力", "防御力"};
        int[] attrPoints = new int[4];
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < attr.length; i++) {
            System.out.println("分配点数到" + attr[i] + "（剩余点数：" + points + "）：");

            // 判断输入属性点是否符合要求
            while (true) {
                int input = sc.nextInt();
                if (input > points) {
                    System.out.println("输入的属性点数大于剩余点数，请重新输入");
                } else if (input < 0) {
                    System.out.println("无效输入，请重新输入");
                } else {
                    attrPoints[i] = input;
                    points -= input;
                    break;
                }
            }
        }

        // 创建玩家角色对象
        HeroCharacter player = new HeroCharacter(
                username, // 角色名
                100 + attrPoints[0] * 10, // 生命值
                30 + attrPoints[1] * 5, // 能量值
                10 + attrPoints[2] * 2, // 攻击力
                0 + attrPoints[3] * 1 // 防御力
        );

        // 添加玩家技能
        player.skillList.add("普通攻击");
        player.skillList.add("强力一击");
        player.skillList.add("生命汲取");

        return player;
    }
}

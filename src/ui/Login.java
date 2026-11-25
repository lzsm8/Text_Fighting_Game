package ui;

import domain.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Login {
    public void start() {
        ArrayList<User> list = new ArrayList<>();

        while (true) {
            System.out.println(" --------------------");
            System.out.println("| 欢迎来到文字格斗游戏 |");
            System.out.println(" --------------------");
            System.out.println("请选择操作：1登录 2注册 3忘记密码 4退出");

            Scanner sc = new Scanner(System.in);
            String choice = sc.next();
            switch (choice) {
                case "1" -> login(list);
                case "2" -> register(list);
                case "3" -> forgetPassword(list);
                case "4" -> {
                    System.exit(0);
                }
                default -> System.out.println("输入无效，请重新输入");
            }
        }
    }

    // 忘记密码
    public void forgetPassword(ArrayList<User> list) {
        System.out.println("请输入用户名：");
        Scanner sc = new Scanner(System.in);
        String inputUsername = sc.next();

        // 检查用户名是否存在
        if (!contains(list, inputUsername)) {
            System.out.println("用户名不存在，请先注册~");
            return;
        }

        // 用户名存在，验证手机号
        int index = findIndex(list, inputUsername);
        while (true) {
            System.out.println("请输入手机号（输入1返回）：");
            String inputPhoneNumber = sc.next();

            User u = list.get(index);
            // 判断手机号
            if (inputPhoneNumber.equals("1")) {
                return;
            }

            if (!inputPhoneNumber.equals(u.getPhoneNumber())) {
                System.out.println("手机号错误，请重新输入");
                continue;
            }

            // 手机号成功，输入新密码
            System.out.println("手机号验证成功，输入新密码");
            createPassword(list.get(index));
            System.out.println("密码修改成功，账号已解封！");
            u.setStatus(true);
            break;
        }
    }

    // 注册
    public void register(ArrayList<User> list) {
        System.out.println("用户选择了注册操作~");

        // 创建User对象
        User u = new User();

        // 验证数据时，先验证格式，再验证唯一性

        Scanner sc = new Scanner(System.in);

        // 创建用户名
        while (true) {
            System.out.println("请输入用户名：");
            String username = sc.next();

            // 检查用户名长度
            if (!checkLen(3, 16, username)) {
                System.out.println("用户名长度需在3 ~ 16位");
                continue;
            }

            // 检查用户名是否使用字母
            if (!checkUsername(username)) {
                System.out.println("用户名需由字母、数字组成，不能是纯数字");
                continue;
            }

            // 检查是否唯一
            if (contains(list, username)) {
                System.out.println("用户名已存在");
                continue;
            }

            u.setUsername(username);
            break;
        }

        // 创建密码
        createPassword(u);

        // 添加手机号
        while (true) {
            System.out.println("请输入手机号：");
            String phoneNumber = sc.next();
            char[] c = phoneNumber.toCharArray();
            int letter = getCount(phoneNumber)[0];
            int other = getCount(phoneNumber)[2];

            if (letter != 0 || other != 0) {
                System.out.println("手机号必须是纯数字");
                continue;
            }

            if (phoneNumber.length() != 11) {
                System.out.println("手机号长度必须是11位");
                continue;
            }

            if ('1' != c[0]) {
                System.out.println("手机号必须以1开头");
                continue;
            }

            u.setPhoneNumber(phoneNumber);
            System.out.println("手机号添加成功");
            break;
        }


        // 把对象加入集合
        list.add(u);

        // 提示注册成功
        System.out.println("用户" + u.getUsername() + "注册成功!");

    }

    // 登录
    public void login(ArrayList<User> list) {
        System.out.println("用户选择了登录操作~");

        // 输入用户名
        System.out.println("请输入用户名：");
        Scanner sc = new Scanner(System.in);
        String username = sc.next();

        // 判断用户名是否存在
        if (!contains(list, username)) {
            System.out.println("用户名未注册，请先注册再登录~");
            return;
        }

        // 判断账号状态
        int index = findIndex(list, username);
        User u = list.get(index);
        if (!u.isStatus()) {
            System.out.println("用户" + u.getUsername() + "已禁用，请选择忘记密码或联系管理员XXX: XXX-XXXX-XXXX");
            return;
        }

        // 判断密码和验证码
        String rightPassword = u.getPassword();
        for (int i = 0; i < 3; i++) {
            // 输入密码
            System.out.println("请输入密码：");
            String password = sc.next();

            // 验证码
            while (true) {
                // 生成验证码
                String code = getCode();
                System.out.println("验证码：" + code);

                // 判断验证码
                System.out.println("请输入验证码：");
                if (!code.equalsIgnoreCase(sc.next())) {
                    System.out.println("验证码错误，请重试");
                    continue;
                } else {
                    break;
                }
            }

            // 判断密码
            if (!rightPassword.equals(password)) {
                if (i == 2) {
                    u.setStatus(false);
                    System.out.println("当前账户" + u.getUsername() + "已锁定，请选择忘记密码或联系管理员XXX: XXX-XXXX-XXXX");
                } else {
                    System.out.println("密码错误，还有" + (2 - i) + "次机会");
                    continue;
                }

            } else {
                System.out.println("登录成功，游戏启动~");

                // 开始游戏
                FightingGame fg = new FightingGame();
                fg.gameStart(username);
                break;
            }
        }


    }

    // 创建密码
    public void createPassword(User u) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入密码：");
            String password1 = sc.next();

            // 检查密码长度
            if (!checkLen(3, 8, password1)) {
                System.out.println("密码长度需在3 ~ 8位");
                continue;
            }

            // 检查密码是否是字母加数字的组合
            if (!checkPassword(password1)) {
                System.out.println("密码必须是字母加数字的组合");
                continue;
            }

            System.out.println("请再次输入密码：");
            String password2 = sc.next();

            // 判断两次密码是否一致
            if (!password2.equals(password1)) {
                System.out.println("两次密码输入不一致，请重新输入");
                continue;
            }

            u.setPassword(password1);
            break;
        }
    }

    // 找用户名所在索引
    public int findIndex(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (u.getUsername().equals(username)) {
                return i;
            }
        }

        return -1;
    }

    // 检查字符串长度是否在指定范围内
    public boolean checkLen(int minLen, int maxLen, String str) {
        return str.length() >= minLen && str.length() <= maxLen;
    }

    // 获取字符串中字母、数字、其他字符的数量
    public int[] getCount(String userInfo) {
        int letterCount = 0;
        int numCount = 0;
        int otherCount = 0;
        for (int i = 0; i < userInfo.length(); i++) {
            char c = userInfo.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                letterCount++;
            } else if (c >= '0' && c <= '9') {
                numCount++;
            } else {
                otherCount++;
            }
        }

        return new int[]{letterCount, numCount, otherCount};
    }

    // 检查用户名是否由字母、数字组成，不是纯数字
    public boolean checkUsername(String username) {
        int[] count = getCount(username);
        return count[0] > 0 && count[2] == 0;
    }

    // 检查集合中是否有该字符串
    public boolean contains(ArrayList<User> list, String username) {
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            if (username.equals(u.getUsername()))
                return true;
        }

        return false;
    }

    // 检查密码是否是字母和数字的组合
    public boolean checkPassword(String password) {
        int[] count = getCount(password);
        return count[0] > 0 && count[1] > 0 && count[2] == 0;
    }

    // 获取验证码
    public String getCode() {
        // 创建一个包含字母大小写的容器
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add((char) ('a' + i));
            list.add((char) ('A' + i));
        }

        // 随机抽取字母
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            int index = r.nextInt(list.size());
            char c = list.get(index);
            sb.append(c);
        }

        // 生成数字
        sb.append(r.nextInt(10));

        // 把数字放到随机位置
        char[] arr = sb.toString().toCharArray();
        int i = r.nextInt(arr.length);
        if (i != arr.length - 1) {
            char temp = arr[i];
            arr[i] = arr[arr.length - 1];
            arr[arr.length - 1] = temp;
        }

        String code = new String(arr);

        return code;
    }
}



import ui.FightingGame;
import ui.Login;

public class App {
    static void main() {
        // 只负责启动整个程序
        // Login l = new Login();
        // l.start();

        FightingGame fg = new FightingGame();
        fg.gameStart("zhangsan");
    }
}

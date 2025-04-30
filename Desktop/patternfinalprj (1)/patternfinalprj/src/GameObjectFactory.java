package factory;
import main.GameObject;
import main.Player;
import main.Enemy;
import main.Platform;


public class GameObjectFactory {

    public static GameObject createObject(String type, int x, int y) {
        switch (type.toLowerCase()) {
            case "player":
                return new Player(x, y);
            case "enemy":
                return new Enemy(x, y);
            case "platform":
                return new Platform(x, y, 100, 20);
            default:
                throw new IllegalArgumentException("Неизвестный тип объекта: " + type);
        }
    }
}

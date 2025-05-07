package manager;

public class ScoreManager {
    private static final ScoreManager instance = new ScoreManager();
    private int score = 0;
    private int lives = 3;

    private ScoreManager() {}

    public static ScoreManager getInstance() {
        return instance;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int value) {
        score += value;
    }

    public void resetScore() {
        score = 0;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife() {
        lives--;
    }

    public void resetLives() {
        lives = 3;
    }
}

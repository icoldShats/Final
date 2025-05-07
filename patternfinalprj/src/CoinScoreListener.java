package observer;

import manager.ScoreManager;

public class CoinScoreListener implements CoinEventListener {
    @Override
    public void onCoinCollected() {
        ScoreManager.getInstance().addScore(1);
    }
}
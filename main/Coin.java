package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import observer.CoinEventListener;
import java.util.List;
import java.util.ArrayList;



public class Coin extends GameObject {
    private boolean collected = false;
    private List<CoinEventListener> listeners = new ArrayList<>();

    public Coin(int x, int y) {
        super(x, y, 20, 20);
    }

    public void collect() {
        if (!collected) {
            collected = true;
            notifyListeners();
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public void addListener(CoinEventListener listener) {
        listeners.add(listener);
    }




}

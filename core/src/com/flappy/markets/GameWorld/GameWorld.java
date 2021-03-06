package com.flappy.markets.GameWorld;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.flappy.markets.GameObjects.Bird;
import com.flappy.markets.GameObjects.ScrollHandler;
import com.flappy.markets.STHelpers.AssetLoader;
import com.flappy.markets.STHelpers.MarketPriceTimeCoordinator;
import com.flappy.markets.STHelpers.PortfolioTimeCoordinator;

public class GameWorld {
    private final ArrayList<BirdData> marketBirdData;

    private int k;

    private ScrollHandler scroller;

    private CameraMan cameraMan;

	private Bird bird;
    private Bird marketBird;

    List<Bird> birds = new ArrayList<Bird>();

    public final static int MIN_BIRD_SPREAD = 150; // m - We don't zoom any closer than as if the birds were N meters apart

    public final static double STARTING_CASH = 1000000;

    public boolean touching = false;
    private boolean alreadyPlaying = false;

    public List<Bird> getBirds() {
        return birds;
    }

    private Rectangle ground;

	private double score = 0.0;
    private double signal = 0.0;
    private double myMoney = 0.0;

	public GameState currentState;
	public int midPointY;

    PortfolioTimeCoordinator myPortfolioTimeCoordinator;
    PortfolioTimeCoordinator marketPortfolioTimeCoordinator;
    MarketPriceTimeCoordinator marketPriceTimeCoordinator;

    public enum GameState {
			READY, RUNNING, GAMEOVER, HIGHSCORE
		}


	public GameWorld(int midPointY) {
		currentState = GameState.READY;
		this.midPointY = midPointY;

        marketPriceTimeCoordinator = new MarketPriceTimeCoordinator(0, 500, 241);
        myPortfolioTimeCoordinator =  new PortfolioTimeCoordinator(STARTING_CASH, 0, 500, 500, 241);
        marketPortfolioTimeCoordinator =  new PortfolioTimeCoordinator(STARTING_CASH, 0, 500, 500, 241);

        myPortfolioTimeCoordinator.setMarketPriceTimeCoordinator(marketPriceTimeCoordinator);
        marketPortfolioTimeCoordinator.setMarketPriceTimeCoordinator(marketPriceTimeCoordinator);

        bird = new Bird(-6.5f, midPointY, 17, 12, myPortfolioTimeCoordinator);
        marketBird = new Bird(-10.5f, midPointY, 17, 12, marketPortfolioTimeCoordinator);
        
        scroller = new ScrollHandler(this, midPointY + 66);

        int i = 2000;
        k = 0;

        marketBirdData = new ArrayList<BirdData>();
        for (int j = 0; j < i; j ++) {
            marketBirdData.add(new BirdData(20, j));
        }

        this.birds.add(marketBird);
        this.birds.add(bird);

        // cameraman needs birds
        this.cameraMan = new CameraMan(this);
	}

	public void update(float delta) {

        //TOOD: END THE GAME AT SOME POINT
		switch (currentState) {
            case READY:
                updateReady(delta);
                break;

            case RUNNING:
                default:

                if(!this.alreadyPlaying){
//                    AssetLoader.ending.setLooping(0l,true);
                    AssetLoader.ending.play();
                    this.alreadyPlaying = true;
                }
                    updateRunning(delta);
                    break;
            }
	}

	private void updateReady(float delta) {

	}

	public void updateRunning(float delta) {
        marketPriceTimeCoordinator.incrementTime((long) (delta * 1000));
        marketPortfolioTimeCoordinator.incrementTime((long) (delta * 1000), true); // market bird never sells
        myPortfolioTimeCoordinator.incrementTime((long) (delta * 1000), touching);

        bird.bouncyUpdate(delta);
        marketBird.slowUpdate(delta);
		scroller.update(delta);

        if (!marketPriceTimeCoordinator.hasNext()) {
            System.out.println("GAME OVER!!!");
            currentState = GameState.GAMEOVER;
        }

        if(currentState != GameState.GAMEOVER) {
            this.score = myPortfolioTimeCoordinator.getCurrentValue() - marketPortfolioTimeCoordinator.getCurrentValue();
        }
        this.myMoney = myPortfolioTimeCoordinator.getCurrentValue();
        this.signal = marketPriceTimeCoordinator.getCurrentSignal();
	}

	public Bird getBird() {
		return bird;
	}

    public Bird getMarketBird() {
        return marketBird;
    }

	public ScrollHandler getScroller() {
		return scroller;
	}

    public CameraMan getCameraMan() {
        return cameraMan;
    }

	public double getScore() {
		return score;
	}

    public double getSignal() {
        return signal;
    }

	public void addScore(int increment) {
		score += increment;
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}

	public void restart() {
		currentState = GameState.READY;
		score = 0;
		bird.onRestart(midPointY - 5);
		scroller.onRestart();
		currentState = GameState.READY;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}

    public float getBirdSpread(){
        float spread = maxBirdAltitude() - minBirdAltitude();
        return Math.max(MIN_BIRD_SPREAD, spread);
    }

    public float maxBirdAltitude(){
        float max = birds.get(0).getY();

        for(Bird b : birds){
            if(b.getY() > max){
                max = b.getY();
            }
        }
        return max;
    }

    public float minBirdAltitude(){
        float min = birds.get(0).getY();

        for(Bird b : birds){
            if(b.getY() < min){
                min = b.getY();
            }
        }
        return min;
    }


    private class BirdData {
        double x, y;
        private Random rand;

        private BirdData(int x, int y) {
            this.x = x;
            this.y = randInt(30, 50);
        }

        public int randInt(int min, int max) {
            rand = new Random();
            int randomNum = rand.nextInt((max - min) + 1) + min;
            return randomNum;
        }

    }
}

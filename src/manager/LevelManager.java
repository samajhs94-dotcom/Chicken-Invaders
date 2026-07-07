package manager;

import enemy.*;
import model.Cell;
import model.EnemyType;
import model.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;

    public LevelManager() {
        createLevels();
    }

    private void createLevels() {

        // LEVEL 1
        levels.add(new Level(1, 2, 1, 2,
                2, 2, 1.0, 20, 3000,
                new EnemyType[]{EnemyType.NORMAL}));

        // LEVEL 2
        levels.add(new Level(2, 2, 1, 2,
                2, 2, 1.5, 20, 2000,
                new EnemyType[]{EnemyType.NORMAL, EnemyType.FAST}));

        // LEVEL 3
        levels.add(new Level(3, 2, 1, 2,
                2, 3, 2.0, 25, 1500,
                new EnemyType[]{EnemyType.NORMAL, EnemyType.ZIGZAG}));

        // LEVEL 4 (BOSS)
        levels.add(new Level(4, 50, 1500, 4));

        // LEVEL 5
        levels.add(new Level(5, 3, 2, 3,
                3, 3, 2.5, 25, 1000,
                new EnemyType[]{EnemyType.SHOOTER, EnemyType.FAST}));

        // LEVEL 6
        levels.add(new Level(6, 3, 2, 3,
                3, 4, 3.0, 30, 800,
                new EnemyType[]{EnemyType.ZIGZAG, EnemyType.SHOOTER}));

        // LEVEL 7
        levels.add(new Level(7, 3, 2, 3,
                3, 4, 3.5, 30, 700,
                new EnemyType[]{EnemyType.NORMAL, EnemyType.FAST, EnemyType.ZIGZAG, EnemyType.SHOOTER}));

        // LEVEL 8 (FINAL BOSS)
        levels.add(new Level(8, 100, 1000, 8));

    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public void nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
        }
    }

    public boolean isLastLevel() {
        return currentLevelIndex == levels.size() - 1;
    }

    public void reset() {
        currentLevelIndex = 0;
    }

    public int getLevelNumber() {
        return levels.get(currentLevelIndex).getLevelNumber();
    }

    public Cell[][] buildGrid(Level level) {

        int rows = 5;
        int cols = 8;

        Cell[][] grid = new Cell[rows][cols];

        EnemyType[] types = level.getAllowedTypes();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                EnemyType type = types[(int)(Math.random() * types.length)];

                int x = 150 + c * 65;
                int y = 40 + r * 60;

                grid[r][c] = new Cell(r, c, x, y, type, level.getCellCounter());
            }
        }

        return grid;
    }


}
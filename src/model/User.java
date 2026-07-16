package model;

public class User {

    private String userName;
    private String password;
    private int highScore;
    private int lastLevel;
    private boolean musicEnabled;
    private boolean shotSoundEnabled;
    private boolean explosionSoundEnabled;
    private boolean gameOverSoundEnabled;
    private int id;
    private int selectedPlane;

    public User(String userName,String password){
        this.userName=userName;
        this.password=password;
        highScore=0;
        lastLevel=1;
        selectedPlane = 1;
        musicEnabled=true;
        shotSoundEnabled=true;
        explosionSoundEnabled=true;
        gameOverSoundEnabled=true;
    }

    @Override
    public String toString(){
        return String.format("User("+
                "UesrName : " + userName +" | " +
                "HighScore : " + highScore + " | " +
                "LastLevel : " + lastLevel + ")");
    }

    //گتر ها
    public String getUsername(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public int getHighScore(){
        return highScore;
    }
    public int getId(){
        return id;
    }
    public int getLastLevel(){
        return lastLevel;
    }
    public boolean getMusicEnabled() {
        return musicEnabled;
    }
    public boolean getShotSoundEnabled() {
        return shotSoundEnabled;
    }
    public boolean getExplosionSoundEnabled() {
        return explosionSoundEnabled;
    }
    public boolean getGameOverSoundEnabled() {
        return gameOverSoundEnabled;
    }
    public int getSelectedPlane() {
        return selectedPlane;
    }
    //ستر ها
    public void setPassword(String password) {
        this.password = password;
    }
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }
    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }
    public void setShotSoundEnabled(boolean shotSoundEnabled) {
        this.shotSoundEnabled = shotSoundEnabled;
    }
    public void setExplosionSoundEnabled(boolean explosionSoundEnabled) {
        this.explosionSoundEnabled = explosionSoundEnabled;
    }
    public void setGameOverSoundEnabled(boolean gameOverSoundEnabled) {
        this.gameOverSoundEnabled = gameOverSoundEnabled;
    }
    public void setSelectedPlane(int selectedPlane) {
        this.selectedPlane = selectedPlane;
    }
    public void setId(int id){
        this.id=id;
    }

}

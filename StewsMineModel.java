import java.util.*;

class StewsMineModel implements MineModel
{
  StewsCell[][] grid;
  //cell's must have functions called isMine and makeMine, isMine returns true if is Mine and makeMine makes it a mine
  Random random = new Random();
  int rows;
  int cols;
  int mines;
 // int visCount;
  long starttime;
  int flags;
  boolean gameWon;
  boolean gameStarted;
  boolean dead;
  
  
  ///////////SET TO TRUE IF YOU WANT THE COOL HELPY THING
  boolean help;
  
  StewsMineModel(){
  }
  
  
  public void newGame(int numRows, int numCols, int numMines){
    grid = new StewsCell[numRows][numCols];
    starttime=System.currentTimeMillis();
    gameWon=false;
    dead=false;
    gameStarted=true;
    help = false;
    int currentRow;
    int currentCol;
    rows=numRows;
    cols=numCols;
    mines= numMines;
    for(int rowCounter = 0; rowCounter<rows; rowCounter=rowCounter+1)
    {
      for(int colCounter = 0; colCounter<cols; colCounter++)
      {
        grid[rowCounter][colCounter] = new StewsCell(rowCounter, colCounter);
      }
    }
    int mineCounter = 0;
    while( mineCounter< numMines){
      currentRow = random.nextInt(rows);
      currentCol = random.nextInt(cols);
      if(!grid[currentRow][currentCol].isMine()) {
        grid[currentRow][currentCol].makeMine();
        mineCounter++;
      }
    }
    for(int rowCounter = 0; rowCounter<rows; rowCounter=rowCounter+1)
    {
      for(int colCounter = 0; colCounter<cols; colCounter++)
      {
        grid[rowCounter][colCounter].setNabors(countNabors(rowCounter, colCounter));
      }
    }
  }
  
  int countNabors(int theRow, int theCol){
    int naborCount=0;
    int checkRow= theRow-1;
    int checkCol= theCol-1;
    for(int check = 0; check < 8; check++){
      if(checkRow >= 0 && checkRow < rows && checkCol >= 0 && checkCol < cols){
        if(grid[checkRow][checkCol].isMine()) naborCount++;
      }
      if(check==0) checkRow++;
      if(check==1) checkRow++;
      if(check==2) checkCol++;
      if(check==3) checkCol++;
      if(check==4) checkRow--;
      if(check==5) checkRow--;
      if(check==6) checkCol--;
    }
    return naborCount;
  }
  
  public int getNumRows(){
    return rows;}
  public int getNumCols(){ return cols;}
  public int getNumMines() { return mines;}
  public int getNumFlags(){
    int numFlags=0;
    for(int rowCounter = 0; rowCounter<rows; rowCounter=rowCounter+1)
    {
      for(int colCounter = 0; colCounter<cols; colCounter++)
      {
        if(grid[rowCounter][colCounter].isFlagged()) numFlags++;
      }
    }
    return numFlags;
  }
  public int getElapsedSeconds(){
    long stoptime = System.currentTimeMillis();
    return (int) ((stoptime - starttime) * .001);}
  
  public Cell getCell(int theRow, int theCol){ return grid[theRow][theCol];}
  
  public void stepOnCell(int theRow, int theCol){
    grid[theRow][theCol].makeVis();
    if(grid[theRow][theCol].isMine()) dead=true;
    if(grid[theRow][theCol].getNeighborMines()==0)
    {
      int checkRow= theRow-1;
      int checkCol= theCol -1;
      for(int check = 0; check < 8; check++){
        if(checkRow >= 0 && checkRow < rows && checkCol >= 0 && checkCol < cols && !grid[checkRow][checkCol].isVisible()){
          stepOnCell(checkRow, checkCol);
          //System.out.println(" " + checkRow + " " + checkCol);
        }
        if(check==0) checkRow++;
        if(check==1) checkRow++;
        if(check==2) checkCol++;
        if(check==3) checkCol++;
        if(check==4) checkRow--;
        if(check==5) checkRow--;
        if(check==6) checkCol--;
      } 
    }
    
    if(help) helper(theRow, theCol);
  }
  
  public void placeOrRemoveFlagOnCell(int theRow, int theCol){
    grid[theRow][theCol].flag();
    /*if(help){
      int checkRow = theRow -1;
      int checkCol = theCol -1;
      for(int check=0; check<8; check++){
        if(checkRow >= 0 && checkRow < rows && checkCol >= 0 && checkCol < cols && grid[checkRow][checkCol].isVisible()){
          stepOnCell(checkRow, checkCol);
          break;
        }
        
        if(check==0) checkRow++;
        if(check==1) checkRow++;
        if(check==2) checkCol++;
        if(check==3) checkCol++;
        if(check==4) checkRow--;
        if(check==5) checkRow--;
        if(check==6) checkCol--;
      }
    }*///uncomment this for a helper that runs when you flag, but if you flag a non-mine you will die
    
  }
  
  public boolean isGameStarted() { return gameStarted;}
  public boolean isGameOver() { if(dead || gameWon) return true; else return false;}
  public boolean isGameWon() {
    int visCount=0;
    for(int rowCount=0; rowCount<rows; rowCount++){
      for(int colCount=0; colCount< cols; colCount++){
        if(grid[rowCount][colCount].isVisible() && !grid[rowCount][colCount].isMine()){
          visCount++;
      //    System.out.println("" + visCount);
        }
      }
    }
    if(visCount==(rows*cols - mines)) gameWon=true;
    return gameWon;
  }
  
  public boolean isPlayerDead() {return dead;}
  
  void helper(int theRow, int theCol){
    if(countNaborFlags(theRow, theCol)== grid[theRow][theCol].getNeighborMines()){
      int checkRow = theRow-1;
      int checkCol = theCol-1;
      for(int check = 0; check < 8; check++){
        if(checkRow >= 0 && checkRow < rows && checkCol >= 0 && checkCol < cols ){
          if(!grid[checkRow][checkCol].isFlagged() && !grid[checkRow][checkCol].isVisible()) 
            stepOnCell(checkRow, checkCol);
        }
        
        if(check==0) checkRow++;
        if(check==1) checkRow++;
        if(check==2) checkCol++;
        if(check==3) checkCol++;
        if(check==4) checkRow--;
        if(check==5) checkRow--;
        if(check==6) checkCol--;
      }
    }
  }
  
  int countNaborFlags(int theRow, int theCol){
    int checkRow= theRow -1;
    int checkCol = theCol -1;
    int flagCount= 0;
    for(int check = 0; check < 8; check++){
      if(checkRow >= 0 && checkRow < rows && checkCol >= 0 && checkCol < cols ){
        if(grid[checkRow][checkCol].isFlagged()) flagCount++;
      }
      
      if(check==0) checkRow++;
      if(check==1) checkRow++;
      if(check==2) checkCol++;
      if(check==3) checkCol++;
      if(check==4) checkRow--;
      if(check==5) checkRow--;
      if(check==6) checkCol--;
    } 
    return flagCount;
  }
  
  public void helperOnOff()
  {
    help=!help;
  }
  public boolean isHelping(){
    return help;}
  
  
  
  
}
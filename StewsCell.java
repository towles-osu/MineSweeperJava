class StewsCell implements Cell
{
  private boolean mine;
  private boolean flagged;
  private boolean visible;
  private int row;
  private int col;
  private int numNabors;
  
  
  StewsCell(int theRow, int theCol)
  {
    mine=false;
    flagged =false;
    visible=false;
    row=theRow;
    col=theCol;
  }
  
  void makeMine(){
    mine=true;
  }
  void flag(){flagged=!flagged;}
  void makeVis(){visible=true;}
  void setNabors(int nabors) { numNabors=nabors;}
  
  public int getRow() {return row;}
  public int getCol() {return col;}
  public boolean isVisible() {return visible;}
  public boolean isFlagged() {return flagged;}
  public boolean isMine() {return mine;}
  public int getNeighborMines() {return numNabors;}
  
  
  
}
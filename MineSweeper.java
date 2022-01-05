class MineSweeper
{
  public static void main(String[] args) 
  {
    MineModel mineModel = new StewsMineModel();
    
    MineView mineView = new MineView(mineModel, 600, 400);
  }
}
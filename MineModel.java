/** A MineModel stores and controls the state of a Mine Sweeper game.
 */

interface MineModel
{
  /** Starts a new game of Mine Sweeper.  This method creates a field of the indicated size,
   * initializes it, and places the indicated number of mines on the field.
   * @param numRows the number of rows for the field
   * @param numCols the number of columns for the field
   * @param numMines the number of mines to place in the field
   */
  public void newGame(int numRows, int numCols, int numMines);
  
  /** Returns the number of rows in the field. */
  public int getNumRows();
  
  /** Returns the number of columns in the field. */
  public int getNumCols();
  
  /** Returns the total number of mines in the field. */
  public int getNumMines();
  
  /** Returns the number of flags that have been placed in the field. */
  public int getNumFlags();
  
  /** Returns the number of seconds that have elapsed since this game started. */
  public int getElapsedSeconds();
  
  /** Returns the Cell in the field at the given coordinates.
   * @param row the row number of the cell [precondition: <code> 0 <= row < getNumRows()</code>]
   * @param col the column number of the cell [precondition: <code> 0 <= col < getNumCols()</code>]
   * @return a valid, non-null Cell object
   */
  public Cell getCell(int row, int col);
  
  /** Called when the player has stepped onto the cell at the given coordinates.
   * This cell is now made visible.  If it is a mine, the player is dead and the game is over.
   * If it is a safe (non-mine) cell with no neighboring mines (a zero cell), then every cell
   * that can be reached from this one by only stepping on zero cells should be made visible as well.
   * @param row the row number of the cell [precondition: <code> 0 <= row < getNumRows()</code>]
   * @param col the column number of the cell [precondition: <code> 0 <= col < getNumCols()</code>]
   */
  public void stepOnCell(int row, int col);
  
  /** Called when the player wants to change the flagged status of a cell.  
   * If the indicated cell has no flag, then place a flag there.
   * If the indicated cell already has a flag, then remove it.
   * Note that it is safe to place a flag on any cell, even if it has a mine.
   * @param row the row number of the cell [precondition: <code> 0 <= row < getNumRows()</code>]
   * @param col the column number of the cell [precondition: <code> 0 <= col < getNumCols()</code>]
   */
  public void placeOrRemoveFlagOnCell(int row, int col);
  
  /** Returns true if a game was started, whether or not it has ended.  
   * Returns false only if no game has ever been started.
   */
  public boolean isGameStarted();
  
  /** Returns true if the current game has ended.  
   * Returns false if the current game is running.
   * If the game hasn't started, then the value returned by this method is not defined.
   */
  public boolean isGameOver();
  
  /** Returns true if the player is dead, because they stepped on a mine.
   * Returns false if the player has not yet stepped on a mine.
   */
  public boolean isPlayerDead();
  
  /** Returns true if the player has won the current game, by exposing every non-mine cell.  
   * Returns false if player hasn't won (either the game hasn't started, it is still going, or the player is dead).
   */
  public boolean isGameWon();
  
  //////STEW CODE
  public void helperOnOff();
  public boolean isHelping();
  
} 

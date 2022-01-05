/** A MineView displays a graphical user interface for the Mine Sweeper game.
 * It relies on a separate MineModel to manage the state of the game itself.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MineView
{
  // set debug to true to see all cells, not just the visible ones
  private boolean debug = false;
  
  // these are the initial values for the three text fields
  private int defaultNumRows = 10;
  private int defaultNumCols = 20;
  private int defaultNumMines = 20;
  
  private JFrame mainFrame;
  private GamePanel gamePanel;
  private ControlPanel controlPanel;
  private MineModel mineModel;

  MineView(MineModel mineModel, int width, int height)
  {
    this.mineModel = mineModel;
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    gamePanel = new GamePanel();
    controlPanel = new ControlPanel();
    mainPanel.add(gamePanel, BorderLayout.CENTER);
    mainPanel.add(controlPanel, BorderLayout.SOUTH);
    
    mainFrame = new JFrame("Minesweeper");
    mainFrame.getContentPane().add(mainPanel);
    mainFrame.setSize(width, height);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////
  /** The ControlPanel is the panel with the New Game button and the text fields for configuring the game. */
  
  private class ControlPanel extends JPanel
  {
    private JTextField rowsField, colsField, minesField;
    private JLabel flagsLabel, timeLabel;
    //STEW CODE
    private JLabel helpLabel;
    //STEW CODE OVER
    private javax.swing.Timer timer;
    
    ControlPanel() 
    {
      setLayout(new FlowLayout());
      JButton newGameButton = new JButton("New Game");
      newGameButton.addActionListener(new NewGameListener());
      newGameButton.setMnemonic(KeyEvent.VK_N);
      add(newGameButton);
//LOOK HERE -> STEW CODE
      JButton helpButton = new JButton("Helper On/Off");
      helpButton.addActionListener(new HelpListener());
      helpButton.setMnemonic(KeyEvent.VK_H);
      add(helpButton);
 ///////STEW CODE OVER
      rowsField = new JTextField(defaultNumRows+"", 4);
      colsField = new JTextField(defaultNumCols+"", 4);
      minesField = new JTextField(defaultNumMines+"", 4);
      add(new JLabel("Rows: "));
      add(rowsField);
      add(new JLabel("Cols: "));
      add(colsField);
      add(new JLabel("Mines: "));
      add(minesField);
      add(new JLabel("Flags: "));
      flagsLabel = new JLabel("0    ");
      add(flagsLabel);
      timeLabel = new JLabel(" 0:00");
      add(new JLabel("Time: "));
      add(timeLabel);
      helpLabel = new JLabel("Off");
      add(new JLabel("Help is "));
      add(helpLabel);
      
      timer = new javax.swing.Timer(1000, new TimerListener());
      timer.start();
    }
    
    
    //////////////////STEW CODE STEW CODE STEW CODE STEW CODE STEW CODE
    
    class HelpListener implements ActionListener
    {
      public void actionPerformed(ActionEvent e)
      {
        mineModel.helperOnOff();
        if(mineModel.isHelping())
        helpLabel.setText("On");
        else
        helpLabel.setText("Off");
      }
    }
    
    
    ////////////////STEW CODE OVER STEW CODE OVER STEW CODE OVER STEW CODE OVER STEW CODE OVER
    
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /** NewGameListener is used when the New Game button is clicked. */
    
    class NewGameListener implements ActionListener
    {
      public void actionPerformed(ActionEvent event) 
      {
        try {
          int numRows, numCols, numMines;
          numRows = Integer.parseInt(rowsField.getText());
          numCols = Integer.parseInt(colsField.getText());
          numMines = Integer.parseInt(minesField.getText());
          mineModel.newGame(numRows, numCols, numMines);
          gamePanel.calculateCellSize();
          gamePanel.repaint();
        } 
        catch (NumberFormatException error) 
          // parseInt throws this exception when a non-number is entered in one of the text fields
        {
          JOptionPane.showMessageDialog(mainFrame, "That was not an integer: " + error.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }  // class NewGameListener
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /** The timer is used to update the display of the current time and the number of flags placed. */
    
    class TimerListener implements ActionListener
    {
      private java.text.DecimalFormat twoDigitFormat = new java.text.DecimalFormat("00");
      
      public void actionPerformed(ActionEvent event) 
      {
        if (mineModel.isGameStarted() && ! mineModel.isGameOver()) 
        {
          int elapsedTime = mineModel.getElapsedSeconds();
          int minutes = elapsedTime / 60;
          int seconds = elapsedTime % 60;
          timeLabel.setText(minutes + ":" + twoDigitFormat.format(seconds));
          flagsLabel.setText(mineModel.getNumFlags() + "");
        }
      }
    }
    
    
  } // class ControlPanel
  
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /* The GamePanel is where the field is drawn on the screen. */
  
  private class GamePanel extends JPanel
  {
    private int panelHeight, panelWidth;  // size of the entire game panel
    private int rowHeight, colWidth;      // size of each cell
    private int offsetX, offsetY;         // offset between the northwest corner of the panel
                                          // and the northwest corner of the upper-left cell
    
    GamePanel()
    {
      addMouseListener(new MineMouseListener());
      addComponentListener(new GamePanelComponentListener());
    }
    
    /* Calculates the dimensions of the cells in the field of the game, 
     * based on the size of the game panel, and the number of rows and columns in the field.
     * This method is called whenever the field is resized.
     */
    void calculateCellSize()
    {
      if (! mineModel.isGameStarted()) return;
      Dimension d = getSize();
      panelWidth = d.width;
      panelHeight = d.height;
      rowHeight = panelHeight / mineModel.getNumRows();
      colWidth = panelWidth / mineModel.getNumCols();
      offsetX = (panelWidth - colWidth*mineModel.getNumCols()) / 2;
      offsetY = (panelHeight - rowHeight*mineModel.getNumRows()) / 2;
    }
    
    /* paintComponent re-draws the field */
    
    protected void paintComponent(Graphics pen)
    {
      // don't do any drawing if the game hasn't started yet
      if (! mineModel.isGameStarted()) return;

      // make the background black
      pen.setColor(Color.BLACK);
      pen.fillRect(0, 0, panelWidth, panelHeight);
            
      int numRows = mineModel.getNumRows();
      int numCols = mineModel.getNumCols();
      
      // draw every cell
      for (int r=0; r<numRows; r++) {
        for (int c=0; c<numCols; c++) {
          Cell cell = mineModel.getCell(r,c);
          drawCell(pen, cell);
        }
      }
      
    } // paintComponent()
    
    private void drawCell(Graphics pen, Cell cell) 
    {   
      // compute x and y coordinates of the north-west corner of the cell
      int nw_x = cell.getCol() * colWidth + offsetX;
      int nw_y = cell.getRow() * rowHeight + offsetY;
      
      // draw a border around the cell
      pen.setColor(Color.BLUE);
      pen.drawRect(nw_x, nw_y, colWidth, rowHeight);
      
      if (cell.isFlagged()) 
      {
        // draw an orange flag on a white background
        pen.setColor(Color.WHITE);
        pen.fillRect(nw_x+1, nw_y+1, colWidth-2, rowHeight-2);
        int[] flag_x = new int[] {nw_x+colWidth/3, nw_x+colWidth/3, nw_x+2*colWidth/3, nw_x+colWidth/3};
        int[] flag_y = new int[] {nw_y+5*rowHeight/6, nw_y+rowHeight/6, nw_y+2*rowHeight/6, nw_y+3*rowHeight/6};
        pen.setColor(Color.ORANGE);
        pen.fillPolygon(flag_x, flag_y, 4);
        pen.setColor(Color.BLACK);
        pen.drawPolygon(flag_x, flag_y, 4);
      } 
      else if (!debug && !cell.isVisible()) 
      {
        // unless we are debugging, draw non-visible squares in gray
        pen.setColor(Color.GRAY);
        pen.fillRect(nw_x+1, nw_y+1, colWidth-2, rowHeight-2);
      } 
      else if (cell.isMine()) 
      {
        // draw mines as red circles on a white background
        pen.setColor(Color.WHITE);
        pen.fillRect(nw_x+1, nw_y+1, colWidth-2, rowHeight-2);
        pen.setColor(Color.RED);
        int radius = Math.min(colWidth,rowHeight)/3;
        pen.fillOval(nw_x+colWidth/2-radius, nw_y+rowHeight/2-radius, 2*radius, 2*radius);
      } 
      else 
      {
        // draw the number of adjacent mines on a green background 
        //   (or a yellow background if we are debugging and this square is not yet visible)
        if (debug && !cell.isVisible()) 
          pen.setColor(Color.YELLOW);
        else
          pen.setColor(Color.GREEN);
        pen.fillRect(nw_x+1, nw_y+1, colWidth-2, rowHeight-2);
        pen.setColor(Color.BLACK);
        pen.setFont(pen.getFont().deriveFont(Font.BOLD, 14));
        pen.drawString(String.valueOf(cell.getNeighborMines()), nw_x + 3*colWidth/7, nw_y + 5*rowHeight/7);
      }
      
    }  // drawCell()
    
    /////////////////////////////////////////////////////////////////////////////////////////
    
    class MineMouseListener implements MouseListener 
    {
      public void mousePressed(MouseEvent event) 
      {
        if (!mineModel.isGameStarted() || mineModel.isGameOver()) return;
        int x = event.getX();
        int y = event.getY();
        if ( x < offsetX || y < offsetY) return;
        
        int row = (y-offsetY) / rowHeight;
        int col = (x-offsetX) / colWidth;
        if (row < 0 || row >= mineModel.getNumRows() || col < 0 || col >= mineModel.getNumCols()) return;
        
        int button = event.getButton();
        if (button == MouseEvent.BUTTON3 || event.isShiftDown()) // right-click or shift-click
        {  
          mineModel.placeOrRemoveFlagOnCell(row, col);
        }
        else if (button == MouseEvent.BUTTON1)              // left-click
        {
          mineModel.stepOnCell(row, col);      
        }
        
        repaint();
        
        if (mineModel.isPlayerDead()) {
          JOptionPane.showMessageDialog(mainFrame, "Boom!  You just stepped on a mine!  You're dead!");
        }
        else if (mineModel.isGameWon()) {
          JOptionPane.showMessageDialog(mainFrame, "Congratulations!  You won the game!");
        }
      }
      
      public void mouseClicked(MouseEvent event) { }
      public void mouseReleased(MouseEvent event) { }
      public void mouseEntered(MouseEvent event) { }
      public void mouseExited(MouseEvent event) { }
    }  // class MineMouseListener
    
    //////////////////////////////////////////////////////////////////////////////////////////
    
    class GamePanelComponentListener extends ComponentAdapter
    {
      public void componentResized(ComponentEvent event) 
      {
        // when the game panel is resized, adjust the size of the cells
        calculateCellSize();
      }
    } // class GamePanelComponentListener
    
    /////////////////////////////////////////////////////////////////////////////////////////
    
  } // class GamePanel
  
} // class MineView
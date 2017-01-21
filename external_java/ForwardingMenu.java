import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import com.mysql.jdbc.Statement;

public class ForwardingMenu extends JPanel {

  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

  private static final String FORW_BUTTON_LABEL = "Forward";
  
  private static final String NOFORW_BUTTON_LABEL = "No Forwarding";

  private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Users";

  private JLabel sourceLabel;

  private JList sourceList;

  private SortedListModel sourceListModel;

  private JButton addButton;
  
  private JButton removeButton;

  private JLabel currentLabel;
  

  public ForwardingMenu() throws SQLException {
    initScreen();
  }

  public String getSourceChoicesTitle() {
    return sourceLabel.getText();
  }

  public void setSourceChoicesTitle(String newValue) {
    sourceLabel.setText(newValue);
  }


  public void clearSourceListModel() {
    sourceListModel.clear();
  }


  public void addSourceElements(ListModel newValue) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(ListModel newValue) {
    clearSourceListModel();
    addSourceElements(newValue);
  }


  private void fillListModel(SortedListModel model, ListModel newValues) {
    int size = newValues.getSize();
    for (int i = 0; i < size; i++) {
      model.add(newValues.getElementAt(i));
    }
  }

  public void addSourceElements(Object newValue[]) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(Object newValue[]) {
    clearSourceListModel();
    addSourceElements(newValue);
  }

  private void fillListModel(SortedListModel model, Object newValues[]) {
    model.addAll(newValues);
  }

  public Iterator sourceIterator() {
    return sourceListModel.iterator();
  }

  public void setSourceCellRenderer(ListCellRenderer newValue) {
    sourceList.setCellRenderer(newValue);
  }

  public ListCellRenderer getSourceCellRenderer() {
    return sourceList.getCellRenderer();
  }

  public int getVisibleRowCount() {
    return sourceList.getVisibleRowCount();
  }

  public Color getSelectionBackground() {
    return sourceList.getSelectionBackground();
  }

  public Color getSelectionForeground() {
    return sourceList.getSelectionForeground();
  }

  private void clearSourceSelected() {
    Object selected[] = sourceList.getSelectedValues();
    for (int i = selected.length - 1; i >= 0; --i) {
      sourceListModel.removeElement(selected[i]);
    }
    sourceList.getSelectionModel().clearSelection();
  }

  private void initScreen() throws SQLException {
	  
    setBorder(BorderFactory.createEtchedBorder());
    setLayout(new GridBagLayout());
    sourceLabel = new JLabel(DEFAULT_SOURCE_CHOICE_LABEL);
    sourceListModel = new SortedListModel();
    sourceList = new JList(sourceListModel);
    add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    add(new JScrollPane(sourceList), new GridBagConstraints(0, 1, 1, 5, .5,
        1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));

    addButton = new JButton(FORW_BUTTON_LABEL);
    add(addButton, new GridBagConstraints(1, 2, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addButton.addActionListener(new AddListener());

    removeButton = new JButton(NOFORW_BUTTON_LABEL);
    add(removeButton, new GridBagConstraints(1, 4, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    removeButton.addActionListener(new RemoveListener());
    currentLabel = new JLabel();
    add(currentLabel, new GridBagConstraints(1, 6, 1, 2, 0, .25,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));
    setCurrentForw();
  }

  public static void main(String args[]) throws SQLException {
    JFrame f = new JFrame("Dual List Box Tester");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ForwardingMenu fmenu = new ForwardingMenu();
    try{  
	    Class.forName("com.mysql.jdbc.Driver");  
	    Connection con=DriverManager.getConnection(  
	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	   
	    
	    

	    String userName ="kanell21";
	    
	    PreparedStatement stmt = null;
	    String selectQuery="SELECT reg_username FROM soft_eng_database.Registrations WHERE "
	    		+ "reg_username != ?";
	    stmt = con.prepareStatement(selectQuery);
	    stmt.setString(1,userName);
	    ResultSet rs = stmt.executeQuery();
	    
	    
	    while(rs.next()){  
	    	
	    	fmenu.addSourceElements(new String[] {(rs.getString(1))});
	    
	    	}
	    
	 }
    catch(Exception e)
    {
    	
    	
    	
    
    }
    
    f.getContentPane().add(fmenu, BorderLayout.CENTER);
    f.setSize(400, 300);
    f.setVisible(true);
  }

  private class AddListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = sourceList.getSelectedValues();
      if(selected.length==0) return;
      System.out.println("You want to forward to "+selected[0].toString());
      
      
      try{  
  	    //Class.forName("com.mysql.jdbc.Driver");  
  	    Connection con=DriverManager.getConnection(  
  	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
  	   
  	    
  	    

  	    String userName ="kanell21";
  	    
  	    PreparedStatement remove_previous_forward = null;
	    String updateQuery="DELETE FROM soft_eng_database.Forwarding WHERE Username = ?";
	    remove_previous_forward = con.prepareStatement(updateQuery);
	    remove_previous_forward.setString(1,userName);
	    remove_previous_forward.executeUpdate();
  	    PreparedStatement add_forward = null;
  	    updateQuery="INSERT INTO soft_eng_database.Forwarding VALUES (?,?);";
  	    add_forward = con.prepareStatement(updateQuery);
  	    add_forward.setString(1,userName);
  	    add_forward.setString(2,selected[0].toString());
  	  	add_forward.executeUpdate();
  	  	setCurrentForw();
  	        
     }
      catch(SQLException e1)
     {
    	  System.err.println("Message: " + e1.getMessage());
     
     }
     }
  }
  
  private class RemoveListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	      
	      System.out.println("You want to stop forwarding");
	      
	      
	      try{   
	  	    Connection con=DriverManager.getConnection(  
	  	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	  	   
	  	    String userName ="kanell21";
	  	    
	  	    PreparedStatement remove_previous_forward = null;
		    String updateQuery="DELETE FROM soft_eng_database.Forwarding WHERE Username = ?";
		    remove_previous_forward = con.prepareStatement(updateQuery);
		    remove_previous_forward.setString(1,userName);
		    remove_previous_forward.executeUpdate();
		    setCurrentForw();
	  	        
	     }
	      catch(SQLException e1)
	     {
	    	  System.err.println("Message: " + e1.getMessage());
	     
	     }
	      
	     }
	  }
  
  private void setCurrentForw() throws SQLException{
	  String userName = "kanell21";
	  Connection con=DriverManager.getConnection(  
	  "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	  PreparedStatement stmt = null;
	  String selectQuery="SELECT Forwarding_user FROM soft_eng_database.Forwarding WHERE "
	    		+ "Username = ?";
	  stmt = con.prepareStatement(selectQuery);
	  stmt.setString(1,userName);
	  ResultSet rs = stmt.executeQuery();
	  if(rs.next()){
		  currentLabel.setText("Currently forwarding to "+rs.getString(1));
	  }
	  else
		  currentLabel.setText("Currently not forwarding");
	  return ;
  }

}

class SortedListModel extends AbstractListModel {

  SortedSet model;

  public SortedListModel() {
    model = new TreeSet();
  }

  public int getSize() {
    return model.size();
  }

  public Object getElementAt(int index) {
    return model.toArray()[index];
  }

  public void add(Object element) {
    if (model.add(element)) {
      fireContentsChanged(this, 0, getSize());
    }
  }

  public void addAll(Object elements[]) {
    Collection c = Arrays.asList(elements);
    model.addAll(c);
    fireContentsChanged(this, 0, getSize());
  }

  public void clear() {
    model.clear();
    fireContentsChanged(this, 0, getSize());
  }

  public boolean contains(Object element) {
    return model.contains(element);
  }

  public Object firstElement() {
    return model.first();
  }

  public Iterator iterator() {
    return model.iterator();
  }

  public Object lastElement() {
    return model.last();
  }

  public boolean removeElement(Object element) {
    boolean removed = model.remove(element);
    if (removed) {
      fireContentsChanged(this, 0, getSize());
    }
    return removed;
  }
}

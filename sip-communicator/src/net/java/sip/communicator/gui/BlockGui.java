package net.java.sip.communicator.gui;

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

import net.java.sip.communicator.sip.CommunicationsException;
import net.java.sip.communicator.sip.SipManager;



public class BlockGui extends JPanel {

	  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

	  private static final String ADD_BUTTON_LABEL = "Add";

	  private static final String REMOVE_BUTTON_LABEL = "Remove";

	  private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Blocked";

	  private static final String DEFAULT_DEST_CHOICE_LABEL = "Unblocked";

	  private JLabel sourceLabel;

	  private JList sourceList;

	  private SortedListModel sourceListModel;

	  private JList destList;

	  private SortedListModel destListModel;

	  private JLabel destLabel;

	  private JButton addButton;
	  private String Username;
	  private JButton removeButton;
	  private SipManager manager;
	  public BlockGui(SipManager manager,String username) {
		  this.Username=username;
		this.manager=manager;
	    initScreen();
	  }

	  public String getSourceChoicesTitle() {
	    return sourceLabel.getText();
	  }

	  public void setSourceChoicesTitle(String newValue) {
	    sourceLabel.setText(newValue);
	  }

	  public String getDestinationChoicesTitle() {
	    return destLabel.getText();
	  }

	  public void setDestinationChoicesTitle(String newValue) {
	    destLabel.setText(newValue);
	  }

	  public void clearSourceListModel() {
	    sourceListModel.clear();
	  }

	  public void clearDestinationListModel() {
	    destListModel.clear();
	  }

	  public void addSourceElements(ListModel newValue) {
	    fillListModel(sourceListModel, newValue);
	  }

	  public void setSourceElements(ListModel newValue) {
	    clearSourceListModel();
	    addSourceElements(newValue);
	  }

	  public void addDestinationElements(ListModel newValue) {
	    fillListModel(destListModel, newValue);
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

	  public void addDestinationElements(Object newValue[]) {
	    fillListModel(destListModel, newValue);
	  }

	  private void fillListModel(SortedListModel model, Object newValues[]) {
	    model.addAll(newValues);
	  }

	  public Iterator sourceIterator() {
	    return sourceListModel.iterator();
	  }

	  public Iterator destinationIterator() {
	    return destListModel.iterator();
	  }

	  public void setSourceCellRenderer(ListCellRenderer newValue) {
	    sourceList.setCellRenderer(newValue);
	  }

	  public ListCellRenderer getSourceCellRenderer() {
	    return sourceList.getCellRenderer();
	  }

	  public void setDestinationCellRenderer(ListCellRenderer newValue) {
	    destList.setCellRenderer(newValue);
	  }

	  public ListCellRenderer getDestinationCellRenderer() {
	    return destList.getCellRenderer();
	  }

	  public void setVisibleRowCount(int newValue) {
	    sourceList.setVisibleRowCount(newValue);
	    destList.setVisibleRowCount(newValue);
	  }

	  public int getVisibleRowCount() {
	    return sourceList.getVisibleRowCount();
	  }

	  public void setSelectionBackground(Color newValue) {
	    sourceList.setSelectionBackground(newValue);
	    destList.setSelectionBackground(newValue);
	  }

	  public Color getSelectionBackground() {
	    return sourceList.getSelectionBackground();
	  }

	  public void setSelectionForeground(Color newValue) {
	    sourceList.setSelectionForeground(newValue);
	    destList.setSelectionForeground(newValue);
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

	  private void clearDestinationSelected() {
	    Object selected[] = destList.getSelectedValues();
	    for (int i = selected.length - 1; i >= 0; --i) {
	      destListModel.removeElement(selected[i]);
	    }
	    destList.getSelectionModel().clearSelection();
	  }
	  private void clearall() {
		    destListModel.clear();
		    sourceListModel.clear();
		   
		  }

	  private void initScreen() {
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

	    addButton = new JButton(ADD_BUTTON_LABEL);
	    add(addButton, new GridBagConstraints(1, 2, 1, 2, 0, .25,
	        GridBagConstraints.CENTER, GridBagConstraints.NONE,
	        EMPTY_INSETS, 0, 0));
	    addButton.addActionListener(new AddListener());
	    removeButton = new JButton(REMOVE_BUTTON_LABEL);
	    add(removeButton, new GridBagConstraints(1, 4, 1, 2, 0, .25,
	        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
	            0, 5, 0, 5), 0, 0));
	    removeButton.addActionListener(new RemoveListener());

	    destLabel = new JLabel(DEFAULT_DEST_CHOICE_LABEL);
	    destListModel = new SortedListModel();
	    destList = new JList(destListModel);
	    add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
	        GridBagConstraints.CENTER, GridBagConstraints.NONE,
	        EMPTY_INSETS, 0, 0));
	    add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 5, .5,
	        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	        EMPTY_INSETS, 0, 0));
	    setVisible(true);
	    
	  }
/*
	  public static void main(String args[]) {
	    JFrame f = new JFrame("Dual List Box Tester");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    BlockGui dual = new BlockGui();
	    try{  
		    Class.forName("com.mysql.jdbc.Driver");  
		    Connection con=DriverManager.getConnection(  
		    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
		   		    
		    

		    String userName ="kanell21";
		    
		    PreparedStatement select_login= null;
		    String selectQuery="select Blocked from soft_eng_database.Blocking where blocker = ? ";
		    select_login = con.prepareStatement(selectQuery);
		    select_login.setString(1,userName);
		    
		    
		    
		   // System.out.println(userName);
		    //System.out.println(new String(password));
		    
		    
		    int exists=0;	 
		    ResultSet rs;
		    rs=select_login.executeQuery();
		    
		    while(rs.next()){  
		    	
		    	dual.addSourceElements(new String[] {(rs.getString(1))});
		    
		    	}
		    
		    
		    ResultSet rs2;
		    selectQuery="SELECT reg_username as Username "+
		    		    "FROM soft_eng_database.Registrations as R "+
		    		    "where not exists(select * from soft_eng_database.Blocking where Blocker= ? and Blocked=R.reg_username ) and reg_username!= ?" ;
		    		
		    PreparedStatement unblocked_st= null;
		    unblocked_st= con.prepareStatement(selectQuery);
		    unblocked_st.setString(1,userName);
		    unblocked_st.setString(2,userName);
		    //System.out.println(selectQuery);
		    rs2=unblocked_st.executeQuery();
		    while(rs2.next()){  
		    	//System.out.println(rs2.getString(1));
		    	dual.addDestinationElements(new String[] {rs2.getString(1)});
		    
		    	}
		    
		 }
	    catch(Exception e)
	    {
	    	
	    	
	    	
	    
	    }
	    
	    f.getContentPane().add(dual, BorderLayout.CENTER);
	    f.setSize(400, 300);
	    f.setVisible(true);
	  }
	  */

	  private class AddListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Object select[]= destList.getSelectedValues();
			if(destList.getSelectedValues()==null) return;
			else
				try {
					manager.addBlock(Username,select[0].toString());
					clearall();

					manager.getBlocked(Username);
				} catch (CommunicationsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		  /*
	    public void actionPerformed(ActionEvent e) {
	      Object selected[] = destList.getSelectedValues();
	      System.out.println(selected[0].toString());
	      
	      
	      try{  
	  	    //Class.forName("com.mysql.jdbc.Driver");  
	  	    Connection con=DriverManager.getConnection(  
	  	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	  	   
	  	    
	  	    

	  	    String userName ="kanell21";
	  	    
	  	    for(Object obj:selected){
	  	    PreparedStatement add_block= null;
	  	    String updateQuery="INSERT INTO soft_eng_database.Blocking VALUES (?,?);";
	  	    add_block = con.prepareStatement(updateQuery);
	  	    add_block.setString(1,userName);
	  	    add_block.setString(2,obj.toString());
	  	    add_block.executeUpdate();
	  	    }
	  	    
	  	    
	  	    
	  	   // System.out.println(userName);
	  	    //System.out.println(new String(password));
	  	    
	      
	     }
	      catch(SQLException e1)
	     {
	    	  System.err.println("Message: " + e1.getMessage());
	     
	     }
	      addSourceElements(selected);
	      clearDestinationSelected();
	     }
	     */
	  }

	  private class RemoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Object select[]= sourceList.getSelectedValues();
			if(sourceList.getSelectedValues()==null) return;
			else
				try {
					manager.removeBlock(Username,select[0].toString());
					clearall();

					manager.getBlocked(Username);
				} catch (CommunicationsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		  /*
	    public void actionPerformed(ActionEvent e) {
	      Object selected[] = sourceList.getSelectedValues();
	      try{  
	    	    //Class.forName("com.mysql.jdbc.Driver");  
	    	    Connection con=DriverManager.getConnection(  
	    	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	    	   
	    	    
	    	    

	    	    String userName ="kanell21";
	    	    
	    	    for(Object obj:selected){
	    	    PreparedStatement add_block= null;
	    	    String updateQuery="DELETE from soft_eng_database.Blocking where Blocker= ? and Blocked= ? ";
	    	    add_block = con.prepareStatement(updateQuery);
	    	    add_block.setString(1,userName);
	    	    add_block.setString(2,obj.toString());
	    	    add_block.executeUpdate();
	    	    }
	    	    
	    	    
	    	    
	    	   // System.out.println(userName);
	    	    //System.out.println(new String(password));
	    	    
	        
	       }
	        catch(SQLException e1)
	       {
	      	  System.err.println("Message: " + e1.getMessage());
	       
	       }
	      addDestinationElements(selected);
	      clearSourceSelected();
	    }
	  }
	  */
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
}

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



public class ForwardGui extends JPanel {

	  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

	  private static final String ADD_BUTTON_LABEL = "Forward";

	  private static final String REMOVE_BUTTON_LABEL = "No Forward";

	  private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Registrations";

	  private static final String DEFAULT_DEST_CHOICE_LABEL = "Unblocked";

	  private JLabel sourceLabel;
	  
	  private JLabel forwardee;;

	  private JList sourceList;

	  private SortedListModel sourceListModel;

	  private JButton addButton;
	  private String Username;
	  private JButton removeButton;
	  private SipManager manager;
	  public ForwardGui(SipManager manager,String username) {
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

	  public void setForwardee(String forwardee){
		  this.forwardee.setText("Forwarding to "+forwardee);
	  }
	  private void clearall() {
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

	    forwardee = new JLabel();
	    add(forwardee, new GridBagConstraints(1, 6, 1, 2, 0, .25,
		        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
		            0, 5, 0, 5), 0, 0));
	    
	    setVisible(true);
	    
	  }


	  private class AddListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Object select[]= sourceList.getSelectedValues();
			if(sourceList.getSelectedValues()==null) return;
			else
				try {
					manager.addForward(Username, select[0].toString());
					Thread.sleep(400);
					clearall();
					manager.getForwardingList(Username);
				} catch (CommunicationsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		  
	  }

	  private class RemoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
				try {
					manager.addForward(Username, null);
					Thread.sleep(400);
					clearall();
					manager.getForwardingList(Username);
				} catch (CommunicationsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
}

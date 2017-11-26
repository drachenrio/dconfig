/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * DConfig - Free Dynamic Configuration Toolkit
 * Copyright (C) 2006, 2007 Jonathan Luo
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package org.moonwave.dconfig.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.ui.RefineryUtilities;
import org.moonwave.dconfig.model.ConnectionInfo;
import org.moonwave.dconfig.ui.ckboxtree.CheckTreeCellEditor;
import org.moonwave.dconfig.ui.ckboxtree.CheckTreeNodeRenderer;
import org.moonwave.dconfig.ui.ckboxtree.MouseSelectionListener;
import org.moonwave.dconfig.ui.ckboxtree.TreeLabelCellEditor;
import org.moonwave.dconfig.ui.listener.TableCellEditorListener;
import org.moonwave.dconfig.ui.listener.TableColumnSelectionListener;
import org.moonwave.dconfig.ui.listener.TableFocusListener;
import org.moonwave.dconfig.ui.listener.TableKeyListener;
import org.moonwave.dconfig.ui.listener.TableSelectionListener;
import org.moonwave.dconfig.ui.listener.TreeDragGestureListener;
import org.moonwave.dconfig.ui.listener.TreeDragSourceListener;
import org.moonwave.dconfig.ui.listener.TreeDropTargetListener;
import org.moonwave.dconfig.ui.listener.TreeFocusListener;
import org.moonwave.dconfig.ui.listener.TreeModelListenerImpl;
import org.moonwave.dconfig.ui.listener.TreeSelectionActionListener;
import org.moonwave.dconfig.ui.model.AppContext;
import org.moonwave.dconfig.ui.model.DConfigTable;
import org.moonwave.dconfig.ui.model.DConfigTreeModel;
import org.moonwave.dconfig.ui.model.LongText;
import org.moonwave.dconfig.ui.model.TableModelImpl;
import org.moonwave.dconfig.ui.util.AppInitializer;
import org.moonwave.dconfig.ui.util.AppProperties;
import org.moonwave.dconfig.ui.util.AppUtil;
import org.moonwave.dconfig.ui.util.ConnectionInfoUtil;
import org.moonwave.dconfig.ui.util.ImageUtil;
import org.moonwave.dconfig.ui.util.TreeUtil;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.util.DemoDataPopulator;
import org.moonwave.dconfig.util.DemoDbManager;


/**
 *
 * @author Jonathan Luo
 *
 */
public class CfgEditor extends javax.swing.JFrame {
    private static final Log log = LogFactory.getLog(CfgEditor.class);	

    public Status txtStatus;
    int width = 800;//626
    int height = 600;//422
    DragSource dragSource;
    DropTarget dropTarget;
    TreeDragSourceListener dragSourceListener;
    TreeDropTargetListener dropTargetListener;
    TreeDragGestureListener dragGestureListener;
    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private JTable table;
    private JSplitPane splitPane;    
    private JScrollPane treeView;
    private JScrollPane tablePane;
    private JToolBar toolbar;
    
    public CfgEditor() {
    }
    
    public JTree getTree() {
        return tree;
    }
    public JTable getTable() {
        return table;
    }
    public JScrollPane getTablePane() {
        return tablePane;
    }
    public JToolBar getToolbar() {
        return toolbar;
    }
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    /** 
     */
    private void initComponents() {
        AppContext.appContext().setCfgEditor(this);
        ImageIcon appImageIcon = ImageUtil.createImageIcon("images/java-logo.gif", "logo");
        this.setIconImage((appImageIcon != null) ? appImageIcon.getImage() : null);

        //Make sure we have nice window decorations.
        javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
        setTitle(AppProperties.getInstance().getProperty("app.name") + " - disconnected");
        
        getContentPane().setLayout(new BorderLayout());
        
        //Create menu bar        
        setJMenuBar(MenuCreator.createMenuBar());
                
        //Create the toolbar
        toolbar = MenuCreator.createToolBar();
        getContentPane().add(toolbar, BorderLayout.PAGE_START);
        
        // -------------- Create tree and populate initial data ---------------
        rootNode = TreePopulator.createTree();
        tree = new AutoScrollingJTree(rootNode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        DConfigTreeModel treeModel = new DConfigTreeModel(rootNode);
        tree.setModel(treeModel);
                
        CheckTreeNodeRenderer treeRenderer = new CheckTreeNodeRenderer();
        tree.setCellRenderer(treeRenderer);        
        TreeLabelCellEditor labelEditor = new TreeLabelCellEditor();
        DefaultTreeCellEditor editor = new CheckTreeCellEditor(tree, treeRenderer, labelEditor);
        tree.setCellEditor(editor);

        TreePath rootPath = new TreePath(rootNode);
        tree.makeVisible(rootPath);
        tree.setSelectionPath(rootPath);
        tree.requestFocusInWindow();
        
        dragSourceListener = new TreeDragSourceListener();
        dropTargetListener = new TreeDropTargetListener();
        dragGestureListener = new TreeDragGestureListener(dragSourceListener);
        dragSource = new DragSource();
        dropTarget = new DropTarget(tree, dropTargetListener);
        dragSource.createDefaultDragGestureRecognizer(tree, 
                        java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE, 
                        dragGestureListener);
        
        tree.setDragEnabled(false);

        //Enable tool tips
        ToolTipManager.sharedInstance().registerComponent(tree);

        // set expand / collasp icons
        ImageIcon plusIcon = ImageUtil.createImageIcon("images/plus.png", "");
        ImageIcon minusIcon = ImageUtil.createImageIcon("images/minus.png", "");        
        BasicTreeUI ui = (BasicTreeUI)tree.getUI();
        ui.setCollapsedIcon(plusIcon);
        ui.setExpandedIcon(minusIcon);
                
        //Listen for when the selection changes.
        tree.setEditable(false);
        tree.addTreeSelectionListener(new TreeSelectionActionListener());
        tree.addFocusListener(new TreeFocusListener());
        tree.addMouseListener(new MouseSelectionListener(tree));
        tree.getModel().addTreeModelListener(new TreeModelListenerImpl());

        //Create tree view pane 
        treeView = new JScrollPane(tree);
        
        // -------------- Create table ----------------------------------------
        TableSorter sorter = new TableSorter(new TableModelImpl());
        table = new DConfigTable(sorter);
        TableColumn column = table.getColumnModel().getColumn(TableModelImpl.IDX_INHERITANCE);
        column.setMaxWidth(18);
        
        sorter.setTableHeader(table.getTableHeader());
        
        //jTable = table;
        table.addFocusListener(new TableFocusListener());
        table.addKeyListener(new TableKeyListener());
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getSelectionModel().addListSelectionListener(new TableColumnSelectionListener());
        //table.addMouseListener();
        //Set up tool tips for column headers.
        table.getTableHeader().setToolTipText("Click to specify sorting; Control-Click to specify secondary sorting");
                
        TableModelImpl model = (TableModelImpl) sorter.getTableModel();
        table.setDefaultEditor(LongText.class, new LongTextEditor());
        table.getDefaultEditor(String.class).addCellEditorListener(new TableCellEditorListener());
        //table.setDefaultEditor(model.getColumnClass(TableModelImpl.ATTRIBUTE_NAME_IDX), new TextEditor());
        BasicTableUI tableUI = (BasicTableUI) table.getUI();
        
        // -------------- Setup spliter ---------------------------------------
        tablePane = new JScrollPane(table);
        tablePane.setBackground(Color.WHITE);
        tablePane.setForeground(Color.WHITE);
        
        //jt1.setPreferredSize(new Dimension(400, 500));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, tablePane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
        
        splitPane.setBackground(Color.WHITE);
        splitPane.setForeground(Color.WHITE);
        
        // -------------- Create pop-up menu ----------------------------------
        MenuCreator.createTreePopupMenu();
        MenuCreator.createTablePopupMenu();
        
        getContentPane().setPreferredSize(new Dimension(width, height));
        getContentPane().setMinimumSize(new Dimension(200, 200));
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(createStatusPanel(), BorderLayout.PAGE_END);

        getContentPane().setBackground(Color.WHITE);
        getContentPane().setForeground(Color.WHITE);
        
        //
        TreeUtil.setSelectionPath(rootNode);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });        
    }
    
    private void formWindowClosed(java.awt.event.WindowEvent evt) 
    {
        if (AppState.isDemo())
            DemoDbManager.shutdown();        
    }                                 
    
    private JComponent createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        txtStatus = new Status();
        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border raisedbevel = BorderFactory.createRaisedBevelBorder(); // no
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);

        panel.add(txtStatus, BorderLayout.CENTER);
        panel.setBorder(loweredbevel);
        return panel;
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        CfgEditor frame = new CfgEditor();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.initComponents();

        frame.pack(); 
        frame.setVisible(true);
        AppContext.getTree().requestFocusInWindow();
        //centerFrameOnScreen(frame);

        RefineryUtilities.centerFrameOnScreen(frame);
        AppContext.appContext().getStateController().setInitialStates();
        // start default connection if any and succeeded, or launch the connection wizard
        ConnectionInfo connInfo = ConnectionInfoUtil.getDefaultConnectionInfo();
        
   }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String[] argv = args;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (AppInitializer.initialize((argv))) {
                    createAndShowGUI();
                    if (AppState.isDemo()) {
                        DemoDbManager.startup();
                        if (new DemoDataPopulator().populate()) {
                            if (TreePopulator.populateTree()) {
                                AppContext.setConnected(true);
                                if (AppState.isDemoDerby())
                                    AppUtil.setTitle("Demo Derby (10.1.3.1, Embedded)");
                                else if (AppState.isDemoHsqldb())
                                    AppUtil.setTitle("Demo Hsqldb (1.7.3.3, Embedded)");
                                else
                                    AppUtil.setTitle("Demo H2 Database (1.0, Embedded)");
                            }
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "An error occured which prevented the application from start. Please look at log file for details", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });
    }
}

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
package dcfgdemo;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.rave.web.ui.component.Body;
import com.sun.rave.web.ui.component.Button;
import com.sun.rave.web.ui.component.Checkbox;
import com.sun.rave.web.ui.component.Form;
import com.sun.rave.web.ui.component.Head;
import com.sun.rave.web.ui.component.Html;
import com.sun.rave.web.ui.component.Hyperlink;
import com.sun.rave.web.ui.component.ImageComponent;
import com.sun.rave.web.ui.component.Label;
import com.sun.rave.web.ui.component.Link;
import com.sun.rave.web.ui.component.Page;
import com.sun.rave.web.ui.component.PanelGroup;
import com.sun.rave.web.ui.component.PanelLayout;
import com.sun.rave.web.ui.component.StaticText;
import com.sun.rave.web.ui.component.Tab;
import com.sun.rave.web.ui.component.TabSet;
import com.sun.rave.web.ui.component.Table;
import com.sun.rave.web.ui.component.TableColumn;
import com.sun.rave.web.ui.component.TableRowGroup;
import com.sun.rave.web.ui.component.TextArea;
import com.sun.rave.web.ui.component.TextField;
import com.sun.rave.web.ui.model.SingleSelectOptionsList;
import dcfgdemo.DConfigTableDataProvider.Data;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.moonwave.dconfig.dao.CacheManager;
import org.moonwave.dconfig.dao.DConfigReader;
import org.moonwave.dconfig.dao.DConfigWriter;
import org.moonwave.dconfig.dao.LibInitializer;
import org.moonwave.dconfig.dao.springfw.DConfigAttributeMapper;
import org.moonwave.dconfig.dao.springfw.DConfigDataTypeMapper;
import org.moonwave.dconfig.dao.springfw.DConfigKeyMapper;
import org.moonwave.dconfig.dao.springfw.DataSourceManager;
import org.moonwave.dconfig.dao.springfw.QueryTemplate;
import org.moonwave.dconfig.model.DConfigAttribute;
import org.moonwave.dconfig.model.DConfigDataType;
import org.moonwave.dconfig.model.DConfigKey;
import org.moonwave.dconfig.util.AppState;
import org.moonwave.dconfig.util.Constants;
import org.moonwave.dconfig.util.DemoDataPopulator;
import org.moonwave.dconfig.util.DemoDbManager;
import org.moonwave.dconfig.util.Timer;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Jonathan Luo
 */
public class Page1 extends AbstractPageBean {
    private static final Log log = LogFactory.getLog(Page1.class);
    private TableColumn[] columns;

    // <editor-fold defaultstate="collapsed" desc="Managed Component Definition">

    private int __placeholder;
        
    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {        
    }
    
    private Page page1 = new Page();
    
    public Page getPage1() {
        return page1;
    }
    
    public void setPage1(Page p) {
        this.page1 = p;
    }
    
    private Html html1 = new Html();
    
    public Html getHtml1() {
        return html1;
    }
    
    public void setHtml1(Html h) {
        this.html1 = h;
    }
    
    private Head head1 = new Head();
    
    public Head getHead1() {
        return head1;
    }
    
    public void setHead1(Head h) {
        this.head1 = h;
    }
    
    private Link link1 = new Link();
    
    public Link getLink1() {
        return link1;
    }
    
    public void setLink1(Link l) {
        this.link1 = l;
    }
    
    private Body body1 = new Body();
    
    public Body getBody1() {
        return body1;
    }
    
    public void setBody1(Body b) {
        this.body1 = b;
    }
    
    private Form form1 = new Form();
    
    public Form getForm1() {
        return form1;
    }
    
    public void setForm1(Form f) {
        this.form1 = f;
    }

    private PanelLayout layoutPanel1 = new PanelLayout();

    public PanelLayout getLayoutPanel1() {
        return layoutPanel1;
    }

    public void setLayoutPanel1(PanelLayout pl) {
        this.layoutPanel1 = pl;
    }

    private TabSet tabSet1 = new TabSet();

    public TabSet getTabSet1() {
        return tabSet1;
    }

    public void setTabSet1(TabSet ts) {
        this.tabSet1 = ts;
    }

    private Tab tab1 = new Tab();

    public Tab getTab1() {
        return tab1;
    }

    public void setTab1(Tab t) {
        this.tab1 = t;
    }

    private PanelLayout layoutPanel2 = new PanelLayout();

    public PanelLayout getLayoutPanel2() {
        return layoutPanel2;
    }

    public void setLayoutPanel2(PanelLayout pl) {
        this.layoutPanel2 = pl;
    }

    private Tab tab2 = new Tab();

    public Tab getTab2() {
        return tab2;
    }

    public void setTab2(Tab t) {
        this.tab2 = t;
    }

    private PanelLayout layoutPanel3 = new PanelLayout();

    public PanelLayout getLayoutPanel3() {
        return layoutPanel3;
    }

    public void setLayoutPanel3(PanelLayout pl) {
        this.layoutPanel3 = pl;
    }

    private Tab tab3 = new Tab();

    public Tab getTab3() {
        return tab3;
    }

    public void setTab3(Tab t) {
        this.tab3 = t;
    }

    private PanelLayout layoutPanel4 = new PanelLayout();

    public PanelLayout getLayoutPanel4() {
        return layoutPanel4;
    }

    public void setLayoutPanel4(PanelLayout pl) {
        this.layoutPanel4 = pl;
    }

    private Button btnCreateDemoDB = new Button();

    public Button getBtnCreateDemoDB() {
        return btnCreateDemoDB;
    }

    public void setBtnCreateDemoDB(Button b) {
        this.btnCreateDemoDB = b;
    }

    private PanelGroup groupPanel1 = new PanelGroup();

    public PanelGroup getGroupPanel1() {
        return groupPanel1;
    }

    public void setGroupPanel1(PanelGroup pg) {
        this.groupPanel1 = pg;
    }

    private StaticText txtStatus = new StaticText();

    public StaticText getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(StaticText st) {
        this.txtStatus = st;
    }

    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }

    private Label label2 = new Label();

    public Label getLabel2() {
        return label2;
    }

    public void setLabel2(Label l) {
        this.label2 = l;
    }

    private Label label3 = new Label();

    public Label getLabel3() {
        return label3;
    }

    public void setLabel3(Label l) {
        this.label3 = l;
    }

    private StaticText staticText5 = new StaticText();

    public StaticText getStaticText5() {
        return staticText5;
    }

    public void setStaticText5(StaticText st) {
        this.staticText5 = st;
    }

    private StaticText staticText6 = new StaticText();

    public StaticText getStaticText6() {
        return staticText6;
    }

    public void setStaticText6(StaticText st) {
        this.staticText6 = st;
    }

    private StaticText staticText7 = new StaticText();

    public StaticText getStaticText7() {
        return staticText7;
    }

    public void setStaticText7(StaticText st) {
        this.staticText7 = st;
    }

    private StaticText staticText8 = new StaticText();

    public StaticText getStaticText8() {
        return staticText8;
    }

    public void setStaticText8(StaticText st) {
        this.staticText8 = st;
    }

    private HtmlOutputLink hyperlink1 = new HtmlOutputLink();

    public HtmlOutputLink getHyperlink1() {
        return hyperlink1;
    }

    public void setHyperlink1(HtmlOutputLink hol) {
        this.hyperlink1 = hol;
    }

    private Tab tab4 = new Tab();

    public Tab getTab4() {
        return tab4;
    }

    public void setTab4(Tab t) {
        this.tab4 = t;
    }

    private PanelLayout layoutPanel5 = new PanelLayout();

    public PanelLayout getLayoutPanel5() {
        return layoutPanel5;
    }

    public void setLayoutPanel5(PanelLayout pl) {
        this.layoutPanel5 = pl;
    }

    private Tab tab5 = new Tab();

    public Tab getTab5() {
        return tab5;
    }

    public void setTab5(Tab t) {
        this.tab5 = t;
    }

    private PanelLayout layoutPanel6 = new PanelLayout();

    public PanelLayout getLayoutPanel6() {
        return layoutPanel6;
    }

    public void setLayoutPanel6(PanelLayout pl) {
        this.layoutPanel6 = pl;
    }

    private Table table1 = new Table();

    public Table getTable1() {
        return table1;
    }

    public void setTable1(Table t) {
        this.table1 = t;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    private TableColumn tableColumn1 = new TableColumn();

    public TableColumn getTableColumn1() {
        return tableColumn1;
    }

    public void setTableColumn1(TableColumn tc) {
        this.tableColumn1 = tc;
    }

    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(StaticText st) {
        this.staticText2 = st;
    }

    private TableColumn tableColumn2 = new TableColumn();

    public TableColumn getTableColumn2() {
        return tableColumn2;
    }

    public void setTableColumn2(TableColumn tc) {
        this.tableColumn2 = tc;
    }

    private StaticText staticText3 = new StaticText();

    public StaticText getStaticText3() {
        return staticText3;
    }

    public void setStaticText3(StaticText st) {
        this.staticText3 = st;
    }

    private TableColumn tableColumn3 = new TableColumn();

    public TableColumn getTableColumn3() {
        return tableColumn3;
    }

    public void setTableColumn3(TableColumn tc) {
        this.tableColumn3 = tc;
    }

    private StaticText staticText4 = new StaticText();

    public StaticText getStaticText4() {
        return staticText4;
    }

    public void setStaticText4(StaticText st) {
        this.staticText4 = st;
    }

    private DConfigTableDataProvider dconfigTableDataProvider = new DConfigTableDataProvider();

    public DConfigTableDataProvider getDconfigTableDataProvider() {
        return dconfigTableDataProvider;
    }

    public void setDconfigTableDataProvider(DConfigTableDataProvider dtdp) {
        this.dconfigTableDataProvider = dtdp;
    }

    private TextArea txtQuery = new TextArea();

    public TextArea getTxtQuery() {
        return txtQuery;
    }

    public void setTxtQuery(TextArea ta) {
        this.txtQuery = ta;
    }

    private Button btnExecuteQuery = new Button();

    public Button getBtnExecuteQuery() {
        return btnExecuteQuery;
    }

    public void setBtnExecuteQuery(Button b) {
        this.btnExecuteQuery = b;
    }

    private Label label4 = new Label();

    public Label getLabel4() {
        return label4;
    }

    public void setLabel4(Label l) {
        this.label4 = l;
    }

    private Hyperlink linkSampleQuery = new Hyperlink();

    public Hyperlink getLinkSampleQuery() {
        return linkSampleQuery;
    }

    public void setLinkSampleQuery(Hyperlink h) {
        this.linkSampleQuery = h;
    }

    private TableColumn tableColumn4 = new TableColumn();

    public TableColumn getTableColumn4() {
        return tableColumn4;
    }

    public void setTableColumn4(TableColumn tc) {
        this.tableColumn4 = tc;
    }

    private StaticText staticText9 = new StaticText();

    public StaticText getStaticText9() {
        return staticText9;
    }

    public void setStaticText9(StaticText st) {
        this.staticText9 = st;
    }

    private TableColumn tableColumn5 = new TableColumn();

    public TableColumn getTableColumn5() {
        return tableColumn5;
    }

    public void setTableColumn5(TableColumn tc) {
        this.tableColumn5 = tc;
    }

    private StaticText staticText10 = new StaticText();

    public StaticText getStaticText10() {
        return staticText10;
    }

    public void setStaticText10(StaticText st) {
        this.staticText10 = st;
    }

    private TableColumn tableColumn6 = new TableColumn();

    public TableColumn getTableColumn6() {
        return tableColumn6;
    }

    public void setTableColumn6(TableColumn tc) {
        this.tableColumn6 = tc;
    }

    private StaticText staticText11 = new StaticText();

    public StaticText getStaticText11() {
        return staticText11;
    }

    public void setStaticText11(StaticText st) {
        this.staticText11 = st;
    }

    private Hyperlink linkReloadDconfigCache = new Hyperlink();

    public Hyperlink getLinkReloadDconfigCache() {
        return linkReloadDconfigCache;
    }

    public void setLinkReloadDconfigCache(Hyperlink h) {
        this.linkReloadDconfigCache = h;
    }

    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }

    private HtmlOutputLink hyperlink3 = new HtmlOutputLink();

    public HtmlOutputLink getHyperlink3() {
        return hyperlink3;
    }

    public void setHyperlink3(HtmlOutputLink hol) {
        this.hyperlink3 = hol;
    }

    private HtmlOutputText hyperlink3Text = new HtmlOutputText();

    public HtmlOutputText getHyperlink3Text() {
        return hyperlink3Text;
    }

    public void setHyperlink3Text(HtmlOutputText hot) {
        this.hyperlink3Text = hot;
    }

    private ImageComponent image1 = new ImageComponent();

    public ImageComponent getImage1() {
        return image1;
    }

    public void setImage1(ImageComponent ic) {
        this.image1 = ic;
    }

    private PanelGroup groupPanel2 = new PanelGroup();

    public PanelGroup getGroupPanel2() {
        return groupPanel2;
    }

    public void setGroupPanel2(PanelGroup pg) {
        this.groupPanel2 = pg;
    }

    private StaticText staticText12 = new StaticText();

    public StaticText getStaticText12() {
        return staticText12;
    }

    public void setStaticText12(StaticText st) {
        this.staticText12 = st;
    }

    private StaticText staticText13 = new StaticText();

    public StaticText getStaticText13() {
        return staticText13;
    }

    public void setStaticText13(StaticText st) {
        this.staticText13 = st;
    }

    private Tab tab6 = new Tab();

    public Tab getTab6() {
        return tab6;
    }

    public void setTab6(Tab t) {
        this.tab6 = t;
    }

    private PanelLayout layoutPanel7 = new PanelLayout();

    public PanelLayout getLayoutPanel7() {
        return layoutPanel7;
    }

    public void setLayoutPanel7(PanelLayout pl) {
        this.layoutPanel7 = pl;
    }

    private Label label5 = new Label();

    public Label getLabel5() {
        return label5;
    }

    public void setLabel5(Label l) {
        this.label5 = l;
    }

    private TextField txtRowsPerPage = new TextField();

    public TextField getTxtRowsPerPage() {
        return txtRowsPerPage;
    }

    public void setTxtRowsPerPage(TextField tf) {
        this.txtRowsPerPage = tf;
    }

    private StaticText tab_Names = new StaticText();

    public StaticText getTab_Names() {
        return tab_Names;
    }

    public void setTab_Names(StaticText st) {
        this.tab_Names = st;
    }

    private Label label6 = new Label();

    public Label getLabel6() {
        return label6;
    }

    public void setLabel6(Label l) {
        this.label6 = l;
    }

    private TextField txtTabname1 = new TextField();

    public TextField getTxtTabname1() {
        return txtTabname1;
    }

    public void setTxtTabname1(TextField tf) {
        this.txtTabname1 = tf;
    }

    private Label label7 = new Label();

    public Label getLabel7() {
        return label7;
    }

    public void setLabel7(Label l) {
        this.label7 = l;
    }

    private TextField txtTabname2 = new TextField();

    public TextField getTxtTabname2() {
        return txtTabname2;
    }

    public void setTxtTabname2(TextField tf) {
        this.txtTabname2 = tf;
    }

    private Label label8 = new Label();

    public Label getLabel8() {
        return label8;
    }

    public void setLabel8(Label l) {
        this.label8 = l;
    }

    private TextField txtTabname3 = new TextField();

    public TextField getTxtTabname3() {
        return txtTabname3;
    }

    public void setTxtTabname3(TextField tf) {
        this.txtTabname3 = tf;
    }

    private Label label9 = new Label();

    public Label getLabel9() {
        return label9;
    }

    public void setLabel9(Label l) {
        this.label9 = l;
    }

    private TextField txtTabname4 = new TextField();

    public TextField getTxtTabname4() {
        return txtTabname4;
    }

    public void setTxtTabname4(TextField tf) {
        this.txtTabname4 = tf;
    }

    private Label label10 = new Label();

    public Label getLabel10() {
        return label10;
    }

    public void setLabel10(Label l) {
        this.label10 = l;
    }

    private TextField txtTabname5 = new TextField();

    public TextField getTxtTabname5() {
        return txtTabname5;
    }

    public void setTxtTabname5(TextField tf) {
        this.txtTabname5 = tf;
    }

    private StaticText static2 = new StaticText();

    public StaticText getStatic2() {
        return static2;
    }

    public void setStatic2(StaticText st) {
        this.static2 = st;
    }

    private Button btnSubmitPreferences = new Button();

    public Button getBtnSubmitPreferences() {
        return btnSubmitPreferences;
    }

    public void setBtnSubmitPreferences(Button b) {
        this.btnSubmitPreferences = b;
    }

    private SingleSelectOptionsList ddnCol1DefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDdnCol1DefaultOptions() {
        return ddnCol1DefaultOptions;
    }

    public void setDdnCol1DefaultOptions(SingleSelectOptionsList ssol) {
        this.ddnCol1DefaultOptions = ssol;
    }

    private Checkbox ckCol1 = new Checkbox();

    public Checkbox getCkCol1() {
        return ckCol1;
    }

    public void setCkCol1(Checkbox c) {
        this.ckCol1 = c;
    }

    private Checkbox ckCol2 = new Checkbox();

    public Checkbox getCkCol2() {
        return ckCol2;
    }

    public void setCkCol2(Checkbox c) {
        this.ckCol2 = c;
    }

    private Checkbox ckCol3 = new Checkbox();

    public Checkbox getCkCol3() {
        return ckCol3;
    }

    public void setCkCol3(Checkbox c) {
        this.ckCol3 = c;
    }

    private Checkbox ckCol4 = new Checkbox();

    public Checkbox getCkCol4() {
        return ckCol4;
    }

    public void setCkCol4(Checkbox c) {
        this.ckCol4 = c;
    }

    private Checkbox ckCol5 = new Checkbox();

    public Checkbox getCkCol5() {
        return ckCol5;
    }

    public void setCkCol5(Checkbox c) {
        this.ckCol5 = c;
    }

    private Checkbox ckCol6 = new Checkbox();

    public Checkbox getCkCol6() {
        return ckCol6;
    }

    public void setCkCol6(Checkbox c) {
        this.ckCol6 = c;
    }

    private HtmlOutputLink hyperlink4 = new HtmlOutputLink();

    public HtmlOutputLink getHyperlink4() {
        return hyperlink4;
    }

    public void setHyperlink4(HtmlOutputLink hol) {
        this.hyperlink4 = hol;
    }

    private HtmlOutputText hyperlink4Text = new HtmlOutputText();

    public HtmlOutputText getHyperlink4Text() {
        return hyperlink4Text;
    }

    public void setHyperlink4Text(HtmlOutputText hot) {
        this.hyperlink4Text = hot;
    }

    private HtmlOutputLink hyperlink2 = new HtmlOutputLink();

    public HtmlOutputLink getHyperlink2() {
        return hyperlink2;
    }

    public void setHyperlink2(HtmlOutputLink hol) {
        this.hyperlink2 = hol;
    }

    private HtmlOutputText hyperlink2Text = new HtmlOutputText();

    public HtmlOutputText getHyperlink2Text() {
        return hyperlink2Text;
    }

    public void setHyperlink2Text(HtmlOutputText hot) {
        this.hyperlink2Text = hot;
    }
    
    // </editor-fold>
    
    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Page1() {
    }
    
    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     *
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    public void init() {
        // Perform initializations inherited from our superclass
        super.init();        
                
        // <editor-fold defaultstate="collapsed" desc="Managed Component Initialization">
        // Initialize automatically managed components
        // *Note* - this logic should NOT be modified
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e: new FacesException(e);
        }
        
        // </editor-fold>
        // Perform application initialization that must complete
        // *after* managed components are initialized
        // TODO - add your own initialization code here
        postInitialization();
    }
    
    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    public void preprocess() {
        boolean b = this.table1.isPaginateButton();
        this.txtStatus.setVisible(false);
        String id  = tabSet1.getSelected();
    }
    
    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    public void prerender() {
        try {
            String[] tabeNames = DConfigReader.getStringArray("config.view.demo.page1", "tab names");
            int[] visibleColumns = DConfigReader.getIntegerArray("config.view.demo.page1", "visible columns");
            int rowsPerPage = DConfigReader.getInteger("config.view.demo.page1", "#Rows Per Page", 5);
            if (tabeNames.length == 5) {
                int i = 0;                         // Default tab names:
                this.tab1.setText(tabeNames[i++]); // Database
                this.tab2.setText(tabeNames[i++]); // DConfig UI - JNLP
                this.tab3.setText(tabeNames[i++]); // Refresh
                this.tab4.setText(tabeNames[i++]); // Search
                this.tab5.setText(tabeNames[i++]); // Resultt
	    }
            // prepare for tab click
            String selectedTabId = tabSet1.getSelected();
            if ((selectedTabId != null) && selectedTabId.equals(this.tab6.getId())) { // Preferences tab
                int i = 0;
                if (tabeNames.length == 5) {
	            this.txtTabname1.setText(tabeNames[i++]);
	            this.txtTabname2.setText(tabeNames[i++]);
	            this.txtTabname3.setText(tabeNames[i++]);
	            this.txtTabname4.setText(tabeNames[i++]);
	            this.txtTabname5.setText(tabeNames[i++]);
                }
                this.ckCol1.setSelected(false);
                this.ckCol2.setSelected(false);
                this.ckCol3.setSelected(false);
                this.ckCol4.setSelected(false);
                this.ckCol5.setSelected(false);
                this.ckCol6.setSelected(false);
                for (i = 0; i < visibleColumns.length; i++) {
                    int col = visibleColumns[i];
                    if (col == 1)
                        this.ckCol1.setSelected(true);
                    else if (col == 2)
                        this.ckCol2.setSelected(true);
                    else if (col == 3)
                        this.ckCol3.setSelected(true);
                    else if (col == 4)
                        this.ckCol4.setSelected(true);
                    else if (col == 5)
                        this.ckCol5.setSelected(true);
                    else if (col == 6)
                        this.ckCol6.setSelected(true);
                }
                this.txtRowsPerPage.setText(String.valueOf(rowsPerPage));
            }
            if ( (selectedTabId != null) && selectedTabId.equals(this.tab5.getId()) ) { // Results tab
                // set table and all columns invisible first
                this.table1.setVisible(false);
                columns = new TableColumn[7]; // one extra, valid index from 1
                columns[1] = this.tableColumn1;
                columns[2] = this.tableColumn2;
                columns[3] = this.tableColumn3;
                columns[4] = this.tableColumn4;
                columns[5] = this.tableColumn5;
                columns[6] = this.tableColumn6;
                for (int i = 1; i <= 6; i++)
                    columns[i].setVisible(false);

                // prepare data
                String searchFor = (String) getSessionBean1().retrieveData("searchFor");
                List retList = (List) this.getSessionBean1().retrieveData("searchResults");
                if (retList != null) {
                    this.table1.setVisible(true);
                    dconfigTableDataProvider.setArray(createTableData(retList));
                    // set column names
                    if (searchFor != null) {
                        if (searchFor.equals("dconfig_key")) {
                            this.tableColumn1.setHeaderText("Id");
                            this.tableColumn2.setHeaderText("Key Name");
                            this.tableColumn1.setVisible(true);
                            this.tableColumn2.setVisible(true);
                        } else if (searchFor.equals("dconfig_attribute")) {
                            this.tableColumn1.setHeaderText("Id");
                            this.tableColumn2.setHeaderText("Key Name");
                            this.tableColumn3.setHeaderText("Attribute Name");
                            this.tableColumn4.setHeaderText("Data Type");
                            this.tableColumn5.setHeaderText("Attribute Value");
                            this.tableColumn6.setHeaderText("Comments");
                        } else if (searchFor.equals("dconfig_datatype")) {
                            this.tableColumn1.setHeaderText("Alias");
                            this.tableColumn2.setHeaderText("Data Type Name");
                            this.tableColumn1.setVisible(true);
                            this.tableColumn2.setVisible(true);
                        }
                    }
                }
                else {
                    this.setStatus("Data is not available");
                }
                // set up rows per page
                tableRowGroup1.setRows(rowsPerPage);

                // set up visible columns for attribute table search
                if ((searchFor != null) && searchFor.equals("dconfig_attribute")) {
                    for (int i = 0; i < visibleColumns.length; i++) {
                        int col = visibleColumns[i];
                        columns[col].setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
    
    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1)getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1)getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1)getBean("RequestBean1");
    }
    
    protected void postInitialization() {
    }

    public Data[] createTableData() {
        int noRows = 1;
        int noCols = 1;
        Data[] dataSet = new Data[noRows];
        for (int i = 0; i < noRows; i++) {
            String[] dataStrs = new String[noCols];
            for(int j=0; j < noCols; j++){
                dataStrs[j] = "No results are available";
            }
            dataSet[i] = new Data(dataStrs);
        }
        return dataSet;
    }    

    public Data[] createTableData(List retList) {
        if ((retList == null) || (retList.size() == 0))
            return null;
        
        DConfigKey key = null;
        DConfigAttribute attribute = null;
        DConfigDataType dataType = null;
        Object item = null;
        int noCols = 6;
        Data[] dataSet = null;
        try {
            item = retList.get(0);
            if (item instanceof DConfigKey)
                noCols = 3;
            else if (item instanceof DConfigAttribute)
                noCols = 6;
            else if (item instanceof DConfigDataType)
                noCols = 2;

            dataSet = new Data[retList.size()];
            for (int i = 0; i < retList.size(); i++) {
                item = retList.get(i);
                String[] dataCells = new String[noCols];
                if (item instanceof DConfigKey) {
                    key = (DConfigKey) item;
                    dataCells[0] = key.getId().toString();
                    dataCells[1] = key.getKeyName();
                } else if (item instanceof DConfigAttribute) {
                    attribute = (DConfigAttribute) item;
                    dataCells[0] = attribute.getId().toString();
                    dataCells[1] = DConfigReader.findKeyById(attribute.getKeyId()).getKeyName();
                    dataCells[2] = attribute.getAttributeName();
                    dataCells[3] = attribute.getDataTypeName();
                    dataCells[4] = attribute.getAttributeValue();
                    dataCells[5] = attribute.getComments();
                }
                dataSet[i] = new Data(dataCells);
            }
        } catch (Exception e) {
            log.error(e);
        }
        return dataSet;
    }

    public String btnCreateDemoDB_action() {
        LibInitializer.initialize();
        AppState.setDemo(true);
        AppState.setDerby(true);
        DemoDbManager.startup();
        if (new DemoDataPopulator().populate()) {
            log.info("run DemoDataPopulator().populate() ok");
            setStatus("Derby database started successfully.");
            // copied from InitServlet
            CacheManager.load();
            //DConfigTimerTask.start();
        } else {
            log.error("run DemoDataPopulator().populate() failed");                
            setStatus("Failed to create Derby database. Check log file for details.");
        }
        return null;
    }

    DataSource dataSource;
    public DataSource getDataSource() {
    	if (dataSource == null)
            dataSource = DataSourceManager.getDataSource();
        return dataSource;
    }
    
    public String btnExecuteQuery_action() {
        List retList = null;
        String sQuery = (String)this.txtQuery.getText();        
        if ((sQuery != null) && this.isPostBack()) {            
            sQuery = sQuery.toLowerCase();      
            try {
                QueryTemplate query = new QueryTemplate(getDataSource());
                query.setSql(sQuery);
                query.compile();
                if (sQuery.indexOf("dconfig_key") >= 0) {
                    query.setRowMapper(new DConfigKeyMapper());
                    getSessionBean1().saveData("searchFor", "dconfig_key");
                }
                else if (sQuery.indexOf("dconfig_attribute") >= 0) {
                    query.setRowMapper(new DConfigAttributeMapper());
                    getSessionBean1().saveData("searchFor", "dconfig_attribute");
                }
                else if (sQuery.indexOf("dconfig_dadatype") >= 0) {
                    query.setRowMapper(new DConfigDataTypeMapper());
                    getSessionBean1().saveData("searchFor", "dconfig_dadatype");
                }
                retList = query.execute();
                this.getSessionBean1().saveData("searchResults", retList);
            } catch (Exception e) {
                log.error(e, e);
                retList = Constants.EMPTY_ARRAYLIST;
            }
            this.tabSet1.setSelected(this.tab5.getId());
        }
        return null;
    }

    public String linkSampleQuery_action() {
        this.txtQuery.setText("select id, key_id, attribute_name, data_type_alias, attribute_value, comments from dconfig_attribute");
        return null;
    }

    public String linkReloadDconfigCache_action() {
        Timer timer = new Timer();
        if (CacheManager.load()) {
            setStatus("Reload DConfig cache" + timer.toMilliseconds());
        } else {
            setStatus("Reload DConfig cache failed");
        }
        return null;
    }
    
    private void setStatus(String msg) {
        this.txtStatus.setText(msg);
        this.txtStatus.setVisible(true);
    }

    public String tab5_action() {
        // TODO: Replace with your code
        
        return null;
    }

    /**
     *  Process the Submit Preferences button click action. Return value is a navigation
     *  case name where null will return to the same page.
     */
    public String btnSubmitPreferences_action() {
        try {
            // save '#Rows Per Page'
            int rowsPerPage = Integer.parseInt(this.txtRowsPerPage.getText().toString());
            DConfigWriter.writeInteger("config.view.demo.page1", "#Rows Per Page", rowsPerPage);

            // save 'tab names'
            String[] sArray = new String[5];
            sArray[0] = this.txtTabname1.getText().toString();
            sArray[1] = this.txtTabname2.getText().toString();
            sArray[2] = this.txtTabname3.getText().toString();
            sArray[3] = this.txtTabname4.getText().toString();
            sArray[4] = this.txtTabname5.getText().toString();
            DConfigWriter.writeStringArray("config.view.demo.page1", "tab names", sArray);

            // save 'visible columns'
            Integer[] iArray = new Integer[6];
            int count = 0;
            if (this.ckCol1.isChecked())
                iArray[count++] = 1;
            if (this.ckCol2.isChecked())
                iArray[count++] = 2;
            if (this.ckCol3.isChecked())
                iArray[count++] = 3;
            if (this.ckCol4.isChecked())
                iArray[count++] = 4;
            if (this.ckCol5.isChecked())
                iArray[count++] = 5;
            if (this.ckCol6.isChecked())
                iArray[count++] = 6;
            DConfigWriter.writeIntegerArray("config.view.demo.page1", "visible columns", iArray);
                    
            setStatus("Write DConfig data into database successful");
        } catch (Exception e) {
            setStatus("Failed to write DConfig data into database");
            log.error(e, e);
        }
        return null;
    }
}

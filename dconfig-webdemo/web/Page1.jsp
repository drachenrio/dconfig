<?xml version="1.0" encoding="UTF-8"?>
<jsp:root version="1.2" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:ui="http://www.sun.com/web/ui">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <style type="text/css">
       
#scrollBox{
max-height: 300px;
max-width: 650px;
border: 1px solid;
padding: 5px;
overflow: auto;
}
       
   </style>
    <f:view>
        <ui:page binding="#{Page1.page1}" id="page1">
            <ui:html binding="#{Page1.html1}" id="html1">
                <ui:head binding="#{Page1.head1}" id="head1">
                    <ui:link binding="#{Page1.link1}" id="link1" url="/resources/stylesheet.css"/>
                </ui:head>
                <ui:body binding="#{Page1.body1}" id="body1" style="-rave-layout: grid">
                    <ui:form binding="#{Page1.form1}" id="form1">
                        <ui:panelLayout binding="#{Page1.layoutPanel1}" id="layoutPanel1" panelLayout="flow" style="height: 382px; left: 72px; top: 72px; position: absolute; width: 598px">
                            <ui:tabSet binding="#{Page1.tabSet1}" id="tabSet1" selected="tab1">
                                <ui:tab binding="#{Page1.tab1}" id="tab1" text="Database">
                                    <ui:panelLayout binding="#{Page1.layoutPanel2}" id="layoutPanel2" style="height: 416px; position: relative; width: 100%; -rave-layout: grid">
                                        <ui:button action="#{Page1.btnCreateDemoDB_action}" binding="#{Page1.btnCreateDemoDB}" id="btnCreateDemoDB"
                                            style="height: 24px; left: 47px; top: 120px; position: absolute; width: 240px" text="Create Derby Demo Database"/>
                                        <ui:label binding="#{Page1.label2}" id="label2"
                                            style="position: absolute; left: 24px; top: 24px; width: 24px; height: 24px" text="1."/>
                                        <ui:label binding="#{Page1.label3}" id="label3"
                                            style="height: 24px; left: 24px; top: 120px; position: absolute; width: 24px" text="2."/>
                                        <ui:staticText binding="#{Page1.staticText5}" id="staticText5"
                                            style="position: absolute; left: 48px; top: 24px; width: 432px; height: 24px" text="Switch to {dconfig downloaded home}/dconfig-db/src/script"/>
                                        <ui:staticText binding="#{Page1.staticText6}" id="staticText6"
                                            style="height: 24px; left: 48px; top: 72px; position: absolute; width: 432px" text="./derby-startup.sh  (Linux) or"/>
                                        <ui:staticText binding="#{Page1.staticText7}" id="staticText7"
                                            style="position: absolute; left: 48px; top: 96px; width: 432px; height: 24px" text="derby-startup.bat (Windows)"/>
                                        <ui:staticText binding="#{Page1.staticText8}" id="staticText8"
                                            style="position: absolute; left: 48px; top: 48px; width: 432px; height: 24px" text="Issue the following command:"/>
                                        <ui:staticText binding="#{Page1.staticText8}" id="staticText8"
                                            style="position: absolute; left: 48px; top: 48px; width: 432px; height: 24px" text="Issue command:"/>
                                        <ui:panelGroup binding="#{Page1.groupPanel2}" id="groupPanel2" style="height: 144px; left: 48px; top: 168px; position: absolute; width: 532px">
                                            <ui:staticText binding="#{Page1.staticText12}" escape="false" id="staticText12" text="&lt;div id=&quot;scrollBox&quot;&gt;&#xa;&lt;pre&gt;The following back-bean code will be executed after the above button is clicked:&#xa;&#xa;import org.moonwave.dconfig.*;&#xa;&#xa;    public String btnCreateDemoDB_action() {&#xa;        LibInitializer.initialize();&#xa;        AppState.setDemo(true);&#xa;        AppState.setDerby(true);&#xa;        DemoDbManager.startup();&#xa;        if (new DemoDataPopulator().populate()) { // create DB schema&#xa;            log.info(&quot;run DemoDataPopulator().populate() ok&quot;);&#xa;            setStatus(&quot;Derby database started successfully.&quot;);&#xa;            CacheManager.load();&#xa;        } else {&#xa;            log.error(&quot;run DemoDataPopulator().populate() failed&quot;);                &#xa;            setStatus(&quot;Failed to create Derby database. Check log file for details.&quot;);&#xa;        }&#xa;        return null;&#xa;    }        &#xa;&lt;/pre&gt;        &#xa;&lt;/div&gt;&#xa;    "/>
                                        </ui:panelGroup>
                                    </ui:panelLayout>
                                </ui:tab>
                                <ui:tab binding="#{Page1.tab2}" id="tab2" text="DConfig UI - JNLP">
                                    <ui:panelLayout binding="#{Page1.layoutPanel3}" id="layoutPanel3" style="height: 271px; width: 100%">
                                        <ui:panelGroup binding="#{Page1.groupPanel1}" id="groupPanel1">
                                            <h:outputLink binding="#{Page1.hyperlink3}" id="hyperlink3" style="left: 72px; top: 48px; position: absolute" value="/dconfig-ui-jnlp/dconfig.jnlp">
                                                <ui:image align="bottom" binding="#{Page1.image1}" id="image1" url="webstart_small.gif"/>
                                                <h:outputText binding="#{Page1.hyperlink3Text}" id="hyperlink3Text" value="Launch DConfig Editor"/>
                                            </h:outputLink>
                                            <ui:staticText binding="#{Page1.staticText13}" escape="false" id="staticText13"
                                                style="left: 72px; top: 128px; position: absolute" text="&lt;div id=&quot;scrollBox&quot;&gt;&#xa;&lt;pre&gt;&#xa;Use the following code:&#xa;&#xa;&amp;lt;a href=&quot;/dconfig-ui-jnlp/dconfig.jnlp&quot;&amp;gt;&amp;lt;img border=0 src=&quot;webstart_small.gif&quot;&amp;gt;Launch DConfig Editor&amp;lt;/a&amp;gt;&#xa;&lt;/pre&gt;        &#xa;&lt;/div&gt;&#xa;"/>
                                        </ui:panelGroup>
                                    </ui:panelLayout>
                                </ui:tab>
                                <ui:tab binding="#{Page1.tab3}" id="tab3" text="Refresh">
                                    <ui:panelLayout binding="#{Page1.layoutPanel4}" id="layoutPanel4" style="height: 416px; position: relative; width: 100%; -rave-layout: grid">
                                        <ui:hyperlink action="#{Page1.linkReloadDconfigCache_action}" binding="#{Page1.linkReloadDconfigCache}"
                                            id="linkReloadDconfigCache"
                                            style="font-size: 14px; height: 46px; left: 48px; top: 24px; position: absolute; width: 430px" text="Reload DConfig Cache (Reload Dynamic Configuration Data)"/>
                                        <ui:staticText binding="#{Page1.staticText1}" escape="false" id="staticText1"
                                            style="font-size: 14px; height: 70px; left: 48px; top: 96px; position: absolute; width: 406px" text="&lt;div id=&quot;scrollBox&quot;&gt;&#xa;&lt;pre&gt;Reload DConfig cache using method &#xa;&#xa;org.moonwave.dconfig.dao.CacheManager.load()&#xa;&#xa;which locates in dconfig-lib.jar        &#xa;&lt;/pre&gt;        &#xa;&lt;/div&gt;&#xa;    "/>
                                    </ui:panelLayout>
                                </ui:tab>
                                <ui:tab binding="#{Page1.tab4}" id="tab4" text="Search">
                                    <ui:panelLayout binding="#{Page1.layoutPanel5}" id="layoutPanel5" style="height: 223px; width: 100%">
                                        <ui:textArea binding="#{Page1.txtQuery}" id="txtQuery" style="height: 96px; left: 72px; top: 48px; position: absolute; width: 312px"/>
                                        <ui:button action="#{Page1.btnExecuteQuery_action}" binding="#{Page1.btnExecuteQuery}" id="btnExecuteQuery"
                                            style="height: 24px; left: 408px; top: 48px; position: absolute; width: 119px" text="Execute Query"/>
                                        <ui:label binding="#{Page1.label4}" id="label4"
                                            style="position: absolute; left: 72px; top: 24px; width: 96px; height: 24px" text="Query"/>
                                        <ui:hyperlink action="#{Page1.linkSampleQuery_action}" binding="#{Page1.linkSampleQuery}" id="linkSampleQuery"
                                            style="position: absolute; left: 72px; top: 168px; width: 96px; height: 24px" text="Sample Query"/>
                                    </ui:panelLayout>
                                </ui:tab>
                                <ui:tab action="#{Page1.tab5_action}" binding="#{Page1.tab5}" id="tab5" text="Results">
                                    <ui:panelLayout binding="#{Page1.layoutPanel6}" id="layoutPanel6" style="width: 100%; height: 128px;">
                                        <ui:table binding="#{Page1.table1}" clearSortButton="true" id="table1" itemsText="Rows" paginateButton="true"
                                            paginationControls="true" sortPanelToggleButton="true" summary="My Summary" title="DConfig Attribute Table" width="360">
                                            <ui:tableRowGroup binding="#{Page1.tableRowGroup1}" id="tableRowGroup1" rows="5"
                                                sourceData="#{Page1.dconfigTableDataProvider}" sourceVar="currentRow">
                                                <ui:tableColumn binding="#{Page1.tableColumn1}" headerText="column1" id="tableColumn1" sort="column1">
                                                    <ui:staticText binding="#{Page1.staticText2}" id="staticText2" text="#{currentRow.value['column1']}"/>
                                                </ui:tableColumn>
                                                <ui:tableColumn binding="#{Page1.tableColumn2}" headerText="column2" id="tableColumn2" sort="column2">
                                                    <ui:staticText binding="#{Page1.staticText3}" id="staticText3" text="#{currentRow.value['column2']}"/>
                                                </ui:tableColumn>
                                                <ui:tableColumn binding="#{Page1.tableColumn3}" headerText="column3" id="tableColumn3" sort="column3">
                                                    <ui:staticText binding="#{Page1.staticText4}" id="staticText4" text="#{currentRow.value['column3']}"/>
                                                </ui:tableColumn>
                                                <ui:tableColumn binding="#{Page1.tableColumn4}" headerText="column4" id="tableColumn4" sort="column4">
                                                    <ui:staticText binding="#{Page1.staticText9}" id="staticText9" text="#{currentRow.value['column4']}"/>
                                                </ui:tableColumn>
                                                <ui:tableColumn binding="#{Page1.tableColumn5}" headerText="column5" id="tableColumn5" sort="column5">
                                                    <ui:staticText binding="#{Page1.staticText10}" id="staticText10" text="#{currentRow.value['column5']}"/>
                                                </ui:tableColumn>
                                                <ui:tableColumn binding="#{Page1.tableColumn6}" headerText="column6" id="tableColumn6" sort="column6">
                                                    <ui:staticText binding="#{Page1.staticText11}" id="staticText11" text="#{currentRow.value['column6']}"/>
                                                </ui:tableColumn>
                                            </ui:tableRowGroup>
                                        </ui:table>
                                    </ui:panelLayout>
                                </ui:tab>
                                <ui:tab binding="#{Page1.tab6}" id="tab6" text="Preferences">
                                    <ui:panelLayout binding="#{Page1.layoutPanel7}" id="layoutPanel7" style="height: 328px; width: 100%">
                                        <ui:label binding="#{Page1.label5}" id="label5" style="left: 24px; top: 24px; position: absolute" text="#Rows Per Page"/>
                                        <ui:textField binding="#{Page1.txtRowsPerPage}" id="txtRowsPerPage" style="height: 24px; left: 144px; top: 24px; position: absolute; width: 72px"/>
                                        <ui:staticText binding="#{Page1.tab_Names}" id="tab_Names"
                                            style="color: rgb(0, 0, 204); font-size: 14px; font-style: normal; font-weight: bolder; left: 24px; top: 48px; position: absolute" text="Tab Names"/>
                                        <ui:label binding="#{Page1.label6}" id="label6" style="position: absolute; left: 24px; top: 72px" text="Tab1"/>
                                        <ui:textField binding="#{Page1.txtTabname1}" id="txtTabname1" style="position: absolute; left: 72px; top: 72px"/>
                                        <ui:label binding="#{Page1.label7}" id="label7" style="left: 24px; top: 96px; position: absolute" text="Tab2"/>
                                        <ui:textField binding="#{Page1.txtTabname2}" id="txtTabname2" style="left: 72px; top: 96px; position: absolute"/>
                                        <ui:label binding="#{Page1.label8}" id="label8" style="left: 24px; top: 120px; position: absolute" text="Tab3"/>
                                        <ui:textField binding="#{Page1.txtTabname3}" id="txtTabname3" style="left: 72px; top: 120px; position: absolute"/>
                                        <ui:label binding="#{Page1.label9}" id="label9" style="left: 24px; top: 144px; position: absolute" text="Tab4"/>
                                        <ui:textField binding="#{Page1.txtTabname4}" id="txtTabname4" style="left: 72px; top: 144px; position: absolute"/>
                                        <ui:label binding="#{Page1.label10}" id="label10" style="left: 24px; top: 168px; position: absolute" text="Tab5"/>
                                        <ui:textField binding="#{Page1.txtTabname5}" id="txtTabname5" style="left: 72px; top: 168px; position: absolute"/>
                                        <ui:staticText binding="#{Page1.static2}" id="static2"
                                            style="color: rgb(0, 0, 204); font-size: 14px; font-style: normal; font-weight: bolder; left: 336px; top: 48px; position: absolute" text="Visible Columns"/>
                                        <ui:button action="#{Page1.btnSubmitPreferences_action}" binding="#{Page1.btnSubmitPreferences}"
                                            id="btnSubmitPreferences" style="position: absolute; left: 240px; top: 264px" text="Submit Preferences"/>
                                        <ui:checkbox binding="#{Page1.ckCol1}" id="ckCol1" label="Column 1 (ID)" style="position: absolute; left: 336px; top: 72px"/>
                                        <ui:checkbox binding="#{Page1.ckCol2}" id="ckCol2" label="Column 2 (Key Name)" style="position: absolute; left: 336px; top: 96px"/>
                                        <ui:checkbox binding="#{Page1.ckCol3}" id="ckCol3" label="Column 3 (Attribute Name)" style="position: absolute; left: 336px; top: 120px"/>
                                        <ui:checkbox binding="#{Page1.ckCol4}" id="ckCol4" label="Column 4 (Data Type)" style="position: absolute; left: 336px; top: 144px"/>
                                        <ui:checkbox binding="#{Page1.ckCol5}" id="ckCol5" label="Column 5 (Attribute Value)" style="position: absolute; left: 336px; top: 168px"/>
                                        <ui:checkbox binding="#{Page1.ckCol6}" id="ckCol6" label="Column 6 (Comments)" style="position: absolute; left: 336px; top: 192px"/>
                                    </ui:panelLayout>
                                </ui:tab>
                            </ui:tabSet>
                        </ui:panelLayout>
                        <ui:staticText binding="#{Page1.txtStatus}" id="txtStatus" style="height: 24px; left: 120px; top: 576px; position: absolute; width: 550px"/>
                        <ui:label binding="#{Page1.label1}" id="label1" labelLevel="1"
                            style="height: 24px; left: 168px; top: 24px; position: absolute; width: 382px" text="Dynamic Configuration Toolkit JSF Web Demo"/>
                        <h:outputLink binding="#{Page1.hyperlink1}" id="hyperlink1"/>
                        <h:outputLink binding="#{Page1.hyperlink4}" id="hyperlink4" style="position: absolute; left: 96px; top: 48px" value="jsp/dconfigReaderWriter.jsp">
                            <h:outputText binding="#{Page1.hyperlink4Text}" id="hyperlink4Text" value="Jsp example"/>
                        </h:outputLink>
                        <h:outputLink binding="#{Page1.hyperlink2}" id="hyperlink2" style="position: absolute; left: 552px; top: 48px" value="dconfigDemo.do">
                            <h:outputText binding="#{Page1.hyperlink2Text}" id="hyperlink2Text" value="Struts / yui demo"/>
                        </h:outputLink>
                    </ui:form>
                </ui:body>
            </ui:html>
        </ui:page>
    </f:view>
</jsp:root>

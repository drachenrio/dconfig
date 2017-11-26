<script language="Javascript">
Dom = YAHOO.util.Dom;
debugMode = false;
function debugModeClicked(ctrl) {
    debugMode = ctrl.checked ? true : false;
    if (!debugMode) {
        Dom.get('divMessage').innerHTML = "&nbsp;";
    }
}
function hintClicked(ctrl) {
    if (ctrl.checked)
        Dom.get('divMessage').innerHTML = "Type * in Key Name or Attribute Name fields to see full list; <b>&^#;</b> is the delimiter for array element";
    else
        Dom.get('divMessage').innerHTML = "&nbsp;";
}
function writeMessage(msg) {
    if (debugMode) {
        var divMessage = Dom.get('divMessage');
        divMessage.innerHTML += msg;
    }
}
function writeStatus(msg) {
    var divMessage = Dom.get('divMessage');
    divMessage.innerHTML += msg;
}
function clearMessage() {
    Dom.get('hint').checked = false;
    Dom.get('divMessage').innerHTML = "";
}
function clearFields() {
    Dom.get('keyName').value = "";
    Dom.get('attributeName').value = "";
    Dom.get('attributeValue').value = "";
    Dom.get('comments').value = "";
    Dom.get('inherited').innerHTML = "";
    Dom.get('arrayElements').innerHTML = "";
    Dom.get('divMessage').innerHTML = "";
    return false;
}
function dataTypeChanged(ctrl) {
    var selectedValue = ctrl.value;
    var idx = selectedValue.lastIndexOf("ar");
    clearMessage();
    if ((idx > 0) && (idx == selectedValue.length - 2))
        writeStatus("<font color=\"blue\">Use &^#; as the delimiter for array element when enter data in attribute value box.</font>");
    else
        writeStatus("");
}
</script>

       
<form name="admin" action="<%=request.getContextPath()%>/jsp/dconfigReaderWriter.jsp" method="post">
  <table align="center">
        <tr>
            <th>DConfigReader / DConfigWriter Demo</th>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
            <td align="center" valign="bottom">
                <input type="button" value="Create Derby Demo Database" onclick="javascript:createDerbyDemoDb()">&nbsp;&nbsp;&nbsp;
                <input type="button" value="Reload DConfig Cache" onclick="javascript:loadCache()">
            </td>
        </tr>
        <tr>        
            <td align="center" valign="top">
                <a href="/dconfig-ui-jnlp/dconfig.jnlp">Launch DConfig GUI Editor</a>&nbsp;&nbsp;&nbsp;
                <a href="/dconfig-webdemo" >JSF Web Demo</a>&nbsp;&nbsp;&nbsp;
                <a href="/dconfig-webdemo/jsp/dconfigReaderWriter.jsp" >JSP Demo</a>
            </td>
        </tr>
        <tr><td align="center" valign="middle">&nbsp;</td></tr>
  </table>
  <table align="center" border="1" cellpadding="3" cellspacing="0">
        <tr>
            <td>Key Name:</td>
            <td width="260px"><div id="divKeyname" class="autocomplete" style="width: 260px"><input id="keyName" type="text" size="30"><div id="keyNameContainer"></div></div></td>
            <td>Attribute Name: </td>
            <td width="260px"><div id="divAttributename" class="autocomplete" style="width: 260px"><input id="attributeName" type="text" size="30"><div id="attributeNameContainer"></div></div></td>
        </tr>
        <tr>
            <td>Data Type: </td>
            <td><span id="divDataType"></span></td>
            <td>Inherited: </td>
            <td><span id="inherited"></span></td>
        </tr>
        <tr>
            <td>Attribute Value: </td>
            <td  colspan="2"><textarea id="attributeValue" rows="3" cols="60"></textarea></td>
            <td align="left" valign="top"><span id="arrayElements"></span></td>
        </tr>
        <tr>
            <td>Comments: </td><td  colspan="2"><textarea id="comments" rows="3" cols="60"></textarea></td><td>&nbsp;</td>
	</tr>
        <tr>
            <td align="center" valign="middle" colspan="4">
                <input type="button" value="Search from Cache" onclick="javascript:readData()">&nbsp;&nbsp;&nbsp;
                <input type="button" value="Write to DB / Cache" onclick="javascript:writeData()">&nbsp;&nbsp;&nbsp;
                <input type="button" value="Clear Fields" onclick="javascript:clearFields()">
                <input type="checkbox" name="debugMode" value="Debug Mode" onclick="debugModeClicked(this)"/>&nbsp;Debug Mode
                <input type="checkbox" id="hint" value="hint" onclick="hintClicked(this)"/>&nbsp;Hint
            </td>
        </tr>
        <tr>
            <td align="left" valign="middle" colspan="4">
            </td>
        </tr>
  </table>
</form>
<script type="text/javascript">
YAHOO.example.ACFlatData = new function(){
    // Define a custom formatter function
    this.fnCustomFormatter = function(oResultItem, sQuery) {
        var aMarkup = [oResultItem[0]];
        return (aMarkup.join(""));
    };

    // Instantiate one XHR DataSource and define schema as an array:
    // ["Record Delimiter", "Field Delimiter"]
    this.oXHRDS = new YAHOO.widget.DS_XHR("<%=request.getContextPath()%>/getKeyName.do", ["\n", "\t"]); // struts action
    this.oXHRDS.scriptQueryParam = "keyName"; // replace default 'query' parameter
    this.oXHRDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
    this.oXHRDS.maxCacheEntries = 0; // make sure always get from server side
    this.oXHRDS.queryMatchSubset = true;

    // Instantiate keyName AutoComplete
    this.oAutoComp1 = new YAHOO.widget.AutoComplete('keyName','keyNameContainer', this.oXHRDS);
    this.oAutoComp1.queryDelay = 0;
    this.oAutoComp1.delimChar = ""; // use ';' to pick multiple items from list
    this.oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
    this.oAutoComp1.formatResult = this.fnCustomFormatter;
    this.oAutoComp1.maxResultsDisplayed = 50;

    // Instantiate one XHR DataSource and define schema as an array:
    // ["Record Delimiter", "Field Delimiter"]
    var oXHRDS2 = new YAHOO.widget.DS_XHR("<%=request.getContextPath()%>/getAttribute.do", ["\n", "\t"]); // struts action
    oXHRDS2.scriptQueryParam = "attributeName"; // replace default 'query' parameter
    oXHRDS2.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
    oXHRDS2.maxCacheEntries = 0; // make sure always get from server side
    oXHRDS2.queryMatchSubset = true;

    this.attributeBeforeSendQuery = function(sQuery) {
        oXHRDS2.scriptQueryAppend = "keyName=" + Dom.get('keyName').value;
        writeMessage('attributeBeforeSendQuery: ' + oXHRDS2.scriptQueryAppend + ', attributeName: ' + sQuery);
        return sQuery; // returns the original query string
    };

    // Instantiate attributeName AutoComplete
    this.oAutoComp2 = new YAHOO.widget.AutoComplete('attributeName','attributeNameContainer', oXHRDS2);
    this.oAutoComp2.queryDelay = 0;
    this.oAutoComp2.delimChar = ""; // use ';' to pick multiple items from list
    this.oAutoComp2.prehighlightClassName = "yui-ac-prehighlight";
    this.oAutoComp2.formatResult = this.fnCustomFormatter;
    this.oAutoComp2.maxResultsDisplayed = 50;
    this.oAutoComp2.doBeforeSendQuery = this.attributeBeforeSendQuery;    
};
</script>
<script>
var div = document.getElementById('divMessage');
var handleSuccess = function(o){	
    if (debugMode && (o.responseText !== undefined)) {
        var msg = "<li>Transaction id: " + o.tId + "</li>";
        msg += "<li>HTTP status: " + o.status + "</li>";
        msg += "<li>Status code message: " + o.statusText + "</li>";
        msg += "<li>HTTP headers: <ul>" + o.getAllResponseHeaders + "</ul></li>";
        msg += "<li>Server response: " + o.responseText + "</li>";
        writeMessage(msg);                                         
    }
    // Parsing JSON strings can throw a SyntaxError exception, so we wrap 
    // the call in a try catch block        
    var oJSON = null;
    try { 
        oJSON = JSON.parse(o.responseText); 
    } 
    catch (e) { 
        alert("Invalid JSON data"); 
    }
    Dom.get('attributeValue').value = oJSON.attributeValue;
    Dom.get('comments').value = oJSON.comments;
    Dom.get('inherited').innerHTML = oJSON.inherited;
    Dom.get('arrayElements').innerHTML = oJSON.arrayElements;
    Dom.get('divDataType').innerHTML = oJSON.dataType;
}

var handleFailure = function(o) {
    if(o.responseText !== undefined){
        var msg = "<ul><li>Transaction id: " + o.tId + "</li>";
        msg += "<li>HTTP status: " + o.status + "</li>";
        msg += "<li>Status code message: " + o.statusText + "</li></ul>";
        writeMessage(msg);
    }
}

var callback =
{
  success:handleSuccess,
  failure:handleFailure,
  argument: {}
};

function readData(){
    var sUrl = "<%=request.getContextPath()%>/readData.do";
    sUrl += "?keyName=" + Dom.get('keyName').value;
    sUrl += "&attributeName=" + Dom.get('attributeName').value;
    sUrl += "&action=readData";
    clearMessage();
    writeMessage(sUrl);
    writeMessage("<br/>");
    // 'GET' has difficult to pass '#' to server side, replace any '#' to '%23'
    // dconfig-webdemo/readData.do?keyName=config.view.demo.page1&attributeName=max # of keynames failed
    // see http://community.contractwebdevelopment.com/url-escape-characters
    sUrl = sUrl.replace(/#/g,"%23");
    sUrl = sUrl.replace(/ /g,"%20");
    writeMessage(sUrl);
    var request = YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
}
function writeData(){
    var sUrl = "<%=request.getContextPath()%>/writeData.do";
    sUrl += "?keyName=" + Dom.get('keyName').value;
    sUrl += "&attributeName=" + Dom.get('attributeName').value;
    sUrl += "&dataType=" + Dom.get('dataType').value;
    
    var attributeValue = Dom.get('attributeValue').value;
    attributeValue = attributeValue.replace(/&/g,"%26");
    //attributeValue = attributeValue.replace(/^/g,"%5E"); // this will add extra ^ at the beginning if there is non ^ in the string
    attributeValue = attributeValue.replace(/#/g,"%23");
    attributeValue = attributeValue.replace(/;/g,"%3B");
    attributeValue = attributeValue.replace(/ /g,"%20");
    sUrl += "&attributeValue=" + attributeValue;    
    
    sUrl += "&comments=" + Dom.get('comments').value;
    sUrl += "&action=writeData";    
    clearMessage();
    writeMessage(sUrl);
    writeMessage("<br/>");
    // 'GET/POST' has difficult to pass '#' to server side, replace any '#' to '%23'
    // dconfig-webdemo/readData.do?keyName=config.view.demo.page1&attributeName=max # of keynames failed
    // see http://community.contractwebdevelopment.com/url-escape-characters
    sUrl = sUrl.replace(/#/g,"%23");
    sUrl = sUrl.replace(/ /g,"%20");
    writeMessage(sUrl);
    var request = YAHOO.util.Connect.asyncRequest('POST', sUrl, callback);
}
</script>
<script>
var handleSuccess2 = function(o){	
    if (debugMode && (o.responseText !== undefined)) {
        var msg = "<li>Transaction id: " + o.tId + "</li>";
        msg += "<li>HTTP status: " + o.status + "</li>";
        msg += "<li>Status code message: " + o.statusText + "</li>";
        msg += "<li>HTTP headers: <ul>" + o.getAllResponseHeaders + "</ul></li>";
        msg += "<li>Server response: " + o.responseText + "</li>";
        writeMessage(msg);                                         
    }
    // Parsing JSON strings can throw a SyntaxError exception, so we wrap 
    // the call in a try catch block        
    var oJSON = null;
    try { 
        oJSON = JSON.parse(o.responseText); 
    }
    catch (e) { 
        alert("Invalid JSON data"); 
    }
    writeStatus(oJSON.msg);
}

var handleFailure2 = function(o) {
    if(o.responseText !== undefined){
        var msg = "<ul><li>Transaction id: " + o.tId + "</li>";
        msg += "<li>HTTP status: " + o.status + "</li>";
        msg += "<li>Status code message: " + o.statusText + "</li></ul>";
        writeMessage(msg);
    }
}

var ajaxCallback = {
  success:handleSuccess2,
  failure:handleFailure2,
  argument: {}
};

function loadCache(){
    var sUrl = "<%=request.getContextPath()%>/dconfig-servlet/dconfigReader";
    sUrl += "?action=loadCache";
    clearMessage();
    writeMessage(sUrl);
    writeMessage("<br/>");
    var request = YAHOO.util.Connect.asyncRequest('GET', sUrl, ajaxCallback);
}
function createDerbyDemoDb(){
    var sUrl = "<%=request.getContextPath()%>/dconfig-servlet/dconfigReader";
    sUrl += "?action=createDerbyDemoDb";
    clearMessage();
    writeMessage(sUrl);
    writeMessage("<br/>");
    var request = YAHOO.util.Connect.asyncRequest('GET', sUrl, ajaxCallback);
}
</script>

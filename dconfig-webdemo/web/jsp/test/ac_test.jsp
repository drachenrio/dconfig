<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Use AutoComplete to access flat-file data from a web service</title>

<!-- Modified from ac_ysearch_flat.html -->

<style type="text/css">
/*margin and padding on body element
  can introduce errors in determining
  element position and are not recommended;
  we turn them off as a foundation for YUI
  CSS treatments. */
body {
	margin:0;
	padding:0;
}
</style>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/yui/fonts/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/yui/tabview/assets/skins/sam/tabview.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/yui/autocomplete/assets/skins/sam/autocomplete.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/yui/utilities/utilities.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/element/element-beta.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/tabview/tabview.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/yui/autocomplete/autocomplete.js"></script>



<!--begin custom header content for this example-->
<style type="text/css">
/* custom styles for multiple stacked instances with custom formatting */
#example0 { z-index:9001; } /* z-index needed on top instances for ie & sf absolute inside relative issue */
#example1 { z-index:9000; } /* z-index needed on top instances for ie & sf absolute inside relative issue */
.autocomplete { padding-bottom:2em;width:40%; }/* set width of widget here*/
.autocomplete .yui-ac-highlight .sample-quantity,
.autocomplete .yui-ac-highlight .sample-result,
.autocomplete .yui-ac-highlight .sample-query { color:#FFF; }
.autocomplete .sample-quantity { float:right; } /* push right */
.autocomplete .sample-result { color:#A4A4A4; }
.autocomplete .sample-query { color:#000; }
</style>


<!--end custom header content for this example-->

</head>

<body class=" yui-skin-sam">

<h1>Use AutoComplete to access flat-file data from a web service</h1>

<div class="exampleIntro">
	<p>This example's XHR DataSource instance points to a custom PHP script that
returns data from tab-delimited text file. For maximum cache performance, the
DataSource property <code>queryMatchSubset</code> has been enabled, and the cache
itself has been turned up to 60 entries for fewer round trips to the server.</p>

<p>On this page there are three separate AutoComplete instances that all
point to a single DataSource, which further maximizes cache efficiency.
Queries can be delimited using the semi-colon (;) character. For
demonstration purposes, each instance decreases the query delay slightly,
and each instance formats the return data in the container with slightly
different markup.</p>
			
</div>

<!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->

<div id="autocomplete_examples">
    <p><strong>Note:</strong> The flat-file database accessed here has a limited number of terms; for best results, type one-letter at at time and let the AutoComplete instance return &mdash; if you type a full, highly-specifc phrase (such as your name) you'll probably get no results from the small dataset.</p>
	<h2>Sample Search from ws_test.jsp (1 sec query delay):</h2>
	<div id="example0" class="autocomplete">
		<input id="ysearchinput0" type="text">
		<div id="ysearchcontainer0"></div>
	</div>
	<h2>Sample Search from struts-action /dconfig-webdemo/getKeyName.do (0.2 sec query delay):</h2>
	<div id="example1" class="autocomplete">
		<input id="ysearchinput1" type="text">
		<div id="ysearchcontainer1"></div>
	</div>
	<h2>Sample Search from struts-action /dconfig-webdemo/getAttributes.do (0.2 sec query delay):</h2>
	<div id="example2" class="autocomplete">
		<input id="ysearchinput2" type="text">
		<div id="ysearchcontainer2"></div>
	</div>
	<h2>Sample Search from dconfig-servlet (0 sec query delay):</h2>
	<div id="example3" class="autocomplete">
		<input id="ysearchinput3" type="text">
		<div id="ysearchcontainer3"></div>
	</div>
</div>
	
<script type="text/javascript">
YAHOO.example.ACFlatData = new function(){
    // Define a custom formatter function
    this.fnCustomFormatter = function(oResultItem, sQuery) {
        /* // original
        var sKey = oResultItem[0];
        var nQuantity = oResultItem[1];
        var sKeyQuery = sKey.substr(0, sQuery.length);
        var sKeyRemainder = sKey.substr(sQuery.length);
        var aMarkup = ["<div class='sample-result'><div class='sample-quantity'>",
            nQuantity,
            "</div><span class='sample-query'>",
            sKeyQuery,
            "</span>",
            sKeyRemainder,
            "</div>"];
        return (aMarkup.join(""));
        */
        
        var sKey = oResultItem[0];
        var item1 = oResultItem[1];
        var item2 = oResultItem[2];
        var item3 = oResultItem[3];
        
        var aMarkup = [item1, item2, item3];
        return (aMarkup.join(" "));
        
    };
        
    // Instantiate one XHR DataSource and define schema as an array:
    //     ["Record Delimiter",
    //     "Field Delimiter"]        
    this.oACDS0 = new YAHOO.widget.DS_XHR("ws_test.jsp", ["\n", "\t"]); // jsp
    this.oACDS0.scriptQueryParam = "keyName"; // replace default 'query' parameter
    this.oACDS0.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
    this.oACDS0.maxCacheEntries = 60;
    this.oACDS0.queryMatchSubset = true;

    // Instantiate first AutoComplete
    var myInput = document.getElementById('ysearchinput0');
    var myContainer = document.getElementById('ysearchcontainer0');
    this.oAutoComp0 = new YAHOO.widget.AutoComplete(myInput,myContainer,this.oACDS0);
    this.oAutoComp0.delimChar = ";";
    this.oAutoComp0.queryDelay = 1;
    this.oAutoComp0.formatResult = this.fnCustomFormatter;

    // Instantiate second AutoComplete with default delay of 0.2 seconds
    this.oACDS1 = new YAHOO.widget.DS_XHR("<%=request.getContextPath()%>/getKeyName.do", ["\n", "\t"]); // struts action
    this.oACDS1.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
    this.oACDS1.maxCacheEntries = 60;
    this.oACDS1.queryMatchSubset = true;

    this.oAutoComp1 = new YAHOO.widget.AutoComplete('ysearchinput1','ysearchcontainer1', this.oACDS1);
    this.oAutoComp1.delimChar = ";";
    this.oAutoComp1.formatResult = this.fnCustomFormatter;

    // Instantiate second AutoComplete with default delay of 0.2 seconds
    this.oACDS2 = new YAHOO.widget.DS_XHR("<%=request.getContextPath()%>/getAttributes.do", ["\n", "\t"]); // struts action
    this.oACDS2.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
    this.oACDS2.maxCacheEntries = 60;
    this.oACDS2.queryMatchSubset = true;    

    this.oAutoComp2 = new YAHOO.widget.AutoComplete('ysearchinput2','ysearchcontainer2', this.oACDS2);
    this.oAutoComp2.delimChar = ";";
    this.oAutoComp2.formatResult = this.fnCustomFormatter;

    // Instantiate third AutoComplete
    this.oACDS3 = new YAHOO.widget.DS_XHR("<%=request.getContextPath()%>/dconfig-servlet/dconfigReader", ["\n", "\t"]); // servlet
    this.oACDS3.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
    this.oACDS3.maxCacheEntries = 60;
    this.oACDS3.queryMatchSubset = true;

    this.oAutoComp3 = new YAHOO.widget.AutoComplete('ysearchinput3','ysearchcontainer3', this.oACDS3);
    this.oAutoComp3.queryDelay = 0;
    //this.oAutoComp2.delimChar = ";"; // used for pick multiple from list
    this.oAutoComp3.delimChar = "";
    this.oAutoComp3.prehighlightClassName = "yui-ac-prehighlight";
    this.oAutoComp3.formatResult = this.fnCustomFormatter;
};
</script>

<!--END SOURCE CODE FOR EXAMPLE =============================== -->

</body>
</html>

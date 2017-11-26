<form name="admin1" action="<%=request.getContextPath()%>/jsp/dconfigReaderWriter.jsp" method="post">
  <table align="center">
        <tr>
            <th>Startup Database / Create DConfig Demo Db</th>
        </tr>
        <tr>
            <td>
            Open a terminal or command line window.<br/>
            Switch to {dconfig downloaded home}/dconfig-db/src/script<br/>
            Issue the following command:<br/>
            ./derby-startup.sh  (Linux) or<br/>
            derby-startup.bat (Windows)<br/>
            <br/>
            <br/>
            <br/>
            If you haven't set up the demo database, just click the following button to create one.
            <br/>
            </td>
        </tr>
        <tr><td align="center" valign="bottom">&nbsp;</td></tr>
        <tr><td align="center" valign="bottom">&nbsp;</td></tr>
        <tr>
            <td align="center" valign="bottom">
                <input type="button" value="Create Derby Demo Database" onclick="javascript:createDerbyDemoDb()">&nbsp;&nbsp;&nbsp;
            </td>
        </tr>
        <tr><td align="center" valign="bottom">&nbsp;</td></tr>
        <tr><td align="center" valign="bottom">&nbsp;</td></tr>
  </table>
</form>

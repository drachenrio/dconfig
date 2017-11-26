

The dconfig-ui-jnlp project creates a self-contained dconfig-ui-jnlp.war file. 
This war file contains DConfig Editor and web configurations to allow download 
and launch DConfig Editor via Java Web Start and Java Network Launching Protocol 
(JNLP).

dconfig-ui-jnlp.war can be deployed to Apache Tomcat web server or J2EE 1.4+ 
compliant web servers.

A web site can use the following html tag to allow DConfig Editor access over 
the web:

<a href="http://<host:port>/dconfig-ui-jnlp/dconfig.jnlp">
<img border=0 src="webstart_small.gif">Launch Dynamic Configuration Editor</a>

Where, host is the hostname of the web server in which dconfig-ui-jnlp.war is 
deployed. port is the port number used.
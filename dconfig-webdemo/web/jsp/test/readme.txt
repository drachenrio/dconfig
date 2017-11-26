# Use the following url to test 'web services', those methods are used by ajax request
# Replace 'localhost:8080' with your actual host and port
# Some ajax / autocomplete usages can be found in web/jsp/inc-dconfigReaderWriter.jsp

# Get key list through struts action w/ tiles and jsp
http://localhost:8080/dconfig-webdemo/getKeyName.do?keyName=con

# Get attribute list through struts action wo/ tiles or jsp
http://localhost:8080/dconfig-webdemo/getAttribute.do?keyName=config.datasource.oracle&attributeName=*

# Get data through servlet
http://localhost:8080/dconfig-webdemo/dconfig-servlet/dconfigReader

# Get attribute through struts action wo/ tiles or jsp
http://localhost:8080/dconfig-webdemo/readData.do?keyName=config.view.demo.page1&attributeName=tab%20names


                    Table of Contents for dconfig-db

                            Jonathan Luo

                        Last update: 4/6/2007


lib/        Derby database server and client jars.
src/script  Derby database startup and shutdown scripts

            to start derby database
            cd src/script
            
            chmod u+x *.sh (Linux)
            
            run 
                ./derby-startup.sh (Linux)
            or
                derby-startup.bat (Windows)

            to shutdown derby database
            run 
                ./derby-shutdown.sh (Linux)
            or
                derby-shutdown.bat (Windows)

src/sql/1.0 sql scripts for major databases.
            The scripts are used to create dconfig database tables, 
            optionally to create sample data.

            If you could not find the scripts for your database or the
            version you're using is different from the scripts supported,
            you may need to change the scripts to fit your need.

DWH AKTIN Database
=========================

The AKTIN database is managed through liquibase. The
database changelog file is located in `src/main/resources`.

Query Database
---------------
```
CREATE TABLE prefs(
	property	VARCHAR(32)	NOT NULL,
	user_access	VARCHAR(4)	NOT NULL, -- R(eadable) W(riteable) WO(write only) H(hidden)
	last_modified	TIMESTAMP	NOT NULL, -- last modified timestamp
	modified_by	VARCHAR(32)	    NULL, -- user name or NULL for system user
	type	VARCHAR(16) NOT NULL,
	value	TEXT,	
	PRIMARY KEY(property)
);
```

Testing
-------

Test the database scripts with jdbc:hsqldb:mem:tempdb... 
e.g. similar to `http://stackoverflow.com/questions/11396219/init-database-for-test-purpose-during-maven-test-phase`


-- for param names, see dwh-gateway/README.md

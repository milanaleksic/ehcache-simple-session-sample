Very simple proof of concept for session keeping in EHCache.

EHCache is configured to push items to disk so as a result this allows data keeping even in case of hard PC restart.

Please take this proof of concept not as a full-fledged solution but as just one way how to do it.

--------------------

How to test: after you started application, log in as admin/admin and press Refresh to notice that counter increases
on each request. Even if you restart application it still has the same number since session does not get invalidated.


Tested on Apache Tomcat 7.0.22
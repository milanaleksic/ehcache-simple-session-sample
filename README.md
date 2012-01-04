Very simple proof of concept for sessions keeping in EHCache.

Since EHCache is configured to push items to disk as a result this allows keeping of data even on even hard PC reset.

Please take this proof of concept not as a full-fledged solution but just one way how to do it.

--------------------

How to test: after you started application, log in as admin/admin and press Refresh to notice that counter increases
on each request. Even if you restart application it still has the same number ("session does not get cleared").


Tested on Apache Tomcat 7.0.22
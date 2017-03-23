select distinct fname,minit,lname,t1,t2,t3 
from mccann.employee, (select fname as t1, minit as t2,lname as t3, ssn as mssn from mccann.employee) 
where mssn=superssn
union select fname,minit,lname,null,null,null from mccann.employee where superssn is null;

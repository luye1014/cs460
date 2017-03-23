select fname, minit, lname from mccann.employee where salary in (select max(salary) from mccann.employee);

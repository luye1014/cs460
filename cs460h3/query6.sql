select dname from mccann.department, (select dno from mccann.employee,(select essn from mccann.dependent where dependent_name='Alice') where ssn=essn)
where dnumber = dno;

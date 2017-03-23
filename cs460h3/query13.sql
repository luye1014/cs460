select dname from mccann.department where dnumber in
(select dno 
	from mccann.employee,(select sex as s, dno as no from mccann.employee) 
	where sex='M' and s='F' and dno=no
);

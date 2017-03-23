select bdate from mccann.employee 
where ssn in(select mgrssn 
from mccann.department 
where dnumber in (select dnum from mccann.project where plocation='Stafford'));

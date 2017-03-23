select ssn from mccann.employee where dno='5'
union
select ssn from mccann.employee where ssn in (select superssn from mccann.employee where dno='5');

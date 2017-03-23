select dependent_name from mccann.dependent where sex='M' and essn in (select ssn from mccann.employee where sex='M');

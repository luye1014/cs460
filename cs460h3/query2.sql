select ssn from mccann.employee, mccann.works_on where works_on.essn = employee.ssn and pno=10 order by ssn desc;

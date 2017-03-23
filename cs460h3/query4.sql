select dlocation from mccann.dept_locations where dnumber in (select dnumber from mccann.department where dname='Research');

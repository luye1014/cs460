select pnumber,pname,numOfEm from mccann.project,
(select pno, count(*) as numOfEm from mccann.works_on group by pno having count(pno) >2)
where pnumber=pno;

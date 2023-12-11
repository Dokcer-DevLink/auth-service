FROM mysql:8.0

COPY ./sqls/data.sql /docker-entrypoint-initdb.d/
ADD ./my.cnf etc/mysql/conf.d/my.cnf

CMD ["mysqld"]
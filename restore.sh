rm -f backup_biblia-backend-postgresql.sql &&
  wget https://www.dropbox.com/s/cutkzbftr365mlr/backup_biblia-backend-postgresql.sql
PGPASSWORD=postgres psql -h localhost -d postgres -U postgres -p 5432 -q -f reset_db.sql &&
  PGPASSWORD=postgres psql -h localhost -d postgres -U postgres -p 5432 -q -f backup_biblia-backend-postgresql.sql &&
    PGPASSWORD=postgres psql -h localhost -d postgres -U postgres -p 5432 -q -f create-function-unaccent.sql
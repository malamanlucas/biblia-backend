PGPASSWORD=postgres psql -h localhost -d postgres -U postgres -p 5432 -q -f reset_db.sql &&
PGPASSWORD=postgres psql -h localhost -d postgres -U postgres -p 5432 -q -f backup_biblia-backend-postgresql.sql

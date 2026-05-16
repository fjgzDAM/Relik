-- Backup recommended before running
CREATE TABLE IF NOT EXISTS tyacimiento_backup AS SELECT * FROM tyacimiento;

-- Show duplicates
SELECT nombre, COUNT(*) AS cnt FROM tyacimiento GROUP BY nombre HAVING cnt > 1;

-- Delete duplicates keeping the lowest id
DELETE t1 FROM tyacimiento t1
INNER JOIN tyacimiento t2
  ON t1.nombre = t2.nombre
  AND t1.id_yacimiento > t2.id_yacimiento;

-- Check again
SELECT nombre, COUNT(*) AS cnt FROM tyacimiento GROUP BY nombre HAVING cnt > 1;


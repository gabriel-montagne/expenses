-- :name expenses-read :? :*
SELECT * FROM expenses;

-- :name by-userid-expenses-read :? :*
SELECT * FROM expenses
WHERE userid = :userid;

-- :name by-id-expense-read :? :1
SELECT * FROM expenses
WHERE userid = :userid AND id = :id;

-- :name expense-insert! :<! :1
INSERT INTO expenses(userid, date, description, amount, comment)
VALUES (:userid, to_timestamp(:date, 'YYYY-MM-DD HH24:MI'), :description, :amount, :comment)
RETURNING *;

-- :name expense-update! :<! :1
UPDATE expenses SET
  userid = :userid, date = to_timestamp(:date, 'YYYY-MM-DD HH24:MI'), description = :description, amount = :amount, comment = :comment
WHERE id = :id AND userid = :userid
RETURNING *;

-- :name expense-remove! :! :1
DELETE FROM expenses
WHERE id = :id AND userid=:userid;

-- :name by-userid-expenses-report :? :*
SELECT to_char(date, 'DD-MM-YYYY HH:mm') as date_str,
  description, to_char(amount, '99999') as amount_str, comment,
  extract(week from date) as week, extract(year from date) as year, amount FROM expenses
WHERE userid = :userid AND extract(week from date) = :week AND extract(year from date) = :year;

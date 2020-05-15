/* INSERT INTO BASE TABLES STATEMENTS */

-- :name add-account! :<! :raw
-- :doc creates a new account, returns account_id
INSERT INTO Account
(email, lastlogin, name, role, username)
VALUES (:email, :lastlogin, :name, :role, :username)
RETURNING account_id

-- :name add-tword! :<! :raw
-- :doc creates a new tword, returns tword_id
INSERT INTO TWord
(account_id, tword, src_lang, dest_lang)
VALUES (:account_id, :tword, :src_lang, :dest_lang)
RETURNING tword_id

-- :name add-collection! :<! :raw
-- :doc creates a new collection, returns collection_id
INSERT INTO Collection
(name, published, archived)
VALUES (:name, :published, :archived)
RETURNING collection_id

-- :name add-course! :<! :raw
-- :doc creates a new course, returns course_id
INSERT INTO Course
(department, catalog_number, section_number)
VALUES (:department, :catalog_number, :section_number)
RETURNING course_id

-- :name add-content! :<! :raw
-- :doc creates a new content, returns content_id
INSERT INTO Content
(collection_id, name, type, requester_email, thumbnail, copyrighted, physical_copy_exists, full_video, published, date_validated, metadata)
VALUES (:collection_id, :name, :type, :requester_email, :thumbnail, :copyrighted, :physical_copy_exists, :full_video, :published, :date_validated, :metadata)
RETURNING content_id

-- :name add-file! :<! :raw
-- :doc creates a new file, returns file_id
INSERT INTO File
(filepath, mime, metadata)
VALUES (:filepath, :mime, :metadata)
RETURNING file_id

/* INSERT INTO MANY-TO-MANY TABLES STATEMENTS */

-- :name add-account-collection! :! :n
-- :doc connects account and collection, given account_id and collection_id
INSERT INTO Account_Collection
(account_id, collection_id, role)
VALUES (:account_id, :collection_id, :role)

-- :name add-collection-course! :! :n
-- :doc connects collection and course, given collection_id and course_id
INSERT INTO Collection_Course
(collection_id, course_id)
VALUES (:collection_id, :course_id)


/* SELECT BY ID STATEMENTS */

-- :name get-account :? :1
-- :doc retrieves account with given account_id
SELECT * FROM Account
WHERE account_id = :account_id

-- :name get-tword :? :1
-- :doc retreives tword with given tword_id
SELECT * FROM TWord
WHERE tword_id = :tword_id

-- :name get-collection :? :1
-- :doc retreives collection with given collection_id
SELECT * FROM Collection
WHERE collection_id = :collection_id

-- :name get-course :? :1
-- :doc retreives course with given course_id
SELECT * FROM Course
WHERE course_id = :course_id

-- :name get-content :? :1
-- :doc retreives content with given content_id
SELECT * FROM Content
WHERE content_id = :content_id

-- :name get-file :? :1
-- :doc retreives file with given file_id
SELECT * FROM File
WHERE file_id = :file_id


/* SELECT FROM MANY-TO-MANY TABLES STATEMENTS */

-- :name get-collections-by-account :? :*
-- :doc retrieves all collections connected to given account
SELECT c.*
FROM Account as a
INNER JOIN Account_Collection as ac
  ON a.account_id = ac.account_id
INNER JOIN Collection as c
  ON ac.collection_id = c.collection_id
WHERE a.account_id = :account_id

-- :name get-collections-by-course :? :*
-- :doc retreives all collections connected to given course
SELECT cll.*
FROM Course as crs
INNER JOIN Collection_Course as cllcrs
  ON crs.course_id = cllcrs.course_id
INNER JOIN Collection as cll
  ON cllcrs.collection_id = cll.collection_id
WHERE crs.course_id = :course_id

/* UPDATE STATEMENTS */



/* DELETE STATEMENTS */

-- :name delete-collection :? :n
-- :doc deletes collection with given collection_id
DELETE FROM Collection
WHERE collection_id = :collection_id


/*-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id
*/

(ns y-video-postgres-swagger.dbaccess.access
  (:require [y-video-postgres-swagger.db.core :as db]))

(declare associate_user_with_collection)

(defn to_uuid
  [text_in]
  (java.util.UUID/fromString text_in))

(defn check_collection_id
  "Checks if collection exists in database"
  [id]
  (let [result (db/get-collection {:id id})]
    (not (nil? result))))

(defn clear_database
  "Clears entire database, requires correct password"
  [password]
  (if (= password "17e6b095-c7a7-471f-8f89-58e0a57f89f3")
    ; clear database
    (db/delete-all)))

;; Create
(defn add_user
  "Adds new user to database"
  [user_without_id]
  (str (:id (get (db/add-user! user_without_id) 0))))


;; Retrieve
(defn get_user
  "Retrieves user with given id"
  [id]
  (update (db/get-user {:id id}) :id str))


;; Update
(defn update_user
  "Updates user with given information"
  [id new_user]
  (db/update-user (into new_user {:id id})))

;; Delete
(defn delete_user
  "Deletes user with given id"
  [id]
  (db/delete-user {:id id}))


;; Create
(defn add_collection
  "Adds collection with current user as owner"
  [body]
  (let [collection_id (:id (get (db/add-collection! {:collection_name (:name body) :published false :archived false}) 0))]
    (try
      (db/add-user-collection! {:user_id (to_uuid (:user_id body)) :collection_id collection_id
                                :account_role 0})
      (catch Exception e
        (.getCause e)))
    (str collection_id)))


;; Retrieve
(defn get_collection
  "Retrieves collection with given id"
  [id]
  (update (db/get-collection {:id id}) :id str))


;; Update
(defn update_collection
  "Updates collection with given information"
  [id new_collection]
  (db/update-collection (into new_collection {:id id})))

;; Delete
(defn delete_collection
  "Deletes collection with given id"
  [id]
  (db/delete-collection {:id id}))



;; Create
(defn add_content
  "Adds new content to database"
  [content_without_id]
  (str (:id (get (db/add-content! content_without_id) 0))))


;; Retrieve
(defn get_content
  "Retrieves content with given id"
  [id]
  (update (db/get-content {:id id}) :id str))


;; Update
(defn update_content
  "Updates content with given information"
  [id new_content]
  (db/update-content (into new_content {:id id})))

;; Delete
(defn delete_content
  "Deletes content with given id"
  [id]
  (db/delete-content {:id id}))


;; Create
(defn add_annotation
  "Adds new annotation to database"
  [annotation_without_id]
  (str (:id
         (get
           (db/add-annotation!
             (update
               (update annotation_without_id :content_id to_uuid) :collection_id to_uuid)) 0))))


;; Retrieve
(defn get_annotation
  "Retrieves annotation with given id"
  [id]
  (update (update (update (db/get-annotation {:id id}) :id str) :content_id str) :collection_id str))


;; Update
(defn update_annotation
  "Updates annotation with given information"
  [id new_annotation]
  (db/update-annotation (update (update (into new_annotation {:id id}) :content_id to_uuid) :collection_id to_uuid)))

;; Delete
(defn delete_annotation
  "Deletes annotation with given id"
  [id]
  (db/delete-annotation {:id id}))


;; Create
(defn add_course
  "Adds new course to database"
  [course_without_id]
  (str (:id (get (db/add-course! course_without_id) 0))))


;; Retrieve
(defn get_course
  "Retrieves course with given id"
  [id]
  (update (db/get-course {:id id}) :id str))


;; Update
(defn update_course
  "Updates course with given information"
  [id new_course]
  (db/update-course (into new_course {:id id})))

;; Delete
(defn delete_course
  "Deletes course with given id"
  [id]
  (db/delete-course {:id id}))


(defn get_collections
  "Retrieve all collections available to given user_id"
  [user_id]
  (map #(get_collection (get % :collection_id)) (db/get-collections-by-user {:user_id user_id})))

(defn OLD_add_collection
  "Add collection with current user as owner"
  [body]
  (let [collection_id (:id (get (db/add-collection! {:collection_name (:name body) :published false :archived false}) 0))]
    (try
      (db/add-user-collection! {:user_id (to_uuid (:user_id body)) :collection_id collection_id
                                :account_role 0})
      (catch Exception e
        (.getCause e)))
    (str collection_id)))

(defn add_collection_old
  "Add collection with given values, adds associated users, contents, courses"
  [current_user_id name published archived assoc_users assoc_content assoc_courses]
  (try
    (def collection_id (:collection_id (get (db/add-collection! {:collection_name name :published published :archived archived}) 0)))
    (db/add-user-collection! {:user_id current_user_id :collection_id collection_id :account_role 0})
    (get_collection collection_id)
   (catch Exception e
     {:message (.getCause e)})))

(defn associate_user_with_collection
  "Adds collection to user's assoc_collections"
  [user_id collection_id account_role]
  (db/add-user-collection! {:user_id user_id :collection_id collection_id
                               :account_role account_role}))


(defn get_contents_by_collection
  [id]
  (let [all_contents (db/get-contents-by-collection {:collection_id id})]
    (map #(update (update % :id str) :collection_id str) all_contents)))

(defn OLD_add_content
  "Adds new content to database"
  [content_without_id]
  (str (:id (get (db/add-content! (update content_without_id :collection_id to_uuid)) 0))))

(defn OLD_get_content
  "Retrieve content with given id"
  [id]
  (update (update (db/get-content {:id id}) :id str) :collection_id str))

(defn add_view_to_content
  "Adds a view to specified content"
  [id]
  (if (= 1 (db/add-view-to-content {:id id}))
    true
    false))

(defn add_course
  "Adds new course to database"
  [course_without_id]
  (str (:id (get (db/add-course! course_without_id) 0))))

(defn get_course
  "Retrieve course with given id"
  [id]
  (update (db/get-course {:id id}) :id str))

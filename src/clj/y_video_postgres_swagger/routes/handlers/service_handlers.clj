(ns y-video-postgres-swagger.routes.handlers.service_handlers
  (:require
    [y-video-postgres-swagger.dbaccess.access :as db-access]
    [y-video-postgres-swagger.models :as models]))


(def echo-patch
  {:summary "echo parameter post"
   :parameters {:body models/echo_patch}
   :responses {200 {:body {:message string?}}}
   :handler (fn [ignore-me] {:status 200 :body {:message "this route does nothing!"}})})

(def connect-collection-and-course ;; Non-functional
  {:summary "Connects specified collection and course (bidirectional)"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def search-by-term ;; Non-functional
  {:summary "Searches users, collections, and content by search term"
   :parameters {:query {:query_term string?}}
   :responses {200 {:body {:users [models/user]
                           :collections [models/collection]
                           :courses [models/course]
                           :contents [models/content]}}}
   :handler (fn [{{{:keys [query_term]} :query} :parameters}]
             (let [result (db-access/search_by_term query_term)]
               {:status 200
                :body result}))})


(def user-create
  {:summary "Creates a new user - FOR DEVELOPMENT ONLY"
   :parameters {:body models/user_without_id}
   :responses {200 {:body {:message string?
                           :id string?}}
               409 {:body {:message string?}}}
   :handler (fn [{{:keys [body]} :parameters}]
             (try {:status 200
                   :body {:message "1 user created"
                          :id (db-access/add_user body)}}
               (catch Exception e
                 {:status 409
                  :body {:message "unable to create user, email likely taken"}})))})

(def user-get-by-id
  {:summary "Retrieves specified user"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body models/user}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [user_result (db-access/get_user id)]
              (if (= "" (:id user_result))
                {:status 404
                 :body {:message "requested user not found"}}
                {:status 200
                 :body user_result})))})

(def user-update
  {:summary "Updates specified user"
   :parameters {:path {:id uuid?} :body models/user_without_id}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path :keys [body]} :parameters}]
             (let [result (db-access/update_user id body)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested user not found"}}
                {:status 200
                 :body {:message (str result " users updated")}})))})

(def user-delete
  {:summary "Deletes specified user"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/delete_user id)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested user not found"}}
                {:status 200
                 :body {:message (str result " users deleted")}})))})


(def user-get-loggged-in ;; Non-functional
  {:summary "Retrieves the current logged-in user"
   :parameters {:query {:user_id string?}}
   :responses {200 {:body {:user_id string? :email string? :lastlogin string? :name string? :role int? :username string?}}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :query} :parameters}]
             (let [user_result (db-access/get_user id)]
              (if (nil? user_result)
                {:status 404
                 :body {:message "requested user not found"}}
                {:status 200
                 :body {:message user_result}})))})


(def user-get-all-collections ;; Non-functional
  {:summary "Retrieves all collections for specified user"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def user-word-create
  {:summary "Creates new word under the specified user"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def user-word-get-by-id
  {:summary "Retrieves the specified word under specified user"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def user-word-update
  {:summary "Updates specified word under specified user"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def user-word-delete
  {:summary "Deletes specified word under specified user"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def user-get-all-words
  {:summary "Retrieves all words under specified user"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})


(def collection-get-all-by-user ;; Non-functional
  {:summary "Retrieves collection info for all collections available to the current user_id"
   :parameters {}
   :responses {200 {:body [{:collection_id string? :name string? :published boolean? :archived boolean?}]}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [user_id]} :query} :parameters}]
             (let [collection_result (db-access/get_collections user_id)]
              (if (nil? collection_result)
                {:status 404
                 :body {:message "requested collection not found"}}
                {:status 200
                 :body collection_result})))})

(def collection-create ;; Non-functional
  {:summary "Creates a new collection with the given (temp) user as an owner"
   :parameters {:body {:name string? :user_id string?}}
   :responses {200 {:body {:message string?
                           :id string?}}
               409 {:body {:message string?}}}
   :handler (fn [{{:keys [body]} :parameters}]
             (try {:status 200
                   :body {:message "1 collection created"
                          :id (db-access/add_collection body)}}
               (catch Exception e
                 {:status 409
                  :body {:message (e)}})))})

(def collection-get-by-id ;; Not tested
  {:summary "Retrieves specified collection"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body models/collection}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [res (db-access/get_collection id)]
              (if (= "" (:id res))
                {:status 404
                 :body {:message "requested collection not found"}}
                {:status 200
                 :body res})))})

(def collection-update ;; Non-functional
  {:summary "Updates the specified collection"
   :parameters {:path {:id uuid?} :body models/collection_without_id}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path :keys [body]} :parameters}]
             (let [result (db-access/update_collection id body)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested collection not found"}}
                {:status 200
                 :body {:message (str result " collections updated")}})))})

(def collection-delete ;; Non-functional
  {:summary "Deletes the specified collection"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/delete_collection id)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested collection not found"}}
                {:status 200
                 :body {:message (str result " collections deleted")}})))})

(def collection-add-user
  {:summary "Adds user to specified collection"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def collection-get-all-contents ;; Non-functional
  {:summary "Retrieves all the contents for the specified collection"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body [models/content]}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (if (db-access/check_collection_id id)
               (let [result (db-access/get_contents_by_collection id)]
                 {:status 200
                  :body result})
               {:status 404
                :body {:message "collection not found"}}))})
(def collection-get-all-courses
  {:summary "Retrieves all courses for the specified collection"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def collection-get-all-users
  {:summary "Retrieves all users for the specified collection"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})


(def content-create ;; Non-functional
  {:summary "Creates new content"
   :parameters {:body models/content_without_id}
   :responses {200 {:body {:message string?
                           :id string?}}}
   :handler (fn [{{:keys [body]} :parameters}]
             (try {:status 200
                   :body {:message "1 content created"
                          :id (db-access/add_content body)}}
               (catch Exception e
                 {:status 409
                  :body {:message "unable to create content, likely bad collection id"
                         :error e}})))})

(def content-get-by-id
  {:summary "Retrieves specified content"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body models/content}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [content_result (db-access/get_content id)]
              (if (= "" (:id content_result))
                {:status 404
                 :body {:message "requested content not found"}}
                {:status 200
                 :body content_result})))})

(def content-update ;; Non-functional
  {:summary "Updates the specified content"
   :parameters {:path {:id uuid?} :body models/content_without_id}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path :keys [body]} :parameters}]
             (let [result (db-access/update_content id body)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested content not found"}}
                {:status 200
                 :body {:message (str result " contents updated")}})))})

(def content-delete ;; Non-functional
  {:summary "Deletes the specified content"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/delete_content id)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested content not found"}}
                {:status 200
                 :body {:message (str result " contents deleted")}})))})

(def content-connect-file ;; Non-functional
  {:summary "Connects specified file and content (bidirectional)"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})
(def content-get-all-files ;; Non-functional
  {:summary "Retrieves all files connected to specified content"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def content-add-view ;; Non-functional
  {:summary "Adds 1 view to specified content"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/add_view_to_content id)]
              (if result
                {:status 200
                 :body {:message "view successfully added"}}
                {:status 404
                 :body {:message "requested content not found"}})))})

(def annotation-create ;; Non-functional
  {:summary "Creates new annotation"
   :parameters {:body models/annotation_without_id}
   :responses {200 {:body {:message string?
                           :id string?}}}
   :handler (fn [{{:keys [body]} :parameters}]
             (try {:status 200
                   :body {:message "1 annotation created"
                          :id (db-access/add_annotation body)}}
               (catch Exception e
                 {:status 409
                  :body {:message "unable to create annotation, likely bad collection id"
                         :error e}})))})

(def annotation-get-by-id
  {:summary "Retrieves specified annotation"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body models/annotation}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [annotation_result (db-access/get_annotation id)]
              (if (= "" (:id annotation_result))
                {:status 404
                 :body {:message "requested annotation not found"}}
                {:status 200
                 :body annotation_result})))})

(def annotation-update ;; Non-functional
  {:summary "Updates the specified annotation"
   :parameters {:path {:id uuid?} :body models/annotation_without_id}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path :keys [body]} :parameters}]
             (let [result (db-access/update_annotation id body)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested annotation not found"}}
                {:status 200
                 :body {:message (str result " annotations updated")}})))})

(def annotation-delete ;; Non-functional
  {:summary "Deletes the specified annotation"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/delete_annotation id)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested annotation not found"}}
                {:status 200
                 :body {:message (str result " annotations deleted")}})))})

(def course-create ;; Non-functional
  {:summary "Creates a new course"
   :parameters {:body models/course_without_id}
   :responses {200 {:body {:message string?
                           :id string?}}
               409 {:body {:message string?}}}
   :handler (fn [{{:keys [body]} :parameters}]
             (try {:status 200
                   :body {:message "1 course created"
                          :id (db-access/add_course body)}}
               (catch Exception e
                 {:status 409
                  :body {:message (e)}})))})

(def course-get-by-id ;; Non-functional
  {:summary "Retrieves specified course"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body models/course}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/get_course id)]
              (if (= "" (:id result))
                {:status 404
                 :body {:message "requested course not found"}}
                {:status 200
                 :body result})))})

(def course-update ;; Non-functional
  {:summary "Updates the specified course"
   :parameters {:path {:id uuid?} :body models/course_without_id}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path :keys [body]} :parameters}]
             (let [result (db-access/update_course id body)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested course not found"}}
                {:status 200
                 :body {:message (str result " courses updated")}})))})

(def course-delete ;; Non-functional
  {:summary "Deletes the specified course"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/delete_course id)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested course not found"}}
                {:status 200
                 :body {:message (str result " courses deleted")}})))})

(def course-get-all-collections ;; Non-functional
  {:summary "Retrieves all collections connected to specified course"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

(def file-create
  {:summary "Creates a new file - FOR DEVELOPMENT ONLY"
   :parameters {:body models/file_without_id}
   :responses {200 {:body {:message string?
                           :id string?}}
               409 {:body {:message string?}}}
   :handler (fn [{{:keys [body]} :parameters}]
             (try {:status 200
                   :body {:message "1 file created"
                          :id (db-access/add_file body)}}
               (catch Exception e
                 {:status 409
                  :body {:message "unable to create file, email likely taken"}})))})

(def file-get-by-id
  {:summary "Retrieves specified file"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body models/file}
               404 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [file_result (db-access/get_file id)]
              (if (= "" (:id file_result))
                {:status 404
                 :body {:message "requested file not found"}}
                {:status 200
                 :body file_result})))})

(def file-update
  {:summary "Updates specified file"
   :parameters {:path {:id uuid?} :body models/file_without_id}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path :keys [body]} :parameters}]
             (let [result (db-access/update_file id body)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested file not found"}}
                {:status 200
                 :body {:message (str result " files updated")}})))})

(def file-delete
  {:summary "Deletes specified file"
   :parameters {:path {:id uuid?}}
   :responses {200 {:body {:message string?}}}
   :handler (fn [{{{:keys [id]} :path} :parameters}]
             (let [result (db-access/delete_file id)]
              (if (= 0 result)
                {:status 404
                 :body {:message "requested file not found"}}
                {:status 200
                 :body {:message (str result " files deleted")}})))})


(def file-get-all-contents ;; Non-functional
  {:summary "Retrieves all contents that use the specified file"
   :parameters {}
   :responses {200 {:body {:message string?}}}
   :handler (fn [args] {:status 200
                        :body {:message "placeholder"}})})

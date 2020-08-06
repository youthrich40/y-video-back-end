(ns y-video-back.routes.permissions.account-type.admin-tests
  (:require
    [y-video-back.config :refer [env]]
    [clojure.test :refer :all]
    [ring.mock.request :refer :all]
    [y-video-back.handler :refer :all]
    [y-video-back.db.test-util :as tcore]
    [muuntaja.core :as m]
    [clojure.java.jdbc :as jdbc]
    [mount.core :as mount]
    [y-video-back.utils.model-generator :as g]
    [y-video-back.utils.route-proxy.proxy :as rp]
    [y-video-back.db.core :refer [*db*] :as db]
    [y-video-back.db.contents :as contents]
    [y-video-back.db.collections-contents-assoc :as collection-contents-assoc]
    [y-video-back.db.users-by-collection :as users-by-collection]
    [y-video-back.db.collections-courses-assoc :as collection-courses-assoc]
    [y-video-back.db.collections :as collections]
    [y-video-back.db.resources :as resources]
    [y-video-back.db.courses :as courses]
    [y-video-back.db.files :as files]
    [y-video-back.db.user-collections-assoc :as user-collections-assoc]
    [y-video-back.db.user-courses-assoc :as user-courses-assoc]
    [y-video-back.db.users :as users]
    [y-video-back.db.words :as words]
    [y-video-back.utils.utils :as ut]
    [y-video-back.utils.db-populator :as db-pop]
    [y-video-back.user-creator :as uc]))

(declare ^:dynamic *txn*)

(use-fixtures
  :once
  (fn [f]
    (mount/start #'y-video-back.config/env
                 #'y-video-back.handler/app
                 #'y-video-back.db.core/*db*)
    (f)))

(tcore/basic-transaction-fixtures
  (mount.core/start #'y-video-back.handler/app))


;get: /api/admin/user/{term}
(deftest admin-search-users
  (testing "admin, admin-search-users"
    (let [user-one (db-pop/add-user (:admin env))
          res (rp/search-by-user (uc/user-id-to-session-id (:id user-one))
                                 "test")]
      (is (= 200 (:status res)))))
  (testing "lab assistant, admin-search-users"
    (let [user-one (db-pop/add-user (:lab-assistant env))
          res (rp/search-by-user (uc/user-id-to-session-id (:id user-one))
                                 "test")]
      (is (= 401 (:status res)))))
  (testing "instructor, admin-search-users"
    (let [user-one (db-pop/add-user (:instructor env))
          res (rp/search-by-user (uc/user-id-to-session-id (:id user-one))
                                 "test")]
      (is (= 401 (:status res)))))
  (testing "student, admin-search-users"
    (let [user-one (db-pop/add-user (:student env))
          res (rp/search-by-user (uc/user-id-to-session-id (:id user-one))
                                 "test")]
      (is (= 401 (:status res))))))


;get: /api/admin/collection/{term}
(deftest admin-search-collections
  (testing "admin, admin-search-collections"
    (let [user-one (db-pop/add-user (:admin env))
          res (rp/search-by-collection (uc/user-id-to-session-id (:id user-one))
                                       "test")]
      (is (= 200 (:status res)))))
  (testing "lab assistant, admin-search-collections"
    (let [user-one (db-pop/add-user (:lab-assistant env))
          res (rp/search-by-collection (uc/user-id-to-session-id (:id user-one))
                                       "test")]
      (is (= 401 (:status res)))))
  (testing "instructor, admin-search-collections"
    (let [user-one (db-pop/add-user (:instructor env))
          res (rp/search-by-collection (uc/user-id-to-session-id (:id user-one))
                                       "test")]
      (is (= 401 (:status res)))))
  (testing "student, admin-search-collections"
    (let [user-one (db-pop/add-user (:student env))
          res (rp/search-by-collection (uc/user-id-to-session-id (:id user-one))
                                       "test")]
      (is (= 401 (:status res))))))

;get: /api/admin/content/{term}
(deftest admin-search-contents
  (testing "admin, admin-search-contents"
    (let [user-one (db-pop/add-user (:admin env))
          res (rp/search-by-content (uc/user-id-to-session-id (:id user-one))
                                    "test")]
      (is (= 200 (:status res)))))
  (testing "lab assistant, admin-search-contents"
    (let [user-one (db-pop/add-user (:lab-assistant env))
          res (rp/search-by-content (uc/user-id-to-session-id (:id user-one))
                                    "test")]
      (is (= 401 (:status res)))))
  (testing "instructor, admin-search-contents"
    (let [user-one (db-pop/add-user (:instructor env))
          res (rp/search-by-content (uc/user-id-to-session-id (:id user-one))
                                    "test")]
      (is (= 401 (:status res)))))
  (testing "student, admin-search-contents"
    (let [user-one (db-pop/add-user (:student env))
          res (rp/search-by-content (uc/user-id-to-session-id (:id user-one))
                                    "test")]
      (is (= 401 (:status res))))))

;get: /api/admin/resource/{term}
(deftest admin-search-resources
  (testing "admin, admin-search-resources"
    (let [user-one (db-pop/add-user (:admin env))
          res (rp/search-by-resource (uc/user-id-to-session-id (:id user-one))
                                     "test")]
      (is (= 200 (:status res)))))
  (testing "lab assistant, admin-search-resources"
    (let [user-one (db-pop/add-user (:lab-assistant env))
          res (rp/search-by-resource (uc/user-id-to-session-id (:id user-one))
                                     "test")]
      (is (= 401 (:status res)))))
  (testing "instructor, admin-search-resources"
    (let [user-one (db-pop/add-user (:instructor env))
          res (rp/search-by-resource (uc/user-id-to-session-id (:id user-one))
                                     "test")]
      (is (= 401 (:status res)))))
  (testing "student, admin-search-resources"
    (let [user-one (db-pop/add-user (:student env))
          res (rp/search-by-resource (uc/user-id-to-session-id (:id user-one))
                                     "test")]
      (is (= 401 (:status res))))))

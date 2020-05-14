(ns y-video-postgres-swagger.test.db.core
  (:require
   [y-video-postgres-swagger.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [next.jdbc :as jdbc]
   [y-video-postgres-swagger.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'y-video-postgres-swagger.config/env
     #'y-video-postgres-swagger.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-account
  (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
    (let [res (db/add-account!
              t-conn
              {:email "test@gmail.com"
               :lastlogin "never"
               :name "Buster"
               :role 0
               :username "b1"})]
               (is (= 1 (count res)))
               (is (= {
                 :account_id (:account_id (get res 0))
                 :email "test@gmail.com"
                 :lastlogin "never"
                 :name "Buster"
                 :role 0
                 :username "b1"
                 } (db/get-account t-conn {:account_id (:account_id (get res 0))}))))))

(deftest test-tword
 (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
   (let [account_id nil tword "a word!" src_lang "ru" dest_lang "en"]
   (let [res (db/add-tword!
             t-conn
             {
               :account_id account_id
               :tword tword
               :src_lang src_lang
               :dest_lang dest_lang
               })]
               (print res)
              (is (= 1 (count res)))
              (is (= {
                :tword_id (:tword_id (get res 0))
                :account_id account_id
                :tword tword
                :src_lang src_lang
                :dest_lang dest_lang
                } (db/get-tword t-conn {:tword_id (:tword_id (get res 0))})))))))

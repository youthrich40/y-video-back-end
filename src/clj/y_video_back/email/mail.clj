(ns y-video-back.email.mail
  (:require
    [y-video-back.config :refer [env]]
    [postal.core :as postal]
    [y-video-back.db.email-logs :as email-logs]))

(defn log-message
  "Log to the database that an email was sent"
  [email]
  (email-logs/CREATE email))

(defn send-message
  "Send email message over SMTP and write to log"
  [email &[server-map]]
  (let [server-map (or server-map
                       (-> env :mail :server)
                       {:host "gateway.byu.edu" :port 25})]
    (postal/send-message server-map email)
    (log-message email)))

(defn send-email
  "Send email and log it"
  [email]
  (if (or (:test env) (:dev env))
      (log-message email)
      (send-message email)))

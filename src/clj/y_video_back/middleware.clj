(ns y-video-back.middleware
  (:require [y-video-back.env :refer [defaults]]
            [clojure.tools.logging :as log]
            [y-video-back.layout :refer [*app-context* error-page]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [y-video-back.config :refer [env]]
            [ring.middleware.flash :refer [wrap-flash]]
            [immutant.web.middleware :refer [wrap-session]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [byu-cas.core :as cas]
            ;[cheshire.generate :as cheshire]
            ;[cognitect.transit :as transit]
            [y-video-back.middleware.formats :as formats]
            ;[muuntaja.middleware :refer [wrap-format wrap-params]]
            [ring-ttl-session.core :refer [ttl-memory-store]])
  (:import [javax.servlet ServletContext]))

(defn wrap-cas [handler]
  (cas/wrap-cas handler (str (-> env :y-video-back :site-url) "/")))

(defn wrap-context [handler]
  (fn [request]
    (binding [*app-context*
              (if-let [context (:servlet-context request)]
                ;; If we're not inside a servlet environment
                ;; (for example when using mock requests), then
                ;; .getContextPath might not exist
                (try (.getContextPath ^ServletContext context)
                     (catch IllegalArgumentException _ context))
                ;; if the context is not specified in the request
                ;; we check if one has been specified in the environment
                ;; instead
                (:app-context env))]
      (handler request))))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t)
        (error-page {:status 500
                     :title "Something very bad has happened!"
                     :message "We've dispatched a team of highly trained gnomes to take care of the problem."})))))

(defn print-handler [handler]
  (fn [req]
    (println "\n\n-----Handler is:")
    (println (str handler))
    (println "\n\n-----Req is:")
    (println (str req))
    (handler req)))

(defn wrap-csrf [handler]
  (wrap-anti-forgery
    handler
    {:error-response
     (error-page
       {:status 403
        :title "Invalid anti-forgery token"})}))

(defn wrap-formats
  "Ensure that json<>map conversion is in place"
  [handler]
  (let [wrapped (wrap-restful-format
                  handler
                  {:formats [:json-kw :transit-json :transit-msgpack]})]
    (fn [request]
      ((if (:websocket? request) handler wrapped) request))))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-flash
      wrap-cas
      ;;wrap-csrf
      (wrap-session {:cookie-attrs {:http-only true}})
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (dissoc :session)))
      wrap-context
      wrap-internal-error))

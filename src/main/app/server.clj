(ns app.server
  (:require [app.parser :refer [api-parser]]
            [org.httpkit.server :as http]
            [com.fulcrologic.fulcro.server.api-middleware :as server]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.resource :refer [wrap-resource]]
            [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; server -> parser -> resolvers -> db
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; -------------------------------------------------------------------------
;; Handles undefined requests
;; -------------------------------------------------------------------------
(def ^:private not-found-handler
  (fn [req]
    {:status 404
     :headers {"Content-Type" "text/plain"}
     :body "Not Found"}))


;; -------------------------------------------------------------------------
;; Ring miiddle warw chain starts content type handler, transit encode/decode
;; the /api to parser handler and finaly the nof-found handler
;; -------------------------------------------------------------------------
(def middleware
  (-> not-found-handler
      (server/wrap-api {:uri "/api"
                        :parser api-parser})
      (server/wrap-transit-params)
      (server/wrap-transit-response)
      (wrap-resource "public")
      (wrap-content-type)))


;; -------------------------------------------------------------------------
;; Atom to hold server stop function
;; -------------------------------------------------------------------------
(defonce stop-fn (atom nil))


;; -------------------------------------------------------------------------
;; Start server 
;; -------------------------------------------------------------------------
(defn start
  "Start the server and store the stop function in the stop-fn atom"
  []
  (log/info "Starting server on port 3000...")
  (reset! stop-fn (http/run-server middleware {:port 3000})))


;; -------------------------------------------------------------------------
;; Stop function
;; -------------------------------------------------------------------------
(defn stop
  "Calls the stop function from the stop-fn tom, and resets the atom"
  []
  (when @stop-fn
    (log/info "Stopping server....")
    (@stop-fn)
    (reset! stop-fn nil)))


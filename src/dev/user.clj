(ns user
  (:require [app.server :as server]
            [clojure.tools.namespace.repl :as tools-ns]))


;;-------------------------------------------------------
;; Ensure we only refresh directories we pull sources from.
;; That means clj, cljs and exclude resource for example
;;-------------------------------------------------------
(tools-ns/set-refresh-dirs "src/dev" "src/main")


;;-------------------------------------------------------
;; Start the server
;;-------------------------------------------------------
(defn start []
  (server/start))


;;-------------------------------------------------------
;; Restart the server
;;-------------------------------------------------------
(defn restart
  "Stop thes server, reload the source code and
  restart"
  []
  (server/stop)
  (tools-ns/refresh :after 'user/start))


;;-------------------------------------------------------
;; If there are compiler errors, then the user namespace
;; might not reload properly. A refresh helps
;;-------------------------------------------------------
(comment
  (tools-ns/refresh)
  (restart)
  )

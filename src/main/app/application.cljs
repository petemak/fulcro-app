(ns app.application
  (:require [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.networking.http-remote :as http]))



;;-----------------------------------------------------------
;; Fulcro application
;;-----------------------------------------------------------
(defonce fapp (app/fulcro-app
               {:remotes {:remote (http/fulcro-http-remote {})}}))

(comment

  (type fapp)

  )

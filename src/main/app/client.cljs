(ns app.client
  (:require [app.ui :as ui]
            [app.application :refer [fapp]]
            [com.fulcrologic.fulcro.components :as comp]
            [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.data-fetch :as df]))

;;-----------------------------------------------------------
;;
;;-----------------------------------------------------------
(defn ^:export init
  "Shadow-cljs sets this as entry-point function.
   See shadow-cljs.edn `:init-fn` in the modules of the main build."
  []
  (app/mount! fapp ui/Root "app")

  ;; nitial load during application startup
  (df/load! fapp :friends ui/PersonList)
  (df/load! fapp :enemies ui/PersonList)
  (js/console.log "Applicatiob loaded"))


;;-----------------------------------------------------------
;;
;;-----------------------------------------------------------
(defn ^:export refresh
  
  "During development, shadow-cljs will call this on every hot reload of source.
  See shadow-cljs.edn"
  []
  ;; re-mounting will cause forced UI refresh, update internals, etc.
  (app/mount! fapp ui/Root "app")
  ;; As of Fulcro 3.3.0, this addition will help with stale queries when using dynamic routing:
  (comp/refresh-dynamic-queries! fapp)
  (js/console.log "Hot reload"))

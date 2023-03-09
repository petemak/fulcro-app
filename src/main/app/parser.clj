(ns app.parser
  (:require [app.resolvers :as res]
            [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc]
            [taoensso.timbre :as log]))


;; ----------------------------------------------------
;; Pathom Resolvers
;; ----------------------------------------------------
(def resolvers [res/resolver-list])


;; ----------------------------------------------------
;; Pathom parser
;; ----------------------------------------------------
(def pathom-parser (p/parser {::p/env {::p/reader [p/map-reader
                                                   pc/reader2
                                                   pc/ident-reader
                                                   pc/index-reader]
                                       ::p/mutation-join-globals [:tempids]}
                              ::p/mutate pc/mutate
                              ::p/plugins [(pc/connect-plugin {::pc/register resolvers})
                                           p/error-handler-plugin
                                           ;; or p/elide-special-outputs-plugin
                                           (p/post-process-parser-plugin p/elide-not-found)] }))


;; ----------------------------------------------------
;; Pathom parser
;; ----------------------------------------------------
(defn api-parser
  [query]
  (log/info "Processing query" query)
  (pathom-parser {} query))

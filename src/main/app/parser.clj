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
;; API parser for Pathom queries
;; ----------------------------------------------------
(defn api-parser
  "Passes the querry to the regsiterred Pathom parsers"
   [query]
  (log/info "Processing query" query)
  (pathom-parser {} query))


;; ----------------------------------------------------
;; Try out api-parser
;; ----------------------------------------------------
(comment
 (api-parser [{[:person/id 1] [:person/name]}])
 ;; => {[:person/id 1] #:person{:name "Sally"}}

 (api-parser [{[:list/id :friends] [:list/id]}])
 ;; => {[:list/id :friends] #:list{:id :friends}}

 ;; Pathom’s magic is that it can traverse the graph based on the declared
 ;; inputs and outputs. The fact that the list resolver says it outputs
 ;; only :person/id means that Pathom can "connect the dots" to fill
 ;; in the rest of the details of that person like :person/name :person/age
 (api-parser [{[:list/id :friends] [:list/id {:list/people [:person/name :person/age]}]}])
 ;; => {[:list/id :friends]
 ;;     #:list{:id :friends,
 ;;            :people
 ;;            [#:person{:name "Sally", :age 27}
 ;;             #:person{:name "Joe", :age 41}
 ;;             #:person{:name "Dan", :age 36}]}}

 (api-parser  [{:friends [:list/id {:list/people [:person/id :person/name :person/age]}]}])
 ;; => {:friends
 ;;     #:list{:id :friends,
 ;;            :people
 ;;            [#:person{:id 1, :name "Sally", :age 27}
 ;;             #:person{:id 2, :name "Joe", :age 41}
 ;;             #:person{:id 3, :name "Dan", :age 36}]}}
 
 (api-parser [{:friends [:list/id {:list/people [:person/name]}]}])
 ;; => {:friends
 ;;     #:list{:id :friends,
 ;;            :people
 ;;            [#:person{:name "Sally"}
 ;;             #:person{:name "Joe"}
 ;;             #:person{:name "Dan"}]}}

 (api-parser [{:enemies [:list/id]}])
 ;; => {:enemies #:list{:id :enemies}}

 (api-parser [{:enemies [:list/id {:list/people [:person/id]}]}])
 ;; => {:enemies #:list{:id :enemies, :people [#:person{:id [4 5]}]}}

 (api-parser [{:enemies [:list/id :list/label]}])
 ;; => {:enemies #:list{:id :enemies, :label "Enemies"}}
  
 (api-parser [{:enemies [:list/id {:list/people [:person/name]}]}])
 ;; => {:enemies #:list{:id :enemies, :people [{}]}}

 
 (api-parser [{[:list/id :enemies] [:list/id {:list/people [:person/id :person/name :person/age]}]}])
 ;; => {:enemies #:list{:id :enemies, :people [#:person{:id [4 5]}]}}

  
 )

(ns app.resolvers
  (:require [app.db :as db]
            [com.wsscode.pathom.core :as p]
            [com.wsscode.pathom.connect :as pc]))


;; ------------------------------------------------------------------
;; Reoslver
;; Given :person/id, this can generate the details of a person
;; ------------------------------------------------------------------
(pc/defresolver person-resolver [env {:person/keys [id]}]
  {::pc/input #{:person/id}
   ::pc/output [:person/name :person/age]}
  (db/get-person id ))

;; ------------------------------------------------------------------
;; Given a :list/id a list label and the people
;; in that list. replace people IDs [1 2 3] with
;;                                  [{:person/id 1} {:person/id 2}..]
;; 
;; Pathom’s magic is that it can traverse the graph based on the declared
;; inputs and outputs. The fact that the list resolver says it outputs
;; only :person/id means that Pathom can "connect the dots" to fill
;; in the rest of the details of that person like :person/name :person/age
;; ------------------------------------------------------------------
(pc/defresolver list-resolver [env {:list/keys [id]}]
  {::pc/input  #{:list/id}
   ::pc/output [:list/label {:list/people [:person/id]}]}
  (when-let [list (db/get-list id)]
    (assoc list
           :list/people (mapv (fn [id] {:person/id id}) (:list/people list)))))


;; ------------------------------------------------------------------
;; It is also possible to define resolvers that don’t require any inputs at all.
;; These "global resolvers" are the same as "root queries" in GraphQL.
;; We could use one of these for our application, so add this to your resolvers.clj file:
;; ------------------------------------------------------------------
(pc/defresolver friends-resolver [env input]
  {::pc/output [{:friends [:list/id]}]}
  {:friends {:list/id :friends}})


(pc/defresolver enemies-resolver [env input]
  {::pc/output [{:enemies [:list/id]}]}
  {:enemies  {:list/id :enemies}})



;; ------------------------------------------------------------------
;; Resolvers
;; ------------------------------------------------------------------
(def resolver-list [person-resolver list-resolver friends-resolver enemies-resolver])

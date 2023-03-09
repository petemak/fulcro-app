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
;; ------------------------------------------------------------------
(pc/defresolver list-resolver [env {:list/keys [id]}]
  {::pc/input  #{:list/id}
   ::pc/output [:list/label {:list/people [:person/id]}]}
  (when-let [list (db/get-list id)]
    (assoc list
      :list/people (mapv (fn [id] {:person/id id}) (:list/people list)))))


;; ------------------------------------------------------------------
;; Resolvers
;; ------------------------------------------------------------------
(def resolver-list [person-resolver list-resolver])

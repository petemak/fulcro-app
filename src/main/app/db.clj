(ns app.db
  (:require [taoensso.timbre :as log]))

;; ------------------------------------------------------------------
;; Normalised DB of lists and people
;; ------------------------------------------------------------------
(def people-table
  {1 {:person/id 1 :person/name "Sarah"   :person/age 27}
   2 {:person/id 2 :person/name "Joe"     :person/age 41}
   3 {:person/id 3 :person/name "Dan"     :person/age 36}
   4 {:person/id 4 :person/name "Abigail" :person/age 29}   
   5 {:person/id 5 :person/name "Helene"  :person/age 33}
   6 {:person/id 6 :person/name "John"    :person/age 33}
   7 {:person/id 7 :person/name "Fred"    :person/age 27}})


(def list-table
  {:friends {:list/id     :friends
             :list/label  "Friends"
             :list/people [1 2 3 4]}
   :enemies {:list/id     :enemies
             :list/label  "Enemies"
             :list/people [5 6 7]}})


;; ------------------------------------------------------------------
;; Reoslver
;; Given :person/id, this can generate the details of a person
;; ------------------------------------------------------------------
(defn get-person
  "Return person from person table with speified id"
  [id]
  (log/info "Handling person id " id)
  (get people-table id))


;; ------------------------------------------------------------------
;; List eoslver
;; Given :list/id, retune the list name and people details of a person
;; ------------------------------------------------------------------
(defn get-list
  "Return person from person table with speified id"
  [id]
  (log/info "Handling list id " id)
  (get list-table id))




(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]))


(def ui-data {:friends {:list/label "Friends"
                        :list/people [{:person/id 1 :person/name "sally" :person/age 32}
                                      {:person/id 2 :person/name "Joe"   :person/age 41}]}
              :enemies {:list/label "Enemies"
                        :list/people [{:person/id 3 :person/name "Fred" :person/age 28}
                                      {:person/id 4 :person/name "Boby" :person/age 55}]}})
;;-----------------------------------------------------------
;; Person component
;; Data is passed by destructuring the second argument of defsc
;; Initial state co-locates the initial desired part of the tree
;; with the component that uses it, here id, name and age
;;
;; :initial-state: starting initial state.
;;    a lambda that gets parameters (optionally from the parent)
;;    and returns a map representing the state of the component.
;; :query: For each scalar property in initial state,
;; there should be an identical simple property in your query.
;;
;;-----------------------------------------------------------
(defsc Person [this {:person/keys [id name age]} {:keys [onDelete]}]
  {:query [:person/id :person/name :person/age]
   :initial-state (fn [{:keys [id name age]}] {:person/id id :person/name name :person/age age})   }
  
  (dom/li
   (dom/h5 (str  "User " id ":    " name "    " age " -  ") (dom/button {:onClick  #(onDelete name)} "x") )))

;; Element factory
(def ui-person (comp/factory Person {:keyfn :person/id}))


;;-----------------------------------------------------------
;; Person list component
;; prints out a list of people
;;
;; :initial-state
;; :query a scalar label and a join for people that composes in Person
;;
;;-----------------------------------------------------------
(defsc PersonList [this {:list/keys [label people]}]
  {:query [:list/label {:list/people (comp/get-query Person)} ]
   :initial-state (fn [{:keys [label]}] {:list/label label
                                         :list/people (if (= label "Friends")
                                                        [(comp/get-initial-state Person {:id 1 :name "Sally" :age 32})
                                                         (comp/get-initial-state Person {:id 2 :name "Joe"   :age 41})]
                                                        [(comp/get-initial-state Person {:id 3 :name "Fred"  :age 28})
                                                         (comp/get-initial-state Person {:id 4 :name "Boby"  :age 55} )])} )}

  (let [delete-person (fn [name] (println "deleting " name  " from " label))]
   (dom/div
    (dom/h3 "List: " label)
    (dom/h4 "Element count: " (count people))
    (dom/ul
     (map (fn [p] (ui-person (comp/computed p {:onDelete delete-person}))) people)))))

(def ui-person-list (comp/factory PersonList))


;;-----------------------------------------------------------
;; Root component
;;-----------------------------------------------------------
(defsc Root [this {:keys [friends enemies]}]
  {:query [{:friends (comp/get-query PersonList)}
           {:enemies (comp/get-query PersonList)}]
   :initial-state (fn [params] {:friends (comp/get-initial-state PersonList {:label "Friends"})
                                :enemies (comp/get-initial-state PersonList {:label "Enemies"})})}
  (dom/div
   (dom/h2 "Basic Fullstack Application based on Fulcro")
   (dom/hr)
   (dom/div
    (dom/h3 "Tech Stack")
    (dom/ul
     (dom/li "Clojure - https://clojure.org/")
     (dom/li "ClojureScript - https://clojurescript.org/")
     (dom/li "Fulcro - http://fulcro.fulcrologic.com")
     (dom/li "Shadow-cljs - https://github.com/thheller/shadow-cljs")
     (dom/li "Pathom/EQL - https://pathom3.wsscode.com"))
    (dom/text "The stack is intended to enable the \"quick development story \", that is,
               getting hot code reload to update the UI whenever source code changes."))
   (dom/hr)
   (dom/div
    (ui-person-list friends )
    (ui-person-list enemies))))

